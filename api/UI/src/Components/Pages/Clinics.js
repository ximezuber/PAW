import React, {Component, useEffect, useState} from 'react';
import {Button, Card, Container} from "react-bootstrap";
import '../CardContainer.css'
import ClinicEditModal from "../Modals/ClinicEditModal";
import {Link, useNavigate} from "react-router-dom";
import ClinicCalls from "../../api/ClinicCalls";
import LocationCalls from "../../api/LocationCalls";
import {useTranslation} from "react-i18next";
import "../../i18n/i18n";

function Clinics(props) {

    const [clinics, setClinics] = useState([])
    const [show, setShow] = useState(false)
    const [editableClinic, setEditableClinic] = useState( {id: ' ', name: ' ', address: ' ', location: ' '})
    const [editIndex, setEditIndex] = useState(-1)
    const [action, setAction] = useState("Add")
    const [locations, setLocations] = useState([])
    const [page, setPage] = useState(0)
    const [maxPage, setMaxPage] = useState(0)
    const [message, setMessage] = useState("")
    const navigate = useNavigate()
    const { t } = useTranslation();

    const fetchClinics = async (pag) => {
        const response = await ClinicCalls.getClinics(pag)
        if (response && response.ok) {
            setClinics(response.data)
            setMaxPage(Number(response.headers.xMaxPage))
        }
    }

    const fetchLocation = async () => {
        const response = await LocationCalls.getAllLocations()
        if (response && response.ok) {
            setLocations(response.data)
            setMessage("")
        }
    }

    useEffect( async () => {
        await fetchClinics(page)
        await fetchLocation()
    }, [])

    const deleteClinic = async (id) => {
        const response = await ClinicCalls.deleteClinic(id);
        if (response && response.ok)
            await fetchClinics(page);
        if (response.status === 404) {
            if (response.data === "clinic-not-found")
                setMessage("errors.clinicNotFoundDelete")
        } else if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        }
    }

    const handleAdd = async (newClinic) => {
        const data = {
            name: newClinic.name,
            address: newClinic.address,
            location: newClinic.location
        }
        const response = await ClinicCalls.addClinic(data);
        setShow(false)
        if (response && response.ok) {
            await fetchClinics(page);
            setMessage("")
        }
        if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        }
        if (response.status === 404) {
            if (response.data === "location-not-found") {
                setMessage("errors.locationNotFound")
            }
        }
        if (response.status === 409) {
            if (response.data === "clinic-exists") {
                setMessage("errors.clinicsExists")
            }
        }
    }

    const handleEdit = async (editedClinic) => {
        const data = {
            name: editedClinic.name,
            address: editedClinic.address,
            location: editedClinic.location
        }
        const response = await ClinicCalls.editClinic(editedClinic.id, data)
        if (response && response.ok) {
            await fetchClinics(page);
            setShow(false)
            setMessage("")
        }
        if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        }
        if (response.status === 404) {
            if (response.data === "clinic-not-found") {
                setMessage("errors.clinicNotFoundEdit")
            }
        }
    }

    const handleShow = (index) => {
        let action
        let clinic
        if (index === -1) {
            action = "add"
            clinic = {id: ' ', name: ' ', address: ' ', location: ' '}
        } else {
            action = "edit"
            clinic = clinics[index]
        }

        setEditableClinic(clinic)
        setEditIndex(index)
        setShow(true)
        setAction(action)
    }

    const hideModal = () => {
        setShow(false)
    }

    const nextPage = async () => {
        const newPage = page + 1
        await setPage(newPage)
        setMessage("")
        await fetchClinics(newPage)

    }
    const prevPage = async () => {
        const newPage = page - 1
        await setPage(newPage)
        setMessage("")
        await fetchClinics(newPage)
    }

    const renderPrevButton = () => {
        if (page !== 0) {
            return <Button className="remove-button doc-button-color shadow-sm"
                           onClick={() => prevPage()}>{t("prevButton")}</Button>
        }
    }

    const renderNextButton = () => {
        if (page < maxPage) {
            return <Button className="remove-button doc-button-color shadow-sm"
                           onClick={() => nextPage()}>{t("nextButton")}</Button>
        }
    }

    return (
        <div className="background">
            <Button variant="outline-secondary"
                    onClick={() => handleShow(-1)}
                    size="lg"
                    className="add-margin">
                {t("CLINIC.addClinic")}
            </Button>
            {message && (
                <div className="form-group">
                    <div className="alert alert-danger" role="alert">
                        {t(message)}
                    </div>
                </div>
            )}
            {show && <ClinicEditModal show={show}
                             clinic={editableClinic}
                             handleAdd={handleAdd}
                             handleEdit={handleEdit}
                             action={action}
                             hideModal={hideModal}
                             locations={locations.map(location => location.name)}
            /> }
            <Container>
                <div className="admin-info-container admin-clinic-container">
                    {clinics.map(( clinic, index) => {
                        return (
                            <Card className="mb-3 shadow" style={{color: "#000", width: '20rem', height: '15rem'}} key={clinic.id}>
                                <Card.Body>
                                    <Card.Title>{clinic.name}</Card.Title>
                                    <Card.Text>
                                        {clinic.address + ' (' + clinic.location + ')'}
                                    </Card.Text>
                                </Card.Body>
                                <Link className="btn btn-outline-dark btn-lg see-prepaid-button shadow-sm"
                                      role="button"
                                      to={'/paw-2019b-4/admin/clinics/' + clinic.id + '/prepaids'}>{t("ADMIN.seePrepaids")}
                                </Link>
                                <div className="buttons-div">
                                    <Button className="edit-remove-button remove-button-color shadow-sm"
                                            onClick={() => deleteClinic(clinic.id)}>
                                        {t("deleteButton")}
                                    </Button>
                                    <Button className="btn edit-remove-button edit-button doc-button-color shadow-sm"
                                            onClick={() => handleShow(index)}>
                                        <i className="far fa-edit"/>
                                    </Button>
                                </div>

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

export default Clinics