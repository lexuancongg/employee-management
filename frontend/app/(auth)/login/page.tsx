"use client"

import { AuthRequest } from "@/models/auth/authRequest"
import authenticationService from "@/services/authenticationService"
import { useState } from "react"

export default function LoginPage() {

    const [email,setEmail] = useState("")
    const [password,setPassword] = useState("")

    const handleLogin = (e:React.FormEvent) => {
        e.preventDefault()
        const authRequest:AuthRequest = {
            password: password,
            username:email
        }
        
        authenticationService.login(authRequest)
        .then(response =>{
            
        })
        

    }

    return (
        <div className="min-h-screen flex items-center justify-center bg-gray-100">

            <div className="bg-white p-8 rounded-lg shadow w-[380px]">

                {/* title */}
                <h1 className="text-2xl font-bold text-center mb-6">
                    HR Management Login
                </h1>

                {/* form */}
                <form onSubmit={handleLogin} className="flex flex-col gap-4">

                    {/* email */}
                    <div>
                        <label className="text-sm text-gray-600">
                            Email
                        </label>
                        <input
                            type="email"
                            value={email}
                            onChange={(e)=>setEmail(e.target.value)}
                            className="w-full border rounded px-3 py-2 mt-1 focus:outline-none focus:ring focus:ring-blue-200"
                            placeholder="admin@gmail.com"
                            required
                        />
                    </div>

                    {/* password */}
                    <div>
                        <label className="text-sm text-gray-600">
                            Password
                        </label>
                        <input
                            type="password"
                            value={password}
                            onChange={(e)=>setPassword(e.target.value)}
                            className="w-full border rounded px-3 py-2 mt-1 focus:outline-none focus:ring focus:ring-blue-200"
                            placeholder="••••••••"
                            required
                        />
                    </div>

                    {/* remember */}
                    <div className="flex justify-between text-sm">
                        <label className="flex items-center gap-2">
                            <input type="checkbox"/>
                            Remember me
                        </label>

                        <a className="text-blue-500 hover:underline">
                            Forgot password?
                        </a>
                    </div>

                    {/* button */}
                    <button
                        type="submit"
                        className="bg-blue-600 text-white py-2 rounded hover:bg-blue-700 transition"
                    >
                        Login
                    </button>

                </form>

            </div>

        </div>
    )
}