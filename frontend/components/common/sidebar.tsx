'use client';
import Link from 'next/link';
import { usePathname } from 'next/navigation';

export default function Sidebar() {
    const pathname = usePathname();

    const links = [
        { href: '/dashboard', label: 'Dashboard' },
        { href: '/dashboard/employees', label: 'Employees' },
        { href: '/dashboard/positions', label: 'Positions' },
        { href: '/dashboard/branchs', label: 'Branchs' },
        { href: '/dashboard/address/countries', label: 'Countries' },
        { href: '/dashboard/address/provinces', label: 'Provinces' },
        { href: '/dashboard/address/districts', label: 'Districts' },
        { href: '/dashboard/departments', label: 'Departments' },
        { href: '/dashboard/salaries', label: 'Salaries' },
    ];

    return (
        <aside className="fixed left-0 top-0 h-screen w-64 bg-slate-800 text-gray-300">
            <div className="h-16 flex items-center px-6 border-b border-slate-700">
                <h1 className="text-xl font-bold text-white">HRSALE</h1>
            </div>
            <nav className="flex flex-col mt-6 text-sm">
                {links.map(({ href, label }) => {
                    const isActive = pathname === href || (href !== '/dashboard' && pathname.startsWith(href));
                    return (
                        <Link
                            key={href}
                            href={href}
                            className={`px-6 py-3 transition-colors ${
                                isActive
                                  ? 'bg-blue-500 text-white font-medium'
                                    : 'hover:bg-slate-700'
                            }`}
                        >
                            {label}
                        </Link>
                    );
                })}
            </nav>
        </aside>
    );
}