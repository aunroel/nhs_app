import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Container } from "react-bootstrap";
import "css/App.css";
import Dashboard from "./components/Dashboard/Dashboard";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";

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
        <Footer />
      </div>
    </Router>
  );
};

export default App;
