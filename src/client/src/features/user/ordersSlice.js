import { createSelector, createEntityAdapter } from "@reduxjs/toolkit";
import { apiSlice } from "../../app/api/apiSlice";
const ordersAdapter = createEntityAdapter({
  selectId: (order) => order.order_id,
  sortComparer: (a, b) => a.date_of_game - b.date_of_game,
});

const initialState = ordersAdapter.getInitialState();

export const ordersApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getUserOrders: builder.query({
      queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
        const { getState } = queryApi;
        const { user_id } = getState().authorization.userData;

        const response = await baseQuery(
          `/${process.env.REACT_APP_ENTITY_USER_PLURAL}/${user_id}/${process.env.REACT_APP_ENTITY_ORDER_PLURAL}`,
          extraOptions
        );

        return response.data
          ? { data: response.data._embedded.orders }
          : { error: response.error };
      },
      providesTags: (result, error, arg) => {
        return result
          ? [
              ...result.map(({ order_id }) => ({
                type: "UserOrders",
                id: order_id,
              })),
              "UserOrders",
            ]
          : ["UserOrders"];
      },
    }),
    getAllOrders: builder.query({
      query: () => `/${process.env.REACT_APP_ENTITY_ORDER_PLURAL}`,
      transformResponse: (responseData) =>
        ordersAdapter.setAll(initialState, responseData._embedded.orders),
      providesTags: (result, error, arg) => [
        { type: "Orders", id: "LIST" },
        ...result.ids.map(({ order_id }) => ({ type: "Orders", id: order_id })),
      ],
    }),
    addOrders: builder.mutation({
      queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
        const { getState } = queryApi;
        const { user_id } = getState().authorization.userData;
        const response = await baseQuery({
          url: `/${process.env.REACT_APP_ENTITY_USER_PLURAL}/${user_id}/${process.env.REACT_APP_ENTITY_ORDER_PLURAL}`,
          method: "POST",
          body: arg,
          ...extraOptions,
        });

        return response.data
          ? { data: response.data }
          : { error: response.error };
      },
      invalidatesTags: (result, error, arg) => {
        return [{ type: "UserOrders" }];
      },
    }),
    deleteOrder: builder.mutation({
      queryFn: async (arg, queryApi, extraOptions, baseQuery) => {
        const { getState } = queryApi;
        const { user_id } = getState().authorization.userData;
        const response = await baseQuery({
          url: `/${process.env.REACT_APP_ENTITY_USER_PLURAL}/${user_id}/${process.env.REACT_APP_ENTITY_ORDER_PLURAL}/${arg}`,
          method: "DELETE",
          ...extraOptions,
        });

        return response.data
          ? { data: response.data }
          : { error: response.error };
      },
      invalidatesTags: (result, error, { order_id }) => {
        return [{ type: "UserOrders", id: order_id }];
      },
    }),
  }),
});

export const {
  useGetUserOrdersQuery,
  useGetAllOrdersQuery,
  useDeleteOrderMutation,
  useAddOrdersMutation,
} = ordersApiSlice;

export const selectAllOrdersResult =
  ordersApiSlice.endpoints.getAllOrders.select();

const selectAllOrdersData = createSelector(
  selectAllOrdersResult,
  (orders) => orders.data
);

export const { selectAll: selectAllOrders } = ordersAdapter.getSelectors(
  (state) => selectAllOrdersData(state) ?? initialState
);
