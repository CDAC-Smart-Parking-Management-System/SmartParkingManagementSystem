import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { register } from "../../services/authService";
import { showSuccess, showError, showWarning, getErrorMessage } from "../../utils/toast";

function Register() {

    const navigate = useNavigate();

    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [mobileNumber, setMobileNumber] = useState("");
    const [role, setRole] = useState("CUSTOMER");

    const [propertyName, setPropertyName] = useState("");
    const [address, setAddress] = useState("");
    const [city, setCity] = useState("");
    const [totalFloors, setTotalFloors] = useState("");
    const [openingTime, setOpeningTime] = useState("08:00");
    const [closingTime, setClosingTime] = useState("20:00");

    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {

        e.preventDefault();

        if (password.length < 6) {
            showWarning("📝 Password should be at least 6 characters long.");
            return;
        }

        if (!/^[6-9]\d{9}$/.test(mobileNumber)) {
            showWarning("📝 Please enter a valid 10-digit mobile number.");
            return;
        }

        if (role === "ADMIN" && (!propertyName || !address || !city || !totalFloors)) {
            showWarning("📝 Please fill in all property details to register as an Admin.");
            return;
        }

        const request = {
            firstName,
            lastName,
            email,
            password,
            mobileNumber,
            role,
            propertyName,
            address,
            city,
            totalFloors: totalFloors === "" ? null : Number(totalFloors),
            openingTime,
            closingTime
        };

        try {

            setLoading(true);

            await register(request);

            showSuccess("📝 Registration successful! Please login to continue.");

            navigate("/login");

        }
        catch (error) {

            showError(getErrorMessage(error, "Registration failed. Please try again."));

        }
        finally {

            setLoading(false);

        }

    }

    return (

        <div className="min-h-screen bg-gray-100 flex justify-center py-10">

            <div className="bg-white w-full max-w-2xl rounded-xl shadow-lg p-8">

                <h2 className="text-3xl font-bold text-center mb-8">
                    Register
                </h2>

                <form onSubmit={handleSubmit} className="space-y-4">

                    <input type="text"
                        placeholder="First Name"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        className="w-full border p-3 rounded-lg"
                        required
                    />

                    <input
                        type="text"
                        placeholder="Last Name"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        className="w-full border p-3 rounded-lg"
                        required
                    />

                    <input
                        type="email"
                        placeholder="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="w-full border p-3 rounded-lg"
                        required
                    />

                    <input
                        type="password"
                        placeholder="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="w-full border p-3 rounded-lg"
                        required
                    />

                    <input
                        type="text"
                        placeholder="Mobile Number"
                        value={mobileNumber}
                        onChange={(e) => {
                            const value = e.target.value;

                            // Allow only digits and maximum 10 digits
                            if (/^\d{0,10}$/.test(value)) {
                                setMobileNumber(value);
                            }
                        }}
                        pattern="^[6-9]\d{9}$"
                        title="Enter a valid 10-digit mobile number"
                        className="w-full border p-3 rounded-lg"
                        required
                    />

                    <select
                        value={role}
                        onChange={(e) => setRole(e.target.value)}
                        className="w-full border p-3 rounded-lg"
                    >
                        <option value="CUSTOMER">Customer</option>
                        <option value="ADMIN">Admin</option>
                    </select>

                    {role === "ADMIN" && (

                        <>

                            <input
                                type="text"
                                placeholder="Property Name"
                                value={propertyName}
                                onChange={(e) => setPropertyName(e.target.value)}
                                className="w-full border p-3 rounded-lg"
                            />

                            <input
                                type="text"
                                placeholder="Address"
                                value={address}
                                onChange={(e) => setAddress(e.target.value)}
                                className="w-full border p-3 rounded-lg"
                            />

                            <input
                                type="text"
                                placeholder="City"
                                value={city}
                                onChange={(e) => setCity(e.target.value)}
                                className="w-full border p-3 rounded-lg"
                            />

                            <input
                                type="number"
                                placeholder="Total Floors"
                                value={totalFloors}
                                min={1}
                                max={50}
                                onChange={(e) => setTotalFloors(e.target.value)}
                                className="w-full border p-3 rounded-lg"
                            />

                            <input
                                type="time"
                                value={openingTime}
                                onChange={(e) => setOpeningTime(e.target.value)}
                                className="w-full border p-3 rounded-lg"
                            />

                            <input
                                type="time"
                                value={closingTime}
                                onChange={(e) => setClosingTime(e.target.value)}
                                className="w-full border p-3 rounded-lg"
                            />

                        </>

                    )}

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-green-600 text-white py-3 rounded-lg hover:bg-green-700 disabled:opacity-60"
                    >
                        {loading ? "Registering..." : "Register"}
                    </button>

                </form>

                <p className="text-center mt-6">

                    Already have an account?

                    <Link
                        to="/login"
                        className="text-blue-600 ml-2"
                    >
                        Login
                    </Link>

                </p>

            </div>

        </div>

    );

}

export default Register;
