import { useEffect, useState } from "react";

import BookingForm from "../../components/booking/BookingForm";
import SlotCard from "../../components/booking/SlotCard";

import { getAllVehicles } from "../../services/vehicleService";
import { getSlotsByProperty } from "../../services/slotService";
import { getAllProperties } from "../../services/propertyService";

import {
    createBooking,
    getMyBookings,
    cancelBooking
} from "../../services/bookingService";
import { showSuccess, showError, getErrorMessage } from "../../utils/toast";

function Booking() {

    const [vehicles, setVehicles] = useState([]);

    const [properties, setProperties] = useState([]);

    const [selectedProperty, setSelectedProperty] = useState("");

    const [slots, setSlots] = useState([]);

    const [bookings, setBookings] = useState([]);

    const [selectedSlot, setSelectedSlot] = useState(null);

    const [selectedVehicleType, setSelectedVehicleType] = useState("");

    useEffect(() => {

        loadData();

    }, []);


    async function loadData() {

        try {

            const vehicleResponse = await getAllVehicles();

            const propertyResponse = await getAllProperties();

            const bookingResponse = await getMyBookings();

            setVehicles(vehicleResponse.data);

            setProperties(propertyResponse.data);

            setBookings(bookingResponse.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "💳 Unable to load booking data."));

        }

    }

    async function handlePropertyChange(propertyId) {

        setSelectedProperty(propertyId);

        setSelectedSlot(null);

        if (!propertyId) {

            setSlots([]);

            return;

        }

        try {

            const response = await getSlotsByProperty(propertyId);

            setSlots(response.data);

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "🅿️ Unable to load slots for this property."));

        }

    }

    async function handleBooking(request) {

        try {

            await createBooking(request);

            showSuccess("💳 Booking confirmed successfully!");

            setSelectedSlot(null);
            setSlots([]);
            setSelectedProperty("");

            await loadData();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "💳 Booking failed. Please try again."));

        }

    }


    async function handleCancel(bookingId) {

        const confirmCancel = window.confirm(

            "Cancel Booking ?"

        );

        if (!confirmCancel) {

            return;

        }

        try {

            await cancelBooking(bookingId);

            showSuccess("💳 Booking cancelled successfully.");

            loadData();

        }
        catch (error) {

            console.log(error);

            showError(getErrorMessage(error, "💳 Unable to cancel booking."));

        }

    }

    const carSlots = slots.filter(s => s.slotType === "CAR");
    const bikeSlots = slots.filter(s => s.slotType === "BIKE");
    const evSlots = slots.filter(s => s.slotType === "EV");

    return (

        <div className="space-y-8">


            <div className="bg-white rounded-lg shadow p-6">

                <h2 className="text-xl font-bold mb-4">
                    Select Parking Property
                </h2>

                <select
                    value={selectedProperty}
                    onChange={(e) => handlePropertyChange(e.target.value)}
                    className="w-full border rounded p-3"
                >

                    <option value="">
                        Select Property
                    </option>

                    {properties.map(property => (

                        <option
                            key={property.propertyId}
                            value={property.propertyId}
                        >
                            {property.propertyName}
                        </option>

                    ))}

                </select>

            </div>

            <BookingForm
                vehicles={vehicles}
                selectedSlot={selectedSlot}
                onBook={handleBooking}
                onVehicleTypeChange={setSelectedVehicleType}
            />

            <div className="bg-white rounded-lg shadow p-6">

                <h2 className="text-2xl font-bold mb-5">

                    Available Parking Slots

                </h2>

                {
                    slots.length === 0 ? (

                        <p>No Slots Available</p>

                    ) : (

                        <div className="space-y-8">

                            {/* CAR SLOTS */}

                            <div>

                                <h3 className="text-lg font-semibold mb-3">
                                    🚗 Car Slots
                                </h3>

                                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">

                                    {slots
                                        .filter(slot => slot.slotType === "CAR")
                                        .map(slot => (

                                            <SlotCard
                                                key={slot.slotId}
                                                slot={slot}
                                                selectedSlot={selectedSlot}
                                                selectedVehicleType={selectedVehicleType}
                                                onSelect={setSelectedSlot}
                                            />

                                        ))}

                                </div>

                            </div>


                            {/* BIKE SLOTS */}

                            <div>

                                <h3 className="text-lg font-semibold mb-3">
                                    🏍 Bike Slots
                                </h3>

                                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">

                                    {slots
                                        .filter(slot => slot.slotType === "BIKE")
                                        .map(slot => (

                                            <SlotCard
                                                key={slot.slotId}
                                                slot={slot}
                                                selectedSlot={selectedSlot}
                                                selectedVehicleType={selectedVehicleType}
                                                onSelect={setSelectedSlot}
                                            />

                                        ))}

                                </div>

                            </div>


                            {/* EV SLOTS */}

                            <div>

                                <h3 className="text-lg font-semibold mb-3">
                                    ⚡ EV Slots
                                </h3>

                                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-5 gap-4">

                                    {slots
                                        .filter(slot => slot.slotType === "EV")
                                        .map(slot => (

                                            <SlotCard
                                                key={slot.slotId}
                                                slot={slot}
                                                selectedSlot={selectedSlot}
                                                selectedVehicleType={selectedVehicleType}
                                                onSelect={setSelectedSlot}
                                            />

                                        ))}

                                </div>

                            </div>

                        </div>

                    )
                }

            </div>

        </div>

    );

}

export default Booking;
