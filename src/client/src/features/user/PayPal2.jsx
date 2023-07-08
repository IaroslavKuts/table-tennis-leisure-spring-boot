import { PayPalScriptProvider, PayPalButtons } from "@paypal/react-paypal-js";
import { useAddOrdersMutation } from "./ordersSlice";
import { useCreatePayPalOrderMutation } from "./PayPalSlice";

//Paypal buttons
function PayPal2({ chosenTimePeriods, disabled, dateOfGame }) {
  const [addOrders] = useAddOrdersMutation();
  const [createOrder2] = useCreatePayPalOrderMutation();
  const onApprove = async (data, actions) => {
    try {
      await addOrders({ chosenTimePeriods, dateOfGame }).unwrap();
    } catch (err) {
      console.log(err);
    }
  };
  const onError = (error) => {
    console.log(error);
  };

  return (
    <PayPalScriptProvider
      options={{
        "client-id": process.env.REACT_APP_PAYPAL_CLIENT_ID,
        currency: "ILS",
      }}
    >
      <PayPalButtons
        disabled={!disabled}
        createOrder={async () => {
          const { id } = await createOrder2(chosenTimePeriods).unwrap();
          return id;
        }}
        onApprove={onApprove}
        onError={onError}
        forceReRender={[chosenTimePeriods]}
        fundingSource={"paypal"}
      />
    </PayPalScriptProvider>
  );
}

export default PayPal2;
