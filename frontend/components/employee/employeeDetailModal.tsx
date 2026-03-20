import { FC } from "react";
import { EmployeeDetailResponse } from "@/models/employee/employee";

type Props = {
  open: boolean;
  employeeDetail: EmployeeDetailResponse;
  onClose: () => void;
};
