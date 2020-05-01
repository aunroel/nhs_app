import React from "react";
import "css/auth.css";
import "css/margins.css";
import { Container, Form, Button } from "react-bootstrap";
import { Link } from "react-router-dom";

const Login = () => {
  return (
    <div className="auth-form">
      <h1 className="margin-bottom">Log In</h1>
      <Form className="margin-bottom">
        <Form.Group controlId="formBasicEmail">
          <Form.Label>Email address</Form.Label>
          <Form.Control type="email" placeholder="Enter email" />
        </Form.Group>

        <Form.Group controlId="formBasicPassword">
          <Form.Label>Password</Form.Label>
          <Form.Control type="password" placeholder="Password" />
        </Form.Group>
        <Form.Group controlId="formBasicCheckbox">
          <Form.Check type="checkbox" label="Remember me" />
        </Form.Group>

        <Button type="submit">Submit</Button>
      </Form>
      <div>
        New User? <Link to="/register">Click here to register!</Link>
      </div>
    </div>
  );
};

export default Login;
