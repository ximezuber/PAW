import React, {useEffect, useState} from 'react';
import SearchBar from "../SearchBar";
import {Button, Card, Col, Container, Row} from "react-bootstrap";
import {Link, useSearchParams} from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';
import DoctorCalls from "../../api/DoctorCalls";
import './home.css'
import {useTranslation} from "react-i18next";
import {getPaths} from "../../utils/paginationHelper";
import {CURRENT, FIRST, NEXT, PREV} from "./Constants";
import ApiCalls from "../../api/apiCalls";
import * as path from "path";


function Home(props) {
    const [searchParams, setSearchParams] = useSearchParams()
    const [doctors, setDoctors] = useState([])
    const [message, setMessage] = useState("")
    const [searchCriteria, setSearchCriteria] = useState(null)
    const [paths, setPaths] = useState({})
    const [loading, setLoading] = useState(false)
    const {t} = useTranslation()

    const setPages = (linkHeader) => {
        const paths1 = getPaths(linkHeader);
        localStorage.setItem('pathCurrent', paths1[CURRENT])
        setPaths(paths1)
    }

    const fetchAllDoctorsWithAvailability = async (queryParams) => {
        setLoading(true)
        const response = await DoctorCalls.searchDocs(0, queryParams)
        if (response && response.ok) {
            setDoctors(response.data)
            setPages(response.headers.link)
            setMessage("")
        }
        setLoading(false)
    }

    const handleSearchParams = (criteria) => {
        let queryParams = "&";
        queryParams += criteria.location === null?
            "": "location=" + criteria.location + "&";
        queryParams += criteria.specialty === null?
            "": "specialty=" + criteria.specialty + "&";
        queryParams += criteria.firstName === null || criteria.firstName  === ""?
            "": "firstName=" + criteria.firstName + "&";
        queryParams += criteria.lastName  === null || criteria.lastName === ""?
            "": "lastName=" + criteria.lastName + "&";
        queryParams += criteria.consultPrice === null || criteria.consultPrice === 0 || criteria.consultPrice === ""?
            "": "consultPrice=" + criteria.consultPrice + "&";
        queryParams += criteria.prepaid === null?
            "": "prepaid=" + criteria.consultPrice + "&";

        return queryParams.slice(0,-1)
    }

    useEffect( () => {
        async function fetchData () {
            const search = {
                location: searchParams.get('location') === "null" ? null : searchParams.get('location'),
                specialty: searchParams.get('specialty') === "null" ? null : searchParams.get('specialty'),
                firstName: searchParams.get('firstName') === "null" ? null : searchParams.get('firstName'),
                lastName: searchParams.get('lastName') === "null" ? null : searchParams.get('lastName'),
                consultPrice: searchParams.get('consultPrice') === "null" || searchParams.get('consultPrice') === "0" ?
                    null : searchParams.get('consultPrice'),
                prepaid: searchParams.get('prepaid') === "null" ? null : searchParams.get('prepaid')

            }
            await handleSearch(search, CURRENT);

            setSearchParams({
                'location': searchParams.get('location') === "null"? null: searchParams.get('location'),
                'specialty': searchParams.get('specialty') === "null"? null: searchParams.get('specialty'),
                'firstName': searchParams.get('firstName') === "null"? null: searchParams.get('firstName'),
                'lastName': searchParams.get('lastName') === "null"? null: searchParams.get('lastName'),
                'consultPrice': searchParams.get('consultPrice') === "null" || searchParams.get('consultPrice') === "0"?
                    null: searchParams.get('consultPrice'),
                'prepaid': searchParams.get('prepaid') === "null"? null: searchParams.get('prepaid')
            })
        }
        fetchData();

    }, [])

    const nextPage = async () => {
        setMessage("")
        await handleSearch(searchCriteria, NEXT)

    }
    const prevPage = async () => {
        setMessage("")
        await handleSearch(searchCriteria, PREV)
    }

    const renderPrevButton = () => {

        if (paths[PREV]) {
            return <Button className="doc-button doc-button-color shadow-sm"
                           onClick={() => prevPage()}>{t('prevButton')}</Button>
        }
    }

    const renderNextButton = () => {
        if (paths[NEXT]) {
            return <Button className="doc-button doc-button-color shadow-sm"
                           onClick={() => nextPage()}>{t('nextButton')}</Button>
        }
    }

    const handleSearch = async (criteria, page) => {

        setLoading(true)
        setSearchCriteria(criteria);
        setSearchParams({
            'location':criteria.location,
            'specialty': criteria.specialty,
            'firstName': criteria.firstName,
            'lastName': criteria.lastName,
            'consultPrice': criteria.consultPrice,
            'prepaid':criteria.prepaid
        })
        const queryParams = handleSearchParams(criteria);

        if (page === FIRST) {
            await fetchAllDoctorsWithAvailability(queryParams);
            return;
        }
        let path = paths[page];

        if (!path) {
            path = localStorage.getItem('pathCurrent')
            if (!path) {
                await fetchAllDoctorsWithAvailability(queryParams);
                return;
            }
        }

        const response = await ApiCalls.makeGetCall(path + queryParams)

        if (response && response.ok) {
            setDoctors(response.data)
            setPages(response.headers.link)
            setMessage("")
        }
        setLoading(false)
    }

    return (
        <>
            <Row>
                <Col>
                    <SearchBar handleSearch={handleSearch}
                               location={searchParams.get('location')}
                               specialty={searchParams.get('specialty')}
                               firstName={searchParams.get('firstName')}
                               lastName={searchParams.get('lastName')}
                               prepaid={searchParams.get('prepaid')}
                               consultPrice={searchParams.get('consultPrice')}
                    />
                </Col>
                <Col xs={9}>
                    <Container>
                        <div className="admin-info-container search-doctor-container">
                            {doctors.map((doctor) => {
                                return (
                                    <Card className="mb-3 doc-card shadow"
                                          style={{color: "#000", width: '20rem', height: '8rem'}}
                                          key={doctor.license}>
                                        <Card.Body className="card-body-doc">
                                            <Card.Title>{doctor.firstName + ' ' + doctor.lastName}</Card.Title>
                                            <Card.Text>
                                                {doctor.specialty}
                                            </Card.Text>
                                        </Card.Body>
                                        <Link className="doc-button-color btn m-1"
                                              role="button"
                                              to={doctor.license + '/profile'}>{t("USER.seeProfile")}
                                        </Link>
                                    </Card>
                                )
                            })}
                        </div>
                    </Container>
                    <div>
                        {renderPrevButton()}
                        {renderNextButton()}
                    </div>
                    {loading && (
                        <span className="spinner-border mt-3" style={{marginRight:"1rem"}}/>
                    )}
                </Col>
            </Row>
        </>
    )
}

export default Home