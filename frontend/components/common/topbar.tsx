'use client';

import React from 'react';
import {  Bell } from 'lucide-react';

export default function Topbar() {
    return (
        <div className="h-16 bg-white border-b flex items-center px-8 justify-between shadow-sm">

            <div className="flex items-center gap-8">
                <div className="text-2xl font-semibold text-gray-800">Fpt Sorfware</div>
                <div className="text-sm text-gray-500 font-medium">Dashboard</div>
            </div>

            <div className="flex items-center gap-6">
               

                <div className="relative cursor-pointer transition hover:scale-110">
                    <Bell className="w-6 h-6 text-gray-600" />
                    <div className="absolute -top-1 -right-1 w-4 h-4 bg-red-500 text-[10px] flex items-center justify-center rounded-full text-white font-medium">
                        3
                    </div>
                </div>

                
            </div>
        </div>
    );
}