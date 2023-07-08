import { useSelector } from "react-redux";
import uuid from "react-uuid";

import { selectCurrentColor, selectDateOfGame } from "../appSettingsSlice";
import Calendar from "../Calendar";
import {
  useAddBlockedDateMutation,
  useDeleteBlockedDateMutation,
  useGetBlockedDatesQuery,
  useGetTimePeriodsForAdminQuery,
} from "../calendarApiSlice";

const DayManagementCertainDate = () => {
  const currentColor = useSelector(selectCurrentColor);
  const dateOfGame = useSelector(selectDateOfGame);
  const [deleteBlockedDate] = useDeleteBlockedDateMutation();
  const [addBlockedDate] = useAddBlockedDateMutation();
  const {
    data: timePeriodsForAdmin,
    isSuccess: isSuccessTimePeriodForAdmin,
    isError,
    error,
    refetch: refetchTimePeriods,
  } = useGetTimePeriodsForAdminQuery(dateOfGame);
  const {
    data: blockedDates,
    isSuccess: isSuccessBlockedDates,
    isError: isErrorBlockedDates,
    error: errorBlockedDates,
  } = useGetBlockedDatesQuery();

  const handleTimePeriodBackGroundColor = (blocked) => {
    return blocked ? "Grey" : currentColor;
  };

  const handleBlockUnBlock = async () => {
    try {
      if (isDateBlocked()) {
        const urlOfDateToDelete = blockedDates.find(
          ({ date }) => date === dateOfGame
        )._links.self.href;
        await deleteBlockedDate(urlOfDateToDelete).unwrap();
      } else {
        await addBlockedDate(dateOfGame).unwrap();
      }
      refetchTimePeriods();
    } catch (err) {
      console.log(err);
    }
  };
  const isDateBlocked = () => {
    return !!blockedDates.find(
      ({ date, open }) => date === dateOfGame && open === "-----"
    );
  };
  return (
    <div className="flex flex-row sm:flex-col lg:flex-row">
      {isErrorBlockedDates && <p>{errorBlockedDates.message}</p>}
      {isSuccessBlockedDates && <Calendar />}
      {/**Side component with info day chosen in calendar*/}
      <div className=" min-w-[50%] bg-white text-slate-900 m-4 p-8 rounded-xl shadow-2xl relative">
        <div className="flex flex-row item-">
          <span className="uppercase px-3 py-1 bg-indigo-200 text-indigo-900 rounded-2xl text-sm">
            {dateOfGame}
          </span>
        </div>

        <div className="flex flex-row-2 ">
          <div className="flex flex-col  ml-10">
            <div className="flex flex-row space-x-4">
              <h2>Tables</h2>
              <h2>Time periods</h2>
            </div>
            {isError && <p>{error.message}</p>}
            {isSuccessTimePeriodForAdmin &&
              timePeriodsForAdmin.map(
                ({ start_time, end_time, blocked, amount }) => {
                  return (
                    <div key={uuid()} className="flex flex-row">
                      <p className="w-full p-1 mt-0.5">{amount}</p>
                      <button
                        className="w-full p-1 bg-slate-700 hover:bg-slate-500 text-gray-100 mt-0.5"
                        style={{
                          backgroundColor:
                            handleTimePeriodBackGroundColor(blocked),
                        }}
                      >
                        {`${start_time}-${end_time}`}
                      </button>
                    </div>
                  );
                }
              )}
          </div>
          <div className="flex flex-col mx-8">
            {isSuccessBlockedDates && new Date(dateOfGame) > new Date() && (
              <button
                onClick={() => {
                  handleBlockUnBlock();
                }}
                style={{ backgroundColor: currentColor }}
                className="w-36 mx-auto rounded-xl py-4 my-4 text-white hover:border-green-600px-4 ease-in duration-300"
              >
                {isDateBlocked() ? "Unblock day" : "Block day"}
              </button>
            )}
          </div>
        </div>
      </div>
    </div>
  );
};

export default DayManagementCertainDate;
