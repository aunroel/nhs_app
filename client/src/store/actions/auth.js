import { REGISTER_SUCCESS } from ".";

export const registerSuccess = (token) => {
  return { type: REGISTER_SUCCESS, payload: { token } };
};
