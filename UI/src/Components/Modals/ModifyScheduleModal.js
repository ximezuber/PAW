import {Button, Form, Modal} from "react-bootstrap";
import React, {useEffect, useState} from "react";
import {useTranslation} from "react-i18next";
import DropDownList from "../DropDownList";
import DoctorCalls from "../../api/DoctorCalls";
import {getWeekDate} from "../../utils/dateHelper";

function ModifyScheduleModal(props) {
    const [show, setShow] = useState(false);
    const {t} = useTranslation()
    const [day, setDay] = useState(null);
    const [hour, setHour] = useState(null);
    const [available, setAvailable] = useState([])

    const handleShow = () => {
        setShow(!show);
    }

    useEffect(async () => {
        setAvailable(getAllSchedules());
    },[])

    const getAllSchedules = () => {
        const availableList = []
        for (let i = 9; i < 19; i++) {
            for (let j = 1; j < 8; j++) {
                availableList.push({day: j, hour: i})

            }
        }
        return availableList;

    }
    const handleSelectDay = (selectedDay) => {
        setDay(parseDays(selectedDay))
    }

    const handleSelectHour = (selectedHour) => {
        setHour(selectedHour)
    }

    const handleClick = () => {
        props.handleClick(day, hour)
        setDay(null)
        setHour(null)
        handleShow()

    }

    const getDays = () => {
        return [t("CAL.mon"), t("CAL.tue"), t("CAL.wed"), t("CAL.thu"), t("CAL.fri"), t("CAL.sat"), t("CAL.sun")]
    }

    const parseDays = (dayString) => {
        switch (dayString) {
            case t("CAL.mon"):
                return 1;
            case t("CAL.tue"):
                return 2;
            case t("CAL.wed"):
                return 3;
            case t("CAL.thu"):
                return 4;
            case t("CAL.fri"):
                return 5;
            case t("CAL.sat"):
                return 6;
            case t("CAL.sun"):
                return 7;
        }
    }
    const getHours = () => {
        if (day === null) {
            return []
        }
        return available.filter(s => s.day === day).map(s => s.hour)

    }

    return (
        <>
            <Button variant="outline-secondary" onClick={handleShow} size="lg" className="add-margin">
                {t("actions." + props.action)} {t("FORM.schedule")}
            </Button>
            <Modal show={show} onHide={handleShow}>
                <Modal.Header closeButton>
                    <Modal.Title>{t("actions."  + props.action)} {t("FORM.schedule")}</Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    <Form.Group className="mb-3" controlId="day">
                        <Form.Label>{t("FORM.day")} {day === null? "": t(getWeekDate(day))}</Form.Label>
                        <DropDownList iterable={getDays()}
                                      selectedElement={day}
                                      handleSelect={handleSelectDay}
                                      elementType={t("FORM.daySelect")}
                                      id='day'/>
                    </Form.Group>
                    <Form.Group className="mb-3" controlId="hour">
                        <Form.Label>{t("FORM.hour")} {hour === null? "": hour}</Form.Label>
                        <DropDownList iterable={getHours()}
                                      selectedElement={hour}
                                      handleSelect={handleSelectHour}
                                      elementType={t("FORM.hourSelect")}
                                      id='hour'/>
                    </Form.Group>
                </Modal.Body>
                <Modal.Footer>
                    <Button variant="secondary" onClick={handleShow}>
                        {t("closeButton")}
                    </Button>
                    <Button className="doc-button-color" onClick={() => handleClick()}>
                        {t("actions." + props.action)}
                    </Button>
                </Modal.Footer>
            </Modal>
        </>

    )
}

export default ModifyScheduleModal