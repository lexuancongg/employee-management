'use client';

import { useForm } from 'react-hook-form';
import { useEffect, useState } from 'react';
import OptionSelect from '../item/optionSelect';
import { BranchField, BranchResponse } from '@/models/branch/branchResponse';
import countryService from '@/services/country/countryService';
import provinceService from '@/services/province/provinceService';
import districtService from '@/services/district/districtService';
import { DepartmentCreateRequest, DepartmentResponse } from '@/models/department/department';

type Option = {
  id: number;
  name: string;
};

type Props = {
  open: boolean;
  onClose: () => void;
  onSubmit: (data: DepartmentCreateRequest) => void;
  defaultValues?: DepartmentResponse;
  loading?: boolean;
  branchs:BranchResponse[];
};

export default function DepartmentFormModal({
  open,
  onClose,
  onSubmit,
  defaultValues,
  loading,
  branchs
}: Props) {
  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors },
  } = useForm<DepartmentCreateRequest>();




  useEffect(() => {
    if (open) {
      if (defaultValues) {
        reset({
          branchId:defaultValues.branchId,
          name:defaultValues.name
        });
      } else {
        reset({
          name: '',
          branchId:undefined
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
          fieldName="branchId"
          labelText="Branch"
          options={branchs}
        />

        

       

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