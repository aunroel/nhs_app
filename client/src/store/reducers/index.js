import { combineReducers } from "redux";
import auth from "./auth";

const app = combineReducers({ auth });

export default app;
