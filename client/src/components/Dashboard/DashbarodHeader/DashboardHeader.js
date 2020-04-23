import React from "react";
import { useLocation } from "react-router-dom";
import "css/dashboard/dashboard-header.css";
import "css/sizing.css";
import LinkButton from "./LinkButton";

const DashboardHeader = () => {
  const { pathname: currentUrl } = useLocation();

  return (
    <div className="header-container">
      <div className="flex-box-vert-align">
        <div
          className="flex-box-vert-align overflow-y"
          style={{ flex: 4, justifyContent: "space-between" }}
        >
          <div className="flex-box-vert-align">
            <LinkButton
              linkUrl="/modellist"
              buttonText="Model Viewer"
              currentUrl={currentUrl}
            />

            <LinkButton
              linkUrl="/modelupload"
              buttonText="Model Upload"
              currentUrl={currentUrl}
            />
            <LinkButton
              linkUrl="/dataViewer"
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
