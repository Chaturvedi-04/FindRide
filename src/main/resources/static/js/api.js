const BASE_URL = "https://YOUR_BACKEND_URL";

export async function apiGet(url) {
    const res = await fetch(BASE_URL + url);
    return res.json();
}

export async function apiPost(url, body = {}) {
    const res = await fetch(BASE_URL + url, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    return res.json();
}

export async function apiPut(url, body = {}) {
    const res = await fetch(BASE_URL + url, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
    });
    return res.json();
}
