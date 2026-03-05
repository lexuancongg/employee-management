"use client"
import React from "react";
import {AppProvider} from "@/context/appContext";

import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import '../styles/global.css'





import { Geist } from 'next/font/google'
import {ToastContainer} from "react-toastify";
import Layout from "@/components/common/layout";

const geist = Geist({
    weight:'400',
    subsets: ['latin'],
})

function App({children}: React.PropsWithChildren) {
    return (
        <html lang="en">
        <body>{children}</body>
        </html>


    )
}

export default App;