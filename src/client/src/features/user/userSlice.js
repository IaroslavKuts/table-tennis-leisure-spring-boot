import { apiSlice } from "../../app/api/apiSlice";
export const userApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUser: builder.query({
      queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
        const { getState } = queryApi;
        const { user_id } = getState().authorization.userData;
        const response = await baseQuery(
          `/${process.env.REACT_APP_ENTITY_USER_PLURAL}/${user_id}`,
          extraOptions
        );

        return response.data
          ? { data: response.data }
          : { error: response.error };
      },
      providesTags: (result, error, arg) => {
        return result ? [{ type: "User", id: result.user_id }] : ["User"];
      },
    }),
    updateUserData: builder.mutation({
      queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
        const { getState } = queryApi;
        const { user_id } = getState().authorization.userData;
        console.log(extraOptions);
        console.log(arg);
        const response = await baseQuery({
          url: `/${process.env.REACT_APP_ENTITY_USER_PLURAL}/${user_id}`,
          method: "PATCH",
          body: arg,
          ...extraOptions,
        });
        console.log(response);

        return response.data
          ? { data: response.data }
          : { error: response.error };
      },
      invalidatesTags: (result, error, arg) => {
        //not productive, because it re-fetches user's abonement in case other data was updated
        return [
          { type: "User", id: result.user_id },
          { type: "UserAbonement" },
        ];
      },
    }),
  }),
});

export const { useGetUserQuery, useUpdateUserDataMutation } = userApiSlice;
