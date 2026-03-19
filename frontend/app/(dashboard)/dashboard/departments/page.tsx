'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import { PageResponse } from '@/models/page/pageResponse';
import Pagination from '@/components/pagination/pagination';
import ConfirmationDialog from '@/components/dialog/confirmDialog';
import branchService from '@/services/branch/branchService';
import { DepartmentResponse } from '@/models/department/department';
import departmentService from '@/services/department/departmentService';

type Option = {
  id: number;
  name: string;
};

export default function DepartmentsPage() {
  const [departments, setDepartments] = useState<DepartmentResponse[]>([]);
  const [branches, setBranches] = useState<Option[]>([]);
  const [selectedBranch, setSelectedBranch] = useState<number | undefined>(undefined);

  const [pageIndex, setPageIndex] = useState(0);
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<DepartmentResponse | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  // fetch departments
  const fetchDepartments = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<DepartmentResponse> =
        await departmentService.getDepartments(pageIndex, keyword, selectedBranch);

      setDepartments(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword, selectedBranch]);

  useEffect(() => {
    fetchDepartments();
  }, [fetchDepartments]);

  // fetch branches
  useEffect(() => {
    const fetchBranches = async () => {
      try {
        const res = await branchService.getAllBranch();
        setBranches(res);
      } catch (err) {
        console.error(err);
      }
    };
    fetchBranches();
  }, []);

  const handleDelete = async (id: number) => {
    try {
    //   const res = await departmentService.deleteDepartment(id);
    //   if (res.ok) fetchDepartments();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">Departments</h1>
          <p className="text-gray-500 text-sm">Manage departments</p>
        </div>
        <button
          onClick={() => setOpen(true)}
          className="bg-black text-white px-4 py-2 rounded-xl"
        >
          + Add
        </button>
      </div>

      {/* Filter */}
      <div className="flex gap-4">
        {/* search */}
        <input
          placeholder="Search..."
          className="w-full border rounded-xl px-4 py-2"
          onChange={(e) => {
            const value = e.target.value;
            if (searchTimeoutRef.current) clearTimeout(searchTimeoutRef.current);

            searchTimeoutRef.current = setTimeout(() => {
              setKeyword(value);
              setPageIndex(0);
            }, 500);
          }}
        />

        {/* branch dropdown */}
        <select
          value={selectedBranch ?? ''}
          onChange={(e) => {
            setPageIndex(0);
            setSelectedBranch(e.target.value ? Number(e.target.value) : undefined);
          }}
          className="border px-4 py-2 rounded-xl"
        >
          <option value="">All branches</option>
          {branches.map((b) => (
            <option key={b.id} value={b.id}>
              {b.name}
            </option>
          ))}
        </select>
      </div>

      {/* Table */}
      <div className="bg-white rounded-2xl shadow overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100 text-sm">
            <tr>
              <th className="p-4">ID</th>
              <th className="p-4">Name</th>
              <th className="p-4">Branch</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>

          <tbody>
            {loading ? (
              <tr>
                <td colSpan={4} className="p-6 text-center">
                  Loading...
                </td>
              </tr>
            ) : departments.length === 0 ? (
              <tr>
                <td colSpan={4} className="p-6 text-center">
                  No data
                </td>
              </tr>
            ) : (
              departments.map((d) => (
                <tr key={d.id} className="border-t hover:bg-gray-50">
                  <td className="p-4">{d.id}</td>
                  <td className="p-4 font-medium">{d.name}</td>
                  <td className="p-4">{d.branchName}</td>

                  <td className="p-4 text-right space-x-2">
                    <button
                      onClick={() => {
                        setSelected(d);
                        setOpen(true);
                      }}
                      className="px-3 py-1 border rounded-lg"
                    >
                      Edit
                    </button>

                    <button
                      onClick={() => setDeleteId(d.id)}
                      className="px-3 py-1 bg-red-500 text-white rounded-lg"
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
        onPrev={() => setPageIndex((prev) => prev - 1)}
        onNext={() => setPageIndex((prev) => prev + 1)}
      />

      {/* Modal */}
      {/* <DepartmentFormModal
        open={open}
        onClose={() => {
          setOpen(false);
          setSelected(null);
        }}
        onSubmit={async (data: DepartmentCreateRequest) => {
          if (selected) {
            await departmentService.updateDepartment(selected.id, data);
          } else {
            await departmentService.createDepartment(data);
          }
          setOpen(false);
          setSelected(null);
          fetchDepartments();
        }}
        defaultValues={
          selected
            ? {
                name: selected.name,
              }
            : undefined
        }
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