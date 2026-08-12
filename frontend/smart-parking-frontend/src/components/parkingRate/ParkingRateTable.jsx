function ParkingRateTable({ rates, onEdit }) {

    return (

        <div className="overflow-x-auto">

            <table className="w-full border">

                <thead className="bg-gray-100">

                    <tr>

                        <th className="border p-3">Vehicle</th>
                        <th className="border p-3">Price</th>
                        <th className="border p-3">Action</th>

                    </tr>

                </thead>

                <tbody>

                    {

                        rates.length === 0 ?

                            (

                                <tr>

                                    <td
                                        colSpan="3"
                                        className="text-center p-5"
                                    >
                                        No Parking Rates Found
                                    </td>

                                </tr>

                            )

                            :

                            rates.map((rate) => (

                                <tr key={rate.rateId}>

                                    <td className="border p-3">
                                        {rate.vehicleType}
                                    </td>

                                    <td className="border p-3">
                                        ₹ {rate.price}
                                    </td>

                                    <td className="border p-3">

                                        <button
                                            onClick={() => onEdit(rate)}
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

export default ParkingRateTable;