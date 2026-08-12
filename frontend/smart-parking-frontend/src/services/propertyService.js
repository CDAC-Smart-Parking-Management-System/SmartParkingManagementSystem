import axiosInstance from "./axiosInstance";

export function getAllProperties() {

    const token = localStorage.getItem("token");

    return axiosInstance({

        method: "GET",

        url: "/properties/all",

        headers: {
            Authorization: `Bearer ${token}`
        }

    });

}

export function getMyProperty() {

    const token = localStorage.getItem("token");

    return axiosInstance({
        method: "GET",
        url: "/properties",
        headers: {
            Authorization: `Bearer ${token}`
        }
    });

}

export function getPropertyById(propertyId) {

    const token = localStorage.getItem("token");

    return axiosInstance({

        method: "GET",

        url: `/properties/${propertyId}`,

        headers: {
            Authorization: `Bearer ${token}`
        }

    });

}

export function updateProperty(propertyId, request) {

    const token = localStorage.getItem("token");

    return axiosInstance({

        method: "PUT",

        url: `/properties/${propertyId}`,

        data: request,

        headers: {
            Authorization: `Bearer ${token}`
        }

    });

}