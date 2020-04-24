/* eslint-disable react/jsx-one-expression-per-line */
import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import DataTable from "./DataTable";

const DataViewer = () => {
  const [tableData, setTableData] = useState(null);
  const [numberOfEntries, setNumberOfEntres] = useState(null);

  useEffect(() => {
    const sleep = (ms) => new Promise((resolve) => setTimeout(resolve, ms));

    const getData = async () => {
      await sleep(500);

      const config = {
        headers: {
          "Content-Type": "application/json",
        },
      };

      const body = JSON.stringify({});

      try {
        const res = await axios.get("/api/data/", body, config);
        const payload = res.data;
        setTableData(payload.data.map((entry) => JSON.parse(entry)));
        setNumberOfEntres(payload.numberOfEntries);
      } catch (error) {
        console.log(error);
      }
    };

    getData();
  });

  return (
    <div>
      <h1> The Aggregator </h1>
      {numberOfEntries ? (
        <>
          <h2>Total entries: {numberOfEntries}</h2>
          <br />
          <h3>Latest {Math.min(numberOfEntries, 10)} entries:</h3>
          <DataTable data={tableData} />
        </>
      ) : (
          <h3>Loading data...</h3>
        )}
    </div>
  );
};

DataViewer.propTypes = {};

export default DataViewer;
