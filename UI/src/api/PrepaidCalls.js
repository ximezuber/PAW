import api from "./index";
import * as cons from './Constants.js'

const getAllPrepaid = async () => api.get(cons.PREPAID_PATH + cons.ALL_PATH)
const getPrepaid = async (pag) => api.get(cons.PREPAID_PATH + "?" + cons.PAGE_QUERY + pag)
const deletePrepaid = async (name) => api.delete(
    cons.PREPAID_PATH + "/" + name,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const addPrepaid = async (data) => api.post(
    cons.PREPAID_PATH,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
);

export default {
    getAllPrepaid,
    getPrepaid,
    deletePrepaid,
    addPrepaid
}

