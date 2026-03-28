'use client';
import { Bell, User } from 'lucide-react';

export default function Topbar() {
    return (
        <div className="h-16 bg-white border-b flex items-center px-8 justify-between shadow-sm sticky top-0 z-10">
            <div className="flex items-center gap-8">
                <div className="text-2xl font-semibold text-gray-800">Fpt Software</div>
                <div className="text-sm text-gray-500 font-medium">Dashboard</div>
            </div>

            <div className="flex items-center gap-5">
                <div className="relative cursor-pointer hover:scale-110 transition">
                    <Bell className="w-6 h-6 text-gray-600" />
                    <div className="absolute -top-1 -right-1 w-4 h-4 bg-red-500 text-[10px] flex items-center justify-center rounded-full text-white font-medium">
                        3
                    </div>
                </div>

                {/* User */}
                <div className="flex items-center gap-2 cursor-pointer hover:opacity-80 transition">
                    <div className="w-8 h-8 rounded-full bg-indigo-100 flex items-center justify-center">
                        <User className="w-4 h-4 text-indigo-600" />
                    </div>
                    <span className="text-sm font-medium text-gray-700">Admin</span>
                </div>
            </div>
        </div>
    );
}