import axios from "axios";

export const fetchData = () => async (dispatch) => {
  const config = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  const body = JSON.stringify({});

  try {
    const res = axios.post("./api/data/", body, config);
  } catch (error) {}
};
