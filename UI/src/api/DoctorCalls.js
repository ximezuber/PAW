import api from "./index";
import * as cons from './Constants.js'

const getDoctorsAdmin = async (pag) => api.get(
    cons.DOCTORS_PATH + cons.ALL_PATH + "?" + cons.PAGE_QUERY + pag);
const searchDocs = async (pag, location, specialty, firstName, lastName, consultPrice, prepaid) => api.get(
    cons.DOCTORS_PATH + "?" + cons.PAGE_QUERY + pag
    + getSearchQuery(location, specialty, firstName, lastName, consultPrice, prepaid))
const addDoctor = async (data) => api.post(
    cons.DOCTORS_PATH,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
);
const deleteDoctor = async (license) => api.delete(
    cons.DOCTORS_PATH + '/' + license,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const editDoctor = async (license, data) => api.put(
    cons.DOCTORS_PATH + "/" + license,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
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
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)
const editPrice = async (license, clinicId, price) => api.put(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "/" + clinicId + "?" + cons.PRICE_QUERY + price,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)
const getSchedule = async (license) => api.get(
    cons.DOCTORS_PATH + "/" + license + "/schedules")
const addSchedule = async (license, clinicId, day, hour) => api.post(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "/" + clinicId + "/schedules",
    {
        day: day,
        hour: hour
    },
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)
const deleteSchedule = async (license, clinicId, day, hour) => api.delete(
    cons.DOCTORS_PATH + "/" + license + cons.CLINICS_PATH + "/" + clinicId + "/schedules"
    + "?" + cons.DAY_QUERY + day + "&" + cons.HOUR_QUERY + hour,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)

const getSearchQuery = (location, specialty, firstName, lastName, consultPrice, prepaid) => {
    const queryParams = [];
    if (location !== undefined & location !== null && location !== "") {
        queryParams.push("location=" + location)
    }
    if (specialty !== undefined & specialty !== null && specialty !== "") {
        queryParams.push("specialty=" + specialty)
    }
    if (firstName !== undefined & firstName !== null && firstName !== "") {
        queryParams.push("firstName=" + firstName)
    }
    if (lastName !== undefined & lastName !== null && lastName !== "") {
        queryParams.push("lastName=" + lastName)
    }
    if (consultPrice !== undefined & consultPrice !== null && consultPrice !== 0) {
        queryParams.push("consultPrice=" + consultPrice)
    }
    if (prepaid !== undefined & prepaid !== null && prepaid !== "") {
        queryParams.push("prepaid=" + prepaid)
    }

    let query = "";

    for (let i = 0; i < queryParams.length; i++) {
        query = query + "&" + queryParams[i];
    }

    return query;
}


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