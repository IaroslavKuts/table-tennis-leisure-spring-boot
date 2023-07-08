import { apiSlice } from "../../app/api/apiSlice";

export const authApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    login: builder.mutation({
      query: ({ email, password }) => ({
        url: `${process.env.REACT_APP_LOGIN}?email=${email}&password=${password}`,
        method: "POST",
      }),
    }),
    getRefreshToken: builder.query({
      query: () => "/refreshToken",
    }),
    signUp: builder.mutation({
      query: (data) => ({
        url: `${process.env.REACT_APP_ENTITY_USER_PLURAL}`,
        method: "POST",
        body: { ...data },
      }),
    }),
  }),
});

export const { useLoginMutation, useGetRefreshTokenQuery, useSignUpMutation } =
  authApiSlice;
