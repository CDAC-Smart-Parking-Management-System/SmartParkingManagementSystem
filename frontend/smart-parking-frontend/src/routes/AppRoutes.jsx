import { Routes, Route } from "react-router-dom";

import Login from "../pages/auth/Login";
import Register from "../pages/auth/Register";

import AdminDashboard from "../pages/admin/AdminDashboard";
import Property from "../pages/admin/Property";
import Floor from "../pages/admin/Floor";
import ParkingRate from "../pages/admin/ParkingRate";
import Attendant from "../pages/admin/Attendant";

import CustomerDashboard from "../pages/customer/CustomerDashboard";
import Vehicle from "../pages/customer/Vehicle";
import Booking from "../pages/customer/Booking";
import MyBookings from "../pages/customer/MyBookings";

import AttendantDashboard from "../pages/attendant/AttendantDashboard";
import EntryExit from "../pages/attendant/EntryExit";

import MainLayout from "../layouts/MainLayout";
import ProtectedRoute from "./ProtectedRoute";

function AppRoutes() {

    return (

        <Routes>

            <Route
                path="/"
                element={<Login />}
            />

            <Route
                path="/login"
                element={<Login />}
            />

            <Route
                path="/register"
                element={<Register />}
            />

            <Route
                path="/admin"
                element={
                    <ProtectedRoute>
                        <MainLayout />
                    </ProtectedRoute>
                }
            >

                <Route
                    path="dashboard"
                    element={<AdminDashboard />}
                />

                <Route
                    path="property"
                    element={<Property />}
                />

                <Route
                    path="floors"
                    element={<Floor />}
                />

                <Route
                    path="parking-rates"
                    element={<ParkingRate />}
                />

                <Route
                    path="attendants"
                    element={<Attendant />}
                />

            </Route>

            <Route
                path="/customer"
                element={
                    <ProtectedRoute>
                        <MainLayout />
                    </ProtectedRoute>
                }
            >

                <Route
                    path="dashboard"
                    element={<CustomerDashboard />}
                />

                <Route
                    path="vehicles"
                    element={<Vehicle />}
                />

                <Route
                    path="bookings"
                    element={<Booking />}
                />

                <Route
                    path="my-bookings"
                    element={<MyBookings />}
                />

            </Route>

            <Route
                path="/attendant"
                element={
                    <ProtectedRoute>
                        <MainLayout />
                    </ProtectedRoute>
                }
            >

                <Route
                    path="dashboard"
                    element={<AttendantDashboard />}
                />

                <Route
                    path="entry-exit"
                    element={<EntryExit />}
                />

            </Route>

            <Route
                path="*"
                element={
                    <h2 className="text-center text-3xl mt-10">
                        404 Page Not Found
                    </h2>
                }
            />

        </Routes>

    );

}

export default AppRoutes;