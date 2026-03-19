import { DepartmentResponse } from "@/models/department/department";
import { PageResponse } from "@/models/page/pageResponse";
import apiClient from "@/utils/api/apiClient";

class DepartmentService {
    private managementUrl: string;
    private employeeUrl: string;
    constructor() {
        this.managementUrl = "http://localhost:8080/api/management/departments";
        this.employeeUrl = "http://localhost:8080/api/employee/departments";
    }

    public async getDepartments(pageIndex: number, keyword: string, branchId?: number): Promise<PageResponse<DepartmentResponse>> {
        const params = new URLSearchParams();
        params.append('pageIndex', pageIndex.toString());
        params.append('keyword', keyword);
        if (branchId) params.append('branchId', branchId.toString());
        const response = await apiClient.get(`${this.managementUrl}?${params.toString()}`);
        if (response.ok) {
            return await response.json();
        }
        throw response;
    }
}

export default new DepartmentService();