import { useEffect, useState } from "react";

import DashboardCard from "../../components/dashboard/DashboardCard";
import { showError, getErrorMessage } from "../../utils/toast";

// import { getAllProperties } from "../../services/propertyService";
import { getAllFloors } from "../../services/floorService";
import { getAvailableSlots } from "../../services/slotService";
import { getAllBookings } from './../../services/entryExitService';

function AdminDashboard() {

    const [totalFloors, setTotalFloors] = useState(0);

    const [totalSlots, setTotalSlots] = useState(0);

    const [availableSlots, setAvailableSlots] = useState(0);

    const [totalBookings, setTotalBookings] = useState(0);

    const [activeBookings, setActiveBookings] = useState(0);

    const [totalRevenue, setTotalRevenue] = useState(0);


    useEffect(() => {

        loadDashboard();

    }, []);


    async function loadDashboard() {

        try {

            const floorResponse = await getAllFloors();

            const slotResponse = await getAvailableSlots();

            const bookingResponse = await getAllBookings();


            setTotalFloors(floorResponse.data.length);

            setAvailableSlots(slotResponse.data.length);

            setTotalBookings(bookingResponse.data.length);


            let slotCount = 0;

            floorResponse.data.forEach(floor => {

                slotCount += floor.totalSlots;

            });

            setTotalSlots(slotCount);


            let active = 0;

            bookingResponse.data.forEach(booking => {

                if (booking.bookingStatus === "ACTIVE") {

                    active++;

                }

            });

            setActiveBookings(active);


            let revenue = 0;

            bookingResponse.data.forEach(booking => {

                if (booking.paymentStatus === "PAID") {

                    revenue += booking.totalAmount;

                }

            });

            setTotalRevenue(revenue);

        }

        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "📊 Unable to load dashboard data."));

        }

    }


    return (

        <div className="space-y-8">

            <div>

                <h1 className="text-3xl font-bold">

                    Admin Dashboard

                </h1>

                <p className="text-gray-500 mt-2">

                    Smart Parking System Overview

                </p>

            </div>


            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

                <DashboardCard

                    title="Total Floors"

                    value={totalFloors}

                    color="text-green-600"

                />

                <DashboardCard

                    title="Total Slots"

                    value={totalSlots}

                    color="text-purple-600"

                />

                <DashboardCard

                    title="Available Slots"

                    value={availableSlots}

                    color="text-indigo-600"

                />

                <DashboardCard

                    title="Total Bookings"

                    value={totalBookings}

                    color="text-orange-600"

                />

                <DashboardCard

                    title="Active Bookings"

                    value={activeBookings}

                    color="text-yellow-600"

                />

                <DashboardCard

                    title="Revenue"

                    value={`₹ ${totalRevenue}`}

                    color="text-emerald-600"

                />

            </div>

        </div>

    );

}

export default AdminDashboard;