import { LoginResponse } from "@/models/auth/loginResponse";
import { AuthRequest } from "@/models/auth/authRequest";
import apiClient from "@/utils/api/apiClient";
import { PageResponse } from "@/models/page/pageResponse";
import { BranchResponse } from "@/models/branch/branchResponse";

class BranchService{
    private baseUrl:string
    constructor() {
        this.baseUrl = "http://localhost:8080/api/branchs"
    }

    public async getBranches():Promise<PageResponse<BranchResponse>>{
        const response = await apiClient.get(this.baseUrl);
        if(response.ok){
            return await response.json();
        }
        throw response;

    }

   

}

export default new BranchService();