import axiosInstance from "./axiosInstance";

export function register(registerRequest) {

    return axiosInstance({

        method: "POST",

        url: "/auth/register",

        data: registerRequest

    });

}

export function login(loginRequest) {

    return axiosInstance({

        method: "POST",

        url: "/auth/login",

        data: loginRequest

    });

}