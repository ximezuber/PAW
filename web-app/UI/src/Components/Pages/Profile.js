import React, {useEffect, useState} from 'react';
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import '../../i18n/i18n'
import {Link, useNavigate} from "react-router-dom";
import PatientCalls from "../../api/PatientCalls";
import PrepaidCalls from "../../api/PrepaidCalls";
import EditUserProfileModal from "../Modals/EditUserProfileModal";
import AppointmentCalls from "../../api/AppointmentCalls";
import {dateToString} from "../../utils/dateHelper";
import './Profile.css'
import ApiCalls from "../../api/apiCalls";
import DoctorCalls from "../../api/DoctorCalls";
import {WEB_CONTEXT} from "../../Constants";

function Profile(props) {
    const [selectedPrepaid, setSelectedPrepaid] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [id, setId] = useState('')
    const [prepaidNumber, setPrepaidNumber] = useState('')
    const [prepaids, setPrepaids] = useState([])
    const [appointments, setAppointments] = useState([])
    const [isLoading, setIsLoading] = useState(false)
    let cachedClinic = {}
    let cachedDoctor = {}

    const { t } = useTranslation();
    const navigate = useNavigate();

    useEffect( () => {
        async function fetchData () {
            fetchProfile()
            fetchPrepaids()
            fetchAppointments()
        }
        fetchData();

    }, [])

    const fetchPrepaids = async () => {
        const response = await PrepaidCalls.getAllPrepaid();
        if (response && response.ok) {
            setPrepaids(response.data.map(prepaid => prepaid.name))
        }
    }

    const fetchProfile = async () => {
        const response = await PatientCalls.getProfile(localStorage.getItem('email'));
        if (response && response.ok) {
            setFirstName(response.data.firstName)
            // localStorage.setItem('firstName', response.data.firstName)
            setLastName(response.data.lastName)
            // localStorage.setItem('lastName', response.data.lastName)
            setSelectedPrepaid(response.data.prepaid)
            // localStorage.setItem('prepaid', response.data.prepaid)
            setPrepaidNumber(response.data.prepaidNumber)
            // localStorage.setItem('prepaidNumber', response.data.prepaidNumber)
            setId(response.data.id)
        } else if (response.status === 401) {
            props.logout()
            navigate(`/${WEB_CONTEXT}/login`)
        }
    }

    const fetchEntity = async (path) => {
        const response = await ApiCalls.makeGetCall(path);
        if (response && response.ok) {
            return response.data
        }
    }

    const fetchAppointments = async () => {
        setIsLoading(true)
        const email = localStorage.getItem('email')
        if (email === null) {
            props.logout()
            navigate(`/${WEB_CONTEXT}/login`)
        }
        const response = await AppointmentCalls.getAppointment(email, 0)
        if (response && response.ok) {
            const list = response.data.slice(0, 3)
            let apps = []
            for (let i = 0; i < list.length; i++) {
                let doctor = cachedDoctor[list[i].doctor]
                if (doctor === undefined) {
                    doctor =  await fetchEntity(list[i].doctor)
                    cachedDoctor[list[i].doctor] = doctor
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
                    patient: list[i].patient
                }
                apps.push(app)
            }
            setAppointments(apps)
        }
        if (response.status === 401) {
            props.logout()
            navigate(`/${WEB_CONTEXT}/login`)
        }
        setIsLoading(false)
    }
    const handleProfileUpdateOk = async () => {
            await fetchProfile()
            await fetchPrepaids()
    }

    const handleDeleteProfile = async () => {
        const email = localStorage.getItem('email')
        if (email === null) {
            props.logout()
            navigate(`/${WEB_CONTEXT}/login`)
        }
        const response = await PatientCalls.deleteProfile(email)
        if (response && response.ok) {
            props.logout()
            navigate('/' + WEB_CONTEXT)
        }
    }

    return (
        <>
            <Container>
                <Row>
                    <Col>
                        <h3 className="mt-3">
                            {t("welcome")}, {firstName} {lastName}!
                        </h3>
                        <hr />
                    </Col>
                </Row>
                <Row>
                    <Col className="info-col">
                        <h4>
                            {t('NAVBAR.profile')}
                        </h4>
                        <div className="info-label">
                            <b>{t('FORM.name')}:</b> {firstName} {lastName}
                        </div>
                        <div className="info-label">
                            <b>{t('FORM.email')}:</b> {localStorage.getItem('email')}
                        </div>
                        <div className="info-label">
                            <b>{t('FORM.id')}:</b> {id}
                        </div>
                        <div className="info-label">
                            <b>{t('FORM.prepaid')}:</b> {selectedPrepaid !== undefined? selectedPrepaid: t("FORM.none")}
                        </div>
                        {selectedPrepaid &&
                            <div className="info-label">
                                <b>{t('FORM.prepaidNumber')}:</b> {prepaidNumber}
                            </div>
                        }
                    </Col>
                    <Col>
                        <h4>{t('nextApp')}</h4>
                        {isLoading && (
                            <span className="spinner-border spinner-border-sm mt-3" style={{marginRight:"1rem"}}/>
                        )}
                        <ul style={{display:"grid", justifyItems:"flex-start"}}>
                            {appointments.map(app => {
                                return(
                                    <li className="my-3">
                                        <b>{dateToString(app, t)}</b> {t("with")}&nbsp;
                                        <b>{app.doctor.firstName + ' ' + app.doctor.lastName}</b>&nbsp;({app.clinic.name})
                                    </li>
                                )
                            })}
                        </ul>

                    </Col>
                </Row>
                <Row>
                    <Col className="col-button">
                        <EditUserProfileModal prepaids={prepaids}
                                              handleOk={handleProfileUpdateOk}
                                              logout={props.logout}
                                              firstName={firstName}
                                              lastName={lastName}
                                              selectedPrepaid={selectedPrepaid}
                                              prepaidNumber={prepaidNumber}
                        />
                        <Button className="mx-3 shadow-sm remove-button-color edit-remove-button edit-button app-btn"
                                onClick={handleDeleteProfile}> {t('deleteProfile')}</Button>
                    </Col>
                    <Col className="col-button">
                        <Link
                            className="edit-remove-button doc-button-color shadow-sm edit-button btn app-btn"
                            role="button"
                            to={`/${WEB_CONTEXT}/appointments`}>{t('NAVBAR.appointments')}
                        </Link>
                    </Col>
                </Row>
                <hr/>
                <Row>
                </Row>
            </Container>
        </>
    )
}

export default Profile;