
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

                <Link href="/staff" className="px-6 py-3 hover:bg-slate-700">
                    Staff
                </Link>

                <Link href="/core-hr" className="px-6 py-3 hover:bg-slate-700">
                    Core HR
                </Link>

                <Link href="/organization" className="px-6 py-3 hover:bg-slate-700">
                    Organization
                </Link>

                <Link href="/assets" className="px-6 py-3 hover:bg-slate-700">
                    Assets
                </Link>

                <Link href="/attendance" className="px-6 py-3 hover:bg-slate-700">
                    Time & Attendance
                </Link>

                <Link href="/payroll" className="px-6 py-3 hover:bg-slate-700">
                    Payroll
                </Link>

            </nav>
        </aside>
    )
}