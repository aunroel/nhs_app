import React from "react";
import ReactDOM from "react-dom";
import App from "./App";

import { Provider as StoreProvider } from "react-redux";
import storeAndPersistor from "./store/store";

import { PersistGate } from "redux-persist/integration/react";
const { store, persistor } = storeAndPersistor;

ReactDOM.render(
  <StoreProvider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <App />
    </PersistGate>
  </StoreProvider>,
  document.getElementById("root")
);
