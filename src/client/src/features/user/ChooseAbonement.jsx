import { CheckIcon } from "@heroicons/react/solid";
import { Header } from "../../common/components";
import { useSelector } from "react-redux";
import {
  selectAllAbonements,
  useGetUserAbonementQuery,
} from "./abonementSlice";
import { selectCurrentColor } from "../appSettingsSlice";

import { useUpdateUserDataMutation } from "./userSlice";
//Component that gives to an user an option to change an abonement
const ChooseAbonement = () => {
  const {
    data: { abonement_id },
  } = useGetUserAbonementQuery();
  const [updateAbonement] = useUpdateUserDataMutation();
  const currentColor = useSelector(selectCurrentColor);
  const abonementsToChoose = useSelector(selectAllAbonements);

  const handleClick = async (href) => {
    try {
      await updateAbonement({ abonement: href }).unwrap();
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <div
      name="Abonements"
      className="w-full mt-0 text-white bg-zinc-100  dark:bg-gray-800 text-center"
    >
      <Header title="Abonements" className="text-2xl" />
      <div className="max-w-[1240px] mx-auto py-12 ">
        {/**Abonements boxes */}
        <div className="grid md:grid-cols-2 ">
          {abonementsToChoose.map((item) => {
            const {
              description,
              name_of_abonement,
              price,
              abonement_id: abonement_id_inner,
              _links: {
                self: { href },
              },
            } = item;

            return (
              <div className="bg-white text-slate-900 m-4 p-8 rounded-xl shadow-2xl relative hover:scale-105 ease-in duration-300">
                <span className="uppercase px-3 py-1 bg-indigo-200 text-indigo-900 rounded-2xl text-sm">
                  {name_of_abonement}
                </span>
                <div>
                  <p className="text-6xl font-bold py-4 flex">
                    {price}
                    <span className="text-xl text-slate-500 flex flex-col justify-end">
                      /mo
                    </span>
                  </p>
                </div>

                <div className="text-2xl">
                  <p className="flex py-4">
                    <CheckIcon className="w-8 mr-5 text-green-600" />
                    {description}
                  </p>
                  <button
                    style={{
                      backgroundColor:
                        abonement_id === abonement_id_inner
                          ? "grey"
                          : currentColor,
                    }}
                    disabled={abonement_id === abonement_id_inner}
                    className="w-full rounded-full py-4 my-4 "
                    onClick={() => handleClick(href)}
                  >
                    Get Started
                  </button>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
};

export default ChooseAbonement;
