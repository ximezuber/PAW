import api from "./index";
import * as cons from './Constants.js'

const getDoctorsAdmin = async (pag) => api.get(
    cons.DOCTORS_PATH + cons.ALL_PATH + "?" + cons.PAGE_QUERY + pag);
const searchDocs = async (pag, params) => api.get(cons.DOCTORS_PATH + "?" + cons.PAGE_QUERY + pag + params)
const addDoctor = async (data) => api.post(
    cons.DOCTORS_PATH,
    data,
    {}
);
const deleteDoctor = async (license) => api.delete(
    cons.DOCTORS_PATH + '/' + license,
    {},
    {})
const editDoctor = async (license, data) => api.put(
    cons.DOCTORS_PATH + "/" + license,
    data,
    {}
    )

const getClinics = async (license, pag) =>
    api.get (cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "?" + cons.PAGE_QUERY + pag)
const getAllClinics = async (license) =>
    api.get (cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + cons.ALL_PATH)
const getDocByEmail = async (email) => api.get (cons.DOCTORS_PATH + cons.EMAIL_PATH + "/" + email)
const getDocByLicense = async (license) => api.get(cons.DOCTORS_PATH + "/" + license)
const addDoctorToClinic = async (data, license) => api.post(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const deleteDoctorsClinic = async (license, clinic) => api.delete(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "/" + clinic,
    {},
    {}
)
const editPrice = async (license, clinicId, price) => api.put(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "/" + clinicId + "?" + cons.PRICE_QUERY + price,
    {},
    {}
)
const getSchedule = async (license) => api.get(
    cons.DOCTORS_PATH + "/" + license + "/schedules")
const addSchedule = async (license, clinicId, day, hour) => api.post(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "/" + clinicId + "/schedules",
    {
        day: day,
        hour: hour
    },
    {}
)
const deleteSchedule = async (license, clinicId, day, hour) => api.delete(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "/" + clinicId + "/schedules"
    + "?" + cons.DAY_QUERY + day + "&" + cons.HOUR_QUERY + hour,
    {},
    {}
)


export default {
    getDoctorsAdmin,
    searchDocs,
    addDoctor,
    deleteDoctor,
    editDoctor,
    getClinics,
    getAllClinics,
    getDocByEmail,
    getDocByLicense,
    addDoctorToClinic,
    deleteDoctorsClinic,
    editPrice,
    getSchedule,
    addSchedule,
    deleteSchedule,
}