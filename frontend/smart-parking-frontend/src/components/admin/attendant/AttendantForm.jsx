import { useState } from "react";
import { showWarning } from "../../../utils/toast";

function AttendantForm({ onSave }) {

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [mobileNumber, setMobileNumber] = useState("");

    function handleSubmit(e) {

        e.preventDefault();

        if (!firstName.trim() || !lastName.trim()) {
            showWarning("👤 Please enter the attendant's first and last name.");
            return;
        }

        if (!/^\S+@\S+\.\S+$/.test(email)) {
            showWarning("👤 Please enter a valid email address.");
            return;
        }

        if (password.length < 6) {
            showWarning("👤 Password should be at least 6 characters long.");
            return;
        }

        if (!/^[6-9]\d{9}$/.test(mobileNumber)) {
            showWarning("👤 Please enter a valid 10-digit mobile number.");
            return;
        }

        onSave({

            firstName,
            lastName,
            email,
            password,
            mobileNumber

        });

        setFirstName("");
        setLastName("");
        setEmail("");
        setPassword("");
        setMobileNumber("");

    }

    return (

        <form
            onSubmit={handleSubmit}
            className="grid grid-cols-2 gap-4 mb-6"
        >

            <input
                type="text"
                placeholder="First Name"
                value={firstName}
                onChange={(e) => setFirstName(e.target.value)}
                className="border rounded p-3"
            />

            <input
                type="text"
                placeholder="Last Name"
                value={lastName}
                onChange={(e) => setLastName(e.target.value)}
                className="border rounded p-3"
            />

            <input
                type="email"
                placeholder="Email"
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="border rounded p-3"
            />

            <input
                type="password"
                placeholder="Password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                className="border rounded p-3"
            />

            <input
                type="text"
                placeholder="Mobile Number"
                value={mobileNumber}
                onChange={(e) => setMobileNumber(e.target.value)}
                className="border rounded p-3"
            />

            <button
                className="bg-green-600 text-white rounded p-3"
            >
                Add Attendant
            </button>

        </form>

    );

}

export default AttendantForm;
