"use client";

import { useEffect, useState } from "react";
import {connectWS, sendMessage} from "@/utils/ws/wsClient";

export default function ChatPage() {

    const [messages, setMessages] = useState<string[]>([]);
    const [input, setInput] = useState("");

    useEffect(() => {
        connectWS((msg) => {
            console.log(msg)
            // setMessages((prev) => [...prev, msg.content]);
        });
    }, []);

    async function createLeaveRequest(leaveRequestData :any) {
        try {
            const response = await fetch("http://localhost:8080/leave-request", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                credentials: "include",
                body: JSON.stringify({
                    leaveType: leaveRequestData.leaveType,
                    startDate: leaveRequestData.startDate,
                    endDate: leaveRequestData.endDate,
                    reason: leaveRequestData.reason
                })
            });

            if (!response.ok) {
                const error = await response.json();
                throw new Error(error.message || "Request failed");
            }

            return await response.json();
        } catch (error) {
            console.error("Failed to create leave request:", error);
            throw error;
        }
    }

// Ví dụ sử dụng
    createLeaveRequest({
        leaveType: "ANNUAL",
        startDate: "2026-06-15",
        endDate: "2026-06-17",
        reason: "Nghỉ phép năm"
    }).then(data => console.log("Created:", data));



    return (
        <div>
            <h1>Chat</h1>

            <div>
                {messages.map((m, i) => (
                    <div key={i}>{m}</div>
                ))}
            </div>

            <input
                value={input}
                onChange={(e) => setInput(e.target.value)}
            />

            <button
                onClick={() => {
                    sendMessage(input);
                    setInput("");
                }}
            >
                Send
            </button>
        </div>
    );
}