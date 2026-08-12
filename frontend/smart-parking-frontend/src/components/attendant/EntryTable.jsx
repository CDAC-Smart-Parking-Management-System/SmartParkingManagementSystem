function EntryTable({

    bookings,

    onCheckIn,

    onCheckOut

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

            <table className="min-w-full border text-center">

                <thead className="bg-gray-100">

                    <tr>

                        <th className="border p-3">Booking No</th>

                        <th className="border p-3">Vehicle</th>

                        <th className="border p-3">Slot</th>

                        <th className="border p-3">Check In</th>

                        <th className="border p-3">Check Out</th>

                        <th className="border p-3">Amount</th>

                        <th className="border p-3">Booking Status</th>

                        <th className="border p-3">Payment</th>

                        <th className="border p-3">Action</th>

                    </tr>

                </thead>

                <tbody>

                    {

                        bookings.length === 0 ?

                            (

                                <tr>

                                    <td
                                        colSpan="9"
                                        className="text-center p-5"
                                    >

                                        No Bookings Found

                                    </td>

                                </tr>

                            )

                            :

                            (

                                bookings.map(booking => (

                                    <tr
                                        key={booking.bookingId}
                                        className="text-center"
                                    >

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

                                            {

                                                booking.checkInTime ?

                                                    booking.checkInTime

                                                    :

                                                    "-"

                                            }

                                        </td>

                                        <td className="border p-3">

                                            {

                                                booking.checkOutTime ?

                                                    booking.checkOutTime

                                                    :

                                                    "-"

                                            }

                                        </td>

                                        <td className="border p-3">

                                            ₹ {booking.totalAmount}

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

                                                booking.bookingStatus === "BOOKED" &&

                                                <button

                                                    onClick={() => onCheckIn(booking.bookingId)}

                                                    className="bg-blue-600 text-white px-4 py-2 rounded"

                                                >

                                                    Check In

                                                </button>

                                            }

                                            {

                                                booking.bookingStatus === "ACTIVE" &&

                                                <button

                                                    onClick={() => onCheckOut(booking)}

                                                    className="bg-green-600 text-white px-4 py-2 rounded"

                                                >

                                                    Check Out

                                                </button>

                                            }

                                            {

                                                booking.bookingStatus === "COMPLETED" &&

                                                <span className="text-green-600 font-semibold">

                                                    Completed

                                                </span>

                                            }

                                            {

                                                booking.bookingStatus === "CANCELLED" &&

                                                <span className="text-red-600 font-semibold">

                                                    Cancelled

                                                </span>

                                            }

                                        </td>

                                    </tr>

                                ))

                            )

                    }

                </tbody>

            </table>

        </div>

    );

}

export default EntryTable;