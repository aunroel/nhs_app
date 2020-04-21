/* eslint-disable no-restricted-syntax */
import React, { useState } from "react";
import { ModelBox } from "./ModelBox";
import "./model-list.css";

const data1 = {
  // labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange", "ff"],
  labels: Array(10).fill(""),
  datasets: [
    {
      label: "Loss",
      // data: [12, 19, 3, 5, 2, 3],
      data: [4.7, 4.6, 4.9, 5.1, 5.0, 5.2, 4.9, 4.8, 4.6, 5],
      backgroundColor: [
        "rgba(255, 99, 132, 0.0)",
        "rgba(54, 162, 235, 0.2)",
        "rgba(255, 206, 86, 0.2)",
        "rgba(75, 192, 192, 0.2)",
        "rgba(153, 102, 255, 0.2)",
        "rgba(255, 159, 64, 0.2)",
      ],
      borderColor: [
        "rgba(255, 99, 132, 1)",
        "rgba(54, 162, 235, 1)",
        "rgba(255, 206, 86, 1)",
        "rgba(75, 192, 192, 1)",
        "rgba(153, 102, 255, 1)",
        "rgba(255, 159, 64, 1)",
      ],
      borderWidth: 1,
    },
  ],
};
const mockModel = {
  name: "Model 1",
  uploadDate: "23/03/2020",
  regressionType: "-//-",
  optimizer: "adam",
  regularizer: "none",

  features: [
    "number of steps",
    "calls per week",
    "post code as world coordinates",
  ],
  trainingAccuracyHistory: [
    0.1,
    0.33,
    0.67,
    0.74,
    0.78,
    0.82,
    0.85,
    0.85,
    0.85,
  ],
  trainingLossHistory: [5, 5.2, 4.9, 4.8, 5.1, 4.9, 5.3, 5.1],
  testAccuracy: 0.85,
  loss: 5.1,
  comments: [
    "This model performed well",
    "dropping that one feature from learning helped",
  ],
  data: data1,
};

const data2 = {
  // labels: ["Red", "Blue", "Yellow", "Green", "Purple", "Orange", "ff"],
  labels: Array(10).fill(""),
  datasets: [
    {
      label: "Loss",
      // data: [12, 19, 3, 5, 2, 3],
      data: [5, 5.2, 4.9, 4.8, 5.1, 4.9, 5.3, 5.1, 5, 4.9, 4.8],
      backgroundColor: [
        "rgba(255, 99, 132, 0.0)",
        "rgba(54, 162, 235, 0.2)",
        "rgba(255, 206, 86, 0.2)",
        "rgba(75, 192, 192, 0.2)",
        "rgba(153, 102, 255, 0.2)",
        "rgba(255, 159, 64, 0.2)",
      ],
      borderColor: [
        "rgba(255, 99, 132, 1)",
        "rgba(54, 162, 235, 1)",
        "rgba(255, 206, 86, 1)",
        "rgba(75, 192, 192, 1)",
        "rgba(153, 102, 255, 1)",
        "rgba(255, 159, 64, 1)",
      ],
      borderWidth: 1,
    },
  ],
};

const mockModel2 = {
  name: "Model 2",
  uploadDate: "24/03/2020",
  regressionType: "-//-",
  optimizer: "sgd",
  regularizer: "L2",
  features: [
    "number of steps",
    "calls per week",
    "post code as world coordinates",
  ],
  trainingAccuracyHistory: [
    0.1,
    0.33,
    0.67,
    0.74,
    0.78,
    0.82,
    0.85,
    0.85,
    0.85,
  ],
  trainingLossHistory: [4.7, 4.6, 4.9, 5.1, 5.0, 5.2, 4.9],
  testAccuracy: 0.85,
  loss: 5.0,
  comments: [
    "This model performed well",
    "dropping that one feature from learning helped",
  ],
  data: data2,
};

const modelNames = [
  "updated model",
  "First basic model",
  "Mock model 1",
  "Mock model 2",
  "Mock model 3",
];

const models = [mockModel, mockModel2];

const ModelList = () => {
  const [searchValue, setSearchValue] = useState(null);

  return (
    <div className="model-list-container">
      <div className="filter-box">
        <input
          type="text"
          placeholder="Search by model name"
          onChange={(event) => setSearchValue(event.target.value)}
        />
      </div>
      {models
        .filter((model) => {
          const { name } = model;
          if (!searchValue) return true;
          const searchItems = searchValue.split(" ");
          for (const string of searchItems) {
            if (name.indexOf(string) !== -1) return true;
          }
          return false;
        })
        .map((model) => {
          return <ModelBox key={model.name} modelData={model} />;
        })}
    </div>
  );
};

export default ModelList;
