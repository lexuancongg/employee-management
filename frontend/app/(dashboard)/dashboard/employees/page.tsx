'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import { PageResponse } from '@/models/page/pageResponse';
import employeeService from '@/services/employee/employeeService';
import branchService from '@/services/branch/branchService';
import departmentService from '@/services/department/departmentService';
import positionService from '@/services/positions/positionService';
import Pagination from '@/components/pagination/pagination';
import ConfirmationDialog from '@/components/dialog/confirmDialog';
import { EmployeeResponse, EmployeeStatus } from '@/models/employee/employee';
import { BranchResponse } from '@/models/branch/branchResponse';
import { DepartmentResponse } from '@/models/department/department';
import { PositionResponse } from '@/models/positions/positionResponse';

export default function EmployeePage() {
    const [employees, setEmployees] = useState<EmployeeResponse[]>([]);
    const [branches, setBranches] = useState<BranchResponse[]>([]);
    const [departments, setDepartments] = useState<DepartmentResponse[]>([]);
    const [positions, setPositions] = useState<PositionResponse[]>([]);

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
    const [sortBy, setSortBy] = useState('id');
    const [sortDir, setSortDir] = useState<'asc' | 'desc'>('desc');

    const [open, setOpen] = useState(false);
    const [selectedEmployee, setSelectedEmployee] = useState<EmployeeResponse | null>(null);
    const [deleteId, setDeleteId] = useState<number | null>(null);
    const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);

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
        .then(res=>{
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
        <div className="p-6 space-y-6">
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-bold">Employees</h1>
                    <p className="text-gray-500 text-sm">Manage employees</p>
                </div>
                <button
                    onClick={() => setOpen(true)}
                    className="bg-black text-white px-4 py-2 rounded-xl hover:opacity-80"
                >
                    + Add
                </button>
            </div>

            <div className="flex flex-wrap gap-4">
                <input
                    placeholder="Search by name..."
                    className="flex-1 border rounded-xl px-4 py-2"
                    onChange={(e) => {
                        if (searchTimeoutRef.current) clearTimeout(searchTimeoutRef.current);
                        const value = e.target.value;
                        searchTimeoutRef.current = setTimeout(() => {
                            setKeyword(value);
                            setPageIndex(0);
                        }, 500);
                    }}
                />

                <select
                    value={selectedBranch ?? ''}
                    onChange={(e) => setSelectedBranch(e.target.value ? Number(e.target.value) : undefined)}
                    className="border px-4 py-2 rounded-xl"
                >
                    <option value="">All branches</option>
                    {branches.map(b => <option key={b.id} value={b.id}>{b.name}</option>)}
                </select>

                <select
                    value={selectedDepartment ?? ''}
                    onChange={(e) => setSelectedDepartment(e.target.value ? Number(e.target.value) : undefined)}
                    className="border px-4 py-2 rounded-xl"
                >
                    <option value="">All departments</option>
                    {departments.map(d => <option key={d.id} value={d.id}>{d.name}</option>)}
                </select>

                <select
                    value={selectedPosition ?? ''}
                    onChange={(e) => setSelectedPosition(e.target.value ? Number(e.target.value) : undefined)}
                    className="border px-4 py-2 rounded-xl"
                >
                    <option value="">All positions</option>
                    {positions.map(p => <option key={p.id} value={p.id}>{p.name}</option>)}
                </select>

                <select
                    value={selectedStatus ?? ''}
                    onChange={(e) => setSelectedStatus(e.target.value as EmployeeStatus || undefined)}
                    className="border px-4 py-2 rounded-xl"
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
                    className="border px-4 py-2 rounded-xl"
                    placeholder="Hire date from"
                />

                <input
                    type="date"
                    value={hireDateTo ?? ''}
                    onChange={(e) => setHireDateTo(e.target.value || undefined)}
                    className="border px-4 py-2 rounded-xl"
                    placeholder="Hire date to"
                />
            </div>

            {/* Table */}
            <div className="bg-white rounded-2xl shadow overflow-hidden">
                <table className="w-full">
                    <thead className="bg-gray-100 text-left text-sm">
                        <tr>
                            <th className="p-4">ID</th>
                            <th className="p-4">Name</th>
                            <th className="p-4">Code</th>
                            <th className="p-4">Position</th>
                            <th className="p-4">Department</th>
                            <th className="p-4">Branch</th>
                            <th className="p-4">Status</th>
                            <th className="p-4 text-right">Actions</th>
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
                                <tr key={e.id} className="border-t hover:bg-gray-50">
                                    <td className="p-4">{e.id}</td>
                                    <td className="p-4 font-medium">{e.name}</td>
                                    <td className="p-4">{e.employeeCode}</td>
                                    <td className="p-4">{e.positionName}</td>
                                    <td className="p-4">{e.departmentName}</td>
                                    <td className="p-4">{branches.find(b => b.id === e.departmentId)?.name || ''}</td>
                                    <td className="p-4">{e.status}</td>
                                    <td className="p-4 text-right space-x-2">
                                        <button
                                            onClick={() => { setSelectedEmployee(e); setOpen(true); }}
                                            className="px-3 py-1 border rounded-lg hover:bg-gray-100"
                                        >
                                            Edit
                                        </button>
                                        <button
                                            onClick={() => setDeleteId(e.id)}
                                            className="px-3 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600"
                                        >
                                            Delete
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

            {/* Modal */}
            {/* <EmployeeFormModal
        open={open}
        onClose={() => { setOpen(false); setSelectedEmployee(null); }}
        defaultValues={selectedEmployee || undefined}
        onSubmit={async () => {
          setOpen(false);
          setSelectedEmployee(null);
          fetchEmployees();
        }}
        branches={branches}
        departments={departments}
        positions={positions}
      /> */}

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
                <p>Bạn có chắc muốn xoá không?</p>
            </ConfirmationDialog>
        </div>
    );
}