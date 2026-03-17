import { LoginResponse } from "@/models/auth/loginResponse";
import { AuthRequest } from "@/models/auth/authRequest";
import apiClient from "@/utils/api/apiClient";
import { PageResponse } from "@/models/page/pageResponse";
import { PositionResponse } from "@/models/positions/positionResponse";

class positionService {
    private baseUrl: string
    constructor() {
        this.baseUrl = "http://localhost:8080/api/positions"
    }

    public async getPositions(): Promise<PageResponse<PositionResponse>> {
        const response = await apiClient.get(this.baseUrl);
         if(response.ok){
            return await response.json();
        }
        throw response;

      
    }

}

export default new positionService();