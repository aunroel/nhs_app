import { REGISTER_SUCCESS, LOGIN_SUCCESS, LOGOUT } from "../actions";

const tokenLocalStorageKey = "token";

const initialState = {
  token: localStorage.getItem(tokenLocalStorageKey),
};

export default function (state = initialState, action) {
  const { type, payload } = action;

  switch (type) {
    case REGISTER_SUCCESS:
    case LOGIN_SUCCESS: {
      const { token } = payload;
      localStorage.setItem(tokenLocalStorageKey, token);
      return {
        token,
      };
    }
    case LOGOUT: {
      localStorage.removeItem(tokenLocalStorageKey);
      return {
        token: null,
      };
    }
    default:
      return state;
  }
}
