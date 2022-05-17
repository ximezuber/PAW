import api from "./index";
import * as cons from './Constants.js'

// Specialties calls
const getSpecialties = async (pag) => api.get(cons.SPECIALTIES_PATH + "?" + cons.PAGE_QUERY + pag);
const getAllSpecialties = async () => api.get(cons.SPECIALTIES_PATH + cons.ALL_PATH);
const addSpecialty = async (data) => api.post(
    cons.SPECIALTIES_PATH,
    data,
    {}
);

const deleteSpecialty = async (name) => api.delete(cons.SPECIALTIES_PATH + "/" + name,
    {},
    {})

export default {
    getSpecialties,
    getAllSpecialties,
    addSpecialty,
    deleteSpecialty
}
