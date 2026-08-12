import { useEffect, useState } from "react";

import AttendantForm from './../../components/admin/attendant/AttendantForm';
import AttendantTable from './../../components/admin/attendant/AttendantTable';

import { getAllAttendants, createAttendant, deleteAttendant } from "../../services/attendantService";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";

function Attendant() {

    const [attendants, setAttendants] = useState([]);

    useEffect(() => {

        loadAttendants();

    }, []);

    async function loadAttendants() {

        try {

            const response = await getAllAttendants();

            setAttendants(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "👤 Unable to load attendants."));

        }

    }

    async function handleSave(request) {

        try {

            await createAttendant(request);

            showSuccess("👤 Attendant added successfully.");

            loadAttendants();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "👤 Unable to add attendant."));

        }

    }

    async function handleDelete(id) {

        if (!window.confirm("Delete Attendant?")) {

            return;

        }

        try {

            await deleteAttendant(id);

            showSuccess("👤 Attendant deleted successfully.");

            loadAttendants();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "👤 Unable to delete attendant."));

        }

    }

    return (

        <div className="bg-white rounded-lg shadow p-6">

            <h2 className="text-2xl font-bold mb-6">

                Attendant Management

            </h2>

            <AttendantForm
                onSave={handleSave}
            />

            <AttendantTable
                attendants={attendants}
                onDelete={handleDelete}
            />

        </div>

    );

}

export default Attendant;
