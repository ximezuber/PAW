import api from "./index";
import * as cons from './Constants.js'

const login = async (email, password) => {
    localStorage.setItem('email', email)
    const params = new URLSearchParams();
    params.append('email', email);
    params.append('password', password);
    return api.post(cons.LOGIN_PATH, params)
        .then(resp => {
            if(resp.status === 200) {
                localStorage.setItem('token', resp.headers.xAuthToken)
            }

            return resp
        })
}

const signUp = async (data) => {
    return api.post(cons.PATIENT_PATH, data)
}

const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('email')
    localStorage.removeItem('license')
    localStorage.removeItem('firstName')
    localStorage.removeItem('lastName')
    localStorage.removeItem('specialty')
    localStorage.removeItem('phone')
    localStorage.removeItem('role')
    localStorage.removeItem('pathCurrent')
}

const makeGetCall = async (path) => api.get(path);

const makeAuthGetCall = async (path) => api.get(path, {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}});

export default {
    login,
    signUp,
    logout,
    makeGetCall,
    makeAuthGetCall
}