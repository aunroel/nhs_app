import React from "react";
import { Table } from "react-bootstrap";

const TeamMembersTable = () => {
  return (
    <>
      <h2>
        Group project for the Software Abstractions and Systems Integration
        module
      </h2>
      <br />
      <h3>Team lead: Marta Smigielska</h3>
      <h4>The project consists of 3 sub-teams</h4>
      <br />

      <Table responsive>
        <thead>
          <tr>
            <th scope="col">#</th>
            <th scope="col">Android</th>
            <th scope="col">Server</th>
            <th scope="col">iOS</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th scope="row">1</th>
            <td>Xu Yichao</td>
            <td>Smigielska Marta</td>
            <td>Baqadir Najat</td>
          </tr>
          <tr>
            <th scope="row">2</th>
            <td>Zhang Yiren</td>
            <td>Matios Roman</td>
            <td>Bassan Davinderpal</td>
          </tr>
          <tr>
            <th scope="row">3</th>
            <td></td>
            <td>Golaszewski Wojciech</td>
            <td></td>
          </tr>
        </tbody>
      </Table>
    </>
  );
};

export default TeamMembersTable;
