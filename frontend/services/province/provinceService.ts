import apiClient from "@/utils/api/apiClient";
import { PageResponse } from "@/models/page/pageResponse";

class ProvinceService {
    private baseUrl: string
    constructor() {
        this.baseUrl = "http://localhost:8080/api/provinces"
    }

    public async getProvinces(): Promise<PageResponse<ProvinceResponse>> {
        const response = await apiClient.get(this.baseUrl);
        if (response.ok) {
            return await response.json();
        }
        throw response;


    }

    public async createProvince(province: ProvinceCreateRequest): Promise<ProvinceResponse> {
        const response = await apiClient.post("http://localhost:8080/api/management/provinces", JSON.stringify(province));
        if (response.ok) {
            return await response.json();
        }
        throw response;

    }
  

}

export default new ProvinceService();