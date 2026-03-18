'use client';

import { useForm } from 'react-hook-form';
import { useEffect } from 'react';
import { BranchField, BranchResponse } from '@/models/branch/branchResponse';
import OptionSelect from '../item/optionSelect';
import { CountryResponse } from '@/models/country/countryResponse';

type Props = {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: BranchField) => void;
  defaultValues?: BranchResponse;
  loading?: boolean;
  provinces: ProvinceResponse[];
  countries:CountryResponse[]
};

export default function BranchFormModal({
  open,
  onClose,
  onSubmit,
  defaultValues,
  loading,
  countries,
  provinces,
}: Props) {
  const { register, handleSubmit, reset, formState: { errors } } = useForm<BranchField>({
    defaultValues: defaultValues
      ? {
          name: defaultValues.name,
          specificAddress: defaultValues.address?.specificAddress ?? '',
          countryId: defaultValues.address?.countryId,
          provinceId: defaultValues.address?.provinceId,
        }
      : { name: '', specificAddress: '', countryId: undefined, provinceId: undefined },
  });

  useEffect(() => {
    if (open) {
      reset(defaultValues
        ? {
            name: defaultValues.name,
            specificAddress: defaultValues.address?.specificAddress ?? '',
            countryId: defaultValues.address?.countryId,
            provinceId: defaultValues.address?.provinceId,
          }
        : { name: '', specificAddress: '', countryId: undefined, provinceId: undefined }
      );
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
          className="w-full border px-4 py-2 rounded-xl focus:outline-none focus:ring-2 focus:ring-black"
        />
        {errors.name && <p className="text-red-500 text-sm">{errors.name.message}</p>}

        <input
          {...register('specificAddress', { required: 'Specific address is required' })}
          placeholder="Specific address..."
          className="w-full border px-4 py-2 rounded-xl focus:outline-none focus:ring-2 focus:ring-black"
        />
        {errors.specificAddress && <p className="text-red-500 text-sm">{errors.specificAddress.message}</p>}

        <OptionSelect
          register={register}
          fieldName="countryId"
          labelText="Country"
          options={countries}
          defaultValue={defaultValues?.address?.countryId}
        />

        <OptionSelect
          register={register}
          fieldName="provinceId"
          labelText="Province"
          options={provinces}
          defaultValue={defaultValues?.address?.provinceId}
        />

        <div className="flex justify-end gap-2 mt-4">
          <button
            onClick={onClose}
            className="px-4 py-2 border rounded-xl"
          >
            Cancel
          </button>
          <button
            onClick={handleSubmit(onSubmit)}
            disabled={loading}
            className="bg-black text-white px-4 py-2 rounded-xl disabled:opacity-50"
          >
            {defaultValues ? 'Update' : 'Create'}
          </button>
        </div>
      </div>
    </div>
  );
}