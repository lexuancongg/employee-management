'use client';

import React from 'react';
import { Search, Bell, Plus } from 'lucide-react';

export default function Topbar() {
    return (
        <div className="h-16 bg-white border-b flex items-center px-8 justify-between shadow-sm">

            {/* Left - Title */}
            <div className="flex items-center gap-8">
                <div className="text-2xl font-semibold text-gray-800">Employee Management</div>
                <div className="text-sm text-gray-500 font-medium">Dashboard</div>
            </div>

            {/* Right side - ĐÃ THAY user avatar + tên thành icon nút thông báo (Bell) */}
            <div className="flex items-center gap-6">
                {/* Search Bar */}
                <div className="relative w-72">
                    <input
                        type="text"
                        placeholder="Search..."
                        className="bg-gray-100 pl-10 pr-5 py-2.5 w-full rounded-3xl text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 placeholder:text-gray-400"
                    />
                    <Search className="w-5 h-5 text-gray-400 absolute left-4 top-3" />
                </div>

                {/* ICON NÚT THÔNG BÁO (Bell) - đã thay thế hoàn toàn phần user "L + lexuancong" */}
                <div className="relative cursor-pointer transition hover:scale-110">
                    <Bell className="w-6 h-6 text-gray-600" />
                    <div className="absolute -top-1 -right-1 w-4 h-4 bg-red-500 text-[10px] flex items-center justify-center rounded-full text-white font-medium">
                        3
                    </div>
                </div>

                {/* Add Employee Button */}
                <button className="bg-orange-500 hover:bg-orange-600 transition-all text-white px-6 py-2.5 rounded-2xl flex items-center gap-2 font-medium text-sm shadow-sm active:scale-95">
                    <Plus className="w-5 h-5" />
                    Add Employee
                </button>
            </div>
        </div>
    );
}