import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";

import { login } from "../../services/authService";
import { saveLogin } from "../../utils/storage";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";

function Login() {

    const navigate = useNavigate();

    const [email, setEmail] = useState("");

    const [password, setPassword] = useState("");

    const [loading, setLoading] = useState(false);

    async function handleSubmit(e) {

        e.preventDefault();

        try {

            setLoading(true);

            const response = await login({

                email,
                password

            });

            saveLogin(response.data);

            const user = response.data.user;
            const role = user.role;

            showSuccess(`🔐 Welcome back, ${user.firstName || "there"}!`);

            if (role === "ADMIN") {
                navigate("/admin/dashboard");
            }
            else if (role === "CUSTOMER") {
                navigate("/customer/dashboard");
            }
            else if (role === "ATTENDANT") {
                navigate("/attendant/dashboard");
            }

        }
        catch (error) {

            showError(getErrorMessage(error, "Invalid email or password."));

        }
        finally {

            setLoading(false);

        }

    }

    return (

        <div className="min-h-screen flex items-center justify-center bg-gray-100">

            <div className="bg-white w-96 p-8 rounded-xl shadow-lg">

                <h2 className="text-3xl font-bold text-center mb-6">
                    Login
                </h2>

                <form onSubmit={handleSubmit}>

                    <div className="mb-4">

                        <label className="block mb-2">
                            Email
                        </label>

                        <input
                            type="email"
                            value={email}
                            onChange={(e) => setEmail(e.target.value)}
                            className="w-full border rounded-lg p-3"
                            required
                        />

                    </div>

                    <div className="mb-6">

                        <label className="block mb-2">
                            Password
                        </label>

                        <input
                            type="password"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            className="w-full border rounded-lg p-3"
                            required
                        />

                    </div>

                    <button
                        type="submit"
                        disabled={loading}
                        className="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700 disabled:opacity-60"
                    >
                        {loading ? "Logging In..." : "Login"}
                    </button>

                </form>

                <p className="text-center mt-5">

                    Don't have an account?

                    <Link
                        to="/register"
                        className="text-blue-600 ml-2"
                    >
                        Register
                    </Link>

                </p>

            </div>

        </div>

    );

}

export default Login;
