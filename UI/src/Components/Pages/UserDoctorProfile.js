import {useNavigate, useParams} from "react-router-dom";
import {Button, Col, Container, Form, Row, Table} from "react-bootstrap";
import {BASE_URL} from "../../Constants";
import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import DoctorCalls from "../../api/DoctorCalls";
import ImageCalls from "../../api/ImageCalls";
import AppointmentCalls from "../../api/AppointmentCalls";
import DropDownList from "../DropDownList";
import {dateToString, getMonth, getWeekDate} from "../../utils/dateHelper";
import PatientCalls from "../../api/PatientCalls";
import './UserDoctorProfile.css'
import ApiCalls from "../../api/apiCalls";


function UserDoctorProfile(props) {
    const {license} = useParams()
    const {t} = useTranslation()
    const navigate = useNavigate()
    const [image, setImage] = useState(null)
    const [doctor, setDoctor] = useState({})
    const [message, setMessage] = useState("")
    const [schedule, setSchedule] = useState([])
    const [available, setAvailable] = useState([])
    const [clinics, setClinics] = useState([])
    const [selectedClinic, setSelectedClinic] = useState(null)
    const [selectedDateTime, setSelectedDateTime] = useState(null)
    const [isFavorite, setIsFavorite] = useState(false)

    const fetchDoctor = async () => {
        const response = await DoctorCalls.getDocByLicense(license);
        if (response && response.ok) {
            setDoctor(response.data)
        }
    }

    const fetchAvailableAppointments = async () => {
        const response = await AppointmentCalls.getAvailableAppointments(license);
        if (response && response.ok) {
            const list = response.data
            let apps = []
            for (let i = 0; i < list.length; i++) {
                const clinic = await fetchClinic(list[i].clinic)
                const app = {
                    clinic: clinic,
                    year: list[i].year,
                    month: list[i].month,
                    day: list[i].day,
                    hour: list[i].hour,
                    dayOfWeek: list[i].dayOfWeek
                }
                apps.push(app)
            }
            setAvailable(apps)
            setMessage("")
        }
    }

    const fetchClinics = async () => {
        const response = await DoctorCalls.getAllClinics(license);
        if (response && response.ok) {
            const docClinic = response.data
            const getClinics = [];
            for (let i = 0; i < docClinic.length; i++) {
                const clinic = await fetchClinic(docClinic[i].clinic)
                getClinics.push(clinic);
            }
            setClinics(getClinics)
            setMessage("")
        }

    }

    const fetchClinic = async (clinicPath) => {
        const response = await ApiCalls.makeGetCall(clinicPath);
        if (response && response.ok) {
            return response.data
        }
    }

    const fetchImage = async () => {
        const response = await ImageCalls.getImage(license);
        if (response && response.ok) {
            setImage(response.data)
        }
        if (response.status === 404) {
            if (response.data === "image-not-found") {
                setImage(null)
            }
            if (response.data === "doctor-not-found") {
                setMessage("errors.noDocFound")
            }
        }
    }

    const fetchSchedule = async () => {
        const response = await DoctorCalls.getSchedule(license)
        if (response && response.ok) {
            const list = response.data;
            let sch = []
            for (let i = 0; i < list.length; i++) {
                const clinic = await fetchClinic(list[i].clinic)
                console.log(clinic)
                const sched = {
                    clinic: clinic,
                    day: list[i].day,
                    hour: list[i].hour,
                }
                sch.push(sched)
                console.log(sched)
            }
            setSchedule(sch)
            setMessage("")
        }
    }

    const fetchIsFavorite = async () => {
        if (localStorage.getItem('email') === null) {
            setIsFavorite(false)
        } else {
            const response = await PatientCalls.isFavorite(localStorage.getItem('email'), license)
            if (response && response.ok) {
                setIsFavorite(true)
                setMessage("")
            }
            if (response.status === 404) {
                if (response.data === "doctor-not-found")
                    setMessage("errors.noDocFound")
                if (response.data === "not-favorite"){
                    setIsFavorite(false)
                    setMessage("")
                }
            }
        }
    }

    useEffect( () => {
        async function fetchData () {
            await fetchDoctor();
            await fetchImage();
            await fetchSchedule();
            await fetchClinics();
            await fetchAvailableAppointments();
            await fetchIsFavorite();
        }
        fetchData();
    },[])

    const handleMakeApp = async () => {
        if (localStorage.getItem('email') === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        if (selectedClinic === null) {
            setMessage("errors.selectTime")
        }
        if (selectedDateTime === null) {
            setMessage("errors.selectTime")
        }
        else {
            const data = {
                license: license,
                clinic: selectedClinic.clinic.id,
                patient: localStorage.getItem('email'),
                time: selectedDateTime.hour,
                day: selectedDateTime.day,
                month: selectedDateTime.month,
                year: selectedDateTime.year
            }
            const response = await AppointmentCalls.makeAppointment(data);
            if (response && response.ok) {
                navigate("/paw-2019b-4/appointments")
            }
            if (response.status === 401) {
                localStorage.setItem('path', "/" + license + "/profile")
                props.logout()
                navigate('/paw-2019b-4/login')
            }
            if (response.status === 400) {
                if (response.data === "past-date")
                    setMessage("errors.datePast")
            }
            if (response.status === 409) {
                if (response.data === "out-of-schedule")
                    setMessage("errors.outOfSchedule")
                if (response.data === "appointment-exists")
                    setMessage("errors.appointmentExists")
                if (response.data === "doctor-already-has-appointment")
                    setMessage("errors.doctorAppointment")
                if (response.data === "patient-already-has-appointment")
                    setMessage("errors.patientAppointment")
             }
        }

    }

    const makeFavorite = async () => {
        if (localStorage.getItem('email') === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        const response = await PatientCalls.addFavoriteDoctor(localStorage.getItem('email'), license)
        if (response && response.ok) {
            setIsFavorite(true)
            setMessage("")
        }
        if (response.status === 404) {
            if (response.data === "doctor-not-found")
                setMessage("errors.noDocFound")
            if (response.data === "patient-not-found")
                setMessage("errors.noPatientEmail")
        }
        if (response.status === 409) {
            if (response.data === "favorite-exists")
                setMessage("errors.favExists")
        }
        if (response.status === 401) {
            localStorage.setItem('path', "/" + license + "/profile")
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    const deleteFavorite = async () => {
        if (localStorage.getItem('email') === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        const response = await PatientCalls.deleteFavoriteDoctor(localStorage.getItem('email'), license)
        if (response && response.ok) {
            setIsFavorite(false)
            setMessage("")
        }
        if (response.status === 404) {
            if (response.data === "doctor-not-found")
                setMessage("errors.noDocFound")
            if (response.data === "patient-not-found")
                setMessage("errors.noPatientEmail")
        }
        if (response.status === 401) {
            localStorage.setItem('path', "/" + license + "/profile")
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    const getRow = (row) => {
        const rowSchedule = schedule.filter(schedule => schedule.hour === row)
        const days = []
        for (let i = 1; i < 8; i++) {
            const scheduleDay = rowSchedule.filter(schedule => (schedule.day) === i)
            if (scheduleDay.length > 0) {
                days[i - 1] = scheduleDay[0].clinic
            } else {
                days[i - 1] = null
            }

        }
        return days
    }

    const handleSelectClinic = (clinicName) => {
        const selected = clinics.filter(clinic => clinic.name + " (" + clinic.location + ")" === clinicName)
        setSelectedClinic(selected[0])
    }

    const handleSelectDateTime = (dateFormated) => {
        const selected = getDateTimes().filter(date => t(getWeekDate(date.dayWeek)) + " " + date.day + " " + t(getMonth(date.month)) + ", " + date.year +
            " " + date.hour + ":00" === dateFormated)
        setSelectedDateTime(selected[0])
    }

    const getAllRows = () => {
        const hours = []
        for (let i = 9; i < 19; i++) {
            hours[i] = getRow(i)
        }
        return hours
    }

    const getDateTimes = () => {
        if (selectedClinic === null) {
            return []
        }
        return available.filter(appointment => appointment.clinic.id === selectedClinic.id)
    }

    return (
        <>
            <Container>
                <Row className="mt-3">

                    <Col className="info-col-user">
                        <h4 style={{display: "flex"}}>
                            <div style={{alignSelf: "center"}}>{t('USER.dataProfile')}</div>
                            {isFavorite ? <Button className="m-2 fav-button" onClick={deleteFavorite}>
                                <img src="/paw-2019b-4/images/yesfav.png"/>
                            </Button>:<Button className="m-2 fav-button" onClick={makeFavorite}>
                                <img src="/paw-2019b-4/images/nofav.png"/>
                            </Button>}
                        </h4>
                        <div className="user-info-label">
                            <b>{t('FORM.name')}:</b> {doctor.firstName + " " + doctor.lastName}
                        </div>
                        <div className="user-info-label">
                            <b>{t('FORM.email')}:</b> {doctor.email}
                        </div>
                        <div className="user-info-label">
                            <b>{t('DOC.license')}:</b> {doctor.license}
                        </div>
                        <div className="user-info-label">
                            <b>{t('FORM.phoneNumber')}:</b> {doctor.phoneNumber}
                        </div>
                        <div className="user-info-label">
                            <b>{t('ADMIN.specialty')}:</b> {doctor.specialty}
                        </div>
                    </Col>
                    <Col className="img-col-user mx-3">
                        <img className="user-img-size"
                             src={image === null?
                                 "/paw-2019b-4/images/docpic.jpg": BASE_URL + "/doctors/" + license + "/image"} />
                    </Col>
                </Row>
                <hr/>
                <Row>
                    <Col className="col-8">
                        <Table className="user-schedule-table">
                            <thead>
                            <tr>
                                <th width="9%">{t('CAL.hour')}</th>
                                <th width="13%">{t('CAL.mon')}</th>
                                <th width="13%">{t('CAL.tue')}</th>
                                <th width="13%">{t('CAL.wed')}</th>
                                <th width="13%">{t('CAL.thu')}</th>
                                <th width="13%">{t('CAL.fri')}</th>
                                <th width="13%">{t('CAL.sat')}</th>
                                <th width="13%">{t('CAL.sun')}</th>
                            </tr>
                            </thead>
                            <tbody>
                            {getAllRows().map((row, index) => {
                                return (
                                    <tr>
                                        <td>{index}:00</td>
                                        {row.map((clinic => {
                                            if (clinic === null) {
                                                return <td/>
                                            } else {
                                                return (
                                                    <td className="user-clinic">{clinic.name} ({clinic.location})</td>
                                                )
                                            }

                                        }))}
                                    </tr>
                                )
                            })}
                            </tbody>
                        </Table>
                    </Col>
                    <Col>
                        <h4 className="m-3">
                            {t("actions.makeApp")}
                        </h4>
                        <Form.Group className="m-3">
                            <Form.Label><b>{t("CLINIC.clinic")}</b>: {selectedClinic === null? "":
                                selectedClinic.name + " - " + selectedClinic.location}</Form.Label>
                            <DropDownList iterable={clinics.map(clinic => clinic.name + " (" + clinic.location + ")")}
                                          selectedElement=''
                                          handleSelect={handleSelectClinic}
                                          elementType={t('FORM.selectClinic')}
                                          id='clinic'/>
                        </Form.Group>
                        <Form.Group className="m-3">
                            <Form.Label><b>{t("FORM.dateTime")}</b>: {selectedDateTime === null? "":
                                dateToString(selectedDateTime, t)}</Form.Label>
                            <DropDownList iterable={getDateTimes().map(date => dateToString(date, t))}
                                          selectedElement=''
                                          handleSelect={handleSelectDateTime}
                                          elementType={t('FORM.selectDateTime')}
                                          id='time'/>
                        </Form.Group>
                        <Button className="doc-button-color m-5" onClick={handleMakeApp}>
                            {t("actions.makeApp")}
                        </Button>
                        {message && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {t(message)}
                                </div>
                            </div>
                        )}
                    </Col>

                </Row>
            </Container>
        </>
    )

}

export default UserDoctorProfile