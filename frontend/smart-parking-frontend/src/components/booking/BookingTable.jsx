function BookingTable({

    bookings,

    onCancel

}) {

    function getStatusColor(status) {

        switch (status) {

            case "BOOKED":
                return "bg-blue-100 text-blue-700";

            case "ACTIVE":
                return "bg-yellow-100 text-yellow-700";

            case "COMPLETED":
                return "bg-green-100 text-green-700";

            case "CANCELLED":
                return "bg-red-100 text-red-700";

            default:
                return "bg-gray-100";

        }

    }

    return (

        <div className="overflow-x-auto">

            <table className="w-full border text-center">

                <thead className="bg-gray-100">

                    <tr>

                        <th className="border p-3">

                            Booking No

                        </th>

                        <th className="border p-3">

                            Vehicle

                        </th>

                        <th className="border p-3">

                            Slot

                        </th>

                        <th className="border p-3">

                            Status

                        </th>

                        <th className="border p-3">

                            Payment

                        </th>

                        <th className="border p-3">

                            Check-in By

                        </th>

                        <th className="border p-3">

                            Amount

                        </th>

                        <th className="border p-3">

                            Action

                        </th>

                    </tr>

                </thead>

                <tbody>

                    {

                        bookings.length === 0 ?

                            (

                                <tr>

                                    <td

                                        colSpan="8"

                                        className="text-center p-5"

                                    >

                                        No Bookings Found

                                    </td>

                                </tr>

                            )

                            :

                            bookings.map(booking => (

                                <tr key={booking.bookingId}>

                                    <td className="border p-3">

                                        {booking.bookingNumber}

                                    </td>

                                    <td className="border p-3">

                                        {booking.vehicleNumber}

                                    </td>

                                    <td className="border p-3">

                                        {booking.slotNumber}

                                    </td>

                                    <td className="border p-3">

                                        <span
                                            className={`px-3 py-1 rounded-full text-sm font-semibold ${getStatusColor(booking.bookingStatus)}`}
                                        >

                                            {booking.bookingStatus}

                                        </span>

                                    </td>

                                    <td className="border p-3">

                                        {booking.paymentStatus}

                                    </td>

                                    <td className="border p-3">

                                        {
                                            booking.bookingStatus === "BOOKED" && booking.expectedCheckInTime
                                                ?
                                                new Date(booking.expectedCheckInTime).toLocaleString()
                                                :
                                                "-"
                                        }

                                    </td>

                                    <td className="border p-3">

                                        ₹ {booking.totalAmount}

                                    </td>

                                    <td className="border p-3">

                                        {

                                            booking.bookingStatus === "BOOKED"

                                            &&

                                            <button

                                                onClick={() => onCancel(booking.bookingId)}

                                                className="bg-red-600 text-white px-3 py-1 rounded"

                                            >

                                                Cancel

                                            </button>

                                        }

                                        {
                                            booking.bookingStatus === "ACTIVE" && (
                                                <span className="text-yellow-600 font-semibold">
                                                    Active
                                                </span>
                                            )
                                        }

                                        {
                                            booking.bookingStatus === "COMPLETED" && (
                                                <span className="text-green-600 font-semibold">
                                                    Completed
                                                </span>
                                            )
                                        }

                                        {
                                            booking.bookingStatus === "CANCELLED" && (
                                                <span className="text-red-600 font-semibold">
                                                    Cancelled
                                                </span>
                                            )
                                        }

                                    </td>

                                </tr>

                            ))

                    }

                </tbody>

            </table>

        </div>

    );

}

export default BookingTable;