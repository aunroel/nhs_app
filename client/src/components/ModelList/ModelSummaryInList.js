import React from "react";
import "../css/collapsable.css";

export const ModelData = ({ modelData }) => {
  return (
    <div>
      <button type="button" class="collapsible">
        {model.name} Open Collapsible
      </button>
      <div class="content">
        <p>model.name</p>
      </div>
    </div>
  );
};
