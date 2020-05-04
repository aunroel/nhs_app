import React from "react";
import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import { Container } from "react-bootstrap";
import "css/App.css";
import { connect } from "react-redux";
import Dashboard from "./components/Dashboard/Dashboard";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import Login from "./components/auth/Login";
import Register from "./components/auth/Register";
import HeaderLink from "./components/Header/HeaderLink";
import TeamMembersTable from "./components/TeamMembersTable/TeamMembersTable";
import AuthForm from "./components/auth/AuthForm";

const App = () => {
  const loggedIn = false;

  return (
    <Router>
      <div className="container-for-content-and-footer">
        <div>
          <Header
            leftButtons={
              <>
                <HeaderLink to="/index" text="Home" />

                {loggedIn ? (
                  <>
                    <HeaderLink to="/doc" text="API Doc" />
                    <HeaderLink to="/available" text="Availibility" />
                  </>
                ) : null}
              </>
            }
            rightButtons={
              <>
                {loggedIn ? (
                  <HeaderLink to="/logout" text="Logout" />
                ) : (
                  <HeaderLink to="/login" text="Login" />
                )}
              </>
            }
          />
          <Container className="container-padding">
            <Switch>
              <Route
                exact
                path={["/", "/index"]}
                component={TeamMembersTable}
              />
              <Route path="/dashboard" component={Dashboard} />
              <Route path="/login">
                <AuthForm Form={Login} />
              </Route>
              <Route path="/register">
                <AuthForm Form={Register} />
              </Route>
            </Switch>
          </Container>
        </div>
        <Footer />
      </div>
    </Router>
  );
};

const mapStateToProps = (state) => ({
  loggedIn: !!state.token,
});

export default connect(mapStateToProps)(App);
