import { useEffect, useState } from "react";

import ParkingRateForm from "../../components/parkingRate/ParkingRateForm";
import ParkingRateTable from "../../components/parkingRate/ParkingRateTable";

import {

    getAllParkingRates,
    createParkingRate,
    updateParkingRate

} from "../../services/parkingRateService";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";

function ParkingRate() {

    const [rates, setRates] = useState([]);

    const [selectedRate, setSelectedRate] = useState(null);

    useEffect(() => {

        loadRates();

    }, []);

    async function loadRates() {

        try {

            const response = await getAllParkingRates();

            setRates(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "💰 Unable to load parking rates."));

        }

    }

    async function handleSave(request) {

        try {

            if (selectedRate == null) {

                await createParkingRate(request);

                showSuccess("💰 Parking rate added successfully.");

            }
            else {

                await updateParkingRate(

                    selectedRate.rateId,
                    request

                );

                showSuccess("💰 Parking rate updated successfully.");

            }

            setSelectedRate(null);

            loadRates();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "💰 Parking rate operation failed."));

        }

    }

    function handleEdit(rate) {

        setSelectedRate(rate);

    }

    return (

        <div className="bg-white rounded-lg shadow p-6">

            <h2 className="text-2xl font-bold mb-6">

                Parking Rates

            </h2>

            <ParkingRateForm

                selectedRate={selectedRate}

                onSave={handleSave}

            />

            <ParkingRateTable

                rates={rates}

                onEdit={handleEdit}

            />

        </div>

    );

}

export default ParkingRate;
