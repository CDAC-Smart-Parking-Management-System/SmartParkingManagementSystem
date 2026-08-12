function FloorTable({ floors, onEdit }) {

    return (

        <div className="overflow-x-auto">

            <table className="w-full border text-center">

                <thead className="bg-gray-100">

                    <tr>

                        <th className="border p-3">Floor Name</th>
                        <th className="border p-3">Car Slots</th>
                        <th className="border p-3">Bike Slots</th>
                        <th className="border p-3">EV Slots</th>
                        <th className="border p-3">Total Slots</th>
                        <th className="border p-3">Action</th>

                    </tr>

                </thead>

                <tbody>

                    {
                        floors.length === 0 ?

                            (

                                <tr>

                                    <td
                                        colSpan="7"
                                        className="text-center p-5"
                                    >
                                        No Floors Found
                                    </td>

                                </tr>

                            )

                            :

                            floors.map((floor) => (

                                <tr key={floor.floorId}>

                                    <td className="border p-3">
                                        {floor.floorName}
                                    </td>

                                    <td className="border p-3">
                                        {floor.carSlots}
                                    </td>

                                    <td className="border p-3">
                                        {floor.bikeSlots}
                                    </td>

                                    <td className="border p-3">
                                        {floor.evSlots}
                                    </td>

                                    <td className="border p-3">
                                        {floor.totalSlots}
                                    </td>

                                    <td className="border p-3">

                                        <button
                                            onClick={() => onEdit(floor)}
                                            className="bg-blue-600 text-white px-3 py-1 rounded"
                                        >
                                            Edit
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

export default FloorTable;