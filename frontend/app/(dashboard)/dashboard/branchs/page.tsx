'use client';

import { useState, useEffect, useCallback } from 'react';
import { Pencil, Trash2 } from 'lucide-react';
import { BranchResponse } from '@/models/branch/branchResponse';
import { PageResponse } from '@/models/page/pageResponse';
import branchService from '@/services/branch/branchService';


export default function BranchPage() {
  const [branches, setBranches] = useState<BranchResponse[]>([]);
  const [pageIndex, setPageIndex] = useState(0);
  const pageSize = 10;
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [open, setOpen] = useState(false);
  const [name, setName] = useState('');
  const [address, setAddress] = useState('');

  // fetch data
  const fetchBranches = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<BranchResponse> =
        await branchService.getBranches();

      setBranches(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword]);

  // load lần đầu + pagination
  useEffect(() => {
    fetchBranches();
  }, [pageIndex]);

  // debounce search
  useEffect(() => {
    const timeout = setTimeout(() => {
      fetchBranches();
    }, 500);

    return () => clearTimeout(timeout);
  }, [keyword]);

  // create
  const handleCreate = async () => {

  }
    

  // delete
  const handleDelete = async (id: number) => {
    try {
    //   await branchService.deleteBranch(id);
      fetchBranches();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">Branch</h1>
          <p className="text-gray-500 text-sm">Manage branches</p>
        </div>

        <button
          onClick={() => setOpen(true)}
          className="bg-black text-white px-4 py-2 rounded-xl"
        >
          + Add
        </button>
      </div>

      {/* Search */}
      <input
        value={keyword}
        onChange={(e) => {
          setPageIndex(0);
          setKeyword(e.target.value);
        }}
        placeholder="Search..."
        className="w-full border px-4 py-2 rounded-xl"
      />

      {/* Table */}
      <div className="bg-white rounded-2xl shadow overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100">
            <tr>
              <th className="p-4 text-left">ID</th>
              <th className="p-4 text-left">Name</th>
              <th className="p-4 text-left">Address</th>
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
            ) : branches.length === 0 ? (
              <tr>
                <td colSpan={4} className="p-6 text-center text-gray-400">
                  No data
                </td>
              </tr>
            ) : (
              branches.map((b) => (
                <tr key={b.id} className="border-t">
                  <td className="p-4">{b.id}</td>
                  <td className="p-4">{b.name}</td>
                  <td className="p-4">
                    {b.address?.specificAddress}
                  </td>

                  <td className="p-4 text-right space-x-2">
                    <button className="p-2 hover:bg-gray-100 rounded-lg">
                      <Pencil size={18} />
                    </button>

                    <button
                      onClick={() => handleDelete(b.id)}
                      className="p-2 hover:bg-red-100 text-red-500 rounded-lg"
                    >
                      <Trash2 size={18} />
                    </button>
                  </td>
                </tr>
              ))
            )}
          </tbody>
        </table>
      </div>

      {/* Pagination */}
      <div className="flex justify-between">
        <button
          disabled={pageIndex === 0}
          onClick={() => setPageIndex((p) => p - 1)}
          className="px-4 py-2 border rounded-xl"
        >
          Prev
        </button>

        <span>
          {pageIndex + 1} / {totalPages}
        </span>

        <button
          disabled={pageIndex + 1 >= totalPages}
          onClick={() => setPageIndex((p) => p + 1)}
          className="px-4 py-2 border rounded-xl"
        >
          Next
        </button>
      </div>

      {/* Modal */}
      {open && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center">
          <div className="bg-white p-6 rounded-2xl w-[400px] space-y-4">
            <h2 className="text-xl font-semibold">Create Branch</h2>

            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Branch name"
              className="w-full border px-4 py-2 rounded-xl"
            />

            <input
              value={address}
              onChange={(e) => setAddress(e.target.value)}
              placeholder="Specific address"
              className="w-full border px-4 py-2 rounded-xl"
            />

            <div className="flex justify-end gap-2">
              <button
                onClick={() => setOpen(false)}
                className="px-4 py-2 border rounded-xl"
              >
                Cancel
              </button>

              <button
                onClick={handleCreate}
                className="bg-black text-white px-4 py-2 rounded-xl"
              >
                Create
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}