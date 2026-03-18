import { LoginResponse } from "@/models/auth/loginResponse";
import { AuthRequest } from "@/models/auth/authRequest";
import apiClient from "@/utils/api/apiClient";
import { PageResponse } from "@/models/page/pageResponse";
import { BranchResponse } from "@/models/branch/branchResponse";

class BranchService {
    private managementUrl: string
    constructor() {
        this.managementUrl = "http://localhost:8080/api/management/branchs"
    }

    public async getBranches(pageIndex: number, keyword: string, countryId?: number, provinceId?: number): Promise<PageResponse<BranchResponse>> {
        const params = new URLSearchParams();
        params.append('pageIndex', pageIndex.toString());
        params.append('keyword', keyword);
        if (countryId) params.append('countryId', countryId.toString());
        if (provinceId) params.append('provinceId', provinceId.toString());

        const response = await apiClient.get(`${this.managementUrl}?${params.toString()}`);
        if (response.ok) {
            return await response.json();
        }
        throw response;

    }



}

export default new BranchService();