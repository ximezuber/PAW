import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import PatientCalls from "../../api/PatientCalls";
import {Button, Card, Col, Container, Row} from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import './Favorites.css'
import {getPaths} from "../../utils/paginationHelper";
import ApiCalls from "../../api/apiCalls";
import {CURRENT, NEXT, PREV} from "./Constants";

function Favorites(props) {
    const [doctors, setDoctors] = useState([])
    const [paths, setPaths] = useState({})
    const [message, setMessage] = useState("")
    const navigate = useNavigate()
    const [isLoading, setIsLoading] = useState(false)
    const {t} = useTranslation()

    const setPages = (linkHeader) => {
        const paths = getPaths(linkHeader);
        setPaths(paths)
    }

    const fetchFavorites = async () => {
        let id = localStorage.getItem('email')
        if (id === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        setIsLoading(true)
        const response = await PatientCalls.getFavoriteDoctors(id, 0)
        if (response && response.ok) {
            setDoctors(response.data)
            setPages(response.headers.link)
            setIsLoading(false)
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    const fetchPage = async (page) => {
        let id = localStorage.getItem('email')
        if (id === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        setIsLoading(true)
        const response = await ApiCalls.makeGetCall(paths[page])
        if (response && response.ok) {
            setDoctors(response.data)
            setPages(response.headers.link)
            setIsLoading(false)
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    const removeFromFavorites = async (license) => {
        let id = localStorage.getItem('email')
        if (id === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        const response = await PatientCalls.deleteFavoriteDoctor(id, license);
        if (response && response.ok) {
            await fetchPage(CURRENT)
            setMessage("")
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    useEffect( () => {
        async function fetchData () {
            await fetchFavorites();
        }
        fetchData();

    }, [])

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
            <Row style={{display:"flex"}}>
                <h2 className="m-3 fav-title">{t("USER.fav")}</h2>
                {message && (
                    <div className="form-group">
                        <div className="alert alert-danger" role="alert">
                            {t(message)}
                        </div>
                    </div>
                )}
                {doctors.length === 0 && !isLoading && <h4 className="m-3 no-fav">{t("USER.emptyFavorites")}</h4>}
            </Row>
            <Row>
                <Col>
                    <Container>
                        <div className="admin-info-container favorite-doctor-container">
                            {doctors.map((doctor) => {
                                return (
                                    <Card className="mb-3 fav-doc-card shadow"
                                          key={doctor.license}>
                                        <Card.Body className="card-body-doc">
                                            <Card.Title>{doctor.firstName + ' ' + doctor.lastName}</Card.Title>
                                            <Card.Text>
                                                {doctor.specialty}
                                            </Card.Text>
                                        </Card.Body>
                                        <div className="buttons-div">
                                            <Link className="doc-button-color btn m-1"
                                                  role="button"
                                                  to={'/paw-2019b-4/' + doctor.license + '/profile'}>{t("USER.seeProfile")}
                                            </Link>
                                            <Button className="edit-remove-button remove-button-color shadow-sm"
                                                    onClick={() => removeFromFavorites(doctor.license)}>
                                                {t("deleteButton")}
                                            </Button>
                                        </div>
                                    </Card>
                                )
                            })}
                        </div>
                    </Container>
                    {renderPrevButton()}
                    {renderNextButton()}
                </Col>
            </Row>
        </>
    )
}

export default Favorites;