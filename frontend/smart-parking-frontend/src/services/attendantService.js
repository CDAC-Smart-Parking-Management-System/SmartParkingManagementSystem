import axiosInstance from "./axiosInstance";

function getToken() {

    return localStorage.getItem("token");

}

export function getAllAttendants() {

    return axiosInstance({

        method: "GET",

        url: "/attendants",

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function createAttendant(request) {

    return axiosInstance({

        method: "POST",

        url: "/attendants",

        data: request,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}

export function deleteAttendant(attendantId) {

    return axiosInstance({

        method: "DELETE",

        url: `/attendants/${attendantId}`,

        headers: {
            Authorization: `Bearer ${getToken()}`
        }

    });

}