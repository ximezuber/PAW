import React, { useState } from 'react';
import {Button, Col, Form, Modal, Row} from "react-bootstrap";
import '../CardContainer.css'
import DropDownList from "../DropDownList";
import {useTranslation} from "react-i18next";


function DoctorAddModal(props) {
    const [show, setShow] = useState(false)
    const [selectedSpecialty, setSelectedSpecialty] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [repeatPassword, setRepeatPassword] = useState('')
    const [license, setLicense] = useState('')
    const [phoneNumber, setPhoneNumber] = useState('')
    const [message, setMessage] = useState('')
    const { t } = useTranslation();

    const handleSelect = (specialty) => {
        setSelectedSpecialty(specialty)
    }

    const handleShow = () =>{
        setShow(!show)
    }

    const handleAdd = (doctor) => {
        if (password !== repeatPassword)
            setMessage("errors.passwordMismatch")
        else {
            setMessage('')
            setSelectedSpecialty("")
            props.handleAdd(doctor)
            handleShow()
        }
    }

    const onChange = (event) => {
        // eslint-disable-next-line default-case
        switch (event.target.id) {
            case "firstName":
                setFirstName(event.target.value)
                break;
            case "lastName":
                setLastName(event.target.value)
                break;
            case "license":
                setLicense(event.target.value)
                break;
            case "email":
                setEmail(event.target.value)
                break;
            case "password":
                setPassword(event.target.value)
                break;
            case "phoneNumber":
                setPhoneNumber(event.target.value)
                break;
            case "repeatPassword":
                setRepeatPassword(event.target.value)
                break;
        }
    }

    return (
        <>
            <Button variant="outline-secondary" onClick={() => handleShow()} size="lg" className="add-margin">
                {t("DOC.addDoc")}
            </Button>
            <Modal show={show} onHide={() => handleShow()}>
                <Modal.Header closeButton>
                    <Modal.Title>{t("DOC.addDoc")}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Row>
                            <Col>
                                <Form.Group className="mb-3" controlId="firstName">
                                    <Form.Label>{t("FORM.firstName")}</Form.Label>
                                    <Form.Control placeholder={t("FORM.enterFirstName")} onChange={onChange}/>
                                </Form.Group>
                            </Col>
                            <Col>
                                <Form.Group className="mb-3" controlId="lastName">
                                    <Form.Label>{t("FORM.lastName")}</Form.Label>
                                    <Form.Control placeholder={t("FORM.enterLastName")} onChange={onChange}/>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group className="mb-3" controlId="specialty">
                                    <Form.Label>{t("FORM.specialty")} {selectedSpecialty}</Form.Label>
                                    <DropDownList iterable={props.specialties}
                                                  selectedElement=''
                                                  handleSelect={handleSelect}
                                                  elementType={t("FORM.selectSpecialty")}
                                                  id='specialty'
                                    />
                                </Form.Group>
                            </Col>
                            <Col>
                                <Form.Group className="mb-3" controlId="license">
                                    <Form.Label>{t("DOC.license")}</Form.Label>
                                    <Form.Control placeholder={t("FORM.enterLicense")} onChange={onChange}/>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group className="mb-3" controlId="phoneNumber">
                                    <Form.Label>{t("FORM.phoneNumber")}</Form.Label>
                                    <Form.Control placeholder={t("FORM.enterPhone")} onChange={onChange}/>
                                </Form.Group>
                            </Col>
                            <Col>
                                <Form.Group className="mb-3" controlId="email">
                                    <Form.Label>{t("FORM.email")}</Form.Label>
                                    <Form.Control type="email" placeholder={t("FORM.enterEmail")} onChange={onChange}/>
                                </Form.Group>
                            </Col>
                        </Row>
                        <Row>
                            <Col>
                                <Form.Group className="mb-3" controlId="password">
                                    <Form.Label>{t("FORM.password")}</Form.Label>
                                    <Form.Control type="password" placeholder={t("FORM.password")} onChange={onChange}/>
                                </Form.Group>
                            </Col>
                            <Col>
                                <Form.Group className="mb-3" controlId="repeatPassword">
                                    <Form.Label>{t("FORM.repeatPassword")}</Form.Label>
                                    <Form.Control type="password" placeholder={t("FORM.repeatPassword")} onChange={onChange}/>
                                </Form.Group>
                            </Col>
                        </Row>
                        {message && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {t(message)}
                                </div>
                            </div>
                        )}
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={() => handleShow()}>
                        {t("closeButton")}
                    </Button>
                    <Button className="doc-button-color" onClick={() => handleAdd({
                        firstName: firstName,
                        lastName: lastName,
                        email: email,
                        password: password,
                        repeatPassword: repeatPassword,
                        license: license,
                        specialty: selectedSpecialty,
                        phoneNumber: phoneNumber
                    })
                    }>
                        {t("DOC.addDoc")}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

export default DoctorAddModal