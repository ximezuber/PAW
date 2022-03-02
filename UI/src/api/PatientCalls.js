import api from "./index";
import {FAVORITE_PATH, LICENSE_QUERY, PATIENT_PATH} from "./Constants.js";
import * as cons from "./Constants";

const getFavoriteDoctors = async (id, pag) => api.get(PATIENT_PATH + "/" + id + FAVORITE_PATH + "?" + cons.PAGE_QUERY + pag,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
    )
const deleteFavoriteDoctor = async (id, license) => api.delete(
    PATIENT_PATH + "/" + id + FAVORITE_PATH + "?" + LICENSE_QUERY + license,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const isFavorite = async (id, license) => api.get(PATIENT_PATH + "/" + id + FAVORITE_PATH + "/" + license,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
    )
const addFavoriteDoctor = async (id, license) => api.post(
    PATIENT_PATH + "/" + id + FAVORITE_PATH + "?" + LICENSE_QUERY + license,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)

const getProfile = async (id) => api.get(PATIENT_PATH + "/" + id,{},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})

const updateProfile = async (data, id) => api.put(PATIENT_PATH + "/" + id, data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})

export default {
    getFavoriteDoctors,
    deleteFavoriteDoctor,
    isFavorite,
    addFavoriteDoctor,
    updateProfile,
    getProfile
}