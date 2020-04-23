import React from "react";
import { Navbar, Nav, NavDropdown, Button, Container } from "react-bootstrap";
const Header = () => {
  const loggedIn = false;

  return (
    <>
      <Navbar bg="light" expand="lg">
        <Container>
          <Navbar.Brand href="#home">NHSX App</Navbar.Brand>
          <Navbar.Toggle aria-controls="basic-navbar-nav" />
          <Navbar.Collapse id="basic-navbar-nav">
            <Nav className="mr-auto">
              <Nav.Link href="home">Home</Nav.Link>
              {loggedIn ? (
                <>
                  <Nav.Link href="dashboard">Dashboard</Nav.Link>
                  <Nav.Link href="doc">API Doc</Nav.Link>
                  <Nav.Link href="available">Availibility</Nav.Link>
                </>
              ) : null}
            </Nav>
            <Nav>
              {loggedIn ? (
                <Nav.Link href="login">Login</Nav.Link>
              ) : (
                <Nav.Link href="logout">Logout</Nav.Link>
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
