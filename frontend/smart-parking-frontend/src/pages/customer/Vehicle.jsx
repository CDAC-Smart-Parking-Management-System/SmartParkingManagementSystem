import { useEffect, useState } from "react";

import VehicleForm from "../../components/vehicle/VehicleForm";
import VehicleTable from "../../components/vehicle/VehicleTable";

import {
    getAllVehicles,
    createVehicle,
    updateVehicle,
    deleteVehicle
} from "../../services/vehicleService";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";


function Vehicle() {

    const [vehicles, setVehicles] = useState([]);

    const [selectedVehicle, setSelectedVehicle] = useState(null);


    useEffect(() => {

        loadVehicles();

    }, []);


    async function loadVehicles() {

        try {

            const response = await getAllVehicles();

            setVehicles(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🚗 Unable to load vehicles."));

        }

    }


    async function handleSave(request) {

        try {

            if (selectedVehicle == null) {

                await createVehicle(request);

                showSuccess("🚗 Vehicle added successfully.");

            }
            else {

                await updateVehicle(
                    selectedVehicle.vehicleId,
                    request
                );

                showSuccess("🚗 Vehicle updated successfully.");

            }


            setSelectedVehicle(null);

            loadVehicles();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🚗 Vehicle operation failed."));

        }

    }


    async function handleDelete(vehicleId) {

        const confirmDelete = window.confirm(
            "Delete this vehicle?"
        );


        if (!confirmDelete) {

            return;

        }


        try {

            await deleteVehicle(vehicleId);

            showSuccess("🚗 Vehicle deleted successfully.");

            loadVehicles();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🚗 Unable to delete vehicle."));

        }

    }


    function handleEdit(vehicle) {

        setSelectedVehicle(vehicle);

    }


    return (

        <div className="bg-white rounded-lg shadow p-6">


            <h2 className="text-2xl font-bold mb-6">

                My Vehicles

            </h2>


            <VehicleForm

                selectedVehicle={selectedVehicle}

                onSave={handleSave}

            />


            <VehicleTable

                vehicles={vehicles}

                onEdit={handleEdit}

                onDelete={handleDelete}

            />


        </div>

    );

}


export default Vehicle;
