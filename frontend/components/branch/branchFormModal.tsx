'use client';

import { useForm } from 'react-hook-form';
import { useEffect, useState } from 'react';
import OptionSelect from '../item/optionSelect';
import { BranchField, BranchResponse } from '@/models/branch/branchResponse';
import countryService from '@/services/country/countryService';
import provinceService from '@/services/province/provinceService';
import districtService from '@/services/district/districtService';

type Option = {
  id: number;
  name: string;
};

type Props = {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: BranchField) => void;
  defaultValues?: BranchResponse;
  loading?: boolean;
};

export default function BranchFormModal({
  open,
  onClose,
  onSubmit,
  defaultValues,
  loading,
}: Props) {
  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<BranchField>();

  const [countries, setCountries] = useState<Option[]>([]);
  const [provinces, setProvinces] = useState<Option[]>([]);
  const [districts, setDistricts] = useState<Option[]>([]);

  const countryId = watch('countryId');
  const provinceId = watch('provinceId');

  useEffect(() => {
    if (!open) return;

    const fetchCountries = async () => {
      const res = await countryService.getCountries();
      setCountries(res);
    };

    fetchCountries();
  }, [open]);

  useEffect(() => {
    const fetchProvinces = async () => {
      if (!countryId) {
        setProvinces([]);
        return;
      }

      const res = await provinceService.getProvinces(0, '', countryId);
      setProvinces(res.content);
    };

    fetchProvinces();
  }, [countryId]);

  useEffect(() => {
    const fetchDistricts = async () => {
      if (!provinceId) {
        setDistricts([]);
        return;
      }

      const res = await districtService.getDistricts('', 0, countryId, provinceId);
      setDistricts(res.content);
    };

    fetchDistricts();
  }, [provinceId]);

  useEffect(() => {
    if (open) {
      if (defaultValues) {
        reset({
          name: defaultValues.name,
          specificAddress: defaultValues.address?.specificAddress ?? '',
          countryId: defaultValues.address?.countryId,
          provinceId: defaultValues.address?.provinceId,
          districtId: defaultValues.address?.districtId,
        });
      } else {
        reset({
          name: '',
          specificAddress: '',
          countryId: undefined,
          provinceId: undefined,
          districtId: undefined,
        });
      }
    }
  }, [open, defaultValues, reset]);

  if (!open) return null;

  return (
    <div className="fixed inset-0 bg-black/40 flex items-center justify-center z-50">
      <div className="bg-white w-[400px] rounded-2xl p-6 space-y-4 shadow-lg">
        <h2 className="text-xl font-semibold">
          {defaultValues ? 'Update Branch' : 'Create Branch'}
        </h2>

        <input
          {...register('name', { required: 'Branch name is required' })}
          placeholder="Enter branch name..."
          className="w-full border px-4 py-2 rounded-xl"
        />
        {errors.name && <p className="text-red-500 text-sm">{errors.name.message}</p>}

        <OptionSelect
          register={register}
          fieldName="countryId"
          labelText="Country"
          options={countries}
        />

        <OptionSelect
          register={register}
          fieldName="provinceId"
          labelText="Province"
          options={provinces}
          disabled={!countryId}
        />

        <OptionSelect
          register={register}
          fieldName="districtId"
          labelText="District"
          options={districts}
          registerOptions={{ required: 'District is required' }}
          disabled={!provinceId}
        />
        {errors.districtId && (
          <p className="text-red-500 text-sm">{errors.districtId.message}</p>
        )}

        <input
          {...register('specificAddress', { required: 'Specific address is required' })}
          placeholder="Specific address..."
          className="w-full border px-4 py-2 rounded-xl"
        />
        {errors.specificAddress && (
          <p className="text-red-500 text-sm">{errors.specificAddress.message}</p>
        )}

        <div className="flex justify-end gap-2 mt-4">
          <button onClick={onClose} className="px-4 py-2 border rounded-xl">
            Cancel
          </button>

          <button
            onClick={handleSubmit(onSubmit)}
            disabled={loading}
            className="bg-black text-white px-4 py-2 rounded-xl"
          >
            {defaultValues ? 'Update' : 'Create'}
          </button>
        </div>
      </div>
    </div>
  );
}