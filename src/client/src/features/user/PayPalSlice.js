import { apiSlice } from "../../app/api/apiSlice";

export const payPalApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    createPayPalOrder: builder.mutation({
      query: (data) => ({
        url: process.env.REACT_APP_CREATE_PAYPAL_ORDER,
        method: "POST",
        body: data,
      }),
    }),
  }),
});

export const { useCreatePayPalOrderMutation } = payPalApiSlice;
