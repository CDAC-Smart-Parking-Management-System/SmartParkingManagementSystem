import { useEffect, useState } from "react";

import DashboardCard from "../../components/dashboard/DashboardCard";
import { showError, getErrorMessage } from "../../utils/toast";

import { getAllBookings } from "../../services/entryExitService";

function AttendantDashboard() {

    const [pendingEntry, setPendingEntry] = useState(0);

    const [vehiclesInside, setVehiclesInside] = useState(0);

    const [completedBookings, setCompletedBookings] = useState(0);

    const [totalBookings, setTotalBookings] = useState(0);

    const [totalRevenue, setTotalRevenue] = useState(0);


    useEffect(() => {

        loadDashboard();

    }, []);


    async function loadDashboard() {

        try {

            const response = await getAllBookings();

            const bookings = response.data;

            setTotalBookings(bookings.length);

            let booked = 0;

            let active = 0;

            let completed = 0;

            let revenue = 0;

            bookings.forEach(booking => {

                if (booking.bookingStatus === "BOOKED") {

                    booked++;

                }

                if (booking.bookingStatus === "ACTIVE") {

                    active++;

                }

                if (booking.bookingStatus === "COMPLETED") {

                    completed++;

                }

                if (booking.paymentStatus === "PAID") {

                    revenue += booking.totalAmount;

                }

            });

            setPendingEntry(booked);

            setVehiclesInside(active);

            setCompletedBookings(completed);

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

                    Attendant Dashboard

                </h1>

                <p className="text-gray-500 mt-2">

                    Parking Operations Overview

                </p>

            </div>


            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">

                <DashboardCard

                    title="Pending Check In"

                    value={pendingEntry}

                    color="text-blue-600"

                />

                <DashboardCard

                    title="Vehicles Inside"

                    value={vehiclesInside}

                    color="text-yellow-600"

                />

                <DashboardCard

                    title="Completed Bookings"

                    value={completedBookings}

                    color="text-green-600"

                />

                <DashboardCard

                    title="Total Bookings"

                    value={totalBookings}

                    color="text-purple-600"

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

export default AttendantDashboard;