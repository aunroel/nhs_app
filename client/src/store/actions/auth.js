import { REGISTER_SUCCESS, LOGIN_SUCCESS } from ".";

export const registerSuccess = (token) => {
  return { type: REGISTER_SUCCESS, payload: { token } };
};

export const loginSuccess = (token) => {
  return { type: LOGIN_SUCCESS, payload: { token } };
};
