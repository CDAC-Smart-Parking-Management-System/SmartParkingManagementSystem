import axiosInstance from "./axiosInstance";

export function getAllFloors() {

    const token = localStorage.getItem("token");

    return axiosInstance({

        method: "GET",

        url: "/floors",

        headers: {
            Authorization: `Bearer ${token}`
        }

    });

}

export function createFloor(request) {

    const token = localStorage.getItem("token");

    return axiosInstance({

        method: "POST",

        url: "/floors",

        data: request,

        headers: {
            Authorization: `Bearer ${token}`
        }

    });

}

export function updateFloor(floorId, request) {

    const token = localStorage.getItem("token");

    return axiosInstance({

        method: "PUT",

        url: `/floors/${floorId}`,

        data: request,

        headers: {
            Authorization: `Bearer ${token}`
        }

    });

}

export function getFloorById(floorId) {

    const token = localStorage.getItem("token");

    return axiosInstance({

        method: "GET",

        url: `/floors/${floorId}`,

        headers: {
            Authorization: `Bearer ${token}`
        }

    });

}