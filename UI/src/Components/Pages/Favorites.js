import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import PatientCalls from "../../api/PatientCalls";
import {Button, Card, Col, Container, Row} from "react-bootstrap";
import {Link, useNavigate} from "react-router-dom";
import './Favorites.css'

function Favorites(props) {
    const [doctors, setDoctors] = useState([])
    const [page, setPage] = useState(0)
    const [maxPage, setMaxPage] = useState(0)
    const [message, setMessage] = useState("")
    const navigate = useNavigate()
    const [isLoading, setIsLoading] = useState(false)
    const {t} = useTranslation()


    const fetchFavorites = async (pag) => {
        let id = localStorage.getItem('email')
        if (id === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        setIsLoading(true)
        const response = await PatientCalls.getFavoriteDoctors(id, pag)
        if (response && response.ok) {
            setDoctors(response.data)
            setMaxPage(Number(response.headers.xMaxPage))
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
            await fetchFavorites(page);
            setMessage("")
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    useEffect(async () => {
        await fetchFavorites(page);
    }, [])

    const nextPage = async () => {
        const newPage = page + 1
        setPage(newPage)
        setMessage("")
        await fetchFavorites(newPage)

    }
    const prevPage = async () => {
        const newPage = page - 1
        setPage(newPage)
        setMessage("")
        await fetchFavorites(newPage)
    }

    const renderPrevButton = () => {
        if (page !== 0) {
            return <Button className="doc-button doc-button-color shadow-sm"
                           onClick={() => prevPage()}>{t('prevButton')}</Button>
        }
    }

    const renderNextButton = () => {
        if (page < maxPage - 1) {
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
                                            <Card.Title>{doctor.user.firstName + ' ' + doctor.user.lastName}</Card.Title>
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