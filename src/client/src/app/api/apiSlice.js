import { createApi, fetchBaseQuery } from "@reduxjs/toolkit/query/react";
import {
  setToken,
  logOut,
} from "../../features/authorization/authorizationSlice";

const baseQuery = fetchBaseQuery({
  reducerPath: "/",
  mode: "cors",
  baseUrl: "http://localhost:8080",
  credentials: "include", // httpOnly
  prepareHeaders: (headers, { getState }) => {
    // console.log(headers);

    const token = getState().authorization.token;
    if (token) {
      headers.set("Authorization", `Bearer ${token}`);
    }
    return headers;
  },
});

const baseQueryReauth = async (args, api, extraOptions) => {
  let result = await baseQuery(args, api, extraOptions);
  // console.log(result);

  if (result?.error?.status === 403) {
    console.log("sending refresh token");
    // send refresh token to get new access token
    const refreshResult = await baseQuery(
      process.env.REACT_APP_REFRESH_TOKEN,
      api,
      extraOptions
    );
    console.log(refreshResult);
    if (refreshResult?.data) {
      // const user = api.getState().authotrization.user;
      // store the new token
      api.dispatch(setToken({ ...refreshResult }));
      // retry the original query with new access token
      result = await baseQuery(args, api, extraOptions);
    } else {
      api.dispatch(logOut());
    }
  }

  return result;
};

export const apiSlice = createApi({
  baseQuery: baseQuery,
  tagTypes: [
    "User",
    "UserAbonement",
    "UserOrders",
    "Person",
    "Abonements",
    "Orders",
    "BlockedDays",
    "BlockedDates",
    "BasicDaysSchedule",
  ],
  endpoints: (builder) => ({}),
});
