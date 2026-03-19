'use client';

import { useState, useEffect, useCallback, useRef } from 'react';
import { PageResponse } from '@/models/page/pageResponse';
import Pagination from '@/components/pagination/pagination';
import ConfirmationDialog from '@/components/dialog/confirmDialog';
import { DistrictCreateRequest, DistrictResponse } from '@/models/district/district';
import districtService from '@/services/district/districtService';
import { CountryResponse } from '@/models/country/countryResponse';
import countryService from '@/services/country/countryService';
import provinceService from '@/services/province/provinceService';
import DistrictFormModal from '@/components/district/districtFormModal';

export default function DistrictsPage() {
    const [districts, setDistricts] = useState<DistrictResponse[]>([]);
    const [pageIndex, setPageIndex] = useState(0);
    const [keyword, setKeyword] = useState('');
    const [totalPages, setTotalPages] = useState(0);
    const [loading, setLoading] = useState(true);
    const [open, setOpen] = useState(false);
    const [selected, setSelected] = useState<DistrictResponse | null>(null);
    const [deleteId, setDeleteId] = useState<number | null>(null);
    const searchTimeoutRef = useRef<NodeJS.Timeout | null>(null);
    const [selectedCountry, setSelectedCountry] = useState<number | undefined>(undefined);
    const [selectedProvince, setSelectedProvince] = useState<number | undefined>(undefined);
    const [countries, setCountries] = useState<CountryResponse[]>([]);
    const [provinces, setProvinces] = useState<ProvinceResponse[]>([]);

    const fetchDistricts = useCallback(async () => {
        setLoading(true);
        try {
            const res: PageResponse<DistrictResponse> =
                await districtService.getDistricts(keyword, pageIndex, selectedCountry, selectedProvince);

            setDistricts(res.content);
            setTotalPages(res.totalPages);
        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    }, [pageIndex, keyword, selectedCountry, selectedProvince]);




    useEffect(() => {
        fetchDistricts();
    }, [fetchDistricts]);



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
        const fetchProvinces = async () => {
            try {
                const res = await provinceService.getProvinces(0, "", selectedCountry);
                setProvinces(res.content)
            } catch (err) {
                console.error(err);
            }
        };
        fetchProvinces();
    }, [selectedCountry]);





    const handleDelete = async (id: number) => {

    };
























    return (
        <div className="p-6 space-y-6">
            {/* Header */}
            <div className="flex justify-between items-center">
                <div>
                    <h1 className="text-2xl font-bold">Districts</h1>
                    <p className="text-gray-500 text-sm">Manage districts</p>
                </div>
                <button
                    onClick={() => setOpen(true)}
                    className="bg-black text-white px-4 py-2 rounded-xl"
                >
                    + Add
                </button>
            </div>
            <div className="flex gap-4">
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

                <select
                    value={selectedCountry ?? ''}
                    onChange={(e) => {
                        setPageIndex(0);
                        setSelectedCountry(e.target.value ? Number(e.target.value) : undefined);
                        setSelectedProvince(undefined);
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



            <div className="bg-white rounded-2xl shadow overflow-hidden">
                <table className="w-full">
                    <thead className="bg-gray-100 text-sm">
                        <tr>
                            <th className="p-4">ID</th>
                            <th className="p-4">Name</th>
                            <th className="p-4">Province</th>
                            <th className="p-4">Country</th>
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
                        ) : districts.length === 0 ? (
                            <tr>
                                <td colSpan={5} className="p-6 text-center">
                                    No data
                                </td>
                            </tr>
                        ) : (
                            districts.map((d) => (
                                <tr key={d.id} className="border-t hover:bg-gray-50">
                                    <td className="p-4">{d.id}</td>
                                    <td className="p-4 font-medium">{d.name}</td>
                                    <td className="p-4">{d.provinceName}</td>
                                    <td className="p-4">{d.countryName}</td>

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

            <DistrictFormModal
                open={open}
                onClose={() => {
                    setOpen(false);
                    setSelected(null);
                }}
                onSubmit={async (data: DistrictCreateRequest) => {
                    if (selected) {
                        await districtService.updateDistrict(selected.id, data);
                    } else {
                        await districtService.createDistrict(data);
                    }
                    setOpen(false);
                    setSelected(null);
                    fetchDistricts();
                }}
                defaultValues={
                    selected
                        ? selected
                        : undefined
                } />

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