import Sidebar from "../components/sidebar/Sidebar";
import Navbar from "../components/navbar/Navbar";
import ChatbotWidget from "../components/chatbot/ChatbotWidget";
import { Outlet } from "react-router-dom";


function MainLayout() {

    return (

        <div className="flex min-h-screen bg-gray-100">

            <Sidebar />

            <div className="flex-1">

                <Navbar />

                <main className="p-6">

                    <Outlet />

                </main>

            </div>

            <ChatbotWidget />

        </div>

    );

}

export default MainLayout;