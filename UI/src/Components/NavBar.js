import React, {useEffect, useState} from 'react';
import {Container, Nav, ButtonGroup, Button, Navbar} from 'react-bootstrap';
import 'bootstrap/dist/css/bootstrap.min.css';
import PropTypes from 'prop-types';
import './NavBar.css'
import ApiCalls from "../api/apiCalls";
import {Link, useLocation, useNavigate} from "react-router-dom";
import { useTranslation } from "react-i18next";
import "../i18n/i18n";
import {changeLanguage} from "i18next";


function NavBar(props) {
    const navigate = useNavigate()
    const location = useLocation()
    const [items, setItems] = useState([]);
    const { t } = useTranslation();

    const userNavbarItems = [
        {
            link: '/paw-2019b-4/favorites',
            text: "favourites"
        },
        {
            link: '/paw-2019b-4/appointments',
            text: 'appointments'
        },
        {
            link: '/paw-2019b-4/profile',
            text: 'profile'
        }
    ]

    const docNavBarItems = [
        {
            link: '/paw-2019b-4/doctor/clinics',
            text: "clinics"
        },
        {
            link: '/paw-2019b-4/doctor/appointments',
            text: 'appointments'
        }
    ]
    const getItems = () => {
        if (!props.isAuth()) return [];
        switch (localStorage.getItem('role')) {
            case "ROLE_ADMIN":
                return [];
            case "ROLE_DOCTOR":
                return docNavBarItems;
            case "ROLE_USER":
                return userNavbarItems;
        }
    }

    useEffect(() => {
        setItems(getItems())
    },[])

    const handleLogout = () => {
        ApiCalls.logout().then(() => {
            navigate("/paw-2019b-4");
            window.location.reload()
        })

    }

    const getRoleHome = () => {
        if (!props.isAuth()) return "/paw-2019b-4";
        switch (localStorage.getItem('role')) {
            case "ROLE_ADMIN":
                return '/paw-2019b-4/admin';
            case "ROLE_DOCTOR":
                return '/paw-2019b-4/doctor';
            case "ROLE_USER":
                return '/paw-2019b-4';
        }
    }

    return (
        <>
            <Navbar variant="dark" expand="lg" sticky="top" className="container-fluid nav-bar shadow-sm">
                <Container style={{justifyContent: "flex-start"}}>
                    <Navbar.Brand as={Link} to={getRoleHome()}>DoctorSearch</Navbar.Brand>
                    {items.map((item) => {
                        return (
                            <Nav.Item class="ml-auto">
                                <Nav.Link as={Link} to={item.link} style={{color: "white"}}>{t("NAVBAR." +item.text)}</Nav.Link>
                            </Nav.Item>
                        )
                    })}
                </Container>
                <Container style={{justifyContent: "flex-end"}}>
                    {localStorage.getItem('role') !== null ?
                        <Nav.Item class="ml-auto">
                            <Nav.Link onClick={() => handleLogout()} style={{color: "white"}}>{t('NAVBAR.logout')}</Nav.Link>
                        </Nav.Item>
                        :
                        <Nav.Item  class="ml-auto">
                            <Nav.Link as={Link} to="/paw-2019b-4/signUp" style={{color: "white"}}>{t('NAVBAR.signUp')}</Nav.Link>
                        </Nav.Item>}
                    {localStorage.getItem('role') !== null ?
                        ''
                        :
                        <Nav.Item class="ml-auto">
                            <Nav.Link as={Link} to="/paw-2019b-4/login" style={{color: "white"}}>{t('NAVBAR.login')}</Nav.Link>
                        </Nav.Item>}
                    <ButtonGroup aria-label="Basic example">
                        <Button className="lang-buttons" onClick={() => changeLanguage('en')}>EN</Button>
                        <Button className="lang-buttons" onClick={() => changeLanguage('es')}>ES</Button>
                    </ButtonGroup>
                </Container>
            </Navbar>
        </>
    )

}

NavBar.propTypes = {
    items: PropTypes.array.isRequired,
    test: PropTypes.string.isRequired

}

export default NavBar