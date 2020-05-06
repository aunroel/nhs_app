import React, { useState } from "react";
import "css/dashboard/model-list.css";
import { Line } from "react-chartjs-2";
import axios from "axios";
import { Button } from "react-bootstrap";

export const ModelBox = ({
  modelName,
  validationLossHistory,
  validationLoss,
  optimizerName,
  learningRate,
  activation,
  isDeployed,
}) => {
  const [deployedInMeantime, setD] = useState(false);

  const setDeployed = async () => {
    const config = {
      headers: {
        "Content-Type": "application/json",
      },
    };

    const body = JSON.stringify({ filename: modelName });

    try {
      await axios.put("/api/models/setDeployed", body, config);
      setD(true);
    } catch (error) {
      console.log(error);
    }
  };

  const depl = isDeployed || deployedInMeantime;

  return (
    <div className="model-box">
      <div className="model-box-text" style={{ paddingRight: "40px" }}>
        <h3 style={{ margin: 0, maxWidth: "15em", overflow: "auto" }}>
          {modelName}
        </h3>
        <br />
        <div style={{ fontSize: "1.4em" }}>
          Validation loss:{"  "}
          <span style={{ fontWeight: "bold" }}>
            {validationLoss.toFixed(1)}
          </span>
        </div>
        <br />
        <div>
          Optimizer:{"  "}
          <span style={{ fontWeight: "bold" }}>{optimizerName}</span>
        </div>
        <div>
          Learning Rate:{"  "}
          <span style={{ fontWeight: "bold" }}>{learningRate.toFixed(5)}</span>
        </div>
        <div>
          Activation:{"  "}
          <span style={{ fontWeight: "bold" }}>{activation}</span>
        </div>
        <br />
        <div>
          Is deplyoed:
          <span style={{ fontWeight: "bold" }}>{depl + ""}</span>
          <br />
          {!depl ? <Button onClick={setDeployed}>Deploy</Button> : null}
        </div>
      </div>
      <div className="model-box-text">
        <Line
          data={{
            labels: validationLossHistory.map((v) => ""),
            datasets: [
              {
                label: "Training Accuracy",
                fill: false,
                lineTension: 0.1,
                backgroundColor: "rgba(75,192,192,0.4)",
                borderColor: "rgba(75,192,192,1)",
                borderCapStyle: "butt",
                borderDash: [],
                borderDashOffset: 0.0,
                borderJoinStyle: "miter",
                pointBorderColor: "rgba(75,192,192,1)",
                pointBackgroundColor: "#fff",
                pointBorderWidth: 1,
                pointHoverRadius: 5,
                pointHoverBackgroundColor: "rgba(75,192,192,1)",
                pointHoverBorderColor: "rgba(220,220,220,1)",
                pointHoverBorderWidth: 2,
                pointRadius: 1,
                pointHitRadius: 10,
                data: validationLossHistory,
              },
            ],
          }}
        />
      </div>
    </div>
  );
};
