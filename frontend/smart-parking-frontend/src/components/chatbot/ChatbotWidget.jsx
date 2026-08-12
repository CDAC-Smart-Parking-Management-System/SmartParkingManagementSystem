import { useState, useRef, useEffect } from "react";
import { sendChatMessage } from "../../services/chatbotService";

// A simple floating chat button (bottom-right corner) that opens a
// small chat panel. Talks to the separate chatbot microservice
// (Node.js + Gemini) running on http://localhost:5200.
function ChatbotWidget() {

    const [isOpen, setIsOpen] = useState(false);

    const [messages, setMessages] = useState([
        { sender: "bot", text: "Hi! I can tell you if your car is parked, check slot availability, tell you parking rates, or answer general questions about how this app works." }
    ]);

    const [input, setInput] = useState("");
    const [loading, setLoading] = useState(false);

    const bottomRef = useRef(null);

    useEffect(() => {
        bottomRef.current?.scrollIntoView({ behavior: "smooth" });
    }, [messages, isOpen]);

    async function handleSend() {

        const trimmed = input.trim();

        if (!trimmed || loading) {
            return;
        }

        setMessages((prev) => [...prev, { sender: "user", text: trimmed }]);
        setInput("");
        setLoading(true);

        try {

            const response = await sendChatMessage(trimmed);

            setMessages((prev) => [...prev, { sender: "bot", text: response.data.reply }]);

        } catch (error) {

            setMessages((prev) => [...prev, {
                sender: "bot",
                text: "Sorry, I couldn't reach the chatbot service. Please make sure it is running."
            }]);

        } finally {
            setLoading(false);
        }
    }

    function handleKeyDown(e) {
        if (e.key === "Enter") {
            handleSend();
        }
    }

    return (

        <div className="fixed bottom-6 right-6 z-50">

            {isOpen && (

                <div className="mb-3 w-80 h-96 bg-white rounded-xl shadow-2xl border flex flex-col overflow-hidden">

                    <div className="bg-blue-600 text-white px-4 py-3 flex justify-between items-center">
                        <span className="font-semibold">Parking Assistant</span>
                        <button onClick={() => setIsOpen(false)} className="text-white/80 hover:text-white">✕</button>
                    </div>

                    <div className="flex-1 overflow-y-auto p-3 space-y-2 bg-gray-50">

                        {messages.map((msg, idx) => (

                            <div
                                key={idx}
                                className={`max-w-[85%] px-3 py-2 rounded-lg text-sm ${
                                    msg.sender === "user"
                                        ? "bg-blue-600 text-white ml-auto"
                                        : "bg-white border text-gray-800"
                                }`}
                            >
                                {msg.text}
                            </div>
                        ))}

                        {loading && (
                            <div className="bg-white border text-gray-500 text-sm px-3 py-2 rounded-lg max-w-[60%]">
                                Typing...
                            </div>
                        )}

                        <div ref={bottomRef} />

                    </div>

                    <div className="p-2 border-t flex gap-2">

                        <input
                            type="text"
                            value={input}
                            onChange={(e) => setInput(e.target.value)}
                            onKeyDown={handleKeyDown}
                            placeholder="Ask about slots, rates, or the app..."
                            className="flex-1 border rounded-lg px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                        />

                        <button
                            onClick={handleSend}
                            disabled={loading}
                            className="bg-blue-600 text-white px-3 py-2 rounded-lg text-sm hover:bg-blue-700 disabled:opacity-50"
                        >
                            Send
                        </button>

                    </div>

                </div>
            )}

            <button
                onClick={() => setIsOpen((prev) => !prev)}
                className="w-14 h-14 rounded-full bg-blue-600 text-white shadow-lg flex items-center justify-center text-2xl hover:bg-blue-700"
                title="Chat with Parking Assistant"
            >
                💬
            </button>

        </div>
    );
}

export default ChatbotWidget;
