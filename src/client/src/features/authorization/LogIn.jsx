import React, { useState } from "react";
import { useForm } from "react-hook-form";
import { useLocation, useNavigate } from "react-router-dom";
import startVideo from "../../data/startVideo.mp4";
import { Link } from "react-router-dom";

import { useDispatch } from "react-redux";
import { setToken, setUserData } from "./authorizationSlice";
import { useLoginMutation } from "./authorizationApiSlice";

//Start page of this project
export default function Login() {
  const [login, { data, status, error, isSuccess }] = useLoginMutation();
  const dispatch = useDispatch();

  const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      email: "",
      password: "",
    },
  });

  const onSubmit = async (data) => {
    try {
      const { accessToken, authorities, user_id } = await login(data).unwrap();

      dispatch(setToken({ accessToken }));
      dispatch(setUserData({ authorities, user_id }));
      authorities === process.env.REACT_APP_ROLE_USER &&
        navigate("/UserApp/Calendar");
      authorities === process.env.REACT_APP_ROLE_ADMIN &&
        navigate("/AdminApp/DayManagmentCertainDate");
    } catch (err) {}
  };

  return (
    <div className="relative w-full h-screen bg-zinc-900/90 ">
      <video
        className="object-cover h-full w-full absolute"
        src={startVideo}
        autoPlay
        loop
        muted
      />
      <div className="flex flex-col justify-center items-center h-full ">
        <form
          className="max-w-[400px] w-full mx-auto bg-none  p-8 bg-white opacity-90 z-10 rounded-lg"
          onSubmit={handleSubmit(onSubmit)}
        >
          <h2 className="text-black opacity-90 text-4xl font-bold text-center py-4">
            Login
          </h2>
          {error && <div>Bad Credentials</div>}
          <div className="flex flex-col mb-4">
            <input
              className="border relative bg-gray-200 opacity-90 p-2 rounded-lg"
              type="text"
              name="email"
              placeholder="Username"
              {...register("email", {
                required: "email is required",
              })}
            />
            {errors?.email?.message}
          </div>
          <div className="flex flex-col ">
            <input
              className="border relative bg-gray-200 opacity-90 p-2 rounded-lg"
              type="password"
              name="password"
              placeholder="Password"
              {...register("password", { required: "Password is required" })}
            />
            {errors?.password?.message}
          </div>
          <button className="w-full py-3 mt-8 rounded-full bg-indigo-600  hover:bg-indigo-500 relative text-white opacity-90 cursor-pointer">
            Sign In
          </button>
          <br></br>
          <b className="text-black text-center mt-2 z-10 text-lg font-bold ">
            Not a member?
            <Link to="/SignUp">Sign Up</Link>
          </b>
        </form>
      </div>
    </div>
  );
}
