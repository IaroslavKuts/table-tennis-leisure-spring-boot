import { useState } from "react";
import { useSelector } from "react-redux";
import uuid from "react-uuid";
import { selectCurrentColor } from "../appSettingsSlice";
import { useGetTimePeriodsForUserQuery } from "../calendarApiSlice";

import PayPal2 from "./PayPal2";
import { useGetUserOrdersQuery } from "./ordersSlice";

//Component that gives to an user an option to place an appOrder
const OrderTimeChoice = ({ dateOfGame }) => {
  const {
    data: timePeriodsForUser,
    isSuccess: isSuccessTimePeriodsForUser,
    isError: isErrorFilteredTimePeriods,
    error: errorFilteredTimePeriods,
  } = useGetTimePeriodsForUserQuery(dateOfGame, {
    skip: !dateOfGame,
  });
  const { data: orders } = useGetUserOrdersQuery();
  const [chosenTimePeriods, setChosenTimePeriods] = useState([]);
  const currentColor = useSelector(selectCurrentColor);

  const handleClick = (e) => {
    const start_end_time = e.target.name;

    if (!!chosenTimePeriods.find((el) => start_end_time === el)) {
      setChosenTimePeriods((prev) =>
        prev.filter((elem) => elem !== start_end_time)
      );
    } else {
      setChosenTimePeriods((prev) => [...prev, start_end_time]);
    }
  };

  const handleChosenPeriodBackGroundColor = ({
    start_end_time: start_end_time_argument,
  }) => {
    const start_time_argument = start_end_time_argument.split("-")[0];
    if (
      timePeriodsForUser.find(
        ({ start_time, blocked }) =>
          start_time === start_time_argument && blocked
      )
    )
      return "Grey";

    if (
      !!orders.find(
        ({ date_of_game, start_time }) =>
          date_of_game === dateOfGame && start_time === start_time_argument
      )
    )
      return "orange";
    return !!chosenTimePeriods.find((el) => start_end_time_argument === el)
      ? "Black"
      : currentColor;
  };

  const handleDisableButton = ({ start_time: start_time_argument }) => {
    if (
      !!orders.find(
        ({ date_of_game, start_time }) =>
          date_of_game === dateOfGame && start_time === start_time_argument
      )
    )
      return true;
  };

  return (
    <div className="min-w-full flex sm:flex-row  flex-col ">
      {/**left part of a page */}
      <div className="w-full flex flex-col pl-16 lg:w-2/5 text-center">
        <p className="text-4xl font-bold py-4 dark:text-white">Time periods</p>
        {isSuccessTimePeriodsForUser &&
          timePeriodsForUser.map(({ start_time, end_time, blocked }) => {
            const start_end_time = `${start_time}-${end_time}`;
            return (
              <button
                style={{
                  backgroundColor: handleChosenPeriodBackGroundColor({
                    start_end_time,
                  }),
                }}
                name={start_end_time}
                key={uuid()}
                disabled={handleDisableButton({ start_time }) || blocked}
                className="w-full shadow-xl shadow-gray-400 p-4 bg-slate-700 text-gray-100 mt-4 hover:scale-105 ease-in duration-300 "
                onClick={handleClick}
              >
                {start_end_time}
              </button>
            );
          })}
      </div>

      {/**right part */}
      <div className=" flex flex-col items-center w-full min-h-[50%] lg:w-3/5  ml-3 shadow-gray-400">
        <p className="text-4xl font-bold py-4 dark:text-white text-center">
          Order info
        </p>

        <div className=" w-full  lg:w-3/5  ml-3 shadow-xl shadow-gray-400 rounded-xl p-4 mt-10">
          <div>
            <span className="uppercase px-3 py-1 bg-indigo-200 text-indigo-900 rounded-2xl text-sm">
              {dateOfGame}
            </span>
          </div>
          <div className="flex flex-col items-center">
            <div className="bg-white dark:bg- text-slate-900  min-h-[50%] w-full  m-4 p-8 rounded-xl shadow-2xl relative hover:scale-105 ease-in duration-300">
              data about
            </div>
            <button
              style={{ backgroundColor: currentColor }}
              className="w-full p-4 bg-slate-700 text-gray-100 mt-4 hover:bg-slate-500  rounded-full"
              // onClick={handleClickToPayment}
            >
              Place appOrder
            </button>
          </div>
        </div>
        {isSuccessTimePeriodsForUser && (
          <PayPal2
            chosenTimePeriods={chosenTimePeriods}
            disabled={chosenTimePeriods.length > 0}
            dateOfGame={dateOfGame}
          />
        )}
      </div>
    </div>
  );
};

export default OrderTimeChoice;
