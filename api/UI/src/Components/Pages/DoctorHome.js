import React, {useEffect, useState} from "react";
import DoctorCalls from "../../api/DoctorCalls";
import {Button, Col, Container, Row} from "react-bootstrap";
import Image from 'react-bootstrap/Image'
import './DoctorHome.css'
import '../CardContainer.css'
import {useTranslation} from "react-i18next";
import EditDocProfileModal from "../Modals/EditDocProfileModal";
import SpecialtyCalls from "../../api/SpecialtyCalls";
import {useNavigate} from "react-router-dom";
import ImageSelectModal from "../Modals/ImageSelectModal";
import ImageCalls from "../../api/ImageCalls";
import {BASE_URL} from "../../Constants";

function DoctorHome(props) {
    const [doctor, setDoctor] = useState({})
    const [message, setMessage] = useState("")
    const [specialties, setSpecialties] = useState([])
    const {t} = useTranslation()
    const navigate = useNavigate()
    const [image, setImage] = useState(null)

    const fetchDoctor = async () => {
        const email = localStorage.getItem('email')
        if (email) {
            const response = await DoctorCalls.getDocByEmail(email)
            if (response && response.ok) {
                setDoctor(response.data)
                localStorage.setItem('license', response.data.license)
                localStorage.setItem('firstName', response.data.userData.firstName)
                localStorage.setItem('lastName', response.data.userData.lastName)
                localStorage.setItem('specialty', response.data.specialty)
                localStorage.setItem('phone', response.data.phoneNumber)
            }
            if (response.status === 404)
                setMessage("errors.noDocEmail")
        }
        else {
            setMessage("errors.noLoggedDoc")
        }

    }

    const fetchImage = async () => {
        const license = localStorage.getItem('license');
        const response = await ImageCalls.getImage(license)
        if (response && response.ok) {
           setImage(response.data)
        }
        if (response.status === 404) {
            if (response.data === "image-not-found") {
                setImage(null)
            }
            if (response.data === "doctor-not-found") {
                setMessage("errors.docLoggedNotFound")
            }
        }
    }

    const fetchSpecialties = async () => {
        const response = await SpecialtyCalls.getAllSpecialties();
        if (response && response.ok) {
            setSpecialties(response.data)
            setMessage("")
        }
    }

    const getDoctorsFirstName = () => {
        if(doctor.userData === undefined)
            return "Doctor!";
        else
            return doctor.userData.firstName + " "
    }

    const getDoctorsLastName = () => {
        if(doctor.userData !== undefined)
            return doctor.userData.lastName
        return ''
    }

    const handleEdit = async (doctor) => {
        const license = localStorage.getItem('license')
        const response = await DoctorCalls.editDoctor(license, doctor)
        if (response && response.ok) {
            await fetchDoctor()
            setMessage("")
        }
        if (response.status === 404) {
            setMessage('errors.doctorNotFoundEdit')
        }
        if (response.status === 401) {
            handleUnauth()
        }
    }

    const handleUpload = async (formData) => {
        const response = await ImageCalls.uploadImage(localStorage.getItem("license"), formData)
        if (response && response.ok) {
            await fetchImage()
            setMessage("")
            window.location.reload()
        }
        if (response.status === 401) {
            handleUnauth();
        }
        if (response.status === 404) {
            if (response.data === "doctor-not-found") {
                setMessage("errors.docLoggedNotFound")
            }
        }
        if (response.status === 400) {
            if (response.data === "image-info-missing") {
                setMessage("errors.imageBroken")
            }
        }
        if (response.status === 415) {
            if (response.data === "image-type-not-supported") {
                setMessage("errors.notSupported")
            }
        }

    }

    const handleDeleteImage = async () => {
        const response = await ImageCalls.deleteImage(localStorage.getItem("license"));
        if (response && response.ok) {
            setImage(null)
            setMessage("")
        }
        if (response.status === 401) {
            handleUnauth()
        }
        if (response.status === 404) {
            if (response.data === "doctor-not-found") {
                setMessage("errors.docLoggedNotFound")
            }
        }
    }

    const handleUnauth = () => {
        localStorage.removeItem('token')
        localStorage.removeItem('role')
        localStorage.removeItem('license')
        localStorage.removeItem('firstName')
        localStorage.removeItem('lastName')
        localStorage.removeItem('specialty')
        localStorage.removeItem('phone')
        navigate('/paw-2019b-4/login')
    }


    useEffect(async () => {
        await fetchDoctor();
        await fetchSpecialties();
        await fetchImage();
    }, [])

    return (
        <>
            <Container>
                <Row>
                    <Col>
                        <h3 className="mt-3">
                            {t('welcome')}, {getDoctorsFirstName()}{getDoctorsLastName()}!
                        </h3>
                        <hr />
                    </Col>
                </Row>
                {message && (
                    <div className="form-group">
                        <div className="alert alert-danger" role="alert">
                            {t(message)}
                        </div>
                    </div>
                )}
                <Row>
                    <Col className="info-col">
                        <h4>
                            {t('NAVBAR.profile')}
                        </h4>
                        <div className="info-label">
                            <b>{t('FORM.name')}:</b> {getDoctorsFirstName()}{getDoctorsLastName()}
                        </div>
                        <div className="info-label">
                            <b>{t('FORM.email')}:</b> {localStorage.getItem('email')}
                        </div>
                        <div className="info-label">
                            <b>{t('DOC.license')}:</b> {doctor.license}
                        </div>
                        <div className="info-label">
                            <b>{t('FORM.phoneNumber')}:</b> {doctor.phoneNumber}
                        </div>
                        <div className="info-label">
                            <b>{t('ADMIN.specialty')}:</b> {doctor.specialty}
                        </div>
                        <EditDocProfileModal specialties={specialties.map(specialty=> specialty.name)}
                                             handleEdit={handleEdit}
                        />
                    </Col>
                    <Col>
                        <img className="img-size"
                             src={image === null?
                                 "/paw-2019b-4/images/docpic.jpg": BASE_URL + "/doctors/" + localStorage.getItem('license') +"/image"} />
                        <div className="mt-3">
                            <ImageSelectModal handleUpload={handleUpload} />
                            <Button className="mx-3 shadow-sm doc-button-color" onClick={handleDeleteImage}> {t('deleteImgButton')}</Button>
                        </div>
                    </Col>
                </Row>
                <hr/>
                <Row>
                </Row>
            </Container>
        </>
    )
}
export default DoctorHome