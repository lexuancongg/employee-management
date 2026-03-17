import { AddressResponse } from "../address/addressResponse";

export type BranchResponse = {
  id: number;
  name: string;
  address?: AddressResponse;
};