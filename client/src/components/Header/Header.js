import React from "react";
import { Navbar, Nav, NavDropdown, Button, Container } from "react-bootstrap";
import { Link } from "react-router-dom";
import "css/header.css";
import HeaderLink from "./HeaderLink";

const Header = () => {
  const loggedIn = false;

  return (
    <>
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="index">NHSX App</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
              <HeaderLink to="/index" text="Home" />

              {loggedIn ? (
                <>
                  <HeaderLink to="/doc" text="API Doc" />
                  <HeaderLink to="/available" text="Availibility" />
                </>
              ) : null}
            </Nav>
            <Nav>
              {loggedIn ? (
                <HeaderLink to="/logout" text="Logout" />
              ) : (
                <HeaderLink to="/login" text="Login" />
              )}
            </Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};

Header.propTypes = {};

export default Header;
