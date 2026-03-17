import apiClient from "@/utils/api/apiClient";
import { PageResponse } from "@/models/page/pageResponse";
import { CountryCreateRequest, CountryResponse } from "@/models/country/countryResponse";

class CountryService{
    private baseUrl:string
    constructor() {
        this.baseUrl = "http://localhost:8080/api/countries"
    }

    public async getCountries():Promise<PageResponse<CountryResponse>>{
        const response = await apiClient.get(this.baseUrl);
        if(response.ok){
            return await response.json();
        }
        throw response;

    }


    public async createCountry(country:CountryCreateRequest):Promise<CountryResponse>{
        const response = await apiClient.post(this.baseUrl,JSON.stringify(country));
          if(response.ok){
            return await response.json();
        }
        throw response;


    }

   

}

export default new CountryService();