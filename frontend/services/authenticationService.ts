import { AuthenticationResponse } from "@/models/auth/authenticationResponse";
import { AuthRequest } from "@/models/auth/authRequest";
import apiClient from "@/utils/api/apiClient";

class AuthenticationService{
    private baseUrl:string
    constructor() {
        this.baseUrl = "http://localhost:8080/api/authetication"
    }

    public async login(authRequest : AuthRequest):Promise<AuthenticationResponse>{
        const response = await apiClient.post(`${this.baseUrl}/login`,JSON.stringify(authRequest));
        if(response.ok){
            return await response.json();
        }
        throw response;
    }

}

export default new AuthenticationService();