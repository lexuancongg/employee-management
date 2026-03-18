type ProvinceResponse = {
  id: number;
  name: string;
  countryId: number;
  type: string;
};

type ProvinceCreateRequest = {
  name: string;
  countryId: number;
  type:string;
};