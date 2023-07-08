import { apiSlice } from "../../app/api/apiSlice";

export const createReportApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getProfit: builder.query({
      query: ({ start_date, end_date }) =>
        `${process.env.REACT_APP_READ_PROFIT}?start_date=${start_date}&end_date=${end_date}`,
    }),
    getDaysLoad: builder.query({
      query: ({ start_date, end_date }) =>
        `${process.env.REACT_APP_READ_DAYS_LOAD}?start_date=${start_date}&end_date=${end_date}`,
    }),
    getProfitByUsers: builder.query({
      query: ({ start_date, end_date }) =>
        `${process.env.REACT_APP_READ_USERS_DATA_BY_PAYMENT}?start_date=${start_date}&end_date=${end_date}`,
    }),
    getUsersAges: builder.query({
      query: () => process.env.REACT_APP_READ_CUSTOMERS_AGES,
    }),
    getUsersAbonements: builder.query({
      query: () => process.env.REACT_APP_READ_CUSTOMERS_ABONEMENTS,
    }),
  }),
});

export const {
  useGetProfitQuery,
  useGetDaysLoadQuery,
  useGetProfitByUsersQuery,
  useGetUsersAgesQuery,
  useGetUsersAbonementsQuery,
  useGetUserOrdersByPassportQuery,
} = createReportApiSlice;
