const storageKey = "User";

export function saveUser(user){
    localStorage.setItem(storageKey,JSON.stringify(user))
}

export function getUser(){
    return JSON.parse(localStorage.getItem(storageKey))
}

export function removeUser(){
    localStorage.removeItem(storageKey)
}