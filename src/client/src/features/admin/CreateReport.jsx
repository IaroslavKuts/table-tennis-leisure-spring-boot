import { useEffect, useState } from "react";

import { Table, ColorMapping, Bar, Pyramid, Doughnut } from "./Charts";
import { userGrid } from "../../data/dummy";
import uuid from "react-uuid";
import { useSelector } from "react-redux";
import { selectCurrentColor } from "../appSettingsSlice";
import {
  useGetProfitQuery,
  useGetDaysLoadQuery,
  useGetProfitByUsersQuery,
  useGetUsersAgesQuery,
  useGetUsersAbonementsQuery,
} from "./createReportSlice";
import { useForm } from "react-hook-form";

//Component that helps to create different reports
const CreateReport = () => {
  const [{ start_date, end_date }, setDateRange] = useState({
    start_date: undefined,
    end_date: undefined,
  });

  // my awful queries, but serves the purpose
  const queryArray = [
    useGetProfitQuery,
    useGetDaysLoadQuery,
    useGetProfitByUsersQuery,
    useGetUsersAgesQuery,
    useGetUsersAbonementsQuery,
  ].map((query, i) => {
    if (i < 3) return query({ start_date, end_date });
    else return query();
  });

  const currentColor = useSelector(selectCurrentColor);

  const [buttonsArray, setButtonsArray] = useState([
    { show: false, name: "Calculate profit" },
    { show: false, name: "Days' load" },
    { show: false, name: "Display profit gained from users" },
    { show: false, name: "Display customers' ages" },
    { show: false, name: "Display users' abonements" },
  ]);

  useEffect(() => {
    setButtonsArray((prev) =>
      prev.map((el, i) => {
        const { name, show } = el;
        return {
          name,
          show,
          btn: (
            <>
              <div key={uuid()} className="flex flex-col md:flex-row ">
                <button
                  style={{ backgroundColor: currentColor }}
                  className="w-36 h-16 p-1 text-gray-100 mt-0.5 mx-1 hover:scale-105 ease-in duration-300"
                  onClick={(_) => {
                    handleShow(i);
                  }}
                >
                  {name}
                </button>
              </div>
            </>
          ),
        };
      })
    );
  }, [currentColor]);

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      start_date: undefined,
      end_date: undefined,
    },
  });

  function handleShow(index) {
    setButtonsArray((prev) =>
      prev.map((el, i) => {
        return { ...el, show: index === i ? true : false };
      })
    );
  }

  return (
    <div className="flex flex-col m-2 md:m-10 mt-24 p-2 md:p-10 bg-zinc-100 rounded-3xl">
      <div className="flex flex-row">
        {/**Start-end date */}
        <form
          className="flex flex-row md:flex-col"
          onSubmit={handleSubmit((data) => {
            setDateRange(data);
          })}
        >
          <div className="flex flex-col w-29 m-2">
            <label htmlFor="start_date">Start date</label>
            <input
              type="date"
              name="start_date"
              id="start_date"
              className="border bg-gray-200 p-2"
              {...register("start_date", {
                required: "Start date is required",
              })}
            />
            {errors?.start_date?.message}
          </div>
          <div className="flex flex-col w-29 m-2">
            <label htmlFor="end_date">End date</label>
            <input
              type="date"
              name="end_date"
              id="end_date"
              className="border  bg-gray-200 p-2"
              {...register("end_date", {
                required: "End date is required",
              })}
            />
            {errors?.end_date?.message}
          </div>
          <button type="submit">Submit</button>
        </form>

        {buttonsArray.map(({ btn }) => btn)}
      </div>
      {queryArray.every(({ isLoading }) => isLoading) && <p>Loading</p>}
      {[Bar, ColorMapping, Table, Pyramid, Doughnut].map((Component, i) => {
        if (i === 2)
          return (
            <>
              {buttonsArray[i].show && queryArray[i].isSuccess && (
                <Component dataToShow={queryArray[i].data} grid={userGrid} />
              )}
            </>
          );
        return (
          <>
            {buttonsArray[i].show && queryArray[i].isSuccess && (
              <Component dataToShow={queryArray[i].data} />
            )}
          </>
        );
      })}
    </div>
  );
};

export default CreateReport;
