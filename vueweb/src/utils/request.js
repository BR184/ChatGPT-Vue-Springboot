import axios from "axios";

const request = axios.create({
    baseURL:'http://localhost:8081',
    timeout:5000,
})
request.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('Token')
        if(token!=null) config.headers.Token = token
        return config;
    },
    (error) => {
        return Promise.reject(error)
    }
)

export default request