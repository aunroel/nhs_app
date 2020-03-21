import React from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import ReactSandbox from "./components/_Debugging/Sandbox/ReactSandbox";
import Container from "./components/AppContainer";
import Header from "./components/Header";
import { ModelList } from "./components/ModelList/ModelList";
import { ModelUpload } from "./components/ModelUpload/ModelUploadMain";
import "./App.css";

const App = () => {
  return (
    <Router>
      <Container>
        <Header />
        <Switch>
          <Route exact path="/test" component={ReactSandbox} />
          <Route exact path="/modellist" component={ModelList} />
          <Route exact path="/modelupload" component={ModelUpload} />
        </Switch>
      </Container>
    </Router>
  );
};

export default App;
