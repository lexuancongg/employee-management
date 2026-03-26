'use client'
import EmployeeForm from "@/components/employee/employeeForm";
import { EmployeeCreateRequest, EmployeeDetailResponse, EmployeeField } from "@/models/employee/employee";
import employeeService from "@/services/employee/employeeService";
import { useParams } from "next/navigation";
import { useEffect, useState } from "react";

export default function UpdateEmployee() {
    const params = useParams();
    const id = params.id;
    console.log(2000)

    const [employee,setEmployee] = useState<EmployeeDetailResponse>();

    useEffect(()=>{
        employeeService.getDetailEmployee(Number(id))
        .then((res)=>{
            setEmployee(res)    
        })
        .catch(error=>{
            console.log(error)
        })
    },[id])

    const handleUpdate = async (data: EmployeeField) => {
       const employee: EmployeeCreateRequest = { ...data };
       try {
          await employeeService.updateEmployee(Number(id),employee)
          console.log("thành công")
       } catch (error) {
        console.log(error)
       }
    }
    return (
        <EmployeeForm
            mode='update'
            onSubmit={handleUpdate}
            defaultValue={employee}

        ></EmployeeForm>
    )
}