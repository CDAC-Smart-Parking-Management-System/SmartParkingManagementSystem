import axiosInstance from "./axiosInstance";

function getToken() {

    return localStorage.getItem("token");

}

export function getAllVehicles() {

    return axiosInstance({

        method: "GET",

        url: "/vehicles",

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function getVehicleById(vehicleId) {

    return axiosInstance({

        method: "GET",

        url: `/vehicles/${vehicleId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function createVehicle(request) {

    return axiosInstance({

        method: "POST",

        url: "/vehicles",

        data: request,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function updateVehicle(vehicleId, request) {

    return axiosInstance({

        method: "PUT",

        url: `/vehicles/${vehicleId}`,

        data: request,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function deleteVehicle(vehicleId) {

    return axiosInstance({

        method: "DELETE",

        url: `/vehicles/${vehicleId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}