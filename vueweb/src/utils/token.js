const storageKey = "Token";

export function saveToken(token){
    localStorage.setItem(storageKey,token)
}

export function getToken(){
    return localStorage.getItem(storageKey)
}

export function removeToken(){
    localStorage.removeItem(storageKey)
}
