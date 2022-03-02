import React, {Component, useEffect, useState} from 'react';
import {Button, Card, Container} from "react-bootstrap";
import '../CardContainer.css'
import SinglePropertyAddModal from "../Modals/SinglePropertyAddModal";
import LocationCalls from "../../api/LocationCalls";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import "../../i18n/i18n"

function Locations(props){
    const [locations, setLocations] = useState([])
    const [page, setPage] = useState(0)
    const [maxPage, setMaxPage] = useState(0)
    const navigate = useNavigate()
    const [message, setMessage] = useState("")
    const { t } = useTranslation();

    const fetchLocations = async (pag) => {
        const response = await LocationCalls.getLocations(pag)
        if (response && response.ok){
            setLocations(response.data)
            setMaxPage(response.headers.xMaxPage)
        }
    }

     useEffect(async () => {
        await fetchLocations(page)
    }, [])

    const deleteLocation = async (name) => {
        const response = await LocationCalls.deleteLocation(name);
        if (response && response.ok) {
            await fetchLocations(page)
            setMessage("")
        }
        if (response.status === 404) {
            if (response.data === "location-not-found") {
                setMessage("errors.LocationNotFoundDelete")
            }
        }
        if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        }
        if (response.status === 409) {
            if (response.data === "clinics-dependency") {
                setMessage("errors.clinicDependency")
            }
        }
    }

    const handleAdd = async (newLocation) => {
        const response = await LocationCalls.addLocation(newLocation);
        if (response && response.ok) {
            await fetchLocations(page)
            setMessage("")
        } else if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        } else if (response.status === 409) {
            if (response.data === "location-exists") {
                setMessage("errors.locationExists")
            }
        }
    }

    const nextPage = async () => {
        const newPage = page + 1
        setPage(newPage)
        setMessage("")
        await fetchLocations(newPage)
    }
    const prevPage = async () => {
        const newPage = page - 1
        setPage(newPage)
        setMessage("")
        await fetchLocations(newPage)
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
            <SinglePropertyAddModal handleAdd={handleAdd} property={t("ADMIN.location")}/>
            {message && (
                <div className="form-group">
                    <div className="alert alert-danger" role="alert">
                        {t(message)}
                    </div>
                </div>
            )}
            <Container>
                <div className="admin-info-container admin-clinic-prepaid-container">
                    {locations.map(location => {
                        return (
                            <Card className="mb-3 shadow"
                                  style={{color: "#000", width: '20rem', height: '7rem'}} key={location.name}>
                                <Card.Body>
                                    <Card.Title>{location.name}</Card.Title>
                                </Card.Body>
                                <Button className="remove-button remove-button-color shadow-sm"
                                        onClick={() => deleteLocation(location.name)}>
                                    {t("deleteButton")}
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

export default Locations