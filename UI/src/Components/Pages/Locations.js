import React, {useEffect, useState} from 'react';
import {Button, Card, Container} from "react-bootstrap";
import '../CardContainer.css'
import SinglePropertyAddModal from "../Modals/SinglePropertyAddModal";
import LocationCalls from "../../api/LocationCalls";
import {useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";
import "../../i18n/i18n"
import {getPaths} from "../../utils/paginationHelper";
import ApiCalls from "../../api/apiCalls";
import {CURRENT, NEXT, PREV} from "./Constants";

function Locations(props){
    const [locations, setLocations] = useState([])
    const [paths, setPaths] = useState({})
    const navigate = useNavigate()
    const [message, setMessage] = useState("")
    const { t } = useTranslation();

    const setPages = (linkHeader) => {
        const paths = getPaths(linkHeader);
        setPaths(paths)
    }

    const fetchLocations = async () => {
        const response = await LocationCalls.getLocations(0)
        if (response && response.ok){
            setLocations(response.data)
            setPages(response.headers.link)
        }
    }

     useEffect( () => {
         async function fetchData () {
             await fetchLocations()
         }
         fetchData();
    }, [])

    const fetchPage = async (page) => {
        const response = await ApiCalls.makeGetCall(paths[page])
        if (response && response.ok) {
            setLocations(response.data)
            setPages(response.headers.link)
        }
    }

    const deleteLocation = async (name) => {
        const response = await LocationCalls.deleteLocation(name);
        if (response && response.ok) {
            await fetchPage(CURRENT)
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
            await fetchPage(CURRENT)
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
        setMessage("")
        await fetchPage(NEXT)
    }
    const prevPage = async () => {
        setMessage("")
        await fetchPage(PREV)
    }

    const renderPrevButton = () => {
        if (paths[PREV]) {
            return <Button className="remove-button doc-button-color shadow-sm"
                           onClick={() => prevPage()}>{t("prevButton")}</Button>
        }
    }

    const renderNextButton = () => {
        if (paths[NEXT]) {
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