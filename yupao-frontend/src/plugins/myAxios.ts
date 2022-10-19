import axios from "axios";

const isDev = process.env.NODE_ENV === 'development'
// Set config defaults when creating the instance
const myAxios = axios.create({
    baseURL: 'http://localhost:8080/api'
});

myAxios.defaults.withCredentials =true;

// Add a request interceptor
myAxios.interceptors.request.use(function (config) {
    // Do something before request is sent
    console.log("发送请求："+ config)
    return config;
}, function (error) {
    // Do something with request error
    return Promise.reject(error);
});


// Add a response interceptor
myAxios.interceptors.response.use(function (response) {
    console.log("收到响应："+ response)
    //调整
    if(response?.data?.code === 40100){
        const redirectUrl = window.location.href;
        window.location.href = `/user/login?redirect=${redirectUrl}`;
    }

    // Do something with response data
    return response.data;
}, function (error) {
    // Do something with response error
    return Promise.reject(error);
});

export default  myAxios;


