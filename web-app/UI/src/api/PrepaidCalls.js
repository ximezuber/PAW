import api from "./index";
import * as cons from './Constants.js'

const getAllPrepaid = async () => api.get(cons.PREPAID_PATH + cons.ALL_PATH)
const getPrepaid = async (pag) => api.get(cons.PREPAID_PATH + "?" + cons.PAGE_QUERY + pag)
const deletePrepaid = async (name) => api.delete(
    cons.PREPAID_PATH + "/" + name,
    {},
    {})
const addPrepaid = async (data) => api.post(
    cons.PREPAID_PATH,
    data,
    {}
);

export default {
    getAllPrepaid,
    getPrepaid,
    deletePrepaid,
    addPrepaid
}

