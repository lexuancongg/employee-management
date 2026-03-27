
"use client"
import Link from "next/link"

export default function Sidebar() {
    return (
        <aside className="fixed  left-0 top-0 h-screen w-64 bg-slate-800 text-gray-300">

            <div className="h-16 flex items-center px-6 border-b border-slate-700">
                <h1 className="text-xl font-bold text-white">HRSALE</h1>
            </div>

            <nav className="flex flex-col mt-6 text-sm">

                <Link href="/dashboard" className="px-6 py-3 hover:bg-slate-700">
                    Dashboard
                </Link>

                <Link href="/dashboard/employees" className="px-6 py-3 hover:bg-slate-700">
                    Employees
                </Link>

                <Link href="/dashboard/positions" className="px-6 py-3 hover:bg-slate-700">
                    Positions
                </Link>

                <Link href="/dashboard/branchs" className="px-6 py-3 hover:bg-slate-700">
                    Branchs
                </Link>

                <Link href="/dashboard/address/countries" className="px-6 py-3 hover:bg-slate-700">
                    Countries
                </Link>

                <Link href="/dashboard/address/provinces" className="px-6 py-3 hover:bg-slate-700">
                    Provinces
                </Link>

                <Link href="/dashboard/address/districts" className="px-6 py-3 hover:bg-slate-700">
                    Districts
                </Link>
                 <Link href="/dashboard/departments" className="px-6 py-3 hover:bg-slate-700">
                    Departments
                </Link>
                 <Link href="/dashboard/salaries" className="px-6 py-3 hover:bg-slate-700">
                    Salaries
                </Link>


            </nav>
        </aside>
    )
}