'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import { PageResponse } from '@/models/page/pageResponse';
import employeeService from '@/services/employee/employeeService';
import branchService from '@/services/branch/branchService';
import departmentService from '@/services/department/departmentService';
import positionService from '@/services/positions/positionService';
import Pagination from '@/components/pagination/pagination';
import ConfirmationDialog from '@/components/dialog/confirmDialog';
import { EmployeeDetailResponse, EmployeeResponse, EmployeeStatus } from '@/models/employee/employee';
import { BranchResponse } from '@/models/branch/branchResponse';
import { DepartmentResponse } from '@/models/department/department';
import { PositionResponse } from '@/models/positions/positionResponse';
import Link from 'next/link';
import { PencilIcon, TrashIcon, EyeIcon } from '@heroicons/react/24/outline';
import EmployeeDetailModal from '@/components/employee/employeeDetailModal';

export default function EmployeePage() {
    const [employees, setEmployees] = useState<EmployeeResponse[]>([]);
    const [branches, setBranches] = useState<BranchResponse[]>([]);
    const [departments, setDepartments] = useState<DepartmentResponse[]>([]);
    const [positions, setPositions] = useState<PositionResponse[]>([]);
    
    const [employeeMore,setEmployeeMore] = useState<EmployeeDetailResponse>();
    const [pageIndex, setPageIndex] = useState(0);
    const [pageSize] = useState(10);
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);

    const [keyword, setKeyword] = useState('');
    const [selectedBranch, setSelectedBranch] = useState<number | undefined>();
    const [selectedDepartment, setSelectedDepartment] = useState<number | undefined>();
    const [selectedPosition, setSelectedPosition] = useState<number | undefined>();
    const [selectedStatus, setSelectedStatus] = useState<EmployeeStatus | undefined>();
    const [hireDateFrom, setHireDateFrom] = useState<string | undefined>();
    const [hireDateTo, setHireDateTo] = useState<string | undefined>();
    const [sortBy, setSortBy] = useState('name');
    const [sortDir, setSortDir] = useState<'asc' | 'desc'>('desc');

    const [selectedEmployee, setSelectedEmployee] = useState<EmployeeResponse | null>(null);
    const [deleteId, setDeleteId] = useState<number | null>(null);
    const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);
    
    const [moreInfoId,setMoreInfoId] = useState<number>();

    const fetchEmployees = useCallback(async () => {
        setLoading(true);
        try {
            const res: PageResponse<EmployeeResponse> = await employeeService.getEmployees(
                keyword,
                undefined,
                undefined,
                selectedDepartment,
                selectedBranch,
                selectedPosition,
                selectedStatus,
                hireDateFrom ? new Date(hireDateFrom) : undefined,
                hireDateTo ? new Date(hireDateTo) : undefined,
                pageIndex,
                pageSize,
                sortBy,
                sortDir
            );
            setEmployees(res.content);
            setTotalPages(res.totalPages);
        } catch (err) {
            console.error('Fetch employees error:', err);
        } finally {
            setLoading(false);
        }
    }, [
        keyword, selectedBranch, selectedDepartment, selectedPosition, selectedStatus,
        hireDateFrom, hireDateTo, pageIndex, pageSize, sortBy, sortDir
    ]);


    
    useEffect(()=>{
        if(!moreInfoId){
            return;
        }
        employeeService.getDetailEmployee(moreInfoId)
        .then(res=>{
            setEmployeeMore(res)
        })
        .catch((error)=>{
            console.log(error)
        })
        
    },[moreInfoId])

    useEffect(() => {
        fetchEmployees();
    }, [fetchEmployees]);

    useEffect(() => {
        if (!selectedBranch) {
            setDepartments([]);
            return;
        }
        departmentService.getDepartmentByBranch(selectedBranch)
            .then(res => setDepartments(res))
            .catch(console.error);
    }, [selectedBranch]);



    useEffect(() => {
        positionService.getPositionAlls()
            .then(res => setPositions(res))
            .catch(console.error);

        branchService.getAllBranch()
            .then(res => {
                setBranches(res)
            })
            .catch(console.error);
    }, []);




    const handleDelete = async (id: number) => {
        try {
            //   await employeeService.deleteEmployee(id);
            fetchEmployees();
        } catch (err) {
            console.error(err);
        }
    };



    return (
        <div className="p-6 space-y-6 bg-gray-50 min-h-screen">
            {/* Header */}
            <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                <div>
                    <h1 className="text-3xl font-extrabold text-gray-900">Employees</h1>
                    <p className="text-gray-500 mt-1">Manage your employees efficiently</p>
                </div>
                <Link href="/dashboard/employees/create">
                    <button className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white px-5 py-2 rounded-xl shadow-md hover:shadow-lg transition-all">
                        + Add Employee
                    </button>
                </Link>
            </div>

            {/* Filters */}
            <div className="flex flex-wrap gap-3 sm:gap-4 items-center">
                <input
                    placeholder="Search by name..."
                    className="flex-1 border border-gray-300 rounded-xl px-4 py-2 focus:ring-2 focus:ring-indigo-400 focus:border-indigo-400 transition"
                    onChange={(e) => {
                        if (searchTimeoutRef.current) clearTimeout(searchTimeoutRef.current);
                        const value = e.target.value;
                        searchTimeoutRef.current = setTimeout(() => {
                            setKeyword(value);
                            setPageIndex(0);
                        }, 500);
                    }}
                />

                {[{ value: selectedBranch, setter: setSelectedBranch, list: branches, label: 'All branches' },
                { value: selectedDepartment, setter: setSelectedDepartment, list: departments, label: 'All departments' },
                { value: selectedPosition, setter: setSelectedPosition, list: positions, label: 'All positions' }].map((f, i) => (
                    <select
                        key={i}
                        value={f.value ?? ''}
                        onChange={(e) => f.setter(e.target.value ? Number(e.target.value) : undefined)}
                        className="border border-gray-300 px-4 py-2 rounded-xl hover:border-indigo-400 transition"
                    >
                        <option value="">{f.label}</option>
                        {f.list.map(item => <option key={item.id} value={item.id}>{item.name}</option>)}
                    </select>
                ))}

                <select
                    value={selectedStatus ?? ''}
                    onChange={(e) => setSelectedStatus(e.target.value as EmployeeStatus || undefined)}
                    className="border border-gray-300 px-4 py-2 rounded-xl hover:border-indigo-400 transition"
                >
                    <option value="">All status</option>
                    <option value="ACTIVE">ACTIVE</option>
                    <option value="ONLEAVE">ONLEAVE</option>
                    <option value="PENDING">PENDING</option>
                </select>

                <input
                    type="date"
                    value={hireDateFrom ?? ''}
                    onChange={(e) => setHireDateFrom(e.target.value || undefined)}
                    className="border border-gray-300 px-4 py-2 rounded-xl hover:border-indigo-400 transition"
                />
                <input
                    type="date"
                    value={hireDateTo ?? ''}
                    onChange={(e) => setHireDateTo(e.target.value || undefined)}
                    className="border border-gray-300 px-4 py-2 rounded-xl hover:border-indigo-400 transition"
                />
            </div>

            {/* Table */}
            <div className="bg-white rounded-2xl shadow-lg overflow-hidden border border-gray-200">
                <table className="w-full text-sm text-gray-700">
                    <thead className="bg-gray-100 text-left">
                        <tr>
                            {['ID', 'Name', 'Code', 'Position', 'Department', 'Branch', 'Status', 'Actions'].map((h, i) => (
                                <th key={i} className="p-4">{h}</th>
                            ))}
                        </tr>
                    </thead>
                    <tbody>
                        {loading ? (
                            <tr>
                                <td colSpan={8} className="p-6 text-center text-gray-500">Loading...</td>
                            </tr>
                        ) : employees.length === 0 ? (
                            <tr>
                                <td colSpan={8} className="p-6 text-center text-gray-400">No data</td>
                            </tr>
                        ) : (
                            employees.map(e => (
                                <tr key={e.id} className="border-t hover:bg-gray-50 transition">
                                    <td className="p-4">{e.id}</td>
                                    <td className="p-4 font-medium">{e.name}</td>
                                    <td className="p-4">{e.employeeCode}</td>
                                    <td className="p-4">{e.positionName}</td>
                                    <td className="p-4">{e.departmentName}</td>
                                    <td className="p-4">{branches.find(b => b.id === e.branchId)?.name || ''}</td>
                                    <td className="p-4">{e.status}</td>
                                    <td className="p-4 text-right flex items-center space-x-2 justify-end">
                                        <button className="p-2 w-9 h-9 flex justify-center items-center border rounded-lg hover:bg-indigo-50 transition">
                                            <PencilIcon className="w-5 h-5 text-indigo-600" />
                                        </button>
                                        <button
                                            onClick={() => setDeleteId(e.id)}
                                            className="p-2 w-9 h-9 flex justify-center items-center bg-red-500 text-white rounded-lg hover:bg-red-600 transition"
                                        >
                                            <TrashIcon className="w-5 h-5" />
                                        </button>
                                        <button onClick={()=> setMoreInfoId(e.id)} className="p-2 w-9 h-9 flex justify-center items-center border rounded-lg hover:bg-indigo-50 transition">
                                            <EyeIcon className="w-5 h-5 text-indigo-600" />
                                        </button>
                                    </td>
                                </tr>
                            ))
                        )}
                    </tbody>
                </table>
            </div>

            {/* Pagination */}
            <Pagination
                pageIndex={pageIndex}
                totalPages={totalPages}
                onPrev={() => setPageIndex(prev => prev - 1)}
                onNext={() => setPageIndex(prev => prev + 1)}
            />

            {/* Confirm delete */}
            <ConfirmationDialog
                isShow={deleteId !== null}
                title="Confirm delete"
                okText="Delete"
                cancelText="Cancel"
                ok={async () => {
                    if (deleteId !== null) {
                        await handleDelete(deleteId);
                        setDeleteId(null);
                    }
                }}
                cancel={() => setDeleteId(null)}
            >
                <p>Are you sure you want to delete this employee?</p>
            </ConfirmationDialog>



        </div>
    );
}