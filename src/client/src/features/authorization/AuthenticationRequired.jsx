import { useLocation, Navigate, Outlet } from "react-router-dom";
import { useSelector } from "react-redux";
import { selectCurrentToken } from "./authorizationSlice";
import { useGetUserQuery } from "../user/userSlice";
import { useGetUserAbonementQuery } from "../user/abonementSlice";
import { useGetUserOrdersQuery } from "../user/ordersSlice";

const RequireAuthorization = () => {
  const accessToken = useSelector(selectCurrentToken);
  const location = useLocation();

  const {
    isLoading: getUserIsLoading,
    isSuccess: getUserIsSuccess,
    isError: getUserIsError,
  } = useGetUserQuery(undefined, {
    skip: !accessToken,
  });
  const {
    isLoading: getUserAbonementIsLoading,
    isSuccess: getUserAbonementIsSuccess,
    isError: getUserAbonementIsError,
  } = useGetUserAbonementQuery(undefined, {
    skip: !accessToken,
  });
  const {
    isLoading: getUserOrdersIsLoading,
    isSuccess: getUserOrdersIsSuccess,
    isError: getUserOrdersIsError,
  } = useGetUserOrdersQuery(undefined, {
    skip: !accessToken,
  });

  if (
    accessToken &&
    getUserIsSuccess &&
    getUserAbonementIsSuccess &&
    getUserOrdersIsSuccess
  ) {
    return <Outlet />;
  }
  if (getUserAbonementIsLoading || getUserIsLoading || getUserOrdersIsLoading)
    return <p>Loading</p>;
  return <Navigate to="/" state={{ from: location }} replace />;
};
export default RequireAuthorization;
