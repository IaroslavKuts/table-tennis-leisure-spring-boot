import { createSelector, createEntityAdapter } from "@reduxjs/toolkit";
import { apiSlice } from "../../app/api/apiSlice";
const abonementsAdapter = createEntityAdapter({
  selectId: (abonement) => abonement.abonement_id,
  sortComparer: (a, b) => a.abonement_id - b.abonement_id,
});

const initialState = abonementsAdapter.getInitialState();

export const abonementsApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUserAbonement: builder.query({
      queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
        const { getState } = queryApi;
        const { user_id } = getState().authorization.userData;

        const response = await baseQuery(
          `/${process.env.REACT_APP_ENTITY_USER_PLURAL}/${user_id}/${process.env.REACT_APP_ENTITY_ABONEMENT_SINGULAR}`,
          extraOptions
        );

        return response.data
          ? { data: response.data }
          : { error: response.error };
      },
      providesTags: (result, error, arg) => {
        return result ? [{ type: "UserAbonement" }] : ["UserAbonement"];
      },
    }),
    getAllAbonements: builder.query({
      query: () => `/${process.env.REACT_APP_ENTITY_ABONEMENT_PLURAL}`,
      transformResponse: (responseData) =>
        abonementsAdapter.setAll(
          initialState,
          responseData._embedded.abonements
        ),
      providesTags: (result, error, arg) => [
        { type: "Abonements", id: "LIST" },
        ...result.ids.map((id) => ({ type: "Abonements", id })),
      ],
    }),
    addAbonement: builder.mutation({
      query: (abonementData) => ({
        url: `/${process.env.REACT_APP_ENTITY_ABONEMENT_PLURAL}`,
        method: "POST",
        body: abonementData,
      }),
      invalidatesTags: [{ type: "Abonements", id: "LIST" }],
    }),
  }),
});

export const {
  useGetAllAbonementsQuery,
  useAddAbonementMutation,
  useGetUserAbonementQuery,
} = abonementsApiSlice;

export const selectAllAbonementsResult =
  abonementsApiSlice.endpoints.getAllAbonements.select();

const selectAllAbonementsData = createSelector(
  selectAllAbonementsResult,
  (abonements) => abonements.data
);

export const {
  selectAll: selectAllAbonements,
  selectById: selectAbonementById,
} = abonementsAdapter.getSelectors(
  (state) => selectAllAbonementsData(state) ?? initialState
);
