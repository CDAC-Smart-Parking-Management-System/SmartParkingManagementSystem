import axiosInstance from "./axiosInstance";


function getToken() {

    return localStorage.getItem("token");

}


// Create Booking

export function createBooking(request) {

    return axiosInstance({

        method: "POST",

        url: "/bookings",

        data: request,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}


// Get My Bookings

export function getMyBookings() {

    return axiosInstance({

        method: "GET",

        url: "/bookings/my",

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}


// Get Booking By Id

export function getBookingById(bookingId) {

    return axiosInstance({

        method: "GET",

        url: `/bookings/${bookingId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}


// Cancel Booking

export function cancelBooking(bookingId) {

    return axiosInstance({

        method: "PUT",

        url: `/bookings/${bookingId}/cancel`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}