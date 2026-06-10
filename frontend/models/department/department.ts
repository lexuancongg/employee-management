export type DepartmentStatus =
    | "ACTIVE"
    | "INACTIVE";

export type DepartmentResponse = {
  id: number;
  name: string;

  branchName: string;
  branchId: number;

  quantityEmployee: number;

  departmentCode: string;

  status: DepartmentStatus;

  managerName: string;
};

export type DepartmentCreateRequest = {
  name: string;
  branchId: number;
  managerId : number;
  departmentCode:string,
  status:DepartmentStatus
};