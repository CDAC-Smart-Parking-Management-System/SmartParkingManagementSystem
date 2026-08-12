import axiosInstance from "./axiosInstance";

function getToken() {

    return localStorage.getItem("token");

}


// Get All Bookings

export function getAllBookings() {

    return axiosInstance({

        method: "GET",

        url: "/bookings",

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}


// Check In

export function checkIn(bookingId) {

    return axiosInstance({

        method: "POST",

        url: `/entry/${bookingId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}


// Check Out

export function checkOut(bookingId) {

    return axiosInstance({

        method: "POST",

        url: `/exit/${bookingId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}