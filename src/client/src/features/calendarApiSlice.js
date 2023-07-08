import { apiSlice } from "../app/api/apiSlice";

export const calendarApiSlice = apiSlice.injectEndpoints({
  endpoints: (builder) => ({
    getBasicDaysSchedules: builder.query({
      query: () => process.env.REACT_APP_ENTITY_BASIC_WORK_SCHEDULE_SINGULAR,
      transformResponse: (responseData) => responseData._embedded.work_schedule,
      providesTags: ["BasicDaysSchedule"],
    }),
    updateBasicDaySchedule: builder.mutation({
      query: ({ day_id, open, close }) => ({
        url: `/${process.env.REACT_APP_ENTITY_BASIC_WORK_SCHEDULE_SINGULAR}/${day_id}`,
        method: "PATCH",
        body: { open, close },
      }),
      invalidatesTags: ["BasicDaysSchedule"],
    }),
    getBlockedDates: builder.query({
      query: () => process.env.REACT_APP_ENTITY_ALTERED_WORK_SCHEDULE_SINGULAR,
      transformResponse: (responseData) =>
        responseData._embedded.altered_work_schedule,
      providesTags: ["BlockedDates"],
    }),
    addBlockedDate: builder.mutation({
      query: (dateOfGame) => ({
        url: process.env.REACT_APP_ENTITY_ALTERED_WORK_SCHEDULE_SINGULAR,
        method: "POST",
        body: { date: dateOfGame, open: "-----", close: "-----" },
      }),
      invalidatesTags: ["BlockedDates"],
    }),
    deleteBlockedDate: builder.mutation({
      query: (urlOfDateToDelete) => ({
        url: urlOfDateToDelete,
        method: "DELETE",
      }),
      invalidatesTags: ["BlockedDates"],
    }),
    getTimePeriodsForUser: builder.query({
      query: (dateOfGame) =>
        `${process.env.REACT_APP_READ_FILTERED_TIME_PERIODS_OF_WORK_DAY_FOR_USER}?dateOfGame=${dateOfGame}`,
      keepUnusedDataFor: 60,
      transformResponse: (responseData) =>
        responseData.map((el) => ({
          ...el,
          blocked: el.blocked === "true" ? true : false,
        })),
    }),
    getTimePeriodsForAdmin: builder.query({
      query: (dateOfGame) =>
        `${process.env.REACT_APP_READ_FILTERED_TIME_PERIODS_OF_WORK_DAY_FOR_ADMIN}?dateOfGame=${dateOfGame}`,
      keepUnusedDataFor: 60,
      transformResponse: (responseData) =>
        responseData.map((el) => ({
          ...el,
          blocked: el.blocked === "true" ? true : false,
        })),
    }),
  }),
});

export const {
  useGetBlockedDatesQuery,
  useAddBlockedDateMutation,
  useDeleteBlockedDateMutation,
  useGetBasicDaysSchedulesQuery,
  useUpdateBasicDayScheduleMutation,
  useGetTimePeriodsForUserQuery,
  useGetTimePeriodsForAdminQuery,
} = calendarApiSlice;
