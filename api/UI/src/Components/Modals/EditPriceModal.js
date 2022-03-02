import React, {useState} from "react";
import {useTranslation} from "react-i18next";
import {Button, Form, Modal} from "react-bootstrap";

function EditPriceModal(props) {
    const [show, setShow] = useState(false);
    const [newPrice, setNewPrice] = useState(props.price);
    const { t } = useTranslation();

    const handleShow = () => {
        setShow(!show)
    }

    const handleEdit = () => {
        props.handleEdit(newPrice)
        setNewPrice(0)
        handleShow()
    }

    const onChange = (event) => {
        setNewPrice(event.target.value)
    }

    return (
        <>
            <Button className="mx-3 shadow-sm doc-button-color" onClick={handleShow}>
                <i className="far fa-edit"/>
            </Button>
            <Modal show={show} onHide={handleShow}>
                <Modal.Header closeButton>
                    <Modal.Title>{t("DOC.editPrice")}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3" controlId="name">
                        <Form.Label>{t("DOC.price")}</Form.Label>
                        <Form.Control value={newPrice}
                                      placeholder={t("FORM.enterPrice")}
                                      onChange={onChange}/>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleShow}>
                        {t("closeButton")}
                    </Button>
                    <Button className="doc-button-color" onClick={() => handleEdit()}>
                        {t("actions.edit")}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

export default EditPriceModal