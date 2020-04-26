import { combineReducers } from "redux";
import auth from "./auth";

import { ADD_ORDER } from "../actions";

const app = combineReducers({ auth });

export default app;
