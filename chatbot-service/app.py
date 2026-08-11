# Smart Parking Chatbot

# > python -m uvicorn app:app --reload --port 5200

# Talks to the main Spring Boot backend (port 8080) using the same
# JWT token the logged-in user's browser already has, and to Google's
# Gemini model for general questions. Every question asked is saved
# into this service's own MySQL database (chatbot_db).

import os
from datetime import datetime

import requests
import mysql.connector
from dotenv import load_dotenv
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from google import genai

load_dotenv()

MAIN_BACKEND_URL = os.getenv(
    "MAIN_BACKEND_URL",
    "http://localhost:8080"
)

LOGGING_SERVICE_URL = os.getenv(
    "LOGGING_SERVICE_URL",
    "http://localhost:5100/api/logs"
)

GOOGLE_MODEL = os.getenv(
    "GEMINI_MODEL",
    "gemini-3.5-flash"
)

DB_CONFIG = {
    "host": os.getenv("DB_HOST"),
    "port": int(os.getenv("DB_PORT", 3306)),
    "user": os.getenv("DB_USER"),
    "password": os.getenv("DB_PASSWORD"),
    "database": os.getenv("DB_NAME"),
}

# if GEMINI_API_KEY is set in the environment (.env), it is picked up
# automatically here - no need to pass it in explicitly.

client = genai.Client()

SYSTEM_PROMPT = """
You are a friendly helpdesk assistant for a "Smart Parking System" web app.
Customers can register vehicles, book a parking slot in advance (choosing to
check-in within 15 or 30 minutes, otherwise the booking auto-cancels), check
in when they arrive, and check out when they leave (which calculates the
final bill). Admins manage properties/floors/slots/rates, and Attendants
handle check-in/check-out at the gate.

Answer only questions related to how this parking app works, in 2-3 short
sentences. If the question is unrelated to the app, politely say you can
only help with Smart Parking System questions.
"""

app = FastAPI()

# allow the React frontend (running on a different port) to call this api

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


# ---------- request / response models ----------

class ChatRequest(BaseModel):
    message: str
    token: str | None = None


class ChatResponse(BaseModel):
    reply: str
    intent: str


# ---------- database (chatbot's own MySQL db - chatbot_db) ----------

def init_db():
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()

    cursor.execute("""
        CREATE TABLE IF NOT EXISTS chat_history (
            id INT AUTO_INCREMENT PRIMARY KEY,
            user_message TEXT NOT NULL,
            bot_reply TEXT NOT NULL,
            intent VARCHAR(50) NOT NULL,
            created_at DATETIME DEFAULT CURRENT_TIMESTAMP
        )
    """)

    conn.commit()
    cursor.close()
    conn.close()

    print("chatbot_db ready (chat_history table checked/created)")


def save_chat(user_message, bot_reply, intent):
    conn = mysql.connector.connect(**DB_CONFIG)
    cursor = conn.cursor()

    cursor.execute(
        "INSERT INTO chat_history "
        "(user_message, bot_reply, intent) VALUES (%s, %s, %s)",
        (user_message, bot_reply, intent)
    )

    conn.commit()
    cursor.close()
    conn.close()


# call it once when this file is loaded (i.e. when uvicorn starts)

init_db()


# ---------- send an activity log to the .NET logging microservice ----------

# logging must never break the chatbot, so any failure here is ignored.

def send_log(action, message):
    try:
        requests.post(
            LOGGING_SERVICE_URL,
            json={
                "serviceName": "chatbot-service",
                "action": action,
                "message": message
            },
            timeout=2
        )
    except Exception as e:
        print("Could not reach logging service:", e)


# ---------- calls to the main Spring Boot backend ----------

def get_my_bookings(token):
    resp = requests.get(
        f"{MAIN_BACKEND_URL}/api/bookings/my",
        headers={"Authorization": f"Bearer {token}"},
        timeout=5
    )
    resp.raise_for_status()
    return resp.json()


def get_available_slots(token):
    resp = requests.get(
        f"{MAIN_BACKEND_URL}/api/slots/available/public",
        headers={"Authorization": f"Bearer {token}"},
        timeout=5
    )
    resp.raise_for_status()
    return resp.json()


def get_parking_properties(token):
    resp = requests.get(
        f"{MAIN_BACKEND_URL}/api/properties/all",
        headers={"Authorization": f"Bearer {token}"},
        timeout=5
    )
    resp.raise_for_status()
    return resp.json()


def get_parking_rates(token):
    resp = requests.get(
        f"{MAIN_BACKEND_URL}/api/parking-rates/public",
        headers={"Authorization": f"Bearer {token}"},
        timeout=5
    )
    resp.raise_for_status()
    return resp.json()


# ---------- the 4 operations ----------

def handle_my_booking_status(token):

    if not token:
        return "Please log in first so I can check your booking."

    bookings = get_my_bookings(token)

    if not bookings:
        return "You don't have any bookings yet."

    # car is "parked right now" if a booking is ACTIVE
    # (checked in, not yet checked out)
    active = next(
        (b for b in bookings if b["bookingStatus"] == "ACTIVE"),
        None
    )

    if active:
        return (
            f"Yes - your vehicle {active['vehicleNumber']} "
            f"is currently parked in slot {active['slotNumber']}."
        )

    # not parked yet, but maybe waiting on check-in
    booked = next(
        (b for b in bookings if b["bookingStatus"] == "BOOKED"),
        None
    )

    if booked:
        return (
            f"Your car isn't parked yet - you have a reserved slot "
            f"{booked['slotNumber']}, waiting for check-in."
        )

    latest = sorted(
        bookings,
        key=lambda b: b["bookingTime"],
        reverse=True
    )[0]

    return (
        f"Your car isn't parked right now. Your most recent booking "
        f"(slot {latest['slotNumber']}) is {latest['bookingStatus']}."
    )


def handle_slot_availability(token):

    if not token:
        return "Please log in first so I can check slot availability."

    slots = get_available_slots(token)

    if not slots:
        return "Sorry, there are no available parking slots right now."

    preview = ", ".join(s["slotNumber"] for s in slots[:5])
    extra = f" and {len(slots) - 5} more" if len(slots) > 5 else ""

    return f"There are {len(slots)} available slot(s) right now: {preview}{extra}."


def handle_parking_rates(message, token):

    if not token:
        return "Please log in first so I can fetch parking rates."

    properties = get_parking_properties(token)

    if not properties:
        return "There are no parking properties available right now."

    rates = get_parking_rates(token)

    if not rates:
        return "No parking rates have been configured yet."

    message_lower = message.lower()

    # Find the property mentioned in the user's message
    selected_property = None

    for prop in properties:
        property_name = prop.get("propertyName", "")

        if property_name and property_name.lower() in message_lower:
            selected_property = prop
            break

    # If no property was mentioned
    if selected_property is None:

        property_names = [
            prop.get("propertyName")
            for prop in properties
            if prop.get("propertyName")
        ]

        # If there is only one property, use it automatically
        if len(property_names) == 1:

            property_id = properties[0].get("propertyId")

            selected_rates = [
                r for r in rates
                if r.get("propertyId") == property_id
            ]

            if not selected_rates:
                return (
                    f"No parking rates are configured for "
                    f"{property_names[0]}."
                )

            lines = ", ".join(
                f"{r['vehicleType']}: ₹{r['price']}/hour"
                for r in selected_rates
            )

            return (
                f"Parking rates at {property_names[0]} are: "
                f"{lines}."
            )

        # Multiple properties exist
        return (
            "We have multiple parking properties: "
            + ", ".join(property_names)
            + ". Please tell me which property you want "
              "the parking rates for."
        )

    # Property was found
    property_id = selected_property.get("propertyId")
    property_name = selected_property.get("propertyName")

    selected_rates = [
        r for r in rates
        if r.get("propertyId") == property_id
    ]

    if not selected_rates:
        return f"No parking rates are configured for {property_name}."

    lines = ", ".join(
        f"{r['vehicleType']}: ₹{r['price']}/hour"
        for r in selected_rates
    )

    return f"Parking rates at {property_name} are: {lines}."


def handle_faq(message):

    if (
        not os.getenv("GEMINI_API_KEY")
        or os.getenv("GEMINI_API_KEY")
        == "PASTE_YOUR_GEMINI_API_KEY_HERE"
    ):
        return (
            "The chatbot's AI key hasn't been set up yet. "
            "Please add GEMINI_API_KEY in .env"
        )

    try:
        result = client.models.generate_content(
            model=GOOGLE_MODEL,
            contents=SYSTEM_PROMPT + "\n\nQuestion: " + message
        )

        return result.text

    except Exception as e:
        return (
            "Sorry, I couldn't reach the AI model right now. "
            f"({e})"
        )


# ---------- routes ----------

@app.get("/api/health")
def health():
    return {"status": "chatbot-service is running"}


@app.post("/api/chat", response_model=ChatResponse)
def chat(req: ChatRequest):

    text = req.message.lower()

    # "my car" check runs FIRST so "is my car parked in an available
    # slot" is treated as a booking-status question, not a generic
    # slot-availability question.

    if any(
        p in text
        for p in ["my car", "my booking", "parked", "my slot"]
    ):
        intent = "MY_BOOKING_STATUS"
        reply = handle_my_booking_status(req.token)

    elif "slot" in text or "available" in text:
        intent = "SLOT_AVAILABILITY"
        reply = handle_slot_availability(req.token)

    elif any(
        w in text
        for w in ["rate", "rates", "price", "cost", "fee"]
    ):
        intent = "PARKING_RATES"
        reply = handle_parking_rates(
            req.message,
            req.token
        )

    else:
        intent = "FAQ"
        reply = handle_faq(req.message)

    save_chat(
        req.message,
        reply,
        intent
    )

    send_log(
        "CHATBOT_QUERY",
        f'Intent={intent} | Question="{req.message}"'
    )

    return ChatResponse(
        reply=reply,
        intent=intent
    )
