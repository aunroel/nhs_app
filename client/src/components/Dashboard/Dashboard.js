import React from "react";
import { Switch, Route, useRouteMatch } from "react-router-dom";
import DashboardHeader from "./DashbarodHeader/DashboardHeader";
import ModelList from "./ModelList/ModelList";
import ModelUpload from "./ModelUpload/ModelUpload";
import DataViewer from "./DataViewer/DataViewer";
import "css/dashboard/dashboard.css"

const Dashboard = () => {
  const { url, path } = useRouteMatch();

  return (
    <>
      <DashboardHeader />
      <div className="dashboard-content">
        <Switch>
          <Route path={`${path}/modellist`} component={ModelList} />
          <Route path={`${path}/modelupload`} component={ModelUpload} />
          <Route path={`${path}/dataViewer`} component={DataViewer} />
        </Switch>
      </div>
    </>
  );
};

export default Dashboard;
