import { useEffect, useState } from "react";
import { showWarning } from "../../utils/toast";

function ParkingRateForm({ selectedRate, onSave }) {

    const [vehicleType, setVehicleType] = useState("CAR");
    const [price, setPrice] = useState("");

    useEffect(() => {

        if (selectedRate) {

            setVehicleType(selectedRate.vehicleType);
            setPrice(selectedRate.price);

        } else {

            setVehicleType("CAR");
            setPrice("");

        }

    }, [selectedRate]);

    function handleSubmit(e) {

        e.preventDefault();

        if (!price || Number(price) <= 0) {
            showWarning("💰 Please enter a valid price greater than 0.");
            return;
        }

        onSave({

            vehicleType,
            price

        });

    }

    return (

        <form
            onSubmit={handleSubmit}
            className="space-y-4 mb-6"
        >

            <select
                value={vehicleType}
                disabled={selectedRate}
                onChange={(e) => setVehicleType(e.target.value)}
                className="w-full border rounded p-3"
            >

                <option value="CAR">CAR</option>
                <option value="BIKE">BIKE</option>
                <option value="EV">EV</option>

            </select>

            <input
                type="number"
                step="0.01"
                min={0.01}
                placeholder="Price"
                value={price}
                onChange={(e) => setPrice(e.target.value)}
                className="w-full border rounded p-3"
            />

            <button
                className="bg-green-600 text-white px-5 py-2 rounded"
            >
                {selectedRate ? "Update Rate" : "Add Rate"}
            </button>

        </form>

    );

}

export default ParkingRateForm;
