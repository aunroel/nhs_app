import React, { Fragment, useState } from "react";
import InputField from "./components/InputField";
import axios from "axios";
// import "./App.css";

const App = () => {
  const [mes, setMes] = useState("Checking server connection...");
  axios.get("/login").then(() => setMes("Server connection OK"));

  return (
    <Fragment>
      <h1>App</h1>
      <InputField />
      <h3>{mes}</h3>
    </Fragment>
  );
};

export default App;
