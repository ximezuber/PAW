import React, {Component, useState} from 'react';
import {Button, Modal, Form} from "react-bootstrap";
import {useTranslation} from "react-i18next";
import "../../i18n/i18n"

function SinglePropertyAddModal(props) {
    const [show, setShow] = useState(false);
    const [newProperty, setNewProperty] = useState('');
    const { t } = useTranslation();

    const handleShow = () => {
        setShow(!show)
    }

    const handleAdd = (newProp) => {
        props.handleAdd(newProp)
        setNewProperty("")
        handleShow()
    }

    const onChange = (event) => {
        setNewProperty(event.target.value)
    }

    return (
        <>
            <Button variant="outline-secondary" onClick={handleShow} size="lg" className="add-margin">
                {t("actions.add")} {props.property}
            </Button>
            <Modal show={show} onHide={handleShow}>
                <Modal.Header closeButton>
                    <Modal.Title>{t("actions.add")} {props.property}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3" controlId="name">
                        <Form.Label>{t("FORM.name")}</Form.Label>
                        <Form.Control value={newProperty}
                                  placeholder={t("FORM.enterName")}
                                  onChange={onChange}/>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleShow}>
                        {t("closeButton")}
                    </Button>
                    <Button className="doc-button-color" onClick={() => handleAdd({name: newProperty})}>
                        {t("actions.add")}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

export default SinglePropertyAddModal;