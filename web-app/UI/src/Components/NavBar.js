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
import {WEB_CONTEXT} from "../Constants";


function NavBar(props) {
    const navigate = useNavigate()
    const location = useLocation()
    const [items, setItems] = useState([]);
    const [home, setHome] = useState("")
    const [loggedIn, setLoggedIn] = useState(false)
    const { t } = useTranslation();

    const userNavbarItems = [
        {
            link: "/"  + WEB_CONTEXT + "/favorites",
            text: "favourites"
        },
        {
            link: "/"  + WEB_CONTEXT + "/appointments",
            text: 'appointments'
        },
        {
            link: "/"  + WEB_CONTEXT + "/profile",
            text: 'profile'
        }
    ]

    const docNavBarItems = [
        {
            link: "/"  + WEB_CONTEXT + "/doctor/clinics",
            text: "clinics"
        },
        {
            link: "/"  + WEB_CONTEXT + "/doctor/appointments",
            text: 'appointments'
        }
    ]
    const getItems = (role) => {
        if (!props.isAuth()) return [];
        // eslint-disable-next-line default-case
        switch (role) {
            case "ROLE_ADMIN":
                return [];
            case "ROLE_DOCTOR":
                return docNavBarItems;
            case "ROLE_USER":
                return userNavbarItems;
        }
    }

    useEffect(() => {
        function checkUserData() {
            setItems(getItems(props.role))
            setHome(getRoleHome(props.role))
            setLoggedIn(props.isAuth())
        }

        checkUserData();
        // window.addEventListener('storage', checkUserData)
        //
        // return () => {
        //     window.removeEventListener('storage', checkUserData)
        // }

    },[props.role])

    const handleLogout = () => {
        props.setRole(null)
        ApiCalls.logout()
        navigate("/"  + WEB_CONTEXT);

    }

    const getRoleHome = (role) => {
        if (!props.isAuth()) return "/"  + WEB_CONTEXT;
        switch (role) {
            case "ROLE_ADMIN":
                return "/"  + WEB_CONTEXT + "/admin";
            case "ROLE_DOCTOR":
                return "/"  + WEB_CONTEXT + "/doctor";
            case "ROLE_USER":
                return "/"  + WEB_CONTEXT;
        }
    }

    return (
        <>
            <Navbar variant="dark" expand="lg" sticky="top" className="container-fluid nav-bar shadow-sm">
                <Container style={{justifyContent: "flex-start"}}>
                    <Navbar.Brand as={Link} to={home}>DoctorSearch</Navbar.Brand>
                    {items.map((item) => {
                        return (
                            <Nav.Item class="ml-auto">
                                <Nav.Link as={Link} to={item.link} style={{color: "white"}}>{t("NAVBAR." +item.text)}</Nav.Link>
                            </Nav.Item>
                        )
                    })}
                </Container>
                <Container style={{justifyContent: "flex-end"}}>
                    {loggedIn ?
                        <Nav.Item class="ml-auto">
                            <Nav.Link onClick={() => handleLogout()} style={{color: "white"}}>{t('NAVBAR.logout')}</Nav.Link>
                        </Nav.Item>
                        :
                        <Nav.Item  class="ml-auto">
                            <Nav.Link as={Link} to={"/"  + WEB_CONTEXT + "/signUp"} style={{color: "white"}}>{t('NAVBAR.signUp')}</Nav.Link>
                        </Nav.Item>}
                    {loggedIn ?
                        ''
                        :
                        <Nav.Item class="ml-auto">
                            <Nav.Link as={Link} to={"/"  + WEB_CONTEXT + "/login"} style={{color: "white"}}>{t('NAVBAR.login')}</Nav.Link>
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