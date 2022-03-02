import React, {Component, useState} from 'react';
import {Button, Modal, Form, Dropdown} from "react-bootstrap";
import DropDownList from "../DropDownList";
import {useTranslation} from "react-i18next";
import "../../i18n/i18n"

function ClinicEditModal(props) {
    const [id, setId] = useState(props.clinic.id);
    const [address, setAddress] = useState(props.clinic.address);
    const [name, setName] = useState(props.clinic.name);
    const [location, setLocation] = useState(props.clinic.location);
    const { t } = useTranslation();


    const onChange = (event) => {
        switch(event.target.id) {
            case "name":
                setName(event.target.value);
                break;
            case "address":
                setAddress(event.target.value);
                break;
            case "location":
                setLocation(event.target.value);
                break;
        }
    }

    const handleSelect = (location) => {
        setLocation(location)
    }

    const handleClick = () => {
        if (props.action === "add") {
            props.handleAdd(
                {
                    id: id,
                    name: name,
                    address: address,
                    location: location
                })
        } else {
            props.handleEdit(
                {
                    id: id,
                    name: name,
                    address: address,
                    location: location
                })
        }
    }

    const getAction = () => {
        if (props.action === "add")
            return "actions.add"
        else
            return "actions.edit"
    }

    return (
        <>
            <Modal show={props.show} onHide={props.hideModal}>
                <Modal.Header closeButton>
                    <Modal.Title>{t(getAction())} {t("CLINIC.clinic")}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form>
                        <Form.Group className="mb-3" controlId="name">
                            <Form.Label>{t("FORM.name")}</Form.Label>
                            <Form.Control value={name}
                                          placeholder={t("FORM.enterName")}
                                          onChange={onChange}/>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="address">
                            <Form.Label>{t("FORM.address")}</Form.Label>
                            <Form.Control value={address}
                                          placeholder={t("FORM.enterAddress")}
                                          onChange={onChange}/>
                        </Form.Group>

                        <Form.Group className="mb-3" controlId="location">
                            <Form.Label>{t("FORM.location")} {location}</Form.Label>
                            <DropDownList iterable={props.locations}
                                          selectedElement={location}
                                          handleSelect={handleSelect}
                                          elementType={t("FORM.selectLocation")}
                                          id='location'/>
                        </Form.Group>
                    </Form>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={props.hideModal}>
                        {t("closeButton")}
                    </Button>
                    <Button className="doc-button-color" onClick={handleClick} >
                        {t(getAction())}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )

}

export default ClinicEditModal;