import React from "react";
import ReactDOM from "react-dom";
import { Provider as StoreProvider } from "react-redux";
import { PersistGate } from "redux-persist/integration/react";
import { withStyles } from "@material-ui/core/styles";
import App from "./App";

import storeAndPersistor from "./store/store";

const { store, persistor } = storeAndPersistor;

const baseSize = 1;
const styles = (theme) => ({
  "@global": {
    // MUI typography elements use REMs, so you can scale the global
    // font size by setting the font-size on the <html> element.
    html: {
      // fontSize: baseSize * 1.4 + "em",
      [theme.breakpoints.up("sm")]: {
        fontSize: baseSize * 1.1 + "em",
      },
      [theme.breakpoints.up("md")]: {
        fontSize: baseSize * 1.4 + "em",
      },
      [theme.breakpoints.up("lg")]: {
        fontSize: baseSize * 1.5 + "em",
      },
    },
  },
});

// const Appp = withStyles(styles)(App);

ReactDOM.render(
  <StoreProvider store={store}>
    <PersistGate loading={null} persistor={persistor}>
      <App />
    </PersistGate>
  </StoreProvider>,
  document.getElementById("root")
);
