import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import {useNavigate} from "react-router-dom";
import AppointmentCalls from "../../api/AppointmentCalls";
import {Button, Card, Container, Row} from "react-bootstrap";
import './Favorites.css'
import {dateToString, getMonth, getWeekDate} from "../../utils/dateHelper";
import {getPaths} from "../../utils/paginationHelper";
import ApiCalls from "../../api/apiCalls";
import {CURRENT, NEXT, PREV} from "./Constants";

function Appointments(props) {
    const [appointments, setAppointments] = useState([])
    const [paths, setPaths] = useState({})
    const [message, setMessage] = useState("")
    const [isLoading, setIsLoading] = useState(false)
    const {t} = useTranslation()
    const navigate = useNavigate()
    let cachedClinic = {}
    let cachedPatient = {}
    let cachedDoctors = {}

    const fetchAppointments = async () => {
        const email = localStorage.getItem('email')
        if (email === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        setIsLoading(true)
        const response = await AppointmentCalls.getAppointment(email, 0)
        await handleResponse(response)
    }

    const fetchPage = async (page) => {
        const email = localStorage.getItem('email')
        if (email === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        } else {
            setIsLoading(true)
            const response = await ApiCalls.makeGetCall(paths[page])
            await handleResponse(response)
        }
    }

    const handleResponse = async (response) => {
        if (response && response.ok) {
            const list = response.data
            let apps = []
            if (props.user === "doctor") {
                for (let i = 0; i < list.length; i++) {
                    let patient = cachedPatient[list[i].patient]
                    if (patient === undefined) {
                        patient = await fetchAuthEntity(list[i].patient)
                        cachedPatient[list[i].patient] = patient
                    }

                    let clinic = cachedClinic[list[i].clinic]
                    if (clinic === undefined) {
                        clinic =  await fetchEntity(list[i].clinic)
                        cachedClinic[list[i].clinic] = clinic
                    }
                    const app = {
                        id: list[i].id,
                        clinic: clinic,
                        year: list[i].year,
                        month: list[i].month,
                        day: list[i].day,
                        hour: list[i].hour,
                        dayOfWeek: list[i].dayOfWeek,
                        doctor: list[i].doctor,
                        patient: patient
                    }
                    apps.push(app)
                }
            } else {
                for (let i = 0; i < list.length; i++) {
                    let doctor = cachedDoctors[list[i].doctor]
                    if (doctor === undefined) {
                        doctor =  await fetchEntity(list[i].doctor)
                        cachedDoctors[list[i].doctor] = doctor
                    }
                    let clinic = cachedClinic[list[i].clinic]
                    if (clinic === undefined) {
                        clinic =  await fetchEntity(list[i].clinic)
                        cachedClinic[list[i].clinic] = clinic
                    }
                    const app = {
                        id: list[i].id,
                        clinic: clinic,
                        year: list[i].year,
                        month: list[i].month,
                        day: list[i].day,
                        hour: list[i].hour,
                        dayOfWeek: list[i].dayOfWeek,
                        doctor: doctor,
                        patient:list[i].patient
                    }
                    apps.push(app)
                }
            }
            setAppointments(apps)
            setPaths(getPaths(response.headers.link))
            setMessage("")
            setIsLoading(false)
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        if (response.status === 404) {
            if (response.data === "user-not-found")
                setMessage("errors.noLoggedDoc")
        }
    }

    const fetchEntity = async (path) => {
        const response = await ApiCalls.makeGetCall(path);
        if (response && response.ok) {
            return response.data
        }
    }
    const fetchAuthEntity = async (path) => {
        const response = await ApiCalls.makeGetCall(path);
        if (response && response.ok) {
            return response.data
        }
    }

    const deleteAppointment = async (app) => {
        const email = localStorage.getItem('email')
        if (email === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        const response = await AppointmentCalls.deleteAppointment(email, app.id)
        if (response && response.ok) {
            await fetchPage(CURRENT)
            setMessage("")
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        if (response.status === 404) {
            if (response.data === "doctor-clinic-not-found")
                setMessage("errors.docClinicNotFound")
            if (response.data === "appointment-not-found")
                setMessage("errors.noAppointmentFound")
        }
    }

    useEffect( () => {
        async function fetchData () {
            await fetchAppointments();
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
                                            {t("USER.doc")}{ap.doctor.firstName + ' ' + ap.doctor.lastName} ({ap.doctor.email})
                                        </div>: <div>
                                            {t("USER.patient")}{ap.patient.firstName + ' ' + ap.patient.lastName} ({ap.patient.email})
                                        </div> }

                                        <div>
                                            {t("USER.clinic")} {ap.clinic.name} - {ap.clinic.location} ({ap.clinic.address})
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