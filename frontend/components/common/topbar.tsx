export default function Topbar() {
    return (
        <div className="h-16 bg-white shadow flex items-center justify-end px-6">


            <div className="flex items-center gap-4">
                <i className="bi bi-bell text-xl"></i>

                <div className="flex items-center gap-2">
                    <img
                        src="/avatar.png"
                        className="w-8 h-8 rounded-full"
                    />
                    <span className="text-sm">lexuancong</span>
                </div>
            </div>

        </div>
    )
}