import React, {Component, useEffect, useState} from 'react';
import {Button, Card, Container, Modal} from "react-bootstrap";
import '../CardContainer.css'
import SinglePropertyAddModal from "../Modals/SinglePropertyAddModal";
import {useNavigate} from "react-router-dom";
import PrepaidCalls from "../../api/PrepaidCalls";
import "../../i18n/i18n"
import {useTranslation} from "react-i18next";
import {getPaths} from "../../utils/paginationHelper";
import ApiCalls from "../../api/apiCalls";
import {CURRENT, NEXT, PREV} from "./Constants";


function Prepaids(props) {
    const [prepaids, setPrepaids] = useState([]);
    const [paths, setPaths] = useState({})
    const navigate = useNavigate()
    const [message, setMessage] = useState("")
    const { t } = useTranslation();

    const setPages = (linkHeader) => {
        const paths = getPaths(linkHeader);
        setPaths(paths)
    }

    const fetchPrepaids = async () => {
        const response = await PrepaidCalls.getPrepaid(0);
        if (response && response.ok){
            setPrepaids(response.data)
            setPages(response.headers.link)
        }
    }

    useEffect( () => {
        async function fetchData () {
            await fetchPrepaids()
        }
        fetchData();
    }, [])

    const fetchPage = async (page) => {
        const response = await ApiCalls.makeGetCall(paths[page])
        if (response && response.ok) {
            setPrepaids(response.data)
            setPages(response.headers.link)
        }
    }

    const deletePrepaids = async (name) => {
        const response = await PrepaidCalls.deletePrepaid(name);
        if (response && response.ok) {
            await fetchPage(CURRENT)
            setMessage("")
        }
        if (response.status === 404) {
            if (response.data === "prepaid-not-found") {
                setMessage("errors.prepaidNotFoundDelete")
            }
        }
        if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        }
    }

    const handleAdd = async (newPrepaid) => {
        const response = await PrepaidCalls.addPrepaid(newPrepaid)
        if (response && response.ok) {
            await fetchPage(CURRENT)
            setMessage("")
        } else if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        } else if (response.status === 409) {
            if (response.data === "prepaid-exists") {
                setMessage("errors.prepaidExists")
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
            <SinglePropertyAddModal handleAdd={handleAdd} property={t("ADMIN.prepaid")}/>
            {message && (
                <div className="form-group">
                    <div className="alert alert-danger" role="alert">
                        {t(message)}
                    </div>
                </div>
            )}
            <Container>
                <div className="admin-info-container admin-clinic-prepaid-container">
                    {prepaids.map((prepaid) => {
                        return (
                            <Card className="mb-3 shadow"
                                  style={{color: "#000", width: '20rem', height: '7rem'}}
                                  key={prepaid.name}>
                                <Card.Body>
                                    <Card.Title>{prepaid.name}</Card.Title>
                                </Card.Body>
                                <Button className="remove-button remove-button-color shadow-sm"
                                        onClick={() => deletePrepaids(prepaid.name)}>
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

export default Prepaids