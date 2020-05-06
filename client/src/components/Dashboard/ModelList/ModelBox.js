import React from "react";
import "css/dashboard/model-list.css";
import { Line } from "react-chartjs-2";

const options = {
  scales: {
    yAxes: [
      {
        display: true,
        labelString: "validation loss",

        ticks: {
          suggestedMin: 0, // minimum will be 0, unless there is a lower value.
          suggestedMax: 10,
          // OR //
          // beginAtZero: true, // minimum value will be 0.
        },
      },
    ],
    xAxes: [
      {
        scaleLabel: {
          display: true,
          labelString: "epoch",
        },
      },
    ],
  },
};

export const ModelBox = ({
  modelName,
  validationLossHistory,
  validationLoss,
  optimizerName,
  learningRate,
  activation,
}) => {
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
