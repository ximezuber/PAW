import React, { useState, useEffect } from 'react';
import {Button, Card, Container} from "react-bootstrap";
import '../CardContainer.css'
import DoctorAddModal from "../Modals/DoctorAddModal";
import DoctorCalls from "../../api/DoctorCalls";
import ApiCalls from "../../api/apiCalls";
import SpecialtyCalls from "../../api/SpecialtyCalls";
import {useNavigate} from "react-router-dom";
import "../../i18n/i18n";
import {useTranslation} from "react-i18next";
import {getPaths} from "../../utils/paginationHelper";
import '../Pages/Constants'
import {CURRENT, NEXT, PREV} from "./Constants";


function Doctors(props) {
    const [doctors, setDoctors] = useState([])
    const [specialties, setSpecialties] = useState([])
    const [paths, setPaths] = useState({})
    const [message, setMessage] = useState("")
    const navigate = useNavigate()
    const { t } = useTranslation();

    const setPages = (linkHeader) => {
        const paths = getPaths(linkHeader);
        setPaths(paths)
    }

    const fetchDoctors = async () => {
        const response = await DoctorCalls.getDoctorsAdmin(0)
        if (response && response.ok) {
            setDoctors(response.data)
            setPages(response.headers.link)
        }
    }

    const fetchPage = async (page) => {
        const response = await ApiCalls.makeGetCall(paths[page])
        if (response && response.ok) {
            setDoctors(response.data)
            setPages(response.headers.link)
        }
    }

    const fetchSpecialties = async () => {
        const response = await SpecialtyCalls.getAllSpecialties()
        if (response && response.ok) {
            setSpecialties(response.data)
        }
    }

    useEffect(() => {
        async function fetchData () {
            await fetchDoctors()
            await fetchSpecialties()
        }
        fetchData();
    }, [])

    const handleAdd = async (newDoctor) => {
        const response = await DoctorCalls.addDoctor(newDoctor);
        if (response && response.ok) {
            await fetchPage(CURRENT)
            setMessage("")

        } else if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        } else if (response.status === 409) {
            if (response.data === 'license-in-use')
                setMessage("errors.licenseInUse")
            if (response.data === 'email-in-use')
                setMessage("errors.emailInUse")
        } else if (response.status === 404) {
            if (response.data === "specialty-not-found") {
                setMessage("errors.specialtyNotFound")
            }
        } else if (response.status === 400) {
            if (response.data === "password-mismatch") {
                setMessage("errors.passwordMismatch")
            }
        }
    }

    const deleteDoctors = async (license) => {
        const response = await DoctorCalls.deleteDoctor(license)
        if (response && response.status === 204) {
            await fetchPage(CURRENT)
            setMessage("")
        }
        else if (response.status === 404) {
            if (response.data === "doctor-not-found") {
                setMessage("errors.doctorsNotFound")
            }
        } else if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    const nextPage = async () => {
        setMessage("")
        await fetchPage(NEXT)

    }
    const prevPage = async () => {
        setMessage("")
        await fetchPage(PREV)
    }

    const renderPrevButton = () => {
        if (paths[PREV]) {
            return <Button className="doc-button doc-button-color shadow-sm"
                           onClick={() => prevPage()}>{t('prevButton')}</Button>
        }
    }

    const renderNextButton = () => {
        if (paths[NEXT]) {
            return <Button className="doc-button doc-button-color shadow-sm"
                           onClick={() => nextPage()}>{t('nextButton')}</Button>
        }
    }

    return (
        <div className="background">
            <DoctorAddModal
                handleAdd={handleAdd}
                specialties={specialties.map(specialty => specialty.name)}
            />
            {message && (
                <div className="form-group">
                    <div className="alert alert-danger" role="alert">
                        {t(message)}
                    </div>
                </div>
            )}
            <Container>
                <div className="admin-info-container admin-doctor-container">
                    {doctors.map((doctor) => {
                        return (
                            <Card className="mb-3 doc-card shadow"
                                  key={doctor.license}>
                                <Card.Body>
                                    <Card.Title><b>{doctor.firstName + ' ' + doctor.lastName}</b></Card.Title>
                                    <Card.Text>
                                        <b>{t('DOC.license')}</b>: {doctor.license}
                                    </Card.Text>
                                </Card.Body>
                                <Button className="remove-button remove-button-color shadow-sm"
                                        onClick={() => deleteDoctors(doctor.license)}>
                                    {t('deleteButton')}
                                </Button>
                            </Card>
                        )
                    })}
                </div>
            </Container>
            {renderPrevButton()}
            {renderNextButton()}
        </div>
    )
}

export default Doctors