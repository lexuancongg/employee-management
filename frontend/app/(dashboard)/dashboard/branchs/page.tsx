'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import { Pencil, Trash2 } from 'lucide-react';
import { BranchCreateRequest, BranchField, BranchResponse } from '@/models/branch/branchResponse';
import { PageResponse } from '@/models/page/pageResponse';
import branchService from '@/services/branch/branchService';
import countryService from '@/services/country/countryService';
import provinceService from '@/services/province/provinceService';
import { CountryResponse } from '@/models/country/countryResponse';
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
  const [selectedCountry, setSelectedCountry] = useState<number | undefined>(undefined);
  const [selectedProvince, setSelectedProvince] = useState<number | undefined>(undefined);

  const [open, setOpen] = useState(false);
  const [selectedBranch, setSelectedBranch] = useState<BranchResponse | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    const fetchCountries = async () => {
      try {
        const res = await countryService.getCountries();
        setCountries(res);
      } catch (err) {
        console.error(err);
      }
    };
    fetchCountries();
  }, []);

  useEffect(() => {
    if (!selectedCountry) {
      setProvinces([]);
      setSelectedProvince(undefined);
      return;
    }
    const fetchProvinces = async () => {
      try {
        const res = await provinceService.getProvinces(0, '', selectedCountry);
        setProvinces(res.content);
        setSelectedProvince(undefined);
      } catch (err) {
        console.error(err);
      }
    };
    fetchProvinces();
  }, [selectedCountry]);


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
    fetchBranches();
  }, [fetchBranches]);



  // delete
  const handleDelete = async (id: number) => {
    try {
      await branchService.deleteBranch(id);
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
          className="bg-black text-white px-4 py-2 rounded-xl hover:opacity-80"
        >
          + Add
        </button>
      </div>

      {/* Filters */}
      <div className="flex gap-4">
        <input
          value={keyword}
          onChange={(e) => {
            setPageIndex(0);
            setKeyword(e.target.value);
          }}
          placeholder="Search branch..."
          className="flex-1 border px-4 py-2 rounded-xl"
        />

        <select
          value={selectedCountry ?? ''}
          onChange={(e) => {
            setPageIndex(0);
            setSelectedCountry(e.target.value ? Number(e.target.value) : undefined);
          }}
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
          onChange={(e) => {
            setPageIndex(0);
            setSelectedProvince(e.target.value ? Number(e.target.value) : undefined);
          }}
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
      <div className="bg-white rounded-2xl shadow overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100 text-left text-sm">
            <tr>
              <th className="p-4">ID</th>
              <th className="p-4">Name</th>
              <th className="p-4">Address</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>
          <tbody>
            {loading ? (
              <tr>
                <td colSpan={4} className="p-6 text-center text-gray-500">
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
                <tr key={b.id} className="border-t hover:bg-gray-50">
                  <td className="p-4">{b.id}</td>
                  <td className="p-4">{b.name}</td>
                  <td className="p-4">
                    {b.address
                      ? `${b.address.specificAddress} - ${b.address.districtName} - ${b.address.provinceName} - ${b.address.countryName}`
                      : ''}
                  </td>
                  <td className="p-4 text-right space-x-2">
                    <button
                      onClick={() => {
                        setSelectedBranch(b);
                        setOpen(true);
                      }}
                      className="px-3 py-1 border rounded-lg hover:bg-gray-100"
                    >
                      <Pencil size={18} />
                    </button>
                    <button
                      onClick={() => setDeleteId(b.id)}
                      className="px-3 py-1 bg-red-500 text-white rounded-lg hover:bg-red-600"
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
      <Pagination
        pageIndex={pageIndex}
        totalPages={totalPages}
        onPrev={() => setPageIndex((p) => p - 1)}
        onNext={() => setPageIndex((p) => p + 1)}
      />

      <BranchFormModal
        open={open}
        defaultValues={selectedBranch ? selectedBranch : undefined}
        onClose={
          () => {
            setOpen(false)
            setSelectedBranch(null)
          }
        }
        onSubmit={async (branchField: BranchField) => {
          const branch: BranchCreateRequest = {
            name: branchField.name,
            address: {
              ...branchField,
            }
          }
          if (selectedBranch) {
            await branchService.updateBranch(branch, selectedBranch.id)
            setOpen(false)
            setSelectedBranch(null)
            fetchBranches();

          } else {

            await branchService.createBranch(branch)
            setOpen(false)
            setSelectedBranch(null)
            fetchBranches();
          }
        }}

      ></BranchFormModal>


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