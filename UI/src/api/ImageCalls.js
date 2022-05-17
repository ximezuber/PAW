import api from "./index";
import * as cons from './Constants.js'

const uploadImage = async (license, img) => api.post(cons.DOCTORS_PATH + "/" + license + cons.IMAGE_PATH,
    img,
    {headers: {
            'Content-Type': 'multipart/form-data'
    }}
);
const getImage = async (license) => api.get(
    cons.DOCTORS_PATH + "/" + license + cons.IMAGE_PATH,
    {},
    {headers: {'Accept': '*'}}
    )

const deleteImage = async (license) => api.delete(
    cons.DOCTORS_PATH + "/" + license + cons.IMAGE_PATH,
    {},
    {}
    )

export default {
    uploadImage,
    getImage,
    deleteImage
}