import axios from 'axios';
import { create } from 'apisauce';
import applyCaseMiddleware from 'axios-case-converter';
import {BASE_URL} from "../Constants";

const options = {
    preservedKeys: ['firstName', 'lastName', 'repeatPassword', 'newPassword',
        'phoneNumber', 'X-AUTH-TOKEN', 'consultPrice', 'profileImage', 'prepaidNumber']
}
const api = applyCaseMiddleware(axios.create({
    baseURL: BASE_URL
}), options);

api.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');

        if (token) {
            const tokenParts = JSON.parse(atob(token.split('.')[1]));

            // exp date in token is expressed in seconds, while now() returns milliseconds:
            const now = Math.ceil(Date.now() / 1000);
            if(tokenParts.exp > now) {
                config.headers['X-AUTH-TOKEN'] = token
            } else {
                localStorage.removeItem('token')
                localStorage.removeItem('role')
            }

        }
        return config;
    },
    error => {
        Promise.reject(error);
    });

export default create({ axiosInstance: api });