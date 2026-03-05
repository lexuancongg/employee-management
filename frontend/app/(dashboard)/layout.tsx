import Sidebar from "@/components/common/sidebar";
import Topbar from "@/components/common/topbar";


export default function DashboardLayout({
                                            children,
                                        }: {
    children: React.ReactNode
}) {
    return (
        <div className="flex">

            <Sidebar  />

            <div className="ml-64 w-full">
                <Topbar />

                <main className="p-6 bg-gray-100 min-h-screen">
                    {children}
                </main>
            </div>

        </div>
    )
}