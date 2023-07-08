import React from "react";
import {
  ChartComponent,
  SeriesCollectionDirective,
  SeriesDirective,
  Inject,
  ColumnSeries,
  Category,
  Tooltip,
  Legend,
  RangeColorSettingsDirective,
  RangeColorSettingDirective,
} from "@syncfusion/ej2-react-charts";

import {
  ColorMappingPrimaryXAxis,
  ColorMappingPrimaryYAxis,
  rangeColorMapping,
} from "../../../data/dummy";
import { useSelector } from "react-redux";
import { useGetUserQuery } from "../../../features/user/userSlice";
import { selectUserId } from "../../../features/authorization/authorizationSlice";

//ColorMapping Chart Template Component
const ColorMapping = ({ data }) => {
  const user_id = useSelector(selectUserId);
  const { data: user } = useGetUserQuery(user_id);
  const { theme } = user;

  return (
    <ChartComponent
      id="charts"
      primaryXAxis={ColorMappingPrimaryXAxis}
      primaryYAxis={ColorMappingPrimaryYAxis}
      chartArea={{ border: { width: 0 } }}
      legendSettings={{ mode: "Range", background: "white" }}
      tooltip={{ enable: true }}
      background={"white"}
    >
      <Inject services={[ColumnSeries, Tooltip, Category, Legend]} />
      <SeriesCollectionDirective>
        <SeriesDirective
          dataSource={data}
          name="Amount of tables"
          xName="first"
          yName="second"
          type="Column"
          cornerRadius={{
            topLeft: 10,
            topRight: 10,
          }}
        />
      </SeriesCollectionDirective>
      <RangeColorSettingsDirective>
        {/* eslint-disable-next-line react/jsx-props-no-spreading */}
        {rangeColorMapping.map((item, index) => (
          <RangeColorSettingDirective key={index} {...item} />
        ))}
      </RangeColorSettingsDirective>
    </ChartComponent>
  );
};

export default ColorMapping;
