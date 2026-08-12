import { NavLink } from "react-router-dom";

import { getUser } from "../../utils/storage";
import menu from './../../utils/menu';

function Sidebar() {

    const user = getUser();

    const role = user?.role || "ADMIN";

    const menuItems = menu[role] || [];

    return (

        <div className="w-64 min-h-screen bg-slate-800 text-white">

            <div className="text-center py-6 border-b border-slate-700">

                <h1 className="text-2xl font-bold">
                    Smart Parking
                </h1>

            </div>

            <div className="p-4 space-y-2">

                {

                    menuItems.map((item) => (

                        <NavLink
                            key={item.path}
                            to={item.path}
                            className={({ isActive }) =>

                                `flex items-center gap-3 px-4 py-3 rounded-lg transition-colors

                                ${isActive
                                    ? "bg-blue-600 text-white"
                                    : "hover:bg-slate-700"}`
                            }
                        >

                            <span className="text-lg">
                                {item.icon}
                            </span>

                            <span>
                                {item.title}
                            </span>

                        </NavLink>

                    ))

                }

            </div>

        </div>

    );

}

export default Sidebar;