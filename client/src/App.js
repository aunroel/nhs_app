import React, { Fragment, useState } from "react";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import ReactSandbox from "./subpages_debugging/ReactSandbox";
import Container from "./components/Container";
import Header from "./components/Header";
import { ModelList } from "./components/ModelList";
import { ModelUpload } from "./components/ModelUpload";

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
