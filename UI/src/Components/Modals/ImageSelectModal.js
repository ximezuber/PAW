import React, {useState} from "react";
import {use} from "i18next";
import {Button, Form, Modal} from "react-bootstrap";
import {useTranslation} from "react-i18next";

function ImageSelectModal(props) {
    const [selectedImage, setSelectedImage] = useState(null)
    const [show, setShow] = useState(false)
    const {t} = useTranslation()

    const handleShow = () => {
        setShow(!show)
    }

    const onFileChange = (event) => {
        setSelectedImage(event.target.files[0]);
    }

    const onUploadImage = () => {
        const formData = new FormData();
        formData.append(
            "profileImage",
            selectedImage,
            selectedImage.name
        );
        props.handleUpload(formData)
        handleShow();

    }

    return (
        <>
            <Button className="mx-3 shadow-sm doc-button-color" onClick={handleShow}>
                {t('changeImgButton')}
            </Button>
            <Modal show={show} onHide={handleShow}>
                <Modal.Header closeButton>
                    <Modal.Title>{t("DOC.editPrice")}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3" controlId="name">
                        <Form.Label>{t("DOC.image")}</Form.Label>
                        <Form.Control type="file"
                                      onChange={onFileChange}/>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleShow}>
                        {t("closeButton")}
                    </Button>
                    <Button className="doc-button-color" onClick={() => onUploadImage()}>
                        {t("actions.upload")}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>
    )
}

export default ImageSelectModal