function VehicleTable({ vehicles, onEdit, onDelete }) {

    return (

        <div className="overflow-x-auto">

            <table className="w-full border">

                <thead className="bg-gray-100">

                    <tr>

                        <th className="border p-3">Vehicle Number</th>
                        <th className="border p-3">Vehicle Type</th>
                        <th className="border p-3">Vehicle Model</th>
                        <th className="border p-3">Action</th>

                    </tr>

                </thead>

                <tbody>

                    {

                        vehicles.length === 0 ?

                            <tr>

                                <td
                                    colSpan="4"
                                    className="text-center p-5"
                                >
                                    No Vehicles Found
                                </td>

                            </tr>

                            :

                            vehicles.map((vehicle) => (

                                <tr key={vehicle.vehicleId}>

                                    <td className="border p-3">
                                        {vehicle.vehicleNumber}
                                    </td>

                                    <td className="border p-3">
                                        {vehicle.vehicleType}
                                    </td>

                                    <td className="border p-3">
                                        {vehicle.vehicleModel}
                                    </td>

                                    <td className="border p-3 space-x-2">

                                        <button
                                            onClick={() => onEdit(vehicle)}
                                            className="bg-blue-600 text-white px-3 py-1 rounded"
                                        >
                                            Edit
                                        </button>

                                        <button
                                            onClick={() => onDelete(vehicle.vehicleId)}
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

export default VehicleTable;