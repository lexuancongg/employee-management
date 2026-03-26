'use client'

import { BranchResponse } from "@/models/branch/branchResponse"
import { DepartmentResponse } from "@/models/department/department"
import { EmployeeDetailResponse, EmployeeField } from "@/models/employee/employee"
import { PositionResponse } from "@/models/positions/positionResponse"
import branchService from "@/services/branch/branchService"
import departmentService from "@/services/department/departmentService"
import positionService from "@/services/positions/positionService"
import { useParams } from "next/navigation"
import { FC, useEffect, useState } from "react"
import { useForm } from "react-hook-form"
import AddressForm from "../address/addressForm"

type Props = {
    mode: 'create' | 'update'
    defaultValue?: EmployeeDetailResponse
    onSubmit: (data: EmployeeField) => void
}

const EmployeeForm: FC<Props> = ({
    mode,
    defaultValue,
    onSubmit,
}) => {

    const params = useParams();
    const id = params.id;

    const [branches, setBranches] = useState<BranchResponse[]>([])
    const [departments, setDepartments] = useState<DepartmentResponse[]>([])
    const [positions, setPositions] = useState<PositionResponse[]>([])

    const {
        register,
        setValue,
        handleSubmit,
        reset,
        watch
    } = useForm<EmployeeField>()
    const selectedBranch = watch('branchId')


    useEffect(() => {
        branchService.getAllBranch().then(setBranches)
        positionService.getPositionAlls().then(setPositions)
    }, [])

    useEffect(() => {
        if (!selectedBranch) {
            setDepartments([])
            return
        }
        departmentService.getDepartmentByBranch(selectedBranch)
            .then(res => {
                setDepartments(res)

            })
    }, [selectedBranch])

    useEffect(() => {
        if (!defaultValue) return

        const branchId =
            defaultValue.department?.branchId

        reset({
            name: defaultValue.name,
            email: defaultValue.email,
            phone: defaultValue.phone,
            gender: defaultValue.gender,
            birthday: defaultValue.birthday,
            hireDate: defaultValue.hireDate,

            branchId: branchId as any,
            departmentId: defaultValue.department?.id as any,
            positionId: defaultValue.position?.id as any
        })

    }, [defaultValue, reset])


    useEffect(() => {
        if (!defaultValue?.department?.id) return
        if (departments.length === 0) return
        const isSameBranch =
            selectedBranch == defaultValue.department.branchId
        if (!isSameBranch){
        //    setValue('departmentId', undefined)
        return
        }

        setValue('departmentId', defaultValue.department.id as any)
    }, [departments, defaultValue])



    return (
        <div className="max-w-5xl mx-auto p-6">
            <form
                onSubmit={handleSubmit(onSubmit)}
                className="bg-white border border-gray-100 shadow-lg rounded-2xl p-8 space-y-8"
            >
                <h2 className="text-2xl font-semibold">
                    {mode === 'create' ? 'Create Employee' : 'Update Employee'}
                </h2>

                <div>
                    <h3 className="text-lg font-medium mb-4">Basic Info</h3>

                    <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                        <FormItem label="Full name">
                            <input {...register('name')} className="input" />
                        </FormItem>

                        <FormItem label="Email">
                            <input {...register('email')} className="input" />
                        </FormItem>

                        <FormItem label="Phone">
                            <input {...register('phone')} className="input" />
                        </FormItem>

                        <FormItem label="Gender">
                            <select {...register('gender')} className="input">
                                <option value="">Select gender</option>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                            </select>
                        </FormItem>

                        <FormItem label="Birthday">
                            <input type="date" {...register('birthday')} className="input" />
                        </FormItem>

                        <FormItem label="Hire date">
                            <input type="date" {...register('hireDate')} className="input" />
                        </FormItem>
                    </div>
                </div>

                <div>
                    <h3 className="text-lg font-medium mb-4">Organization</h3>

                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <FormItem label="Branch">
                            <select
                                {...register('branchId', {
                                    onChange: () => {
                                        setValue('departmentId', undefined as any)
                                    }
                                })}
                                className="input"
                            >
                                <option value="">Select branch</option>
                                {branches.map(b => (
                                    <option key={b.id} value={b.id}>{b.name}</option>
                                ))}
                            </select>
                        </FormItem>

                        <FormItem label="Department">
                            <select
                                {...register('departmentId')}
                                className="input"
                                disabled={!selectedBranch}
                            >
                                <option value="">Select department</option>
                                {departments.map(d => (
                                    <option key={d.id} value={d.id}>{d.name}</option>
                                ))}
                            </select>
                        </FormItem>

                        <FormItem label="Position">
                            <select {...register('positionId')} className="input">
                                <option value="">Select position</option>
                                {positions.map(p => (
                                    <option key={p.id} value={p.id}>{p.name}</option>
                                ))}
                            </select>
                        </FormItem>
                    </div>
                </div>


                {/* {id ?
                    <div>
                        <h3 className="text-lg font-medium mb-4">Address</h3>

                        <AddressForm
                            register={register}
                            addressInit={defaultValue?.address}
                            setValue={setValue}
                        ></AddressForm>
                    </div>

                    : ''} */}

                <div className="flex justify-end">
                    <button
                        type="submit"
                        className="px-6 py-2.5 bg-blue-600 text-white rounded-xl font-medium hover:bg-blue-700 active:scale-95 transition"
                    >
                        {mode === 'create' ? 'Create' : 'Update'}
                    </button>
                </div>
            </form>


            <style jsx>{`
        .input {
          width: 100%;
          height: 44px;
          padding: 0 14px;
          border: 1px solid #e5e7eb;
          border-radius: 10px;
          background: #fafafa;
          transition: all 0.2s;
        }

        .input:focus {
          background: white;
          border-color: #2563eb;
          box-shadow: 0 0 0 3px rgba(37,99,235,0.15);
          outline: none;
        }
      `}</style>
        </div>
    )
}

function FormItem({ label, children }: any) {
    return (
        <div className="flex flex-col gap-1">
            <label className="text-sm text-gray-600">{label}</label>
            {children}
        </div>
    )
}

export default EmployeeForm