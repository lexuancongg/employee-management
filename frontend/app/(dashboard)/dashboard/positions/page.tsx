'use client';

import { useState, useEffect, useCallback } from 'react';
import positionService from '@/services/positions/positionService';
import { PositionResponse } from '@/models/positions/positionResponse';
import { PageResponse } from '@/models/page/pageResponse';
import { PositionCreateRequest } from '@/models/positions/positionCreateRequest';


export default function PositionsPage() {
  const [positions, setPositions] = useState<PositionResponse[]>([]);
  const [pageIndex, setPageIndex] = useState(0);
  const pageSize = 10;
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [open, setOpen] = useState(false);
  const [name, setName] = useState('');

  const fetchPositions = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<PositionResponse> =
        await positionService.getPositions();

      setPositions(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error('Fetch error:', err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword]);

  useEffect(() => {
    fetchPositions();
  }, [fetchPositions]);



  const handleCreate = async () => {
    if (!name.trim()) return;

    const position: PositionCreateRequest = {
      name: name
    }

    try {
      await positionService.createPosition(position);
      setOpen(false);
      setName('');
      fetchPositions();
    } catch (e) {
      console.error(e);
    }
  };


  const handleDelete = async (id: number) => {
    positionService.deletePosition(id)
      .then(res => {
        if (res.ok) {
          fetchPositions();
        }
      })
      .catch((err)=>{
        console.log(err)
      })
  }

  return (
    <div className="p-6 space-y-6">
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">Positions</h1>
          <p className="text-gray-500 text-sm">Manage positions</p>
        </div>
        <button
          onClick={() => setOpen(true)}
          className="bg-black text-white px-4 py-2 rounded-xl hover:opacity-80"
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
        className="w-full border rounded-xl px-4 py-2 focus:outline-none focus:ring-2 focus:ring-black"
      />

      {/* Table */}
      <div className="bg-white rounded-2xl shadow overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100 text-left text-sm">
            <tr>
              <th className="p-4">ID</th>
              <th className="p-4">Name</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan={3} className="p-6 text-center text-gray-500">
                  Loading...
                </td>
              </tr>
            ) : positions.length === 0 ? (
              <tr>
                <td colSpan={3} className="p-6 text-center text-gray-400">
                  No data
                </td>
              </tr>
            ) : (
              positions.map((p) => (
                <tr key={p.id} className="border-t hover:bg-gray-50">
                  <td className="p-4">{p.id}</td>
                  <td className="p-4 font-medium">{p.name}</td>
                  <td className="p-4 text-right space-x-2">
                    <button
                      className="px-3 py-1 border rounded-lg hover:bg-gray-100">
                      Edit
                    </button>
                    <button
                      onClick={() => handleDelete(p.id)}
                      className="px-3 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600">
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
      <div className="flex justify-between items-center">
        <button
          disabled={pageIndex === 0}
          onClick={() => setPageIndex((prev) => prev - 1)}
          className="px-4 py-2 border rounded-xl disabled:opacity-50"
        >
          Prev
        </button>

        <span className="text-sm text-gray-600">
          Page {pageIndex + 1} / {totalPages}
        </span>

        <button
          disabled={pageIndex + 1 >= totalPages}
          onClick={() => setPageIndex((prev) => prev + 1)}
          className="px-4 py-2 border rounded-xl disabled:opacity-50"
        >
          Next
        </button>
      </div>

      {/* Modal Create */}
      {open && (
        <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
          <div className="bg-white w-[400px] rounded-2xl p-6 space-y-4 shadow-lg">
            <h2 className="text-xl font-semibold">Create Position</h2>

            <input
              value={name}
              onChange={(e) => setName(e.target.value)}
              placeholder="Enter position name..."
              className="w-full border px-4 py-2 rounded-xl focus:outline-none focus:ring-2 focus:ring-black"
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