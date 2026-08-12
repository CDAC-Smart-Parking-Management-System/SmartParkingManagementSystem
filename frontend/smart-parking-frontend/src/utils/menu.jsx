import { FaBuilding, FaCalendarCheck, FaCarSide, FaLayerGroup, FaMoneyBillWave, FaTachometerAlt, FaUsers } from "react-icons/fa";

const menu = {

    ADMIN: [

        {
            title: "Dashboard",
            path: "/admin/dashboard",
            icon: <FaTachometerAlt />
        },

        {
            title: "Property",
            path: "/admin/property",
            icon: <FaBuilding />
        },

        {
            title: "Floors",
            path: "/admin/floors",
            icon: <FaLayerGroup />
        },

        {
            title: "Parking Rates",
            path: "/admin/parking-rates",
            icon: <FaMoneyBillWave />
        },

        {
            title: "Attendants",
            path: "/admin/attendants",
            icon: <FaUsers />
        }

    ],

    CUSTOMER: [

        {
            title: "Dashboard",
            path: "/customer/dashboard",
            icon: <FaTachometerAlt />
        },

        {
            title: "Vehicles",
            path: "/customer/vehicles",
            icon: <FaCarSide />
        },

        {
            title: "Book Parking",
            path: "/customer/bookings",
            icon: <FaCalendarCheck />
        },
        {
            title: "My Bookings",
            path: "/customer/my-bookings",
            icon: <FaCalendarCheck />
        }

    ],

    ATTENDANT: [

        {
            title: "Dashboard",
            path: "/attendant/dashboard",
            icon: <FaTachometerAlt />
        },

        {
            title: "Entry / Exit",
            path: "/attendant/entry-exit",
            icon: <FaCarSide />
        }

    ]

};

export default menu;