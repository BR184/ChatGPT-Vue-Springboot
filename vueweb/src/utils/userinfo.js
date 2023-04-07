const storageKey = "User";

export function saveUser(user) {
    localStorage.setItem(storageKey, JSON.stringify(user))
}

export function getUser() {
    if (JSON.parse(localStorage.getItem(storageKey)) != null)
        return JSON.parse(localStorage.getItem(storageKey))
    else
        return {}
}

export function removeUser() {
    localStorage.removeItem(storageKey)
}

export function getAvaPerfix() {
    return "https://kl-gpt.oss-cn-beijing.aliyuncs.com/avatar/"
}