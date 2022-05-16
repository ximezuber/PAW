import api from "./index";
import * as cons from './Constants.js'

const getAvailableAppointments = async (license) => api.get(
    cons.DOCTORS_PATH + '/' + license + cons.APPOINTMENT_PATH);

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

const deleteAppointment = async (email, id) => api.delete(
    cons.APPOINTMENT_PATH + "/" + id + '?' + cons.USER_QUERY + email,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
)


export default {
    getAvailableAppointments,
    makeAppointment,
    getAppointment,
    deleteAppointment
}