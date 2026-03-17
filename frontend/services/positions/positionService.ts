import { LoginResponse } from "@/models/auth/loginResponse";
import { AuthRequest } from "@/models/auth/authRequest";
import apiClient from "@/utils/api/apiClient";
import { PageResponse } from "@/models/page/pageResponse";
import { PositionResponse } from "@/models/positions/positionResponse";
import { PositionCreateRequest } from "@/models/positions/positionCreateRequest";
import { json } from "stream/consumers";

class positionService {
    private baseUrl: string
    constructor() {
        this.baseUrl = "http://localhost:8080/api/positions"
    }

    public async getPositions(): Promise<PageResponse<PositionResponse>> {
        const response = await apiClient.get(this.baseUrl);
        if (response.ok) {
            return await response.json();
        }
        throw response;


    }

    public async createPosition(position: PositionCreateRequest): Promise<PositionResponse> {
        const response = await apiClient.post(this.baseUrl, JSON.stringify(position));
        if (response.ok) {
            return await response.json();
        }
        throw response;

    }
    public async deletePosition(id: number): Promise<Response> {
        return await apiClient.delete(this.baseUrl + `/${id}`);
    }

}

export default new positionService();