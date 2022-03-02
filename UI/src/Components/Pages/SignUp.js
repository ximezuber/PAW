import React, {useEffect, useState} from 'react';
import {Button, Col, Form, Modal, Row} from "react-bootstrap";
import '../CardContainer.css'
import DropDownList from "../DropDownList";
import {useTranslation} from "react-i18next";
import '../../i18n/i18n'
import ApiCalls from "../../api/apiCalls"
import {useNavigate} from "react-router-dom";
import PrepaidCalls from "../../api/PrepaidCalls";

function SignUp() {
    const [selectedPrepaid, setSelectedPrepaid] = useState('')
    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [repeatPassword, setRepeatPassword] = useState('')
    const [document, setDocument] = useState('')
    const [prepaidNumber, setPrepaidNumber] = useState('')
    const [prepaids, setPrepaids] = useState([])

    const [message, setMessage] = useState("")
    const [firstNameErrors, setFirstNameErrors] = useState([])
    const [lastNameErrors, setLastNameErrors] = useState([])
    const [emailErrors, setEmailErrors] = useState([])
    const [prepaidNumberErrors, setPrepaidNumberErrors] = useState([])
    const [passwordErrors, setPasswordErrors] = useState([])
    const [repeatPasswordErrors, setRepeatPasswordErrors] = useState([])
    const [documentErrors, setDocumentErrors] = useState([])
    const [invalidForm, setInvalidForm] = useState(true)

    const { t } = useTranslation();
    const navigate = useNavigate();

    const handleSelect = (prepaid) => {
        setSelectedPrepaid(prepaid)
    }

    useEffect(async () => {
        await fetchPrepaids()
    }, [])

    const fetchPrepaids = async () => {
        const response = await PrepaidCalls.getAllPrepaids();
        if (response && response.ok) {
            setPrepaids(response.data.map(prepaid => prepaid.name))
        }
    }

    const isPresent = (value) => {
        let is = true
        if(!value) {
            is = false
        }
        return is
    }

    const checkRequired = (values, errors) => {
        console.log('first name antes de required: ' + values.firstName)
        if(!isPresent(values.firstName)) {
            errors.push(t("FORM.firstName") + "  " + t("errors.required"))
        }
        if(!isPresent(values.lastName)) {
            errors.push(t("FORM.lastName") + "  " + t("errors.required"))
        }
        console.log('email antes de required: ' + values.email)
        if(!isPresent(values.email)) {
            console.log('entro igual')
            errors.push(t("FORM.email") + "  " + t("errors.required"))
        }
        if(!isPresent(values.id)) {
            errors.push(t("FORM.document") + "  " + t("errors.required"))
        }
        console.log('pass antes de required: ' + values.password)
        if(!isPresent(values.password)) {
            console.log('fallo pass')
            errors.push(t("FORM.password") + "  " + t("errors.required"))
        }
        if(!isPresent(values.repeatPassword)) {
            errors.push(t("FORM.repeatPassword") + "  " + t("errors.required"))
        }
        if(!isPresent(values.prepaidNumber)) {
            errors.push(t("FORM.prepaidNumber") + "  " + t("errors.required"))
        }
    }

    const checkDigits = (values, errors) => {
        if(!/^\d+$/.test(values.id)) {
            errors.push(t("FORM.document") + "  " + t("errors.numeric"))
        }
        if(!/^\d+$/.test(values.prepaidNumber)) {
            errors.push(t("FORM.prepaidNumber") + "  " + t("errors.numeric"))
        }
    }

    const checkPassword = (values, errors) => {
        if(values.password !== values.repeatPassword) {
            errors.push(t("errors.passwordMismatch"))
        }
        if(values.password.length < 8) {
            errors.push(t("errors.passwordTooShort"))
        }
    }

    const checkAlpha = (values, errors) => {
        if(!/^[a-zA-ZÀ-ÿ\s]{1,40}$/.test(values.firstName)) {
            errors.push(t("FORM.firstName") + "  " + t("errors.alphabetic"))
        }
        if(!/^[a-zA-ZÀ-ÿ\s]{1,40}$/.test(values.lastName)) {
            errors.push(t("FORM.lastName") + "  " + t("errors.alphabetic"))        }
    }

    const checkEmail = (values, errors) => {
        if(!/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(values.email)) {
            errors.push(t("errors.invalidEmail"))
        }
    }

    const handleSignUp = async (e) => {
        e.preventDefault();
        setInvalidForm(true)
        let data = {
            firstName: firstName,
            lastName: lastName,
            email: email,
            password: password,
            repeatPassword: repeatPassword,
            id: document,
            prepaid: selectedPrepaid,
            prepaidNumber: prepaidNumber
        }

        const resp = await ApiCalls.signUp(data)

        if (resp.status === 201) {
            navigate("/paw-2019b-4/login");
            window.location.reload()
        }
        if (resp.status === 409) {
            if (resp.data === "user-exists") {
                setMessage(t("errors.emailInUse"))
            }
        }
        setInvalidForm(false)
    }

    const onChange = (event) => {
        // eslint-disable-next-line default-case
        let error = false
        let errors = []
        switch (event.target.id) {
            case "firstName":
                setFirstName(event.target.value)
                if(!isPresent(event.target.value)) {
                    errors.push(t("FORM.firstName") + "  " + t("errors.required"))
                    error = true
                }
                if(!/^[a-zA-ZÀ-ÿ\s]{1,40}$/.test(event.target.value)) {
                    errors.push(t("FORM.firstName") + "  " + t("errors.alphabetic"))
                    error = true
                }
                setFirstNameErrors(errors)
                break;
            case "lastName":
                setLastName(event.target.value)
                if(!isPresent(event.target.value)) {
                    errors.push(t("FORM.lastName") + "  " + t("errors.required"))
                    error = true
                }
                if(!/^[a-zA-ZÀ-ÿ\s]{1,40}$/.test(event.target.value)) {
                    errors.push(t("FORM.lastName") + "  " + t("errors.alphabetic"))
                    error = true
                }
                setLastNameErrors(errors)
                break;
            case "document":
                setDocument(event.target.value)
                if(!isPresent(event.target.value)) {
                    errors.push(t("FORM.document") + "  " + t("errors.required"))
                    error = true
                }
                if(event.target.value.length !== 8) {
                    errors.push(t("errors.invalidDocumentLength"))
                    error = true
                }
                if(event.target.value.length > 20) {
                    errors.push(t("errors.prepaidNumberTooLong"))
                    error = true
                }
                if(!/^\d+$/.test(event.target.value)) {
                    errors.push(t("FORM.document") + "  " + t("errors.numeric"))
                    error = true
                }
                setDocumentErrors(errors)
                break;
            case "email":
                setEmail(event.target.value)
                if(!isPresent(event.target.value)) {
                    errors.push(t("FORM.email") + "  " + t("errors.required"))
                    error = true
                }
                if(!/^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\.[a-zA-Z0-9-.]+$/.test(event.target.value)) {
                    errors.push(t("errors.invalidEmail"))
                    error = true
                }
                setEmailErrors(errors)
                break;
            case "password":
                setPassword(event.target.value)
                if(event.target.value !== '' && event.target.value.length < 8) {
                    errors.push(t("errors.passwordTooShort"))
                    error = true
                }
                if(event.target.value !== '' && repeatPassword !== event.target.value) {
                    setRepeatPasswordErrors([t("errors.passwordMismatch")])
                    error = true
                }
                setPasswordErrors(errors)
                break;
            case "prepaidNumber":
                setPrepaidNumber(event.target.value)
                if(!/^\d+$/.test(event.target.value)) {
                    errors.push(t("FORM.prepaidNumber") + "  " + t("errors.numeric"))
                    error = true
                }
                if(event.target.value.length > 20) {
                    errors.push(t("errors.prepaidNumberTooLong"))
                    error = true
                }
                if(!isPresent(event.target.value)) {
                    errors.push(t("FORM.prepaidNumber") + "  " + t("errors.required"))
                    error = true
                }
                setPrepaidNumberErrors(errors)
                break;
            case "repeatPassword":
                setRepeatPassword(event.target.value)
                if(event.target.value !== '' && password !== '' && password !== event.target.value) {
                    errors.push(t("errors.passwordMismatch"))
                    error = true
                }
                setRepeatPasswordErrors(errors)
                break;
        }
        setInvalidForm(error)
    }

    return (
        <div className="m-3">
            <h3>{t("FORM.signUp")}</h3>
            <Form onSubmit={handleSignUp}>
                <Row>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="firstName">
                            <Form.Label className="label-signup m-3">{t("FORM.firstName")}</Form.Label>
                            <Form.Control placeholder={t("FORM.enterFirstName")}
                                          value={firstName}
                                          onChange={onChange}
                            />
                        </Form.Group>
                        {firstNameErrors.length !== 0 && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    <ul>
                                        {firstNameErrors.map((error) => {
                                            return (
                                                <li>{error}</li>
                                            )
                                        })}
                                    </ul>
                                </div>
                            </div>
                        )}
                    </Col>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="lastName">
                            <Form.Label className="label-signup m-3">{t("FORM.lastName")}</Form.Label>
                            <Form.Control placeholder={t("FORM.enterLastName")}
                                          value={lastName}
                                          onChange={onChange}
                            />
                        </Form.Group>
                        {lastNameErrors.length !== 0 && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    <ul>
                                        {lastNameErrors.map((error) => {
                                            return (
                                                <li>{error}</li>
                                            )
                                        })}
                                    </ul>
                                </div>
                            </div>
                        )}
                    </Col>
                </Row>
                <Row>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="prepaids">
                            <Form.Label className="label-signup m-3">{t("FORM.prepaid")}</Form.Label>
                            <DropDownList iterable={prepaids}
                                          selectedElement=''
                                          handleSelect={handleSelect}
                                          elementType={t("FORM.selectPrepaid")}
                                          id='prepaids'
                            />
                            <Form.Label className="label-signup m-3">{selectedPrepaid}</Form.Label>
                        </Form.Group>
                    </Col>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="prepaidNumber">
                            <Form.Label className="label-signup m-3">{t("FORM.prepaidNumber")}</Form.Label>
                            <Form.Control placeholder={t("FORM.enterPrepaidNumber")}
                                          value={prepaidNumber}
                                          onChange={onChange}
                            />
                            {prepaidNumberErrors.length !== 0 && (
                                <div className="form-group">
                                    <div className="alert alert-danger" role="alert">
                                        <ul>
                                            {prepaidNumberErrors.map((error) => {
                                                return (
                                                    <li>{error}</li>
                                                )
                                            })}
                                        </ul>
                                    </div>
                                </div>
                            )}
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="document">
                            <Form.Label className="label-signup m-3">{t("FORM.document")}</Form.Label>
                            <Form.Control placeholder={t("FORM.enterDocument")}
                                          value={document}
                                          onChange={onChange}
                            />
                        </Form.Group>
                        {documentErrors.length !== 0 && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    <ul>
                                        {documentErrors.map((error) => {
                                            return (
                                                <li>{error}</li>
                                            )
                                        })}
                                    </ul>
                                </div>
                            </div>
                        )}
                    </Col>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="email">
                            <Form.Label className="label-signup m-3">{t("FORM.email")}</Form.Label>
                            <Form.Control type="email"
                                          placeholder={t("FORM.enterEmail")}
                                          value={email}
                                          onChange={onChange}
                            />
                            {emailErrors.length !== 0 && (
                                <div className="form-group">
                                    <div className="alert alert-danger" role="alert">
                                        <ul>
                                            {emailErrors.map((error) => {
                                                return (
                                                    <li>{error}</li>
                                                )
                                            })}
                                        </ul>
                                    </div>
                                </div>
                            )}
                        </Form.Group>
                    </Col>
                </Row>
                <Row>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="password">
                            <Form.Label className="label-signup m-3">{t("FORM.password")}</Form.Label>
                            <Form.Control type="password"
                                          placeholder={t("FORM.password")}
                                          value={password} onChange={onChange}
                            />
                            {passwordErrors.length !== 0 && (
                                <div className="form-group">
                                    <div className="alert alert-danger" role="alert">
                                        <ul>
                                            {passwordErrors.map((error) => {
                                                return (
                                                    <li>{error}</li>
                                                )
                                            })}
                                        </ul>
                                    </div>
                                </div>
                            )}
                        </Form.Group>
                    </Col>
                    <Col className="mx-4">
                        <Form.Group className="mb-3 div-signup" controlId="repeatPassword">
                            <Form.Label className="label-signup m-3">{t("FORM.repeatPassword")}</Form.Label>
                            <Form.Control type="password"
                                          placeholder={t("FORM.repeatPassword")}
                                          value={repeatPassword}
                                          onChange={onChange}
                            />
                            {repeatPasswordErrors.length !== 0 && (
                                <div className="form-group">
                                    <div className="alert alert-danger" role="alert">
                                        <ul>
                                            {repeatPasswordErrors.map((error) => {
                                                return (
                                                    <li>{error}</li>
                                                )
                                            })}
                                        </ul>
                                    </div>
                                </div>
                            )}
                        </Form.Group>
                    </Col>
                </Row>
                <br/>
                <Button type="submit" disabled={invalidForm && selectedPrepaid !== ''} variant="secondary" >
                    {t("FORM.signUp")}
                </Button>
            </Form>
            {message && (
                <div className="form-group">
                    <div className="alert alert-danger m-3" role="alert">
                        {t(message)}
                    </div>
                </div>
            )}
        </div>
    )
}

export default SignUp;