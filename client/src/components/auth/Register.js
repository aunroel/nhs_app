import React, { useState } from "react";
import "css/auth.css";
import "css/margins.css";
import { Form, Button, Alert } from "react-bootstrap";
import axios from "axios";
import { useHistory } from "react-router-dom";
import { connect } from "react-redux";
import { registerSuccess } from "store/actions/auth";

const Register = ({ registerSuccess, setErrorMessage, setSuccessMessage }) => {
  const [inputs, setInputs] = useState({
    username: "",
    email: "",
    password: "",
    password2: "",
  });

  const handleInput = (event) => {
    const { name, value } = event.target;
    setInputs({ ...inputs, [name]: value });
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
        setErrorMessage(res.data.error);
      } else {
        const { token } = res.data;

        registerSuccess(token);

        setSuccessMessage("Registration successful.");
      }
    } catch (error) {
      setErrorMessage("An error has occured, please try again.");
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    if (inputs.password !== inputs.password2) {
      setErrorMessage("Passwords do not match");
      return;
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
    </div>
  );
};

// const mapStateToProps = (state) => ({
//   loggedIn: !!state.token,
// });

export default connect(null, { registerSuccess })(Register);
