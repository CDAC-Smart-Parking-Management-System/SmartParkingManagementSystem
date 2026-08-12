import { useEffect, useState } from "react";
import { showWarning } from "../../utils/toast";

function FloorForm({ selectedFloor, onSave }) {

    const [floorName, setFloorName] = useState("");
    const [carSlots, setCarSlots] = useState("");
    const [bikeSlots, setBikeSlots] = useState("");
    const [evSlots, setEvSlots] = useState("");

    useEffect(() => {

    if (selectedFloor) {

        setFloorName(selectedFloor.floorName);
        setCarSlots(selectedFloor.carSlots);
        setBikeSlots(selectedFloor.bikeSlots);
        setEvSlots(selectedFloor.evSlots);

    } else {

        setFloorName("");
        setCarSlots("");
        setBikeSlots("");
        setEvSlots("");

    }

}, [selectedFloor]);

    function handleSubmit(e) {

        e.preventDefault();

        if (!floorName.trim()) {
            showWarning("🏬 Please enter a floor name.");
            return;
        }

        if (!selectedFloor && carSlots === "" && bikeSlots === "" && evSlots === "") {
            showWarning("🏬 Please specify the number of slots for this floor.");
            return;
        }

        onSave({

            floorName,
            carSlots: Number(carSlots),
            bikeSlots: Number(bikeSlots),
            evSlots: Number(evSlots)

        });

    }

    return (

        <form
            onSubmit={handleSubmit}
            className="space-y-4 mb-6"
        >

            <input
                type="text"
                placeholder="Floor Name"
                value={floorName}
                onChange={(e) => setFloorName(e.target.value)}
                className="w-full border rounded p-3"
            />

            <input
                type="number"
                placeholder="Car Slots"
                value={carSlots}
                disabled={selectedFloor}
                onChange={(e) => setCarSlots(e.target.value)}
                className="w-full border rounded p-3"
            />

            <input
                type="number"
                placeholder="Bike Slots"
                value={bikeSlots}
                disabled={selectedFloor}
                onChange={(e) => setBikeSlots(e.target.value)}
                className="w-full border rounded p-3"
            />

            <input
                type="number"
                placeholder="EV Slots"
                value={evSlots}
                disabled={selectedFloor}
                onChange={(e) => setEvSlots(e.target.value)}
                className="w-full border rounded p-3"
            />

            <button
                className="bg-green-600 text-white px-5 py-2 rounded"
            >
                {selectedFloor ? "Update Floor" : "Add Floor"}
            </button>

        </form>

    );

}

export default FloorForm;
