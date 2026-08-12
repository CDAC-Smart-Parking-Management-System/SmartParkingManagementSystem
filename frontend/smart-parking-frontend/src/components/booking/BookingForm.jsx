import { useEffect, useState } from "react";
import { showWarning } from "../../utils/toast";

function BookingForm({

    vehicles,
    selectedSlot,
    onBook,
    onVehicleTypeChange
}) {

    const [vehicleId, setVehicleId] = useState("");
    const [arrivalMinutes, setArrivalMinutes] = useState(15);

    useEffect(() => {

        if (vehicles.length > 0) {
            // setVehicleId(vehicles[0].vehicleId);
            const vehicle = vehicles[0];
            setVehicleId(vehicle.vehicleId);
            onVehicleTypeChange(vehicle.vehicleType);
        }

    }, [vehicles]);

    function handleSubmit(e) {

        e.preventDefault();

        if (vehicles.length === 0) {

            showWarning("🚗 You need to add a vehicle before booking a slot.");

            return;

        }

        if (!selectedSlot) {

            showWarning("🅿️ Please select a parking slot first.");

            return;

        }

        onBook({

            vehicleId: Number(vehicleId),

            slotId: selectedSlot.slotId,

            arrivalMinutes: Number(arrivalMinutes)

        });

    }

    return (

        <form
            onSubmit={handleSubmit}
            className="bg-white rounded-lg shadow p-6 mb-6"
        >

            <h2 className="text-xl font-bold mb-4">

                Book Parking Slot

            </h2>

            <select

                value={vehicleId}

                onChange={(e) => {
                    setVehicleId(e.target.value);

                    const vehicle = vehicles.find(
                        v => v.vehicleId === Number(e.target.value)
                    );

                    onVehicleTypeChange(vehicle.vehicleType);
                }}

                className="w-full border rounded p-3 mb-4"

            >

                {

                    vehicles.map(vehicle => (

                        <option

                            key={vehicle.vehicleId}

                            value={vehicle.vehicleId}

                        >

                            {vehicle.vehicleNumber}
                            {" - "}
                            {vehicle.vehicleType}

                        </option>

                    ))

                }

            </select>

            <label className="block text-sm font-medium mb-1">
                Check-in within
            </label>

            <select

                value={arrivalMinutes}

                onChange={(e) => setArrivalMinutes(e.target.value)}

                className="w-full border rounded p-3 mb-1"

            >

                <option value="15">15 Minutes</option>
                <option value="30">30 Minutes</option>

            </select>

            <p className="text-sm text-gray-500 mb-4">
                If you don't check-in within this time, the slot is
                automatically released for other customers.
            </p>

            {

                selectedSlot ?

                    (

                        <div className="mb-4 p-3 rounded bg-green-100">

                            Selected Slot :
                            {" "}
                            <b>

                                {selectedSlot.slotNumber}

                            </b>

                        </div>

                    )

                    :

                    (

                        <div className="mb-4 p-3 rounded bg-red-100">

                            No Slot Selected

                        </div>

                    )

            }

            <button

                className="bg-blue-600 text-white px-6 py-3 rounded"

            >

                Book Slot

            </button>

        </form>

    );

}

export default BookingForm;
