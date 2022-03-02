import api from "./index";
import * as cons from './Constants.js'

const getAllPrepaids = async () => api.get(cons.PREPAIDS_PATH + cons.ALL_PATH)
const getPrepaids = async (pag) => api.get(cons.PREPAIDS_PATH + "?" + cons.PAGE_QUERY + pag)
const deletePrepaid = async (name) => api.delete(
    cons.PREPAIDS_PATH + "/" + name,
    {},
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}})
const addPrepaid = async (data) => api.post(
    cons.PREPAIDS_PATH,
    data,
    {headers: {'X-AUTH-TOKEN': localStorage.getItem('token')}}
);

export default {
    getAllPrepaids,
    getPrepaids,
    deletePrepaid,
    addPrepaid
}

