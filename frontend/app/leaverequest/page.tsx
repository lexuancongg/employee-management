'use client'

import { useState } from 'react'

enum LeaveType {
    ANNUAL = 'ANNUAL',
    SICK = 'SICK',
    UNPAID = 'UNPAID',
    MATERNITY = 'MATERNITY'
}

export default function LeaveRequestPage() {
    const [leaveType, setLeaveType] = useState<LeaveType>(LeaveType.ANNUAL)
    const [startDate, setStartDate] = useState('')
    const [endDate, setEndDate] = useState('')
    const [reason, setReason] = useState('')
    const [loading, setLoading] = useState(false)

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault()

        if (!startDate || !endDate) {
            alert('Vui lòng chọn ngày nghỉ')
            return
        }

        if (new Date(startDate) > new Date(endDate)) {
            alert('Ngày bắt đầu phải nhỏ hơn ngày kết thúc')
            return
        }

        setLoading(true)

        try {
            const res = await fetch('/api/leave-request', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    leaveType,
                    startDate,
                    endDate,
                    reason
                })
            })

            if (!res.ok) throw new Error('Submit failed')

            alert('Gửi đơn nghỉ phép thành công!')

            // reset form
            setLeaveType(LeaveType.ANNUAL)
            setStartDate('')
            setEndDate('')
            setReason('')
        } catch (err) {
            alert('Có lỗi xảy ra khi gửi đơn')
        } finally {
            setLoading(false)
        }
    }

    return (
        <div className="max-w-xl mx-auto p-6 bg-white shadow rounded-xl">
            <h1 className="text-2xl font-bold mb-6">Xin nghỉ phép</h1>

            <form onSubmit={handleSubmit} className="space-y-4">

                {/* Leave Type */}
                <div>
                    <label className="block font-medium">Loại nghỉ</label>
                    <select
                        value={leaveType}
                        onChange={(e) => setLeaveType(e.target.value as LeaveType)}
                        className="w-full border p-2 rounded"
                    >
                        <option value={LeaveType.ANNUAL}>Nghỉ phép năm</option>
                        <option value={LeaveType.SICK}>Nghỉ ốm</option>
                        <option value={LeaveType.UNPAID}>Nghỉ không lương</option>
                        <option value={LeaveType.MATERNITY}>Nghỉ thai sản</option>
                    </select>
                </div>

                {/* Start Date */}
                <div>
                    <label className="block font-medium">Ngày bắt đầu</label>
                    <input
                        type="date"
                        value={startDate}
                        onChange={(e) => setStartDate(e.target.value)}
                        className="w-full border p-2 rounded"
                    />
                </div>

                {/* End Date */}
                <div>
                    <label className="block font-medium">Ngày kết thúc</label>
                    <input
                        type="date"
                        value={endDate}
                        onChange={(e) => setEndDate(e.target.value)}
                        className="w-full border p-2 rounded"
                    />
                </div>

                {/* Reason */}
                <div>
                    <label className="block font-medium">Lý do</label>
                    <textarea
                        value={reason}
                        onChange={(e) => setReason(e.target.value)}
                        className="w-full border p-2 rounded"
                        rows={4}
                        placeholder="Nhập lý do nghỉ..."
                    />
                </div>

                {/* Submit */}
                <button
                    type="submit"
                    disabled={loading}
                    className="w-full bg-blue-600 text-white py-2 rounded hover:bg-blue-700 disabled:opacity-50"
                >
                    {loading ? 'Đang gửi...' : 'Gửi đơn'}
                </button>
            </form>
        </div>
    )
}