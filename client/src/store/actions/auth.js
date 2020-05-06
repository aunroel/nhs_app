import { REGISTER_SUCCESS, LOGIN_SUCCESS, LOGOUT } from ".";

export const registerSuccess = (token) => {
  return { type: REGISTER_SUCCESS, payload: { token } };
};

export const loginSuccess = (token) => {
  return { type: LOGIN_SUCCESS, payload: { token } };
};

export const logout = () => {
  return { type: LOGOUT };
};
