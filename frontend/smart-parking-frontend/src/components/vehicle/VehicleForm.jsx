import { useEffect, useState } from "react";
import { showWarning } from "../../utils/toast";

function VehicleForm({ selectedVehicle, onSave }) {

    const [vehicleNumber, setVehicleNumber] = useState("");
    const [vehicleType, setVehicleType] = useState("CAR");
    const [vehicleModel, setVehicleModel] = useState("");

    useEffect(() => {

        if (selectedVehicle) {

            setVehicleNumber(selectedVehicle.vehicleNumber);
            setVehicleType(selectedVehicle.vehicleType);
            setVehicleModel(selectedVehicle.vehicleModel);

        } else {

            setVehicleNumber("");
            setVehicleType("CAR");
            setVehicleModel("");

        }

    }, [selectedVehicle]);

    function handleSubmit(e) {

        e.preventDefault();

        if (!vehicleNumber.trim()) {
            showWarning("🚗 Please enter a vehicle number.");
            return;
        }

        if (!vehicleModel.trim()) {
            showWarning("🚗 Please enter a vehicle model.");
            return;
        }

        onSave({

            vehicleNumber,
            vehicleType,
            vehicleModel

        });

    }

    return (

        <form
            onSubmit={handleSubmit}
            className="grid grid-cols-2 gap-4 mb-6"
        >

            <input
                type="text"
                placeholder="Vehicle Number"
                value={vehicleNumber}
                onChange={(e) => setVehicleNumber(e.target.value)}
                className="border rounded p-3"
            />

            <select
                value={vehicleType}
                onChange={(e) => setVehicleType(e.target.value)}
                className="border rounded p-3"
            >
                <option value="CAR">CAR</option>
                <option value="BIKE">BIKE</option>
                <option value="EV">EV</option>
            </select>

            <input
                type="text"
                placeholder="Vehicle Model"
                value={vehicleModel}
                onChange={(e) => setVehicleModel(e.target.value)}
                className="border rounded p-3"
            />

            <button
                className="bg-green-600 text-white rounded p-3"
            >
                {selectedVehicle ? "Update Vehicle" : "Add Vehicle"}
            </button>

        </form>

    );

}

export default VehicleForm;
