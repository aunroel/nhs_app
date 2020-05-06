import React from "react";
import { Navbar, Nav, Container } from "react-bootstrap";
import "css/header.css";
import PropTypes from "prop-types";

const Header = ({ leftButtons, rightButtons }) => {
  return (
    <>
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="index">NHSX App</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto"> {leftButtons} </Nav>
            <Nav>{rightButtons}</Nav>
          </Navbar.Collapse>
        </Container>
      </Navbar>
    </>
  );
};

Header.propTypes = {
  leftButtons: PropTypes.func.isRequired,
  rightButtons: PropTypes.func.isRequired,
};

export default Header;
