export type DepartmentResponse = {
  id: number;
  name: string;
  branchName: string;
  branchId:number
};


export type DepartmentCreateRequest = {
  name: string;
  branchId: number;
};