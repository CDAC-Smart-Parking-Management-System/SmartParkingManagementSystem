import axiosInstance from "./axiosInstance";

function getToken() {

    return localStorage.getItem("token");

}

export function getAllParkingRates() {

    return axiosInstance({

        method: "GET",

        url: "/parking-rates",

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function getParkingRateById(rateId) {

    return axiosInstance({

        method: "GET",

        url: `/parking-rates/${rateId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function createParkingRate(request) {

    return axiosInstance({

        method: "POST",

        url: "/parking-rates",

        data: request,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function updateParkingRate(rateId, request) {

    return axiosInstance({

        method: "PUT",

        url: `/parking-rates/${rateId}`,

        data: request,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}