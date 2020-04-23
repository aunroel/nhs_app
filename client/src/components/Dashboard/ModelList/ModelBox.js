import React from "react";
import "./model-list.css";
import { Line } from "react-chartjs-2";

const options = {
  scales: {
    yAxes: [
      {
        display: true,
        ticks: {
          suggestedMin: 0, // minimum will be 0, unless there is a lower value.
          suggestedMax: 10,
          // OR //
          beginAtZero: true, // minimum value will be 0.
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

export const ModelBox = ({ modelData }) => {
  return (
    <div className="model-box">
      <div className="model-box-text" style={{ paddingRight: "40px" }}>
        <h1 style={{ margin: 0 }}>{modelData.name}</h1>
        <br />
        <div style={{ fontSize: "1.4em" }}>
          Test loss:{"  "}
          <span style={{ fontWeight: "bold" }}>
            {modelData.loss.toFixed(1)}
          </span>
        </div>
        <br />
        <div>
          Upload date:{"  "}
          <span style={{ fontWeight: "bold" }}>{modelData.uploadDate}</span>
        </div>
        <br />
        <div>
          Regularizer:{"  "}
          <span style={{ fontWeight: "bold" }}>{modelData.regularizer}</span>
        </div>
        <br />
        <div>
          Optimizer:{"  "}
          <span style={{ fontWeight: "bold" }}>{modelData.optimizer}</span>
        </div>
        <br />
        {/* <div>
          Features used{" "}
          <span style={{ fontWeight: "bold" }}>
            ({modelData.features.length + " "} total)
          </span>
          :
          <ol style={{ fontWeight: "bold" }}>
            {modelData.features.map(featureString => (
              <li>{featureString}</li>
            ))}
          </ol>
        </div> */}
      </div>
      <div className="model-box-text">
        <Line
          // data={{
          //   datasets: [
          //     {
          //       label: "Training Accuracy",
          //       data: modelData.trainingAccuracyHistory
          //     }
          //   ],
          //   backgroundColor: "rgb(255, 0,0)",
          //   borderColor: "rgb(255, 0,0)"
          // }}
          data={modelData.data}
          options={options}
        />
      </div>
    </div>
  );
};
