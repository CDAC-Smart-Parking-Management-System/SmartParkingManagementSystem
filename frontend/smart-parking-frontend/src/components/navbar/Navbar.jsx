import { useNavigate } from "react-router-dom";

import { clearStorage, getUser } from "../../utils/storage";
import { showInfo } from "../../utils/toast";

function Navbar() {

    const navigate = useNavigate();

    const user = getUser();

    function handleLogout() {

        clearStorage();

        showInfo("🔐 You have been logged out.");

        navigate("/login");

    }

    return (

        <header className="bg-white shadow-sm h-16 flex items-center justify-between px-8">

            <div>

                <h1 className="text-2xl font-bold text-blue-600">
                    Smart Parking
                </h1>

            </div>

            <div className="flex items-center gap-5">

                <span className="font-medium text-gray-700">

                    Welcome {user?.firstName || user?.name || user?.username || ""}

                </span>

                <button
                    onClick={handleLogout}
                    className="bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg"
                >
                    Logout
                </button>

            </div>

        </header>

    );

}

export default Navbar;
