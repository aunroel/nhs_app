import React from "react";
import {
  BrowserRouter as Router,
  Switch,
  Route,
  useHistory,
} from "react-router-dom";
import "./css/App.css";
import Dashboard from "./components/Dashboard/Dashboard";
import Header from "./components/Header/Header";

const App = () => {
  return (
    // <Container>
    <Router>
      <Header />
      <Switch>
        <Route path="/dashboard" component={Dashboard} />
      </Switch>
    </Router>
    // </Container>
  );
};

export default App;
