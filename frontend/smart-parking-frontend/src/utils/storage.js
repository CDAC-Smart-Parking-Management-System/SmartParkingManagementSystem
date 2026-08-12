export function saveLogin(loginResponse) {

    localStorage.setItem("token", loginResponse.token);
    localStorage.setItem("user", JSON.stringify(loginResponse.user));

}

export function getToken() {

    return localStorage.getItem("token");

}

export function getUser() {

    const user = localStorage.getItem("user");
    return user ? JSON.parse(user) : null;

}

export function clearStorage() {

    localStorage.removeItem("token");
    localStorage.removeItem("user");

}