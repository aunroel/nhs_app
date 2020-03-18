import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import ReactSandbox from "./components/_Debugging/Sandbox/Pages/ReactSandbox";
import Container from "./components/AppContainer";
import Header from "./components/Header";
import ModelList from "./components/ModelList/ModelList";
import ModelUpload from "./components/ModelUpload/ModelUpload";
import "./App.css";
import { Register } from "./components/auth/Register";

const App = () => {
  return (
    <Router>
      <Container>
        <Header />
        <Switch>
          <Route path="/modellist" component={ModelList} />
          <Route path="/modelupload" component={ModelUpload} />
          <Route path="/register" component={Register} />
          <Route path="/test" component={ReactSandbox} />
        </Switch>
      </Container>
    </Router>
  );
};

export default App;
