'use client';

import { useState, useEffect, useCallback } from 'react';
import { Pencil, Trash2 } from 'lucide-react';
import { PageResponse } from '@/models/page/pageResponse';
import provinceService from '@/services/province/provinceService';
import { CountryResponse } from '@/models/country/countryResponse';
import countryService from '@/services/country/countryService';
import OptionSelect from '@/components/item/optionSelect';
import { useForm } from 'react-hook-form';

export default function ProvincePage() {
  const [provinces, setProvinces] = useState<ProvinceResponse[]>([]);
  const [countries, setCountries] = useState<CountryResponse[]>([]);
  const { register, handleSubmit, formState: { errors },reset  } = useForm<ProvinceCreateRequest>();

  
  const [pageIndex, setPageIndex] = useState(0);
  const pageSize = 10;
  const [keyword, setKeyword] = useState('');
  const [totalPages, setTotalPages] = useState(0);
  const [loading, setLoading] = useState(true);

  const [open, setOpen] = useState(false);


  const fetchCountries = useCallback(async () => {
      setLoading(true);
      try {
        const res: CountryResponse[] =
          await countryService.getCountries();
          setCountries(res)
  
      } catch (err) {
        console.error(err);
      }
    }, []);
  

    useEffect(()=>{
        fetchCountries();
    },[])

  const fetchProvinces = useCallback(async () => {
    setLoading(true);
    try {
      const res: PageResponse<ProvinceResponse> =
        await provinceService.getProvinces();

      setProvinces(res.content);
      setTotalPages(res.totalPages);
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, [pageIndex, keyword]);

  useEffect(() => {
    fetchProvinces();
  }, [pageIndex]);

  useEffect(() => {
    const timeout = setTimeout(() => {
      fetchProvinces();
    }, 500);

    return () => clearTimeout(timeout);
  }, [keyword]);

  const handleCreate = async (province:ProvinceCreateRequest) => {

    try {
      await provinceService.createProvince(province);
      setOpen(false);
      fetchProvinces();
      reset()
    } catch (err) {
      console.error(err);
    }
  };

  // delete
  const handleDelete = async (id: number) => {
    // try {
    //   await provinceService.deleteProvince(id);
    //   fetchProvinces();
    // } catch (err) {
    //   console.error(err);
    // }
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
              <th className="p-4 text-left">CountryId</th>
              <th className="p-4 text-left">Type</th>
              <th className="p-4 text-right">Actions</th>
            </tr>
          </thead>

          <tbody>
            {loading ? (
              <tr>
                <td colSpan={5} className="p-6 text-center">
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
                <tr key={p.id} className="border-t">
                  <td className="p-4">{p.id}</td>
                  <td className="p-4">{p.name}</td>
                  <td className="p-4">{p.countryId}</td>
                  <td className="p-4">{p.type}</td>

                  <td className="p-4 text-right space-x-2">
                    <button className="p-2 hover:bg-gray-100 rounded-lg">
                      <Pencil size={18} />
                    </button>

                    <button
                      onClick={() => handleDelete(p.id)}
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
            <h2 className="text-xl font-semibold">Create Province</h2>

            <input
  {...register('name', { required: true })}
  placeholder="Province name"
  className="w-full border px-4 py-2 rounded-xl"
/>

            {/* <input
              type="number"
              value={countryId ?? ''}
              onChange={(e) => setCountryId(Number(e.target.value))}
              placeholder="Country ID"
              className="w-full border px-4 py-2 rounded-xl"
            /> */}
            <OptionSelect
            register={register}
            labelText='country'
            fieldName='countryId'
            options={countries}

            ></OptionSelect>

            <input
  {...register('type')}
  placeholder="Type"
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
                onClick={handleSubmit(handleCreate)}
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