'use client'
import EmployeeForm from "@/components/employee/employeeForm"
import { EmployeeField } from "@/models/employee/employee"

export default function UpdateEmployee() {
  const handleUpdate=(data:EmployeeField)=>{

  }
  return  (
    <EmployeeForm
    mode='update'
    onSubmit={handleUpdate}
    
    ></EmployeeForm>
  )
}