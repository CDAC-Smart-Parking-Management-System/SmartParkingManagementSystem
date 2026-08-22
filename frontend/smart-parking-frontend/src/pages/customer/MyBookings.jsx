import { useEffect, useState } from "react";

import BookingTable from "../../components/booking/BookingTable";

import {
    getMyBookings,
    cancelBooking
} from "../../services/bookingService";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";

function MyBookings() {

    const [bookings, setBookings] = useState([]);

    useEffect(() => {

        loadBookings();

        const interval = setInterval(() => {
            loadBookings();
        }, 60000);

        return () => clearInterval(interval);

    }, []);

    async function loadBookings() {

        try {

            const response = await getMyBookings();

            setBookings(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "💳 Unable to load bookings."));

        }

    }


    async function handleCancel(bookingId) {

        const confirmCancel = window.confirm(
            "Cancel Booking ?"
        );

        if (!confirmCancel) {

            return;

        }

        try {

            await cancelBooking(bookingId);

            showSuccess("💳 Booking cancelled successfully.");

            loadBookings();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "💳 Unable to cancel booking."));

        }

    }


    const currentBookings = bookings.filter(

        booking =>

            booking.bookingStatus === "BOOKED" ||

            booking.bookingStatus === "ACTIVE"

    );


    const bookingHistory = bookings.filter(

        booking =>

            booking.bookingStatus === "COMPLETED" ||

            booking.bookingStatus === "CANCELLED"

    );


    return (

        <div className="space-y-8">

            <div className="bg-white rounded-lg shadow p-6">

                <h2 className="text-2xl font-bold mb-5">

                    Current Bookings

                </h2>

                <BookingTable

                    bookings={currentBookings}

                    onCancel={handleCancel}

                />

            </div>


            <div className="bg-white rounded-lg shadow p-6">

                <h2 className="text-2xl font-bold mb-5">

                    Booking History

                </h2>

                <BookingTable

                    bookings={bookingHistory}

                    onCancel={handleCancel}

                />

            </div>

        </div>

    );

}

export default MyBookings;
