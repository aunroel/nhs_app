import React from "react";
import ReactDOM from "react-dom";
import App from "./App";

import { createStore, StoreProvider, persist } from "easy-peasy";
import storeModel from "./store/store";

const store = createStore(persist(storeModel));

ReactDOM.render(
  <StoreProvider store={store}>
    <App />
  </StoreProvider>,
  document.getElementById("root")
);
