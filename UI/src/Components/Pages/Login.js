import React, {Component, useState} from "react";

import Form from "react-validation/build/form";
import Input from "react-validation/build/input";
import CheckButton from "react-validation/build/button";

import ApiCalls from "../../api/apiCalls"
import "./Login.css"
import './AdminHome.css';
import '../CardContainer.css'
import '../../i18n/i18n'

import {useLocation, useNavigate} from "react-router-dom";
import {useTranslation} from "react-i18next";


const required = (value) => {
    if (!value) {
        return (
            <div className="alert alert-danger" role="alert">
                This field is required!
            </div>
        );
    }
};

const WrappedLogin = props => {
    const navigate = useNavigate()
    const location = useLocation()
    const {t} = useTranslation()

    return <Login navigate={navigate} t={t} location={location} setRole={props.setRole} {...props} />
}

class Login extends Component {

    constructor(props) {
        super(props);
        this.handleLogin = this.handleLogin.bind(this);
        this.onChangeEmail = this.onChangeEmail.bind(this);
        this.onChangePassword = this.onChangePassword.bind(this);

        this.state = {
            email: "",
            password: "",
            loading: false,
            message: ""
        };

    }

    onChangeEmail(e) {
        this.setState({
            email: e.target.value
        });
    }

    onChangePassword(e) {
        this.setState({
            password: e.target.value
        });
    }

    handleLogin(e) {
        e.preventDefault();

        this.setState({
            message: "",
            loading: true
        });

        this.form.validateAll();

        if (this.checkBtn.context._errors.length === 0) {
            ApiCalls.login(this.state.email, this.state.password).then(
                (resp) => {
                    if (resp.status === 200) {
                        const role = resp.headers.xRole
                        this.props.setRole(role)
                        switch (role) {
                            case "ROLE_ADMIN":
                                this.props.navigate("/paw-2019b-4/admin");
                                break;
                            case "ROLE_DOCTOR":
                                this.props.navigate("/paw-2019b-4/doctor");
                                break;
                            case "ROLE_USER":
                                if (localStorage.getItem("path") !== null) {
                                    this.props.navigate(localStorage.getItem("path"));
                                    localStorage.removeItem("path")
                                } else {
                                    this.props.navigate("/paw-2019b-4");
                                    // window.location.reload()
                                }
                                break;
                        }
                    }
                    if (resp.status === 401) {
                        this.setState({
                            loading: false,
                            message: "Email or password are not correct. Try again"
                        });
                        this.props.navigate("/paw-2019b-4/login")
                    }
                },
                error => {
                    const resMessage =
                        (error.response &&
                            error.response.data &&
                            error.response.data.message) ||
                        error.message ||
                        error.toString();

                    this.setState({
                        loading: false,
                        message: resMessage
                    });
                }
            );
        } else {
            this.setState({
                loading: false
            });
        }
    }

    render() {
        return (
            <div className="col-md-12">
                <div className="card card-container centered background">
                    <Form
                        onSubmit={this.handleLogin}
                        ref={c => {
                            this.form = c;
                        }}
                    >
                        <div className="form-group" className="labels mb-4">
                            <label htmlFor="email" >{this.props.t("FORM.email")}</label>
                            <Input
                                type="email"
                                className="form-contro text-fields"
                                name="email"
                                value={this.state.email}
                                onChange={this.onChangeEmail}
                                validations={[required]}
                            />
                        </div>

                        <div className="form-group" className="labels">
                            <label htmlFor="password">{this.props.t("FORM.password")}</label>
                            <Input
                                type="password"
                                className="form-control text-fields"
                                name="password"
                                value={this.state.password}
                                onChange={this.onChangePassword}
                                validations={[required]}
                            />
                        </div>

                        <div className="form-group">
                            <button
                                className="btn btn-block login-button"
                                disabled={this.state.loading}
                            >
                                {this.state.loading && (
                                    <span className="spinner-border spinner-border-sm"/>
                                )}
                                <span>{this.props.t("NAVBAR.login")}</span>
                            </button>
                        </div>

                        {this.state.message && (
                            <div className="form-group">
                                <div className="alert alert-danger" role="alert">
                                    {this.state.message}
                                </div>
                            </div>
                        )}
                        <CheckButton
                            style={{ display: "none" }}
                            ref={c => {
                                this.checkBtn = c;
                            }}
                        />
                    </Form>
                </div>
            </div>
        );
    }

}

export default WrappedLogin