import React, {Component, useEffect, useState} from 'react';
import {Button, Card, Container, Modal} from "react-bootstrap";
import '../CardContainer.css'
import SinglePropertyAddModal from "../Modals/SinglePropertyAddModal";
import {useNavigate} from "react-router-dom";
import SpecialtyCalls from "../../api/SpecialtyCalls";
import {useTranslation} from "react-i18next";
import "../../i18n/i18n"
import {getPaths} from "../../utils/paginationHelper";
import ApiCalls from "../../api/apiCalls";
import {CURRENT, NEXT, PREV} from "./Constants";

function Specialties(props){
    const [specialties, setSpecialties] = useState([])
    const [paths, setPaths] = useState({})
    const navigate = useNavigate()
    const [message, setMessage] = useState("")
    const { t } = useTranslation();

    const setPages = (linkHeader) => {
        const paths = getPaths(linkHeader);
        setPaths(paths)
    }

    const fetchSpecialties = async () => {
        const response = await SpecialtyCalls.getSpecialties(0);
        if (response && response.ok) {
            setSpecialties(response.data);
            setPages(response.headers.link);
        }
    }

    useEffect( () => {
        async function fetchData () {
            await fetchSpecialties()
        }
        fetchData();
    }, [])

    const fetchPage = async (page) => {
        const response = await ApiCalls.makeGetCall(paths[page])
        if (response && response.ok) {
            setSpecialties(response.data)
            setPages(response.headers.link)
        }
    }

    const deleteSpecialty = async (name) => {
        const response = await SpecialtyCalls.deleteSpecialty(name);
        if (response && response.ok) {
            await fetchPage(CURRENT)
            setMessage("")
        }
        if (response.status === 404) {
            if (response.data === "specialty-not-found") {
                setMessage("errors.specialtyNotFoundDelete")
            }
        }
        if (response.status === 409) {
            if (response.data === "doctors-dependency") {
                setMessage("errors.doctorDependency")
            }
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    const handleAdd = async (newSpecialty) => {
        const response = await SpecialtyCalls.addSpecialty(newSpecialty);
        if (response && response.ok) {
            await fetchPage(CURRENT)
            setMessage("")
        } else if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        } else if (response.status === 409) {
            if (response.data === "specialty-exists") {
                setMessage("errors.specialtyExists")
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
            <SinglePropertyAddModal handleAdd={handleAdd} property={t("ADMIN.specialty")}/>
            {message && (
                <div className="form-group">
                    <div className="alert alert-danger" role="alert">
                        {t(message)}
                    </div>
                </div>
            )}
            <Container>
                <div className="admin-info-container admin-clinic-prepaid-container">
                    {specialties.map(specialty => {
                        return (
                            <Card className="mb-3 shadow" style={{color: "#000", width: '20rem', height: '7rem'}}
                                  key={specialty.name}>
                                <Card.Body>
                                    <Card.Title>{specialty.name}</Card.Title>
                                </Card.Body>
                                <Button className="remove-button remove-button-color shadow-sm"
                                        onClick={() => deleteSpecialty(specialty.name)}>
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

export default Specialties