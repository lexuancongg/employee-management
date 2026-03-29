'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import {
  MagnifyingGlassIcon,
  PlusIcon,
  PencilIcon,
  TrashIcon,
  MapPinIcon,
} from '@heroicons/react/24/outline';

import branchService from '@/services/branch/branchService';
import countryService from '@/services/country/countryService';
import provinceService from '@/services/province/provinceService';

import {
  BranchResponse,
  BranchCreateRequest,
  BranchField,
} from '@/models/branch/branchResponse';
import { CountryResponse } from '@/models/country/countryResponse';
import { PageResponse } from '@/models/page/pageResponse';

import Pagination from '@/components/pagination/pagination';
import BranchFormModal from '@/components/branch/branchFormModal';
import ConfirmationDialog from '@/components/dialog/confirmDialog';

export default function BranchPage() {
  const [branches, setBranches] = useState<BranchResponse[]>([]);
  const [pageIndex, setPageIndex] = useState(0);
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [countries, setCountries] = useState<CountryResponse[]>([]);
  const [provinces, setProvinces] = useState<ProvinceResponse[]>([]);
  const [selectedCountry, setSelectedCountry] = useState<number | undefined>();
  const [selectedProvince, setSelectedProvince] = useState<number | undefined>();

  const [open, setOpen] = useState(false);
  const [selectedBranch, setSelectedBranch] = useState<BranchResponse | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const fetchCountries = useCallback(async () => {
    try {
      const res = await countryService.getCountries();
      setCountries(res);
    } catch (err) {
      console.error(err);
    }
  }, []);

  const fetchProvinces = useCallback(async (countryId?: number) => {
    if (!countryId) {
      setProvinces([]);
      setSelectedProvince(undefined);
      return;
    }
    try {
      const res = await provinceService.getProvinces(0, '', countryId);
      setProvinces(res.content);
      setSelectedProvince(undefined);
    } catch (err) {
      console.error(err);
    }
  }, []);

  const fetchBranches = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<BranchResponse> = await branchService.getBranches(
        pageIndex,
        keyword,
        selectedCountry,
        selectedProvince
      );
      setBranches(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword, selectedCountry, selectedProvince]);

  useEffect(() => {
    fetchCountries();
  }, [fetchCountries]);

  useEffect(() => {
    fetchProvinces(selectedCountry);
  }, [selectedCountry, fetchProvinces]);

  useEffect(() => {
    fetchBranches();
  }, [fetchBranches]);

  const handleSearchChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value;
    if (searchTimeoutRef.current) {
      clearTimeout(searchTimeoutRef.current)
    };

    searchTimeoutRef.current = setTimeout(() => {
      setKeyword(value);
      setPageIndex(0);
    }, 500);
  };

  const handleOpenCreate = () => {
    setSelectedBranch(null);
    setOpen(true);
  };
  const handleOpenEdit = (branch: BranchResponse) => {
    setSelectedBranch(branch);
    setOpen(true);
  };
  const handleCloseModal = () => {
    setSelectedBranch(null);
    setOpen(false);
  };
  const handleSubmitBranch = async (branchField: BranchField) => {
    const branch: BranchCreateRequest = { name: branchField.name, address: { ...branchField } };
    if (selectedBranch) await branchService.updateBranch(branch, selectedBranch.id);
    else await branchService.createBranch(branch);

    handleCloseModal();
    fetchBranches();
  };

  const handleDelete = async (id: number) => {
    try {
      await branchService.deleteBranch(id);
      fetchBranches();
    } catch (err) {
      console.error(err);
    }
  };
  const handleDeleteClick = (id: number) => setDeleteId(id);
  const handleConfirmDelete = async () => {
    if (deleteId === null) return;
    await handleDelete(deleteId);
    setDeleteId(null);
  };
  const handleCancelDelete = () => setDeleteId(null);

  // ---------------- RENDER ----------------
  return (
    <div className="p-6 space-y-6 bg-gray-50 min-h-screen">
      {/* Header */}
      <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
        <div>
          <h1 className="text-3xl font-extrabold text-gray-900 tracking-tight">Branches</h1>
          <p className="text-gray-500 text-sm mt-1">Manage branch data</p>
        </div>
        <button
          onClick={handleOpenCreate}
          className="inline-flex items-center gap-2 bg-gradient-to-r from-indigo-600 to-indigo-700 text-white px-5 py-2.5 rounded-xl shadow-md hover:shadow-lg hover:opacity-90 transition text-sm font-semibold"
        >
          <PlusIcon className="w-4 h-4" />
          Add Branch
        </button>
      </div>

      {/* Filters */}
      <div className="flex flex-col sm:flex-row gap-3 sm:gap-4 items-start sm:items-center">
        <div className="relative max-w-sm w-full">
          <MagnifyingGlassIcon className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
          <input
            value={keyword}
            onChange={handleSearchChange}
            placeholder="Search branch..."
            className="w-full pl-10 pr-4 py-2.5 border border-gray-200 rounded-xl text-sm bg-white focus:outline-none focus:ring-2 focus:ring-indigo-300 focus:border-indigo-400 transition"
          />
        </div>

        <select
          value={selectedCountry ?? ''}
          onChange={(e) => setSelectedCountry(e.target.value ? Number(e.target.value) : undefined)}
          className="border px-4 py-2 rounded-xl"
        >
          <option value="">All countries</option>
          {countries.map((c) => (
            <option key={c.id} value={c.id}>
              {c.name}
            </option>
          ))}
        </select>

        <select
          value={selectedProvince ?? ''}
          onChange={(e) => setSelectedProvince(e.target.value ? Number(e.target.value) : undefined)}
          disabled={!selectedCountry}
          className="border px-4 py-2 rounded-xl"
        >
          <option value="">All provinces</option>
          {provinces.map((p) => (
            <option key={p.id} value={p.id}>
              {p.name}
            </option>
          ))}
        </select>
      </div>

      {/* Table */}
      <div className="bg-white rounded-2xl border border-gray-200 shadow-sm overflow-hidden">
        <table className="w-full text-sm text-gray-700">
          <thead>
            <tr className="bg-gray-50 border-b border-gray-200 text-xs uppercase tracking-wider text-gray-500">
              <th className="px-6 py-3.5 text-left font-semibold">#</th>
              <th className="px-6 py-3.5 text-left font-semibold">Branch Name</th>
              <th className="px-6 py-3.5 text-left font-semibold">Address</th>
              <th className="px-6 py-3.5 text-right font-semibold">Actions</th>
            </tr>
          </thead>
          <tbody className="divide-y divide-gray-100">
            {loading ? (
              <tr>
                <td colSpan={4} className="px-6 py-12 text-center">
                  <div className="flex items-center justify-center gap-2 text-gray-400">
                    <div className="w-4 h-4 border-2 border-indigo-400 border-t-transparent rounded-full animate-spin" />
                    Loading...
                  </div>
                </td>
              </tr>
            ) : branches.length === 0 ? (
              <tr>
                <td colSpan={4} className="px-6 py-16 text-center">
                  <MapPinIcon className="w-10 h-10 mx-auto text-gray-200 mb-3" />
                  <p className="text-gray-400 text-sm">No branches found</p>
                </td>
              </tr>
            ) : (
              branches.map((b, idx) => (
                <tr key={b.id} className="hover:bg-indigo-50/40 transition-colors group">
                  <td className="px-6 py-4 text-gray-400 text-xs">{pageIndex * 10 + idx + 1}</td>
                  <td className="px-6 py-4 font-medium text-gray-800">{b.name}</td>
                  <td className="px-6 py-4">
                    {b.address
                      ? `${b.address.specificAddress} - ${b.address.districtName} - ${b.address.provinceName} - ${b.address.countryName}`
                      : ''}
                  </td>
                  <td className="px-6 py-4 text-right flex gap-2 justify-end">
                    <button
                      onClick={() => handleOpenEdit(b)}
                      className="w-8 h-8 flex items-center justify-center border border-gray-200 rounded-lg hover:bg-indigo-50 hover:border-indigo-300 transition"
                    >
                      <PencilIcon className="w-4 h-4 text-indigo-600" />
                    </button>
                    <button
                      onClick={() => handleDeleteClick(b.id)}
                      className="w-8 h-8 flex items-center justify-center border border-gray-200 rounded-lg hover:bg-red-50 hover:border-red-300 transition"
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

      <Pagination
        pageIndex={pageIndex}
        totalPages={totalPages}
        onPrev={() => setPageIndex((p) => p - 1)}
        onNext={() => setPageIndex((p) => p + 1)}
      />

      <BranchFormModal
        open={open}
        defaultValues={selectedBranch ?? undefined}
        onClose={handleCloseModal}
        onSubmit={handleSubmitBranch}
      />

      <ConfirmationDialog
        isShow={deleteId !== null}
        title="Confirm delete"
        okText="Delete"
        cancelText="Cancel"
        ok={handleConfirmDelete}
        cancel={handleCancelDelete}
      >
        <p>Bạn có chắc muốn xoá không?</p>
      </ConfirmationDialog>
    </div>
  );
}