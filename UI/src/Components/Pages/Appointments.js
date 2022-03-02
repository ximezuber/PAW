import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import AppointmentCalls from "../../api/AppointmentCalls";
import {Button, Card, Container, Row} from "react-bootstrap";
import './Favorites.css'
import {dateToString, getMonth, getWeekDate} from "../../utils/dateHelper";

function Appointments(props) {
    const [appointments, setAppointments] = useState([])
    const [page, setPage] = useState(0)
    const [maxPage, setMaxPage] = useState(0)
    const [message, setMessage] = useState("")
    const [isLoading, setIsLoading] = useState(false)
    const {t} = useTranslation()
    const navigate = useNavigate()

    const fetchAppointments = async (pag) => {
        const email = localStorage.getItem('email')
        if (email === null) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        }
        setIsLoading(true)
        const response = await AppointmentCalls.getAppointment(email, pag)
        if (response && response.ok) {
            setAppointments(response.data)
            setMaxPage(Number(response.headers.xMaxPage))
            setMessage("")
            setIsLoading(false)
        }
        if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            localStorage.removeItem('email')
            navigate('/paw-2019b-4/login')
        }
        if (response.status === 404) {
            if (response.data === "user-not-found")
                setMessage("errors.noLoggedDoc")
        }
    }

    const deleteAppointment = async (app) => {
        const email = localStorage.getItem('email')
        if (email === null) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            navigate('/paw-2019b-4/login')
        }
        const response = await AppointmentCalls.deleteAppointment(
            email,
            app.doctorClinic.doctor.license,
            app.doctorClinic.clinic.id,
            app.year,
            app.month,
            app.day,
            app.hour)
        if (response && response.ok) {
            await fetchAppointments(page)
            setMessage("")
        }
        if (response.status === 401) {
            localStorage.removeItem('token')
            localStorage.removeItem('role')
            localStorage.removeItem('email')
            navigate('/paw-2019b-4/login')
        }
        if (response.status === 404) {
            if (response.data === "doctor-clinic-not-found")
                setMessage("errors.docClinicNotFound")
            if (response.data === "appointment-not-found")
                setMessage("errors.noAppointmentFound")
        }
    }

    useEffect(async () => {
        await fetchAppointments(page);
    }, [])

    const nextPage = async () => {
        const newPage = page + 1
        setPage(newPage)
        setMessage("")
        await fetchAppointments(newPage)

    }
    const prevPage = async () => {
        const newPage = page - 1
        setPage(newPage)
        setMessage("")
        await fetchAppointments(newPage)
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

    return(
        <>
            <Row style={{display:"flex"}}>
                <h2 className="m-3 fav-title">{t("NAVBAR.appointments")}</h2>
                {message && (
                    <div className="form-group">
                        <div className="alert alert-danger" role="alert">
                            {t(message)}
                        </div>
                    </div>
                )}
                {appointments.length === 0 && !isLoading && <h4 className="m-3 no-fav">{t("USER.noApp")}</h4>}
            </Row>
            <Container>
                <div className="admin-info-container app-user-container">
                    {appointments.map((ap) => {
                        return (
                            <Card className="mb-3 app-card shadow">
                                <Card.Body>
                                    <Card.Title><b>{dateToString(ap, t)}</b></Card.Title>
                                    <Card.Text>
                                        {props.user === "patient"? <div>
                                            {t("USER.doc")}{ap.doctorClinic.doctor.user.firstName + ' ' + ap.doctorClinic.doctor.user.lastName}
                                        </div>: <div>
                                            {t("USER.patient")}{ap.patient.firstName + ' ' + ap.patient.lastName} ({ap.patient.email})
                                        </div> }

                                        <div>
                                            {t("USER.clinic")} {ap.doctorClinic.clinic.name} - {ap.doctorClinic.clinic.location} ({ap.doctorClinic.clinic.address})
                                        </div>

                                    </Card.Text>
                                </Card.Body>
                                <Button className="remove-button remove-button-color shadow-sm"
                                        onClick={() => {deleteAppointment(ap)}}>
                                    {t('cancelButton')}
                                </Button>
                            </Card>
                        )
                    })}
                </div>
            </Container>
            {renderPrevButton()}
            {renderNextButton()}
        </>
    )

}
export default Appointments;