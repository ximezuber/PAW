import React, {useEffect, useState} from "react";
import DoctorCalls from "../../api/DoctorCalls";
import ClinicCalls from "../../api/ClinicCalls";
import {Button, Card, Container} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Link, useNavigate} from "react-router-dom";
import DoctorClinicAddModal from "../Modals/DoctorClinicAddModal";
import EditPriceModal from "../Modals/EditPriceModal";
import ApiCalls from "../../api/apiCalls";
import {getPaths} from "../../utils/paginationHelper";
import {CURRENT, NEXT, PREV} from "./Constants";

function DoctorClinics(props) {
    const [clinics, setClinics] = useState([]);
    const [allClinics, setAllClinics] = useState([])
    const [allDoctorClinics, setAllDoctorClinics] = useState([])
    const [paths, setPaths] = useState({})
    const [message, setMessage] = useState("")
    const license = localStorage.getItem('license');
    const {t} = useTranslation()
    const navigate = useNavigate()

    const fetchClinic = async (clinicPath) => {
        const response = await ApiCalls.makeGetCall(clinicPath);
        if (response && response.ok) {
            return response.data
        }
    }

    const handleDCResponse = async (response) => {
        if (response && response.ok) {
            setClinics(response.data)
            const docClinic = response.data
            const getClinics = [];
            for (let i = 0; i < docClinic.length; i++) {
                const clinic = await fetchClinic(docClinic[i].clinic)
                const dc = {
                    consultPrice: docClinic[i].consultPrice,
                    clinic: clinic
                }
                getClinics.push(dc);
            }
            setClinics(getClinics)
            setPaths(getPaths(response.headers.link))
            setMessage("")
        }
        if(response.status === 404) {
            setMessage("errors.docLoggedNotFound")
        }
    }

    const fetchDoctorsClinics = async () => {
        const response = await DoctorCalls.getClinics(license, 0)
        await handleDCResponse(response)
    }

    const fetchPage = async (page) => {
        const response = await ApiCalls.makeGetCall(paths[page])
        await handleDCResponse(response)
    }

    const fetchAllDoctorClinics = async () => {
        const response = await DoctorCalls.getAllClinics(license)
        if (response && response.ok) {
            setAllDoctorClinics(response.data)
            setMessage("")
        }
        if(response.status === 404) {
            setMessage("errors.docLoggedNotFound")
        }
    }

    const fetchAllClinics = async () => {
        const response = await ClinicCalls.getAllClinics();
        if (response && response.ok) {
            setAllClinics(response.data);
        }
    }

    const handleAdd = async (newDocClinic) => {
        const response = await DoctorCalls.addDoctorToClinic(newDocClinic, license);
        if (response && response.ok) {
            await fetchPage(CURRENT)
        }
        if (response.status === 409){
            if (response.data === "doctor-clinic-exists")
                setMessage("errors.alreadySubscribed")
        }
        else {
            handleErrors(response);
        }
    }

    const handleEditPrice = async (clinicId, price) => {
        const response = await DoctorCalls.editPrice(license, clinicId, price)
        if (response && response.ok) {
            await fetchPage(CURRENT)
            await fetchAllDoctorClinics()
            setMessage('')
        } else {
            handleErrors(response);
        }
    }


    const handleDelete = async (clinicId) => {
        const response = await DoctorCalls.deleteDoctorsClinic(license, clinicId)
        if (response && response.ok) {
            await fetchPage(CURRENT)
            await fetchAllDoctorClinics()
            setMessage('')
        } else {
            handleErrors(response);
        }
    }

    const handleErrors = (response) => {
        if (response.status === 404) {
            if (response.data === "doctor-not-found")
                setMessage("errors.docLoggedNotFound")
            if (response.data === "clinic-not-found")
                setMessage("errors.clinicNotFound")
            if (response.data === "doctor-clinic-not-found")
                setMessage("errors.docClinicNotFound")
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }
    useEffect( () => {
        async function fetchData () {
            await fetchDoctorsClinics();
            await fetchAllClinics();
            await fetchAllDoctorClinics();
        }
        fetchData();
    },[])

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
        <>
            <DoctorClinicAddModal
                handleAdd={handleAdd}
                allClinics={allClinics}
                clinics={allDoctorClinics.map(clinic => clinic.id)}
            />
            {message && (
                <div className="form-group">
                    <div className="alert alert-danger" role="alert">
                        {t(message)}
                    </div>
                </div>
            )}
            <Container>
                <div className="admin-info-container admin-clinic-container">
                    {clinics.map((dc) => {
                        return (
                            <Card className="mb-3 shadow" style={{color: "#000", width: '20rem', height: '16.5rem'}} key={dc.id}>
                                <Card.Body>
                                    <Card.Title>{dc.clinic.name}</Card.Title>
                                    <Card.Text>
                                        {dc.clinic.address + ' (' + dc.clinic.location + ')'}
                                    </Card.Text>
                                    <Card.Text>
                                        {t('DOC.price')}: {String(dc.consultPrice)}
                                        <EditPriceModal handleEdit={(newPrice) => handleEditPrice(dc.clinic.id, newPrice)}
                                                        price={dc.consultPrice}
                                        />
                                    </Card.Text>
                                    <Link className="btn btn-outline-dark btn-lg see-prepaid-button shadow-sm"
                                          role="button"
                                          to={`/paw-2019b-4/doctor/${license}/clinics/${dc.clinic.id}/schedule`}>
                                        {t('scheduleButton')}
                                    </Link>
                                </Card.Body>
                                <div className="buttons-div">
                                    <Button className="edit-remove-button remove-button-color shadow-sm"
                                            onClick={() => handleDelete(dc.clinic.id)}
                                    >
                                        {t("deleteButton")}
                                    </Button>
                                </div>

                            </Card>
                        )
                    })}
                </div>
            </Container>
            <div>
                {renderPrevButton()}
                {renderNextButton()}
            </div>
        </>
    )
}

export default DoctorClinics