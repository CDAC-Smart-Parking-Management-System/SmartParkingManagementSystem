import axiosInstance from "./axiosInstance";


function getToken() {

    return localStorage.getItem("token");

}


// Get all available slots

export function getAvailableSlots() {

    return axiosInstance({

        method: "GET",

        url: "/slots/available",

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function getSlotsByProperty(propertyId) {

    return axiosInstance({

        method: "GET",

        url: `/slots/property/${propertyId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}


// Get slots by floor

export function getSlotsByFloor(floorId) {

    return axiosInstance({

        method: "GET",

        url: `/slots/floor?floorId=${floorId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}