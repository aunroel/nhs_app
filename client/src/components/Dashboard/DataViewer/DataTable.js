/* eslint-disable import/no-extraneous-dependencies */
/* eslint-disable react/forbid-prop-types */
import React from "react";
import { makeStyles } from "@material-ui/core/styles";
import Table from "@material-ui/core/Table";
import TableBody from "@material-ui/core/TableBody";
import TableCell from "@material-ui/core/TableCell";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import Paper from "@material-ui/core/Paper";
import PropTypes from "prop-types";
import idGen from "../../../utils/idGenerator";

const useStyles = makeStyles({
  table: {
    minWidth: 650,
  },
});

const modelToViewNotation = {
  supportCode: "Support Code",
  wellBeingScore: "Wellbing Score",
  weeklySteps: "Weekly Steps",
  weeklyCalls: "Weekly Calls",
  errorRate: "Error Rate",
  postCode: "Post Code",
};

const DataTable = ({ data }) => {
  const classes = useStyles();

  return (
    <TableContainer component={Paper}>
      <Table className={classes.table} aria-label="simple table">
        <TableHead>
          <TableRow>
            {Object.keys(data[0]).map((key) => {
              return <TableCell>{modelToViewNotation[key]}</TableCell>;
            })}
          </TableRow>
        </TableHead>
        <TableBody>
          {data.map((entry) => (
            <TableRow key={entry.id}>
              {Object.values(entry).map((val) => {
                return (
                  <TableCell key={idGen()} component="th" scope="row">
                    {val}
                  </TableCell>
                );
              })}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </TableContainer>
  );
};

DataTable.propTypes = {
  data: PropTypes.array.isRequired,
};

export default DataTable;
