import React, {Component} from 'react';
import {Link} from "react-router-dom";
import {Card, Col, Row} from "react-bootstrap";
import "../../i18n/i18n";

import './AdminHome.css';
import {useTranslation} from "react-i18next";

function AdminHome(props) {
    const { t } = useTranslation();

    return (
        <>
            <container>
                <Row className="admin_row">
                    <Col>
                        <Card className="admin_card shadow" style={{marginLeft: '3.5rem', marginRight: '1rem'}}>
                            <Card.Img variant="top" className="card_img" src="/paw-2019b-4/images/doctor.png" />
                            <Card.Body className="card_body">
                                <Card.Title style={{verticalAlign: "bottom"}}>{t('ADMIN.doctors')}</Card.Title>
                                <Link className="btn btn-outline-dark btn-lg"
                                      role="button"
                                      to="doctors">{t('ADMIN.seeDocs')}
                                </Link>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col>
                        <Card className="mb-3 admin_card shadow" style={{marginLeft: '2.5rem', marginRight: '1rem'}}>
                            <Card.Img variant="top" className="card_img" src="/paw-2019b-4/images/clinic.png" />
                            <Card.Body className="card_body">
                                <Card.Title>{t('ADMIN.clinics')}</Card.Title>
                                <Link className="btn btn-outline-dark btn-lg"
                                      role="button"
                                      to="clinics">{t('ADMIN.seeClinics')}
                                </Link>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col>
                        <Card className="mb-3 admin_card shadow" style={{ marginLeft: '1rem'}}>
                            <Card.Img variant="top" className="card_img" src="/paw-2019b-4/images/location.png" />
                            <Card.Body className="card_body">
                                <Card.Title>{t('ADMIN.locations')}</Card.Title>
                                <Link className="btn btn-outline-dark btn-lg"
                                      role="button"
                                      to="locations">{t('ADMIN.seeLocations')}
                                </Link>
                            </Card.Body>
                        </Card>
                    </Col>
                </Row>
                <Row className="admin_row">
                    <Col>
                        <Card className="mb-3 admin_card shadow" style={{marginLeft: '3.5rem', marginRight: '1rem'}}>
                            <Card.Img className="card_img" variant="top" src="/paw-2019b-4/images/specialty.png" />
                            <Card.Body className="card_body">
                                <Card.Title>{t('ADMIN.specialties')}</Card.Title>
                                <Link className="btn btn-outline-dark btn-lg"
                                      role="button"
                                      to="specialties">{t('ADMIN.seeSpecialties')}
                                </Link>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col>
                        <Card className="mb-3 admin_card shadow" style={{marginLeft: '2.5rem', marginRight: '1rem'}}>
                            <Card.Img variant="top" className="card_img" src="/paw-2019b-4/images/prepaid.png" />
                            <Card.Body className="card_body">
                                <Card.Title>{t('ADMIN.prepaids')}</Card.Title>
                                <Link className="btn btn-outline-dark btn-lg"
                                      role="button"
                                      to="prepaids">{t('ADMIN.seePrepaids')}
                                </Link>
                            </Card.Body>
                        </Card>
                    </Col>
                    <Col></Col>
                </Row>
            </container>
        </>
    )
}

export default AdminHome