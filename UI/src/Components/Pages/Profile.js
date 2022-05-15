import React, {useEffect, useState} from 'react';
import {Button, Col, Container, Form, Row} from "react-bootstrap";
import DropDownList from "../DropDownList";
import {useTranslation} from "react-i18next";
import '../../i18n/i18n'
import {Link, useNavigate} from "react-router-dom";
import PatientCalls from "../../api/PatientCalls";
import PrepaidCalls from "../../api/PrepaidCalls";
import EditUserProfileModal from "../Modals/EditUserProfileModal";
import AppointmentCalls from "../../api/AppointmentCalls";
import {dateToString} from "../../utils/dateHelper";
import './Profile.css'

function Profile(props) {
    const [selectedPrepaid, setSelectedPrepaid] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [id, setId] = useState('')
    const [prepaidNumber, setPrepaidNumber] = useState('')
    const [prepaids, setPrepaids] = useState([])
    const [appointments, setAppointments] = useState([])


    const { t } = useTranslation();
    const navigate = useNavigate();

    useEffect(async () => {
        await fetchProfile()
        await fetchPrepaids()
        await fetchAppointments()
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
            setFirstName(response.data.userData.firstName)
            localStorage.setItem('firstName', response.data.userData.firstName)
            setLastName(response.data.userData.lastName)
            localStorage.setItem('lastName', response.data.userData.lastName)
            setSelectedPrepaid(response.data.prepaid)
            localStorage.setItem('prepaid', response.data.prepaid)
            setPrepaidNumber(response.data.prepaidNumber)
            localStorage.setItem('prepaidNumber', response.data.prepaidNumber)
            setId(response.data.id)
        } else if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }

    const fetchAppointments = async () => {
        const email = localStorage.getItem('email')
        if (email === null) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
        const response = await AppointmentCalls.getAppointment(email, 0)
        if (response && response.ok) {
            setAppointments(response.data.slice(0, 3))
        }
        if (response.status === 401) {
            props.logout()
            navigate('/paw-2019b-4/login')
        }
    }
    const handleProfileUpdateOk = async () => {
            await fetchProfile()
            await fetchPrepaids()
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
                        <ul style={{display:"grid", justifyItems:"flex-start"}}>
                            {appointments.map(app => {
                                return(
                                    <li className="my-3">
                                        <b>{dateToString(app, t)}</b> {t("with")} <b>{app.doctorClinic.doctor.user.firstName + ' ' + app.doctorClinic.doctor.user.lastName}</b> ({app.doctorClinic.clinic.name})
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
                                              prepaidNumber={prepaidNumber}
                        />
                    </Col>
                    <Col className="col-button">
                        <Link
                            className="edit-remove-button doc-button-color shadow-sm edit-button btn app-btn"
                            role="button"
                            to="/paw-2019b-4/appointments">{t('NAVBAR.appointments')}
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