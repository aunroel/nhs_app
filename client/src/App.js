import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Container, Button } from "react-bootstrap";
import "css/App.css";
import { connect } from "react-redux";
import { logout } from "store/actions/auth";
import Dashboard from "./components/Dashboard/Dashboard";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import HeaderLink from "./components/Header/HeaderLink";
import TeamMembersTable from "./components/TeamMembersTable/TeamMembersTable";
import AuthForm from "./components/auth/AuthForm";
import ModelList from "./components/Dashboard/ModelList/ModelList";
import ModelUpload from "./components/Dashboard/ModelUpload/ModelUpload";
import DataViewer from "./components/Dashboard/DataViewer/DataViewer";
import DashboardHeader from "./components/Dashboard/DashbarodHeader/DashboardHeader";

const App = ({ loggedIn, logout }) => {
  return (
    <Router>
      {/* <Header /> */}
      <DashboardHeader />
      <div style={{ padding: "20px" }}>
        <Switch>
          {/* <Route path="/login" component={Login} /> */}
          {/* <Route path="/register" component={Register} /> */}
          <Route path="/modellist" component={ModelList} />
          <Route path="/modelupload" component={ModelUpload} />
          <Route path="/dataViewer" component={DataViewer} />
          {/* <Route path="/test" component={ReactSandbox} /> */}
        </Switch>
      </div>
    </Router>
    // <Router>
    //   <div className="container-for-content-and-footer">
    //     <div>
    //       <Header
    //         leftButtons={
    //           <>
    //             <HeaderLink to="/index" text="Home" />

    //             {loggedIn ? (
    //               <>
    //                 <HeaderLink to="/doc" text="API Doc" />
    //                 <HeaderLink to="/dashboard" text="Dashboard" />
    //                 <HeaderLink to="/available" text="Availibility" />
    //               </>
    //             ) : null}
    //           </>
    //         }
    //         rightButtons={
    //           <>
    //             {loggedIn ? (
    //               <Button
    //                 variant="link secondary"
    //                 onClick={() => {
    //                   console.log("click");
    //                   logout();
    //                 }}
    //               >
    //                 Logout
    //               </Button>
    //             ) : (
    //               <HeaderLink to="/login" text="Login" />
    //             )}
    //           </>
    //         }
    //       />
    //       <Container className="container-padding">
    //         <Switch>
    //           <Route
    //             exact
    //             path={["/", "/index"]}
    //             component={TeamMembersTable}
    //           />
    //           <Route path="/dashboard" component={Dashboard} />
    //           <Route path="/login">
    //             <AuthForm Form={Login} />
    //           </Route>
    //           <Route path="/register">
    //             <AuthForm Form={Register} />
    //           </Route>
    //         </Switch>
    //       </Container>
    //     </div>
    //     <Footer />
    //   </div>
    // </Router>
  );
};

const mapStateToProps = (state) => ({
  loggedIn: !!state.auth.token,
});

export default connect(mapStateToProps, { logout })(App);
