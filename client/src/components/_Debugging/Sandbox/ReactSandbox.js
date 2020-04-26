import React, { useState } from "react";
import axios from "axios";
import { Switch, Route, useRouteMatch } from "react-router-dom";
import TodoList from "./Pages/TodoList";
import SubPageBox from "./Pages/SubPageBox";
import LinksList from "./Components/LinksList";
import { subURLs, testServerConnection } from "./config";

const subURLsAsArray = Object.values(subURLs);

const ReactSandbox = () => {
  const [serverStatusMessage, setMes] = useState(
    "Checking server connection..."
  );

  if (testServerConnection) {
    axios.get("/login").then(
      () => setMes("Server connection OK"),
      () => setMes("Server cannot be reached")
    );
  }

  const { url } = useRouteMatch();

  return (
    <>
      <h1>Link switcher</h1>
      <LinksList url={url} subUrlPaths={subURLsAsArray} />
      <Switch>
        <Route exact path={url}>
          <h3>Please select a subpage</h3>
        </Route>
        <Route path={`${url}/${subURLs.subsubLinks.subURL}`}>
          <SubPageBox />
        </Route>
        <Route path={`${url}/${subURLs.todo.subURL}`}>
          <TodoList />
        </Route>
      </Switch>
      <h3>{serverStatusMessage}</h3>
    </>
  );
};

export default ReactSandbox;
