'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import countryService from '@/services/country/countryService';
import { CountryResponse, CountryCreateRequest } from '@/models/country/countryResponse';
import { PageResponse } from '@/models/page/pageResponse';
import Pagination from '@/components/pagination/pagination';
import ConfirmationDialog from '@/components/dialog/confirmDialog';
import CountryFormModal from '@/components/country/countryFormModel';

export default function CountryPage() {
  const [countries, setCountries] = useState<CountryResponse[]>([]);
  const [pageIndex, setPageIndex] = useState(0);
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [open, setOpen] = useState(false);
  const [selected, setSelected] = useState<CountryResponse | null>(null);
  const [deleteId, setDeleteId] = useState<number | null>(null);

  const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);

  const fetchCountries = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<CountryResponse> =
        await countryService.getCountriesPaging(pageIndex, keyword);

      setCountries(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error('Fetch error:', err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword]);

  useEffect(() => {
    fetchCountries();
  }, [fetchCountries]);

  const handleDelete = async (id: number) => {
    try {
      const res = await countryService.delete(id);
      fetchCountries();
    } catch (err) {
      console.error(err);
    }
  };

  return (
    <div className="p-6 space-y-6">
      {/* Header */}
      <div className="flex justify-between items-center">
        <div>
          <h1 className="text-2xl font-bold">Countries</h1>
          <p className="text-gray-500 text-sm">Manage countries</p>
        </div>

        <button
          onClick={() => setOpen(true)}
          className="bg-black text-white px-4 py-2 rounded-xl hover:opacity-80"
        >
          + Add
        </button>
      </div>

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
            ) : countries.length === 0 ? (
              <tr>
                <td colSpan={3} className="p-6 text-center text-gray-400">
                  No data
                </td>
              </tr>
            ) : (
              countries.map((c) => (
                <tr key={c.id} className="border-t hover:bg-gray-50">
                  <td className="p-4">{c.id}</td>
                  <td className="p-4 font-medium">{c.name}</td>

                  <td className="p-4 text-right space-x-2">
                    <button
                      onClick={() => {
                        setSelected(c);
                        setOpen(true);
                      }}
                      className="px-3 py-1 border rounded-lg hover:bg-gray-100"
                    >
                      Edit
                    </button>

                    <button
                      onClick={() => setDeleteId(c.id)}
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

      {/* Form modal */}
      <CountryFormModal
        open={open}
        onClose={() => {
          setOpen(false);
          setSelected(null);
        }}
        onSubmit={async (country: CountryCreateRequest) => {
          if (selected) {
            await countryService.updateCountry(selected.id, country);
          } else {
            await countryService.createCountry(country);
          }

          setOpen(false);
          setSelected(null);
          fetchCountries();
        }}
        defaultValues={selected ? { name: selected.name } : undefined}
      />

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