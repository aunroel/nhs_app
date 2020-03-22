import React, { useState } from "react";
import axios from "axios";
import { Switch, Route, useRouteMatch, Link } from "react-router-dom";
import TodoList from "./TodoList";

const subURLs = {
  todo: "todo",
  message: "message"
};

const LinksList = ({ url }) => {
  return (
    <>
      <h2>Topics</h2>
      <ul>
        <li>
          <Link to={`${url}/${subURLs.todo}`}>Rendering with React</Link>
        </li>
        <li>
          <Link to={`${url}/${subURLs.message}`}>Components</Link>
        </li>
      </ul>
    </>
  );
};

const ReactSandbox = () => {
  const [serverStatusMessage, setMes] = useState(
    "Checking server connection..."
  );
  axios.get("/login").then(() => setMes("Server connection OK"));

  const { path, url } = useRouteMatch();

  return (
    <>
      <h1>Link switcher</h1>
      <LinksList url={url} />
      <Switch>
        <Route exact path={path}>
          <h3>Please select a subpage</h3>
        </Route>
        <Route path={`${path}/:${subURLs.todo}`} component={TodoList} />
        <Route path={`${path}/:${subURLs.message}`}>
          <h3>Simple message</h3>
        </Route>
      </Switch>
      <h3>{serverStatusMessage}</h3>
    </>
  );
};

export default ReactSandbox;
