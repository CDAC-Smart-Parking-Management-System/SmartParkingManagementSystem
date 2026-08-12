function SlotCard({
    slot,
    selectedSlot,
    selectedVehicleType,
    onSelect
}) {

    const isSelected = selectedSlot?.slotId === slot.slotId;

    const isWrongType =
        selectedVehicleType &&
        slot.slotType !== selectedVehicleType;

    return (

        <button

            onClick={() => onSelect(slot)}

            disabled={
                slot.slotStatus !== "AVAILABLE" ||
                isWrongType
            }

            className={`p-4 rounded-lg border text-center transition-all ${isSelected
                    ? "bg-blue-600 text-white border-blue-700"

                    : isWrongType
                        ? "bg-gray-200 border-gray-400 text-gray-500 opacity-50 cursor-not-allowed"

                        : slot.slotStatus === "AVAILABLE"
                            ? "bg-green-100 hover:bg-green-200 border-green-500 cursor-pointer"

                            : slot.slotStatus === "RESERVED"
                                ? "bg-yellow-100 border-yellow-500 text-yellow-800 cursor-not-allowed"

                                : slot.slotStatus === "OCCUPIED"
                                    ? "bg-red-100 border-red-500 text-red-800 cursor-not-allowed"

                                    : "bg-gray-200 border-gray-400 text-gray-600 cursor-not-allowed"
                }`}

        >

            <h3 className="font-bold">
                {slot.slotNumber}
            </h3>

            <p>
                {slot.slotType}
            </p>

            <p className="text-sm">
                {slot.slotStatus}
            </p>

        </button>

    );

}

export default SlotCard;