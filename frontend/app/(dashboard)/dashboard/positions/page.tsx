'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import { PlusIcon, PencilIcon, TrashIcon } from '@heroicons/react/24/outline';
import positionService from '@/services/positions/positionService';
import { PositionResponse } from '@/models/positions/positionResponse';
import { PageResponse } from '@/models/page/pageResponse';
import { PositionCreateRequest } from '@/models/positions/positionCreateRequest';
import Pagination from '@/components/pagination/pagination';
import PositionFormModal from '@/components/position/PositionFormModal';
import ConfirmationDialog from '@/components/dialog/confirmDialog';

export default function PositionsPage() {
  const [positions, setPositions] = useState<PositionResponse[]>([]);
  const [pageIndex, setPageIndex] = useState(0);
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);
  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<PositionResponse | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);
  const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const fetchPositions = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<PositionResponse> = await positionService.getPositions(pageIndex, keyword);
      setPositions(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error('Fetch error:', err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword]);

  useEffect(() => { fetchPositions(); }, [fetchPositions]);

  const handleDelete = async (id: number) => {
    try {
      await positionService.deletePosition(id);
      fetchPositions();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6 space-y-6 bg-gray-50 min-h-screen">

      {/* Header */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900">Positions</h1>
          <p className="text-gray-500 text-sm mt-1">Manage positions</p>
        </div>
        <button
          onClick={() => setOpen(true)}
          className="inline-flex items-center gap-2 bg-gradient-to-r from-indigo-600 to-indigo-700 text-white px-5 py-2.5 rounded-xl shadow-md hover:shadow-lg hover:opacity-90 transition text-sm font-semibold"
        >
          <PlusIcon className="w-4 h-4" />
          Add Position
        </button>
      </div>

      {/* Search */}
      <div className="relative max-w-sm">
        <input
          type="text"
          placeholder="Search positions..."
          className="w-full border border-gray-200 rounded-xl px-4 py-2.5 text-sm bg-white focus:outline-none focus:ring-2 focus:ring-indigo-300 focus:border-indigo-400 transition"
          onChange={(e) => {
            const value = e.target.value;
            if (searchTimeoutRef.current) clearTimeout(searchTimeoutRef.current);
            searchTimeoutRef.current = setTimeout(() => {
              setKeyword(value);
              setPageIndex(0);
            }, 500);
          }}
        />
      </div>

      {/* Table */}
      <div className="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
        <table className="w-full text-sm text-gray-700">
          <thead className="bg-gray-50 text-left text-xs uppercase tracking-wider text-gray-500">
            <tr>
              <th className="px-6 py-3.5 font-semibold">#</th>
              <th className="px-6 py-3.5 font-semibold">Position Name</th>
              <th className="px-6 py-3.5 text-right font-semibold">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? (
              <tr>
                <td colSpan={3} className="px-6 py-12 text-center text-gray-400">Loading...</td>
              </tr>
            ) : positions.length === 0 ? (
              <tr>
                <td colSpan={3} className="px-6 py-12 text-center text-gray-400">No positions found</td>
              </tr>
            ) : (
              positions.map((p, idx) => (
                <tr key={p.id} className="hover:bg-indigo-50/40 transition-colors">
                  <td className="px-6 py-4 text-gray-400">{pageIndex * 10 + idx + 1}</td>
                  <td className="px-6 py-4 font-medium">{p.name}</td>
                  <td className="px-6 py-4 text-right flex justify-end gap-2">
                    <button
                      onClick={() => { setSelected(p); setOpen(true); }}
                      className="w-8 h-8 flex items-center justify-center border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-300 transition"
                      title="Edit"
                    >
                      <PencilIcon className="w-4 h-4 text-indigo-600" />
                    </button>
                    <button
                      onClick={() => setDeleteId(p.id)}
                      className="w-8 h-8 flex items-center justify-center border border-gray-200 rounded-lg hover:bg-red-50 hover:border-red-300 transition"
                      title="Delete"
                    >
                      <TrashIcon className="w-4 h-4 text-red-500" />
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

      {/* Form Modal */}
      <PositionFormModal
        open={open}
        onClose={() => { setOpen(false); setSelected(null); }}
        onSubmit={async (position: PositionCreateRequest) => {
          if (selected) {
            await positionService.updatePosition(selected.id, position);
          } else {
            await positionService.createPosition(position);
          }
          setOpen(false);
          setSelected(null);
          fetchPositions();
        }}
        defaultValues={selected ? { name: selected.name } : undefined}
      />

      {/* Confirm Delete */}
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