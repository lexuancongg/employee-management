import { AddressCreateRequest, AddressResponse } from "../address/addressResponse";

export type BranchResponse = {
  id: number;
  name: string;
  address: AddressResponse;
};





export type BranchCreateRequest = {
  name: string;
  address: AddressCreateRequest;
};


export type BranchField = {
  name:string;
  countryId: number;
  provinceId: number;
  districtId: number;
  specificAddress:string;
  
}