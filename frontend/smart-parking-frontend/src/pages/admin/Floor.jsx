import { useEffect, useState } from "react";
import { getMyProperty } from "../../services/propertyService";

import FloorForm from "../../components/floor/FloorForm";
import FloorTable from "../../components/floor/FloorTable";

import {
    getAllFloors,
    createFloor,
    updateFloor
} from "../../services/floorService";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";

function Floor() {

    const [floors, setFloors] = useState([]);

    const [property, setProperty] = useState(null);

    const [selectedFloor, setSelectedFloor] = useState(null);

    async function loadFloors() {

        try {

            const response = await getAllFloors();

            setFloors(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🏬 Unable to load floors."));

        }

    }

    async function loadProperty() {

        try {

            const response = await getMyProperty();
            console.log("Property Response:", response.data);
            setProperty(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🏬 Unable to load property details."));

        }

    }

    useEffect(() => {

        loadFloors();
        loadProperty();

    }, []);

    async function handleSave(request) {

        try {

            if (selectedFloor == null) {

                await createFloor(request);

                showSuccess("🏬 Floor added successfully.");

            }
            else {

                await updateFloor(
                    selectedFloor.floorId,
                    request
                );

                showSuccess("🏬 Floor updated successfully.");

            }

            setSelectedFloor(null);

            loadFloors();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🏬 Floor operation failed."));

        }

    }

    function handleEdit(floor) {

        setSelectedFloor(floor);

    }

    return (

        <div className="bg-white rounded-lg shadow p-6">

            <h2 className="text-2xl font-bold mb-6">

                Floor Management

            </h2>

            {
                property == null ? (

                    <div>Loading...</div>

                ) : selectedFloor || floors.length < property.totalFloors ? (

                    <FloorForm
                        selectedFloor={selectedFloor}
                        onSave={handleSave}
                    />

                ) : (

                    <div className="mb-6 p-4 bg-yellow-100 text-yellow-800 rounded">

                        Maximum number of floors has already been added.

                    </div>

                )
            }

            <FloorTable

                floors={floors}

                onEdit={handleEdit}

            />

        </div>

    );

}

export default Floor;
