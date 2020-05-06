import React, { useState } from "react";
import "css/auth.css";
import "css/margins.css";
import { Container, Form, Button } from "react-bootstrap";
import { Link } from "react-router-dom";
import axios from "axios";
import { connect } from "react-redux";
import { loginSuccess } from "store/actions/auth";

const Login = ({ loginSuccess, setErrorMessage, setSuccessMessage }) => {
  const [inputs, setInputs] = useState({
    username: "",
    password: "",
  });

  const handleInput = (event) => {
    const { name, value } = event.target;
    setInputs({ ...inputs, [name]: value });
  };

  const login = async () => {
    const config = {
      headers: {
        "Content-Type": "application/json",
      },
    };

    const body = JSON.stringify(inputs);

    try {
      const res = await axios.post("/api/auth/login", body, config);

      if (res.data.error) {
        setErrorMessage(res.data.error);
      } else {
        const { token } = res.data;

        loginSuccess(token);

        setSuccessMessage("Login successful.");
      }
    } catch (error) {
      setErrorMessage("An error has occured, please try again.");
    }
  };

  const handleSubmit = (event) => {
    event.preventDefault();
    login();
  };

  return (
    <div className="auth-form">
      <h1 className="margin-bottom">Log In</h1>
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

        <Form.Group>
          <Form.Label>Password</Form.Label>
          <Form.Control
            type="password"
            placeholder="Password"
            onChange={handleInput}
            value={inputs.password}
            name="password"
          />
        </Form.Group>

        <Button type="submit">Submit</Button>
      </Form>
      <div>
        New User? <Link to="/register">Click here to register!</Link>
      </div>
      <br />
    </div>
  );
};

export default connect(null, { loginSuccess })(Login);
