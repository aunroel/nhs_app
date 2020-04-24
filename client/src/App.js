import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  useHistory,
} from "react-router-dom";
import { Card, Container, Navbar } from "react-bootstrap";
import "css/App.css";
import Dashboard from "./components/Dashboard/Dashboard";
import Header from "./components/Header/Header";

const App = () => {
  return (
    <Router>
      <div className="container-for-content-and-footer">
        <div>
          <Header />
          <Container>
            <Switch>
              <Route path="/dashboard" component={Dashboard} />
            </Switch>
          </Container>
        </div>
        <Card.Footer bg="light">
          <Container>
            <text className="footer">NHSX Project, UCL - 2020</text>
          </Container>
        </Card.Footer>
      </div>
    </Router>
  );
};

export default App;
