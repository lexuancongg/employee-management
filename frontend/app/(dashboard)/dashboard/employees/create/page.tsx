'use client'

import { EmployeeCreateRequest, EmployeeField } from '@/models/employee/employee'

import EmployeeForm from '@/components/employee/employeeForm'

export default function CreateEmployeePage() {
  const handleCreate=(data:EmployeeField)=>{

  }
  return  (
    <EmployeeForm
    mode='create'
    onSubmit={handleCreate}
    
    ></EmployeeForm>
  )
}