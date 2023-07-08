import { MdOutlineCancel } from "react-icons/md";
import { BsCheck } from "react-icons/bs";
import { TooltipComponent } from "@syncfusion/ej2-react-popups";

import { themeColors } from "../../data/dummy";
import { useDispatch, useSelector } from "react-redux";
import {
  selectCurrentColor,
  setCurrentColor,
  setThemeSettings,
} from "../../features/appSettingsSlice";
import {
  selectUser,
  useGetUserQuery,
  useUpdateUserDataMutation,
} from "../../features/user/userSlice";
import { selectUserId } from "../../features/authorization/authorizationSlice";

//ThemeSetting TemplateComponent
//Contains switching between dark and light modes and changing colors of buttons
const ThemeSettings = () => {
  const dispatch = useDispatch();
  const { data: user } = useGetUserQuery();
  const { theme } = user;

  const [updateTheme] = useUpdateUserDataMutation();
  const currentColor = useSelector(selectCurrentColor);

  const handleClick = async (target) => {
    try {
      await updateTheme({ [target.name]: target.value }).unwrap();
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div className="bg-half-transparent w-screen fixed nav-item top-0 right-0">
      <div className="float-right h-screen dark:text-gray-200  bg-white dark:bg-[#484B52] w-400">
        <div className="flex justify-between items-center p-4 ml-4">
          <p className="font-semibold text-lg">Settings</p>
          <button
            type="button"
            onClick={() => dispatch(setThemeSettings({ themeSettings: false }))}
            style={{ color: "rgb(153, 171, 180)", borderRadius: "50%" }}
            className="text-2xl p-3 hover:drop-shadow-xl hover:bg-light-gray"
          >
            <MdOutlineCancel />
          </button>
        </div>
        <div className="flex-col border-t-1 border-color p-4 ml-4">
          <p className="font-semibold text-xl ">Theme Option</p>

          <div className="mt-4">
            <input
              type="radio"
              id="light"
              name="theme"
              value="light"
              className="cursor-pointer"
              onChange={({ target }) => handleClick(target)}
              checked={theme === "light"}
            />
            <label htmlFor="light" className="ml-2 text-md cursor-pointer">
              Light
            </label>
          </div>
          <div className="mt-2">
            <input
              type="radio"
              id="dark"
              name="theme"
              value="dark"
              onChange={({ target }) => handleClick(target)}
              className="cursor-pointer"
              checked={theme === "dark"}
            />
            {/* eslint-disable-next-line jsx-a11y/label-has-associated-control */}
            <label htmlFor="dark" className="ml-2 text-md cursor-pointer">
              Dark
            </label>
          </div>
        </div>
        <div className="p-4 border-t-1 border-color ml-4">
          <p className="font-semibold text-xl ">Theme Colors</p>
          <div className="flex gap-3">
            {themeColors.map((item, index) => (
              <TooltipComponent
                key={index}
                content={item.name}
                position="TopCenter"
              >
                <div
                  className="relative mt-2 cursor-pointer flex gap-5 items-center"
                  key={item.name}
                >
                  <button
                    type="button"
                    className="h-10 w-10 rounded-full cursor-pointer"
                    style={{ backgroundColor: item.color }}
                    onClick={(_) =>
                      dispatch(setCurrentColor({ currentColor: item.color }))
                    }
                  >
                    <BsCheck
                      className={`ml-2 text-2xl text-white ${
                        item.color === currentColor ? "block" : "hidden"
                      }`}
                    />
                  </button>
                </div>
              </TooltipComponent>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
};

export default ThemeSettings;
