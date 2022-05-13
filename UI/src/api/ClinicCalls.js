import api from "./index";
import * as cons from './Constants.js'
import {PREPAID_PATH} from "./Constants.js";

// Clinic calls and clinic prepaids
const getClinics = async (pag) => api.get(cons.CLINICS_PATH + "?" + cons.PAGE_QUERY + pag);
const getAllClinics = async () => api.get(cons.CLINICS_PATH  + cons.ALL_PATH);
const getClinic = async (id) => api.get(cons.CLINICS_PATH + "/" + id)
const getClinicPrepaid = async (id, pag) =>
    api.get(cons.CLINICS_PATH + "/" + id + PREPAID_PATH + "?" + cons.PAGE_QUERY + pag)
const getAllClinicPrepaid = async (id) => api.get(cons.CLINICS_PATH + "/" + id + PREPAID_PATH + cons.ALL_PATH )
const addClinic = async (data) => api.post(
    cons.CLINICS_PATH,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)
const addClinicPrepaid = async (clinicId, prepaidId) => api.put(
    cons.CLINICS_PATH + "/" + clinicId + PREPAID_PATH + '/' + prepaidId,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const editClinic = async (id, data) => api.put(
    cons.CLINICS_PATH + "/" + id,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
    )
const deleteClinic = async (id) => api.delete(
    cons.CLINICS_PATH + "/" + id,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const deleteClinicPrepaid = async (clinicId, prepaidId) => api.delete(
    cons.CLINICS_PATH + "/" + clinicId + PREPAID_PATH + "/" + prepaidId,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})

export default {
    getClinics,
    getAllClinics,
    getClinic,
    getClinicPrepaid,
    getAllClinicPrepaid,
    addClinic,
    addClinicPrepaid,
    editClinic,
    deleteClinic,
    deleteClinicPrepaid
}