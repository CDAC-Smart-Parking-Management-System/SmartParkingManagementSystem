import axios from "axios";
import { getToken } from "../utils/storage";

// The chatbot microservice runs on its own port (5200), completely
// separate from the main backend (8080) and the logging service (5100).
const CHATBOT_BASE_URL = "http://localhost:5200/api";

// Sends the user's message to the chatbot microservice. We also send
// the current JWT token, so the chatbot can call the main backend's
// protected slot / rate APIs on the logged-in user's behalf.
export function sendChatMessage(message) {

    return axios({

        method: "POST",

        url: `${CHATBOT_BASE_URL}/chat`,

        data: {
            message,
            token: getToken()
        }

    });

}
