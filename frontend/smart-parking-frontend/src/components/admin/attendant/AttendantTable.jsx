function AttendantTable({ attendants, onDelete }) {

    return (

        <div className="overflow-x-auto">

            <table className="w-full border">

                <thead className="bg-gray-100">

                    <tr>

                        <th className="border p-3">First Name</th>
                        <th className="border p-3">Last Name</th>
                        <th className="border p-3">Email</th>
                        <th className="border p-3">Mobile</th>
                        <th className="border p-3">Action</th>

                    </tr>

                </thead>

                <tbody>

                    {

                        attendants.length === 0 ?

                            (

                                <tr>

                                    <td
                                        colSpan="5"
                                        className="text-center p-5"
                                    >
                                        No Attendants Found
                                    </td>

                                </tr>

                            )

                            :

                            attendants.map((attendant) => (

                                <tr key={attendant.userId}>

                                    <td className="border p-3">
                                        {attendant.firstName}
                                    </td>

                                    <td className="border p-3">
                                        {attendant.lastName}
                                    </td>

                                    <td className="border p-3">
                                        {attendant.email}
                                    </td>

                                    <td className="border p-3">
                                        {attendant.mobileNumber}
                                    </td>

                                    <td className="border p-3">

                                        <button
                                            onClick={() => onDelete(attendant.userId)}
                                            className="bg-red-600 text-white px-3 py-1 rounded"
                                        >
                                            Delete
                                        </button>

                                    </td>

                                </tr>

                            ))

                    }

                </tbody>

            </table>

        </div>

    );

}

export default AttendantTable;