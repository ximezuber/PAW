import React, {useEffect, useState} from 'react';
import {Form, FormControl, FormGroup, InputGroup, Button} from "react-bootstrap";
import DropDownList from "./DropDownList";
import LocationCalls from "../api/LocationCalls";
import SpecialtyCalls from "../api/SpecialtyCalls";
import PrepaidCalls from "../api/PrepaidCalls";
import {useTranslation} from "react-i18next";
import {FIRST} from "./Pages/Constants";
import './SearchBar.css';

function SearchBar(props) {
    const {t} = useTranslation()
    const [selectedSpecialty, setSelectedSpecialty] = useState(props.specialty === "null"? '-': props.specialty)
    const [selectedLocation, setSelectedLocation] = useState(props.location === "null"? '-': props.location)
    const [selectedPrepaid, setSelectedPrepaid] = useState(props.prepaid === "null"? '-': props.prepaid)
    const [firstName, setFirstName] = useState(props.firstName === "null"? '': props.firstName)
    const [lastName, setLastName] = useState(props.lastName === "null"? '': props.lastName)
    const [consultPrice, setConsultPrice] = useState(props.consultPrice === "null" || props.consultPrice  === "0"?
        '': props.consultPrice)
    const [locations, setLocations] = useState([])
    const [specialties, setSpecialties] = useState([])
    const [prepaids, setPrepaids] = useState([])


    const fetchLocations = async () => {
        const response = await LocationCalls.getAllLocations();
        if (response && response.ok) {
            const data = response.data
            data.push({name: '-'})
            setLocations(data);

        }
    }

    const fetchSpecialties = async () => {
        const response = await SpecialtyCalls.getAllSpecialties();
        if (response && response.ok) {
            const data = response.data
            data.push({name: '-'})
            setSpecialties(data);
        }

    }

    const fetchPrepaids = async () => {
        const response = await PrepaidCalls.getAllPrepaid();
        if (response && response.ok) {
            const data = response.data
            data.push({name: '-'})
            setPrepaids(data);
        }
    }

    useEffect(async () => {
        await fetchPrepaids();
        await fetchLocations();
        await fetchSpecialties();
    }, [])


    const onChange = (event) => {
        // eslint-disable-next-line default-case
        switch (event.target.id) {
            case "firstName":
                setFirstName(event.target.value)
                break;
            case "lastName":
                setLastName(event.target.value)
                break;
            case "consultPrice":
                setConsultPrice(event.target.value)
                break;
        }
    }

    const handleSelectSpecialty = (specialty) => {
        setSelectedSpecialty(specialty)
    }

    const handleSelectLocation = (location) => {
        setSelectedLocation(location)
    }

    const handleSelectPrepaid = (prepaid) => {
        setSelectedPrepaid(prepaid)
    }

    const handleClear = () => {
        setSelectedLocation('-')
        setSelectedSpecialty('-')
        setSelectedPrepaid('-')
        setLastName('')
        setFirstName('')
        setConsultPrice('')
        props.handleSearch({
                firstName: null,
                lastName: null,
                location: null,
                specialty: null,
                prepaid: null,
                consultPrice: null
            }, FIRST
        )
    }


    return (
        <>
            <div className="w3-sidebar w3-bar-block" style={{width: '100%'}}>
                <Form>
                    <div className="list-group-item list-group-item-action">
                        <FormGroup className="mb-3" controlId="location">
                                    <Form.Label>{t("FORM.location")} {selectedLocation}</Form.Label>
                                    <DropDownList iterable={locations.map(loc => loc.name)}
                                                  selectedElement=''
                                                  handleSelect={handleSelectLocation}
                                                  elementType={t("FORM.selectLocation")}
                                                  id='location'/>
                                </FormGroup>
                    </div>

                    <div className="list-group-item list-group-item-action">
                        <FormGroup className="mb-3" controlId="specialty">
                                    <Form.Label>{t("FORM.specialty")} {selectedSpecialty}</Form.Label>
                                    <DropDownList iterable={specialties.map(spe => spe.name)}
                                                  selectedElement=''
                                                  handleSelect={handleSelectSpecialty}
                                                  elementType={t("FORM.selectSpecialty")}
                                                  id='specialty'/>
                                </FormGroup>
                    </div>

                    <div className="list-group-item list-group-item-action">
                        <FormGroup className="mb-3" controlId="prepaid">
                                    <Form.Label>{t("ADMIN.prepaid")}: {selectedPrepaid}</Form.Label>
                                    <DropDownList iterable={prepaids.map(pre => pre.name)}
                                                  selectedElement=''
                                                  handleSelect={handleSelectPrepaid}
                                                  elementType={t("FORM.selectPrepaid")}
                                                  id='prepaid'/>
                                </FormGroup>
                    </div>

                    <div className="list-group-item list-group-item-action">
                        <FormGroup className="mb-3" controlId="firstName">
                                    <Form.Label>{t("FORM.firstName")}</Form.Label>
                                    <Form.Control placeholder="Enter first name" value={firstName}
                                                  onChange={onChange}/>
                                </FormGroup>
                    </div>

                    <div className="list-group-item list-group-item-action">
                        <Form.Group className="mb-3" controlId="lastName">
                                    <Form.Label>{t("FORM.lastName")}</Form.Label>
                                    <Form.Control placeholder="Enter last name" value={lastName}
                                                  onChange={onChange}/>
                                </Form.Group>
                    </div>

                    <div className="list-group-item list-group-item-action">
                        <FormGroup controlId="consultPrice">
                            <Form.Label>{t("FORM.maxPrice")}</Form.Label>
                            <InputGroup className="mb-3">
                                <InputGroup.Text>$</InputGroup.Text>
                                <FormControl aria-label="Amount (to the nearest dollar)" value={consultPrice}
                                             onChange={onChange}/>
                                <InputGroup.Text>.00</InputGroup.Text>
                            </InputGroup>
                        </FormGroup>
                    </div>
                    <div className="list-group-item list-group-item-action button-div">
                        <Button className="doc-button-color" onClick={() => props.handleSearch({
                                firstName: firstName,
                                lastName: lastName,
                                location: selectedLocation === '-'? null : selectedLocation,
                                specialty: selectedSpecialty === '-'? null : selectedSpecialty,
                                prepaid: selectedPrepaid === '-'? null : selectedPrepaid,
                                consultPrice: consultPrice
                            }, FIRST
                        )}>
                            {t("searchButton")}
                        </Button>
                        <Button className="doc-button-color" onClick={() => handleClear()}>
                            {t("clearButton")}
                        </Button>
                    </div>
                </Form>
            </div>
        </>
    )
}

export default SearchBar