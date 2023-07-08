import React from "react";
import {
  AccumulationChartComponent,
  AccumulationSeriesCollectionDirective,
  AccumulationSeriesDirective,
  Inject,
  AccumulationLegend,
  AccumulationDataLabel,
  AccumulationTooltip,
  PyramidSeries,
  AccumulationSelection,
} from "@syncfusion/ej2-react-charts";
import { useSelector } from "react-redux";

import { selectUserId } from "../../../features/authorization/authorizationSlice";
import { useGetUserQuery } from "../../../features/user/userSlice";

//Pyramid Template Component
const Pyramid = ({ data }) => {
  const user_id = useSelector(selectUserId);
  const { data: user } = useGetUserQuery(user_id);
  const { theme } = user;

  return (
    <AccumulationChartComponent
      id="pyramid-chart"
      legendSettings={{ background: "white" }}
      tooltip={{ enable: true }}
      background={"white"}
    >
      <Inject
        services={[
          AccumulationDataLabel,
          AccumulationTooltip,
          PyramidSeries,
          AccumulationLegend,
          AccumulationSelection,
        ]}
      />
      <AccumulationSeriesCollectionDirective>
        <AccumulationSeriesDirective
          // name="Food"
          dataSource={data}
          xName="first"
          yName="second"
          type="Pyramid"
          width="45%"
          height="80%"
          neckWidth="15%"
          gapRatio={0.03}
          explode
          emptyPointSettings={{ mode: "Drop", fill: "red" }}
          dataLabel={{
            visible: true,
            position: "Inside",
            name: "text",
          }}
        />
      </AccumulationSeriesCollectionDirective>
    </AccumulationChartComponent>
  );
};

export default Pyramid;
