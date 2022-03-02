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

export default create({ axiosInstance: api });