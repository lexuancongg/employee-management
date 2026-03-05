export default function Home() {
    return (
        <div className="space-y-6">

            <div className="grid grid-cols-4 gap-6">

                <DashboardCard title="People" count="3" color="bg-orange-400" />
                <DashboardCard title="Company" count="1" color="bg-pink-500" />
                <DashboardCard title="Leave Management" count="" color="bg-teal-500" />
                <DashboardCard title="HR Sale Settings" count="" color="bg-green-500" />

                <DashboardCard title="Total Projects" count="2" color="bg-green-400" />
                <DashboardCard title="Tasks" count="3" color="bg-cyan-500" />
                <DashboardCard title="Document Management" count="" color="bg-red-400" />
                <DashboardCard title="Theme Settings" count="" color="bg-orange-300" />

            </div>

            <div className="grid grid-cols-3 gap-6">

                <div className="bg-white rounded shadow p-4">
                    <h2 className="font-semibold mb-4">Calendar Options</h2>

                    <div className="flex flex-col gap-2 text-sm">

                        <OptionItem color="bg-blue-500" text="Holidays" />
                        <OptionItem color="bg-green-500" text="Leave Request" />
                        <OptionItem color="bg-sky-500" text="Travel Request" />
                        <OptionItem color="bg-orange-500" text="Upcoming Birthday" />
                        <OptionItem color="bg-red-500" text="Trainings" />
                        <OptionItem color="bg-purple-500" text="Projects" />

                    </div>
                </div>

                {/* Calendar */}
                <div className="col-span-2 bg-white rounded shadow p-4">
                    <h2 className="font-semibold mb-4 text-center">
                        March 2026
                    </h2>

                    <div className="grid grid-cols-7 gap-2 text-center text-sm">

                        {["Sun","Mon","Tue","Wed","Thu","Fri","Sat"].map(d => (
                            <div key={d} className="font-semibold text-gray-500">
                                {d}
                            </div>
                        ))}

                        {Array.from({length: 31}).map((_,i)=>(
                            <div
                                key={i}
                                className="border rounded p-3 hover:bg-gray-100"
                            >
                                {i+1}
                            </div>
                        ))}

                    </div>
                </div>

            </div>

        </div>
    );
}

function DashboardCard({ title, count, color }: any) {
    return (
        <div className={`${color} text-white p-6 rounded shadow hover:scale-105 transition`}>
            <h3 className="text-lg font-semibold">{title}</h3>
            {count && <p className="text-sm mt-1">↑ {count}</p>}
        </div>
    );
}

function OptionItem({ color, text }: any) {
    return (
        <div className={`${color} text-white px-3 py-2 rounded`}>
            {text}
        </div>
    );
}