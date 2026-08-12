import { toast } from "react-toastify";

/**
 * Centralized toast notification helpers.
 * Every success / error / warning / info notification in the app
 * should be triggered through these helpers so behaviour and
 * styling stay consistent across the whole project.
 */

const baseOptions = {
    position: "top-center",
};

export function showSuccess(message, options = {}) {
    toast.success(message, { ...baseOptions, ...options });
}

export function showError(message, options = {}) {
    toast.error(message, { autoClose: 4000, ...baseOptions, ...options });
}

export function showWarning(message, options = {}) {
    toast.warning(message, { ...baseOptions, ...options });
}

export function showInfo(message, options = {}) {
    toast.info(message, { ...baseOptions, ...options });
}

/**
 * Pulls the most useful, human readable message out of an Axios
 * error so every catch block can show something meaningful instead
 * of a generic failure message.
 *
 * @param {*} error - the error thrown by an axios call
 * @param {string} fallback - message to use if nothing better is found
 */
export function getErrorMessage(error, fallback = "Something went wrong. Please try again.") {

    if (!error) {
        return fallback;
    }

    // No response at all -> network / server unreachable / CORS / timeout
    if (!error.response) {

        if (error.code === "ECONNABORTED") {
            return "Request timed out. Please try again.";
        }

        if (error.message === "Network Error") {
            return "Unable to reach the server. Please check your internet connection.";
        }

        return error.message || fallback;
    }

    const data = error.response.data;

    if (typeof data === "string" && data.trim().length > 0) {
        return data;
    }

    if (data?.message) {
        return data.message;
    }

    if (data?.error) {
        return data.error;
    }

    // Validation error maps, e.g. { fieldErrors: { email: "already exists" } }
    if (data?.errors && typeof data.errors === "object") {
        const firstKey = Object.keys(data.errors)[0];
        if (firstKey) {
            const value = data.errors[firstKey];
            return Array.isArray(value) ? value[0] : String(value);
        }
    }

    switch (error.response.status) {
        case 400:
            return fallback || "Invalid request. Please check the details and try again.";
        case 401:
            return "Your session has expired. Please login again.";
        case 403:
            return "You don't have permission to perform this action.";
        case 404:
            return "The requested resource was not found.";
        case 409:
            return "This action conflicts with existing data.";
        case 500:
            return "Server error. Please try again later.";
        default:
            return fallback;
    }

}
