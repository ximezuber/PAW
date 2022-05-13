import api from "./index";
import * as cons from './Constants.js'

// Location Endpoints
const getLocations = async (pag) => api.get(cons.LOCATIONS_PATH + "?" + cons.PAGE_QUERY + pag);
const getAllLocations = async () => api.get(cons.LOCATIONS_PATH + cons.ALL_PATH)
const addLocation = async (data) => api.post(
    cons.LOCATIONS_PATH,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
);
const deleteLocation = async (name) => api.delete(
    cons.LOCATIONS_PATH + "/" + name,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const getLocation = async (name) => api.get(cons.LOCATIONS_PATH + "/" + name);
export default {
    getLocations,
    getAllLocations,
    addLocation,
    deleteLocation,
    getLocation
}