import React, { useState } from "react";
import "css/auth.css";
import "css/margins.css";
import { Form, Button, Alert } from "react-bootstrap";
import axios from "axios";
import { useHistory } from "react-router-dom";
import { connect } from "react-redux";
import { registerSuccess } from "store/actions/auth";

const errorMessageTimeoutMs = 5000;
const homePageRedirectTimeoutMs = 2000;

const errMsg = (msg, cancelTimeout) => ({
  msg,
  cancelTimeout,
});

const Register = ({ registerSuccess }) => {
  const [inputs, setInputs] = useState({
    username: "",
    email: "",
    password: "",
    password2: "",
  });
  const [errorMessage, setErrorMessage] = useState(errMsg(null, null));
  const [registrationSuccessful, setRS] = useState(false);

  const history = useHistory();

  const handleInput = (event) => {
    const { name, value } = event.target;
    setInputs({ ...inputs, [name]: value });
  };

  const handleErrorMessage = (msg) => {
    if (errorMessage.msg) {
      errorMessage.cancelTimeout();
    }

    const timeoutCanc = setTimeout(() => {
      setErrorMessage(errMsg(null, null));
    }, errorMessageTimeoutMs);

    setErrorMessage(msg, timeoutCanc);
  };

  const register = async () => {
    const config = {
      headers: {
        "Content-Type": "application/json",
      },
    };

    const body = JSON.stringify(inputs);

    try {
      const res = await axios.post("/api/auth/register", body, config);

      if (res.data.error) {
        handleErrorMessage(res.data.error);
      } else {
        const { token } = res.data;

        registerSuccess(token);

        setRS(true);
        setTimeout(() => {
          history.push("/index");
        }, homePageRedirectTimeoutMs);
      }
    } catch (error) {
      handleErrorMessage("An error has occured, please try again.");
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (inputs.password !== inputs.password2) {
      handleErrorMessage("Passwords do not match");
    }

    register();
  };

  return (
    <div className="auth-form">
      <h1 className="margin-bottom">Register</h1>
      <Form className="margin-bottom" onSubmit={handleSubmit}>
        <Form.Group name="username">
          <Form.Label>Username</Form.Label>
          <Form.Control
            placeholder="Username"
            onChange={handleInput}
            value={inputs.username}
            name="username"
          />
        </Form.Group>
        <Form.Group controlId="formBasicEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control
            type="email"
            placeholder="Enter email"
            onChange={handleInput}
            value={inputs.email}
            name="email"
          />
        </Form.Group>
        <Form.Group controlId="formBasicPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            onChange={handleInput}
            value={inputs.password}
            name="password"
          />
        </Form.Group>
        <Form.Group controlId="formBasicPassword">
          <Form.Label>Repeat Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Repeat Password"
            onChange={handleInput}
            value={inputs.password2}
            name="password2"
          />
        </Form.Group>
        <Button type="submit">Submit</Button>
      </Form>
      {registrationSuccessful ? (
        <Alert variant="success">
          Registration successful, you will be soon redirected to home page...
        </Alert>
      ) : errorMessage.msg ? (
        <Alert variant="danger">{errorMessage}</Alert>
      ) : null}
    </div>
  );
};

// const mapStateToProps = (state) => ({
//   loggedIn: !!state.token,
// });

export default connect(null, { registerSuccess })(Register);
