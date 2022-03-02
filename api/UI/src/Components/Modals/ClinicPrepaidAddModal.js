import React, { useState} from 'react';
import {Button, Modal, Form} from "react-bootstrap";
import DropDownList from "../DropDownList";
import '../CardContainer.css'
import {useTranslation} from "react-i18next";
import "../../i18n/i18n"

 function ClinicPrepaidAddModal(props) {
    const [show, setShow] = useState(false);
    const [newPrepaid, setNewPrepaid] = useState('');
     const { t } = useTranslation();

    const handleSelect = (prepaid) => {
        setNewPrepaid(prepaid)
    }

    const handleShow = () => {
        setShow(!show)
    }

    const handleAdd = async () => {
        setNewPrepaid("")
        await props.handleAdd(newPrepaid)
        handleShow()

    }

    const remainingPrepaids = () => {
        return props.allPrepaids.filter(prepaid => !props.prepaids.includes(prepaid));
    }


    return (
        <>
            <Button variant="outline-secondary add-margin" onClick={handleShow} size="lg">
                {t("CLINIC.addPrepaid")}
            </Button>
            <Modal show={show} onHide={handleShow}>
                <Modal.Header closeButton>
                    <Modal.Title>{t("CLINIC.addPrepaid")}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3" controlId="name">
                        <Form.Label>{t("FORM.name")}: {newPrepaid}</Form.Label>
                        <DropDownList iterable={remainingPrepaids()}
                                      selectedElement=''
                                      handleSelect={handleSelect}
                                      elementType={t("FORM.selectPrepaid")}
                                      id='prepaid'/>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleShow}>
                        {t("closeButton")}
                    </Button>
                    <Button className="doc-button-color" onClick={handleAdd}>
                        {t("actions.add")}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )

}

export default ClinicPrepaidAddModal;