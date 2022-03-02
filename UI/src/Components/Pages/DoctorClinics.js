import React, {useEffect, useState} from "react";
import DoctorCalls from "../../api/DoctorCalls";
import ClinicCalls from "../../api/ClinicCalls";
import {Button, Card, Container, Row} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import {Link, useNavigate} from "react-router-dom";
import DoctorClinicAddModal from "../Modals/DoctorClinicAddModal";
import EditPriceModal from "../Modals/EditPriceModal";

function DoctorClinics(props) {
    const [clinics, setClinics] = useState([]);
    const [allClinics, setAllClinics] = useState([])
    const [allDoctorClinics, setAllDoctorClinics] = useState([])
    const [page, setPage] = useState(0);
    const [maxPage, setMaxPage] = useState(0);
    const [message, setMessage] = useState("")
    const license = localStorage.getItem('license');
    const {t} = useTranslation()
    const navigate = useNavigate()

    const fetchDoctorsClinics = async (pag) => {
        const response = await DoctorCalls.getClinics(license, pag)
        if (response && response.ok) {
            setClinics(response.data)
            setMaxPage(Number(response.headers.xMaxPage))
            setMessage("")
        }
        if(response.status === 404) {
            setMessage("errors.docLoggedNotFound")
        }
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
            await fetchDoctorsClinics(page)
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
            await fetchDoctorsClinics(page)
            await fetchAllDoctorClinics()
            setMessage('')
        } else {
            handleErrors(response);
        }
    }


    const handleDelete = async (clinicId) => {
        const response = await DoctorCalls.deleteDoctorsClinic(license, clinicId)
        if (response && response.ok) {
            await fetchDoctorsClinics(page)
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
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            localStorage.removeItem('license')
            localStorage.removeItem('firstName')
            localStorage.removeItem('lastName')
            localStorage.removeItem('specialty')
            navigate('/paw-2019b-4/login')
        }
    }
    useEffect(async () => {
        await fetchDoctorsClinics(page);
        await fetchAllClinics();
        await fetchAllDoctorClinics();
    },[])

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
        </>
    )
}

export default DoctorClinics