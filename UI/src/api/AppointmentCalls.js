import api from "./index";
import * as cons from './Constants.js'

const getAvailableAppointments = async (license) => api.get(
    cons.DOCTORS_PATH + '/' + license + '/' + cons.APPOINTMENT_PATH);

const makeAppointment = async (data) => api.post(
    cons.APPOINTMENT_PATH,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
    )

const getAppointment = async (email, page) => api.get(
    cons.APPOINTMENT_PATH + '?' + cons.USER_QUERY + email + "&" + cons.PAGE_QUERY + page,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)

const deleteAppointment = async (email, license, clinic, year, month, day, time) => api.delete(
    cons.APPOINTMENT_PATH + '?' + cons.USER_QUERY + email + '&' + deleteQueryParams(license, clinic, year,
        month, day, time),
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)

const deleteQueryParams = (license, clinic, year, month, day, time) => {
    return cons.LICENSE_QUERY + license + '&' + cons.CLINIC_QUERY + clinic + '&' + cons.YEAR_QUERY + year + '&' +
        cons.MONTH_QUERY + month + '&' + cons.DAY_QUERY + day + '&' + cons.TIME_QUERY + time;
}
export default {
    getAvailableAppointments,
    makeAppointment,
    getAppointment,
    deleteAppointment
}