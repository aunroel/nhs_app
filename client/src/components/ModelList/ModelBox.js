import React from "react";
import "./model-list.css";
import { Line } from "react-chartjs-2";

const data = {
  // labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange", "ff"],
  labels: Array(10).fill(""),
  datasets: [
    {
      label: "Training accuracy",
      // data: [12, 19, 3, 5, 2, 3],
      data: [0.1, 0.48, 0.67, 0.74, 0.78, 0.82, 0.85, 0.85, 0.85, 0.85],
      backgroundColor: [
        "rgba(255, 99, 132, 0.0)",
        "rgba(54, 162, 235, 0.2)",
        "rgba(255, 206, 86, 0.2)",
        "rgba(75, 192, 192, 0.2)",
        "rgba(153, 102, 255, 0.2)",
        "rgba(255, 159, 64, 0.2)"
      ],
      borderColor: [
        "rgba(255, 99, 132, 1)",
        "rgba(54, 162, 235, 1)",
        "rgba(255, 206, 86, 1)",
        "rgba(75, 192, 192, 1)",
        "rgba(153, 102, 255, 1)",
        "rgba(255, 159, 64, 1)"
      ],
      borderWidth: 1
    }
  ]
};

const options = {
  legend: {
    display: false
  },
  tooltips: {
    callbacks: {
      label: function(tooltipItem) {
        return tooltipItem.yLabel;
      }
    }
  }
};

export const ModelBox = ({ modelData }) => {
  return (
    <div className="model-box">
      <div className="model-box-text" style={{ paddingRight: "40px" }}>
        <h1 style={{ margin: 0 }}>{modelData.name}</h1>
        <br />
        <div style={{ fontSize: "1.4em" }}>
          Test accuracy:{"  "}
          <span style={{ fontWeight: "bold" }}>{modelData.testAccuracy}</span>
        </div>
        <br />
        <div>
          Upload date:{"  "}
          <span style={{ fontWeight: "bold" }}>{modelData.uploadDate}</span>
        </div>
        <br />
        <div>
          Regression type:{"  "}
          <span style={{ fontWeight: "bold" }}>{modelData.regressionType}</span>
        </div>
        <br />
        <div>
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
        </div>
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
          data={data}
          // options={options}
        />
      </div>
    </div>
  );
};
