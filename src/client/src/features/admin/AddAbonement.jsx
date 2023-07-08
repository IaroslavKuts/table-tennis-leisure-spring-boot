import { CheckIcon } from "@heroicons/react/solid";
import { useForm } from "react-hook-form";
import { useSelector } from "react-redux";
import { selectCurrentColor } from "../appSettingsSlice";
import {
  selectAllAbonements,
  useAddAbonementMutation,
} from "../user/abonementSlice";
import uuid from "react-uuid";

//Component that allow to browse current abonements and add new ones
const AddAbonement = () => {
  const currentColor = useSelector(selectCurrentColor);
  const allAbonements = useSelector(selectAllAbonements);
  const [addAbonement] = useAddAbonementMutation();
  const {
    register,
    handleSubmit,

    reset,
    formState: { errors },
  } = useForm({
    defaultValues: {
      name_of_abonement: "",
      price: "",
      description: "",
    },
  });

  const onSubmit = async (abonementData) => {
    await addAbonement(abonementData).unwrap();
    try {
    } catch (err) {
      console.log(err);
    }
  };

  const handleReset = () => {
    reset();
  };

  return (
    <div
      name="Abonements"
      className="w-full mt-0 text-white bg-zinc-100  dark:bg-gray-800"
    >
      <div className="max-w-[1240px] mx-auto py-12 ">
        {/**to create new abonement */}

        {/**Abonements boxes */}
        <div className="grid md:grid-cols-3 ">
          {allAbonements.map((item) => {
            return (
              <div
                key={uuid()}
                className="bg-white text-slate-900 m-4 p-8 rounded-xl shadow-2xl relative hover:scale-105 ease-in duration-300"
              >
                <span className="uppercase px-3 py-1 bg-indigo-200 text-indigo-900 rounded-2xl text-sm">
                  {item.name_of_abonement}
                </span>
                <div>
                  <p className="text-6xl font-bold py-4 flex">
                    {`$${item.price}`}
                    <span className="text-xl text-slate-500 flex flex-col justify-end">
                      /mo
                    </span>
                  </p>
                </div>

                <div className="text-2xl">
                  <p className="flex py-4">
                    <CheckIcon className="w-8 mr-5 text-green-600" />
                    {item.description}
                  </p>
                </div>
              </div>
            );
          })}

          {/**Create Abonement*/}
          <div className="bg-white text-slate-900 m-4 p-8 rounded-xl shadow-2xl relative hover:scale-105 ease-in duration-300">
            <form className="flex flex-col" onSubmit={handleSubmit(onSubmit)}>
              <h2 className="text-black">Creating abonement</h2>
              <input
                className="bg-[#ccd6f6] p-2 text-black my-10"
                name="name_of_abonement"
                type="text"
                placeholder="enter name of abonement"
                {...register("name_of_abonement", {
                  required: "Abonement name is required",
                  pattern: {
                    value: /^[A-Za-z\s]*$/, // any amount of letters and spaces
                    message: "Letters allowed only",
                  },
                })}
              />
              {errors?.name_of_abonement?.message}
              <input
                className="bg-[#ccd6f6] p-2 text-black my-10"
                name="price"
                type="text"
                placeholder="enter price of abonement"
                {...register("price", {
                  required: "Abonement price is required",
                  pattern: {
                    value: /^[0-9]*$/, // digits only
                    message: "Digits alowed only",
                  },
                })}
              />
              {errors?.price?.message}
              <textarea
                className="bg-[#ccd6f6] p-2"
                name="description"
                rows="10"
                placeholder="Enter description"
                {...register("description", {
                  required: "Abonement description is required",
                })}
              />
              {errors.description?.message}
              <button
                style={{ backgroundColor: currentColor }}
                className="w-64 rounded-full py-3 mt-8  relative text-white"
                type="Submit"
              >
                Add new Abonement
              </button>
              <button
                className="w-64 rounded-full py-3 mt-8  relative text-white bg-red-600"
                onClick={handleReset}
              >
                Clear
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AddAbonement;
