import { useEffect, useState } from "react";

import DashboardCard from "../../components/dashboard/DashboardCard";
import { showError, getErrorMessage } from "../../utils/toast";

import { getAllVehicles } from "../../services/vehicleService";
import { getMyBookings } from "../../services/bookingService";

function CustomerDashboard() {

    const [totalVehicles, setTotalVehicles] = useState(0);

    const [totalBookings, setTotalBookings] = useState(0);

    const [activeBookings, setActiveBookings] = useState(0);

    const [completedBookings, setCompletedBookings] = useState(0);

    const [cancelledBookings, setCancelledBookings] = useState(0);


    useEffect(() => {

        loadDashboard();

    }, []);


    async function loadDashboard() {

        try {

            const vehicleResponse = await getAllVehicles();

            const bookingResponse = await getMyBookings();


            setTotalVehicles(vehicleResponse.data.length);

            setTotalBookings(bookingResponse.data.length);


            let active = 0;

            let completed = 0;

            let cancelled = 0;


            bookingResponse.data.forEach(booking => {

                if (booking.bookingStatus === "ACTIVE") {

                    active++;

                }

                if (booking.bookingStatus === "COMPLETED") {

                    completed++;

                }

                if (booking.bookingStatus === "CANCELLED") {

                    cancelled++;

                }

            });


            setActiveBookings(active);

            setCompletedBookings(completed);

            setCancelledBookings(cancelled);

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

                    Customer Dashboard

                </h1>

                <p className="text-gray-500 mt-2">

                    Welcome to Smart Parking System

                </p>

            </div>


            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

                <DashboardCard

                    title="My Vehicles"

                    value={totalVehicles}

                    color="text-blue-600"

                />

                <DashboardCard

                    title="My Bookings"

                    value={totalBookings}

                    color="text-purple-600"

                />

                <DashboardCard

                    title="Active Bookings"

                    value={activeBookings}

                    color="text-yellow-600"

                />

                <DashboardCard

                    title="Completed Bookings"

                    value={completedBookings}

                    color="text-green-600"

                />

                <DashboardCard

                    title="Cancelled Bookings"

                    value={cancelledBookings}

                    color="text-red-600"

                />

            </div>

        </div>

    );

}

export default CustomerDashboard;