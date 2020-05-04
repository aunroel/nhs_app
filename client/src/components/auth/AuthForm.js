import React, { useState } from "react";
import { Alert } from "react-bootstrap";
import { useHistory } from "react-router-dom";
import PropTypes from "prop-types";

const errorMessageTimeoutMs = 5000;
const homePageRedirectTimeoutMs = 2000;

const errMsg = (msg, cancelTimeout) => ({
  msg,
  cancelTimeout,
});

const AuthForm = (props) => {
  const [errorMessage, setErrorMessage] = useState(errMsg(null, null));
  const [successMessage, setSuccessMessage] = useState(null);

  const history = useHistory();

  const handleErrorMessage = (msg) => {
    if (errorMessage.msg) {
      errorMessage.cancelTimeout();
    }

    const timeoutCanc = setTimeout(() => {
      setErrorMessage(errMsg(null, null));
    }, errorMessageTimeoutMs);

    setErrorMessage(errMsg(msg, timeoutCanc));
  };

  const handleSuccess = (msg) => {
    setSuccessMessage(msg);

    setTimeout(() => {
      history.push("/");
    }, homePageRedirectTimeoutMs);
  };

  return (
    <div>
      {/* <Form
        setErrorMessage={handleErrorMessage}
        setSuccessMessage={handleSuccess}
      /> */}
      {props.Form({
        setErrorMessage: handleErrorMessage,
        setSuccessMessage: handleSuccess,
      })}
      {successMessage ? (
        <Alert variant="success">
          {successMessage + "You will be soon redirected to home page..."}
        </Alert>
      ) : errorMessage.msg ? (
        <Alert variant="danger">{errorMessage}</Alert>
      ) : null}
    </div>
  );
};

AuthForm.propTypes = {
  Form: PropTypes.func.isRequired,
};

export default AuthForm;
