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
            config.headers['X-AUTH-TOKEN'] = token
        }
        return config;
    },
    error => {
        Promise.reject(error);
    });

export default create({ axiosInstance: api });