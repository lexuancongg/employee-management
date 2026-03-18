'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import { PageResponse } from '@/models/page/pageResponse';
import provinceService from '@/services/province/provinceService';
import countryService from '@/services/country/countryService';
import { CountryResponse } from '@/models/country/countryResponse';
import dynamic from 'next/dynamic';

const ReactSelect = dynamic(() => import('react-select'), { ssr: false });


import Pagination from '@/components/pagination/pagination';
import ProvinceFormModal from '@/components/province/provinceFormModal';
import ConfirmationDialog from '@/components/dialog/confirmDialog';

export default function ProvincePage() {
  const [provinces, setProvinces] = useState<ProvinceResponse[]>([]);
  const [countries, setCountries] = useState<CountryResponse[]>([]);
  const [countryId, setCountryId] = useState<number | undefined>(undefined);


  const [pageIndex, setPageIndex] = useState(0);
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<ProvinceResponse | null>(null);
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

  useEffect(() => {
    fetchCountries();

  }, []);

  // fetch provinces
  const fetchProvinces = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<ProvinceResponse> =
        await provinceService.getProvinces(pageIndex, keyword, countryId);

      setProvinces(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword, countryId]);

  useEffect(() => {
    fetchProvinces();
  }, [fetchProvinces]);

  // delete
  const handleDelete = async (id: number) => {
    try {
      const res = await provinceService.deleteProvince(id);
      fetchProvinces();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">Province</h1>
          <p className="text-gray-500 text-sm">Manage provinces</p>
        </div>

        <button
          onClick={() => setOpen(true)}
          className="bg-black text-white px-4 py-2 rounded-xl hover:opacity-80"
        >
          + Add
        </button>
      </div>

      {/* Search (debounce) */}
      <div className="flex gap-4">
        {/* Search */}
        <input
          onChange={(e) => {
            const value = e.target.value;

            if (searchTimeoutRef.current) {
              clearTimeout(searchTimeoutRef.current);
            }

            searchTimeoutRef.current = setTimeout(() => {
              setKeyword(value);
              setPageIndex(0);
            }, 500);
          }}
          placeholder="Search..."
          className="flex-1 border px-4 py-2 rounded-xl"
        />

        {/* Filter country */}
        <select
          value={countryId ?? ''}
          onChange={(e) => {
            const value = e.target.value;
            setCountryId(value === '' ? undefined : Number(value));
            setPageIndex(0);
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

        <ReactSelect
          options={countries.map(c => ({ value: c.id, label: c.name }))}
          value={countries.find(c => c.id === countryId) ? { value: countryId, label: countries.find(c => c.id === countryId)?.name } : null}
          onChange={(selectedOption) => {
            setCountryId(selectedOption ? selectedOption.value : undefined);
            setPageIndex(0);
          }}
          isClearable
          placeholder="Select country..."
        />
      </div>

      {/* Table */}
      <div className="bg-white rounded-2xl shadow overflow-hidden">
        <table className="w-full">
          <thead className="bg-gray-100 text-left text-sm">
            <tr>
              <th className="p-4">ID</th>
              <th className="p-4">Name</th>
              <th className="p-4">Country</th>
              <th className="p-4">Type</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>

          <tbody>
            {loading ? (
              <tr>
                <td colSpan={5} className="p-6 text-center text-gray-500">
                  Loading...
                </td>
              </tr>
            ) : provinces.length === 0 ? (
              <tr>
                <td colSpan={5} className="p-6 text-center text-gray-400">
                  No data
                </td>
              </tr>
            ) : (
              provinces.map((p) => (
                <tr key={p.id} className="border-t hover:bg-gray-50">
                  <td className="p-4">{p.id}</td>
                  <td className="p-4 font-medium">{p.name}</td>
                  <td className="p-4">{p.countryName}</td>
                  <td className="p-4">{p.type}</td>

                  <td className="p-4 text-right space-x-2">
                    <button
                      onClick={() => {
                        setSelected(p);
                        setOpen(true);
                      }}
                      className="px-3 py-1 border rounded-lg hover:bg-gray-100"
                    >
                      Edit
                    </button>

                    <button
                      onClick={() => setDeleteId(p.id)}
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
        onPrev={() => setPageIndex((prev) => prev - 1)}
        onNext={() => setPageIndex((prev) => prev + 1)}
      />

      <ProvinceFormModal
        open={open}
        countries={countries}
        onClose={() => {
          setOpen(false);
          setSelected(null);
        }}
        onSubmit={async (province: ProvinceCreateRequest) => {
          if (selected) {
            await provinceService.updateProvince(selected.id, province);
          } else {
            await provinceService.createProvince(province);
          }

          setOpen(false);
          setSelected(null);
          fetchProvinces();
        }}
        defaultValues={
          selected
            ? selected
            : undefined
        }
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
        <p>Bạn có chắc muốn xoá không?</p>
      </ConfirmationDialog>
    </div>
  );
}