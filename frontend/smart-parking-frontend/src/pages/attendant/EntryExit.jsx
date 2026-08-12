import { useEffect, useState } from "react";

import EntryTable from "../../components/attendant/EntryTable";
import PaymentModal from "../../components/attendant/PaymentModal";

import {
    getAllBookings,
    checkIn,
    checkOut
} from "../../services/entryExitService";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";

function EntryExit() {

    const [bookings, setBookings] = useState([]);

    const [selectedBooking, setSelectedBooking] = useState(null);

    const [showPayment, setShowPayment] = useState(false);

    const [search, setSearch] = useState("");


    useEffect(() => {

        loadBookings();

    }, []);


    async function loadBookings() {

        try {

            const response = await getAllBookings();

            setBookings(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🚪 Unable to load bookings."));

        }

    }


    async function handleCheckIn(bookingId) {

        try {

            await checkIn(bookingId);

            showSuccess("🚪 Vehicle checked in successfully.");

            loadBookings();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🚪 Check-in failed."));

        }

    }


    function handleCheckOut(booking) {

        setSelectedBooking(booking);

        setShowPayment(true);

    }


    async function handlePayment(bookingId) {

        try {

            await checkOut(bookingId);

            showSuccess("🚪 Vehicle checked out successfully.");

            loadBookings();

            closeModal();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🚪 Check-out failed."));

        }

    }


    function closeModal() {

        setShowPayment(false);

        setSelectedBooking(null);

    }

    const filteredBookings = bookings.filter(booking => {

        const keyword = search.toLowerCase();

        return (
            booking.bookingNumber.toLowerCase().includes(keyword) ||
            booking.vehicleNumber.toLowerCase().includes(keyword) ||
            booking.slotNumber.toLowerCase().includes(keyword)
        );

    });

    return (

        <div className="space-y-6">

            <div className="bg-white rounded-lg shadow p-6">

                <h1 className="text-3xl font-bold mb-2">

                    Entry / Exit Management

                </h1>

                <p className="text-gray-500">

                    Manage vehicle check in and check out.

                </p>

            </div>


            <div className="bg-white rounded-lg shadow p-6">

                <div className="mb-5">

                    <input
                        type="text"
                        placeholder="Search by Booking No / Vehicle / Slot"
                        value={search}
                        onChange={(e) => setSearch(e.target.value)}
                        className="w-full border rounded-lg p-3"
                    />

                </div>

                <EntryTable

                    bookings={filteredBookings}

                    onCheckIn={handleCheckIn}

                    onCheckOut={handleCheckOut}

                />

            </div>


            <PaymentModal

                open={showPayment}

                booking={selectedBooking}

                onPay={handlePayment}

                onClose={closeModal}

            />

        </div>

    );

}

export default EntryExit;
