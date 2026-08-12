import { useEffect, useState } from "react";

import {
    getMyProperty,
    updateProperty
} from "../../services/propertyService";
import { showSuccess, showError, showWarning, getErrorMessage } from "../../utils/toast";

function Property() {

    const [propertyId, setPropertyId] = useState("");

    const [propertyName, setPropertyName] = useState("");
    const [address, setAddress] = useState("");
    const [city, setCity] = useState("");
    const [totalFloors, setTotalFloors] = useState("");
    const [openingTime, setOpeningTime] = useState("");
    const [closingTime, setClosingTime] = useState("");

    async function loadProperty() {

        try {

            const response = await getMyProperty();

            const property = response.data;

            console.log(property);

            setPropertyId(property.propertyId);
            setPropertyName(property.propertyName);
            setAddress(property.address);
            setCity(property.city);
            setTotalFloors(property.totalFloors);
            setOpeningTime(property.openingTime);
            setClosingTime(property.closingTime);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🏢 Unable to load property."));

        }

    }

    useEffect(() => {

        loadProperty();

    }, []);

    async function handleSubmit(e) {

        e.preventDefault();

        if (!propertyName.trim() || !address.trim() || !city.trim()) {

            showWarning("🏢 Please fill in all property details before saving.");

            return;

        }

        const request = {

            propertyName,
            address,
            city,
            totalFloors,
            openingTime,
            closingTime

        };

        try {

            await updateProperty(propertyId, request);

            showSuccess("🏢 Property updated successfully.");

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🏢 Property update failed."));

        }

    }

    return (

        <div className="bg-white rounded-lg shadow p-6">

            <h2 className="text-2xl font-bold mb-6">
                Property Details
            </h2>

            <form
                onSubmit={handleSubmit}
                className="space-y-4"
            >

                <input
                    type="text"
                    placeholder="Property Name"
                    value={propertyName}
                    onChange={(e) => setPropertyName(e.target.value)}
                    className="w-full border rounded-lg p-3"
                />

                <input
                    type="text"
                    placeholder="Address"
                    value={address}
                    onChange={(e) => setAddress(e.target.value)}
                    className="w-full border rounded-lg p-3"
                />

                <input
                    type="text"
                    placeholder="City"
                    value={city}
                    onChange={(e) => setCity(e.target.value)}
                    className="w-full border rounded-lg p-3"
                />

                <input
                    type="number"
                    placeholder="Total Floors"
                    value={totalFloors}
                    disabled
                    className="w-full border rounded-lg p-3 bg-gray-100"
                />

                <input
                    type="time"
                    value={openingTime}
                    onChange={(e) => setOpeningTime(e.target.value)}
                    className="w-full border rounded-lg p-3"
                />

                <input
                    type="time"
                    value={closingTime}
                    onChange={(e) => setClosingTime(e.target.value)}
                    className="w-full border rounded-lg p-3"
                />

                <button
                    className="bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700"
                >
                    Update Property
                </button>

            </form>

        </div>

    );

}

export default Property;
