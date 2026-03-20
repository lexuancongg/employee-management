import { AddressCreateRequest, AddressResponse } from "../address/addressResponse";
import { DepartmentResponse } from "../department/department";
import { ImageGetResponse } from "../image/image";
import { PositionResponse } from "../positions/positionResponse";

export type EmployeeStatus = 'ACTIVE' | 'ONLEAVE' | 'PENDING';

export type EmployeeResponse = {
  id: number;
  employeeCode: string;
  name: string;
  status: EmployeeStatus;
  positionName: string;
  positionId: number;
  departmentId: number;
  departmentName: string;
  branchId:number
};



export type EmployeeDetailResponse = {
  id: number
  name: string
  gender: string
  email: string
  phone: string
  birthday: string 
  hireDate: string 
  status: EmployeeStatus
  position: PositionResponse
  department: DepartmentResponse
  avatar: ImageGetResponse
  address?: AddressResponse
}


export type EmployeeField = {
  name: string
  gender: string
  email: string
  phone: string
  birthday: string 
  hireDate: string
   positionId:number
  departmentId:number
  branchId:number
  specificAddress:string,
   countryId: number;
  provinceId: number;
  districtId: number;


}

export type EmployeeCreateRequest = {
  name: string;
  gender: string; 
  email: string; 
  phone: string;
  birthday: string; 
  hireDate: string; 
  status?: EmployeeStatus;
  avatarId?: number;
  positionId: number;
  departmentId: number;
  // address: AddressCreateRequest



  //  countryId: number;
  // provinceId: number;
  // districtId: number;
  // specificAddress: string;
};

