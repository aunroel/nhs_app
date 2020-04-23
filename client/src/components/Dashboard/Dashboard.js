import React from "react";
import PropTypes from "prop-types";
import { Switch, Route, useRouteMatch } from "react-router-dom";
import DashboardHeader from "./DashbarodHeader/DashboardHeader";
import ModelList from "./ModelList/ModelList";
import ModelUpload from "./ModelUpload/ModelUpload";
import DataViewer from "./DataViewer/DataViewer";

const Dashboard = () => {
  const match = useRouteMatch();

  return (
    <>
      <h1>dcsdcsdc</h1>
      <DashboardHeader />
      <Switch>
        <Route path="/modellist" component={ModelList} />
        <Route path="/modelupload" component={ModelUpload} />
        <Route path="/dataViewer" component={DataViewer} />
      </Switch>
    </>
  );
};

Dashboard.propTypes = {};

export default Dashboard;
