import React from "react";
import { useLocation, useRouteMatch } from "react-router-dom";
import "css/dashboard/dashboard-header.css";
import "css/sizing.css";
import LinkButton from "./LinkButton";

const DashboardHeader = () => {
  const { pathname: currentUrl } = useLocation();
  const { url } = useRouteMatch();

  return (
    <div className="header-container">
      <div className="flex-box-vert-align">
        <div
          className="flex-box-vert-align overflow-y"
          style={{ flex: 4, justifyContent: "space-between" }}
        >
          <div className="flex-box-vert-align">
            <LinkButton
              linkUrl={`${url}/modellist`}
              buttonText="Model Viewer"
              currentUrl={currentUrl}
            />
            <LinkButton
              linkUrl={`${url}/modelupload`}
              buttonText="Model Upload"
              currentUrl={currentUrl}
            />
            <LinkButton
              linkUrl={`${url}/dataViewer`}
              buttonText="Data Viewer"
              currentUrl={currentUrl}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

DashboardHeader.propTypes = {};

export default DashboardHeader;
