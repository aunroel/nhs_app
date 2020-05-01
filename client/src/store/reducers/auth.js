import { REGISTER_SUCCESS } from "../actions";

const initialState = {
  token: localStorage.getItem("token"),
};

export default function (state = initialState, action) {
  const { type, payload } = action;

  switch (type) {
    case REGISTER_SUCCESS: {
      const { token } = payload;
      localStorage.setItem("token", token);
      return {
        token,
      };
    }
    default:
      return state;
  }
}
