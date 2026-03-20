'use client'

import { EmployeeCreateRequest, EmployeeField } from '@/models/employee/employee'

import EmployeeForm from '@/components/employee/employeeForm'
import employeeService from '@/services/employee/employeeService';

export default function CreateEmployeePage() {
  const handleCreate= async (data:EmployeeField)=>{
     const employee: EmployeeCreateRequest = { ...data };
     try {
       await employeeService.createEmployee(employee)
       

     } catch (error) {
      
     }

  }
  return  (
    <EmployeeForm
    mode='create'
    onSubmit={handleCreate}
    
    ></EmployeeForm>
  )
}