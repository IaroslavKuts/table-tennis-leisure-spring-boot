import React, { useEffect } from "react";
import { AiOutlineMenu } from "react-icons/ai";
import { TooltipComponent } from "@syncfusion/ej2-react-popups";
import { useDispatch, useSelector } from "react-redux";
import {
  selectActiveMenu,
  selectCurrentColor,
  selectScreenSize,
  setActiveMenu,
  setScreenSize,
} from "../../features/appSettingsSlice";
import { apiSlice } from "../../app/api/apiSlice";

import { logOut } from "../../features/authorization/authorizationSlice";

// Navigation Button Template Component that are placed in Navbar component
const NavButton = ({ title, customFunc, icon, color, dotColor }) => (
  <TooltipComponent content={title} position="BottomCenter">
    <button
      type="button"
      onClick={() => customFunc()}
      style={{ color }}
      className="relative text-xl rounded-full p-3 hover:bg-light-gray"
    >
      <span
        style={{ background: dotColor }}
        className="absolute inline-flex rounded-full h-2 w-2 right-2 top-2"
      />
      {icon}
    </button>
  </TooltipComponent>
);

// Navigation Bar Template Component.
// Contains Menu and Logout buttons
// Buttos are designed with  Navigation Button Template Component
const Navbar = () => {
  // const logout = useLogout();
  const dispatch = useDispatch();

  const currentColor = useSelector(selectCurrentColor);
  const activeMenu = useSelector(selectActiveMenu);
  const screenSize = useSelector(selectScreenSize);

  /*Check for mobile if nav bar will open or not */

  useEffect(() => {
    const handleResize = () => dispatch(setScreenSize(window.innerWidth));

    window.addEventListener("resize", handleResize);

    handleResize();

    return () => window.removeEventListener("resize", handleResize);
  }, []);

  //will hide the sidebar if screen lower than 900 */

  useEffect(() => {
    if (screenSize <= 900) {
      dispatch(setActiveMenu({ activeMenu: false }));
    } else {
      dispatch(setActiveMenu({ activeMenu: true }));
    }
  }, [screenSize]);

  const handleActiveMenu = () =>
    dispatch(setActiveMenu({ activeMenu: !activeMenu }));

  return (
    <div className="flex justify-between p-2 md:ml-6 md:mr-6 relative">
      <NavButton
        title="Menu"
        customFunc={handleActiveMenu}
        color={currentColor}
        icon={<AiOutlineMenu />}
      />
      <div className="flex"></div>
      <button
        type="button"
        onClick={async () => {
          dispatch(logOut());
          dispatch(apiSlice.util.resetApiState());
          localStorage.clear();
          await fetch(process.env.REACT_APP_LOGOUT);
        }}
        style={{
          backgroundColor: currentColor,
          color: "white",
          borderRadius: "5px",
        }}
        className={`text-xl p-1 w-36 border-lg hover:drop-shadow-xl`}
      >
        Log Out
      </button>
    </div>
  );
};

export default Navbar;
