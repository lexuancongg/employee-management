"use client"
import React, {Context, PropsWithChildren} from "react";
import {UserInfoProvider} from "@/context/userInfoContext";

export const  AppContext : Context<Object> = React.createContext({});

export function AppProvider({children}: React.PropsWithChildren<{}>){
    return (
        <UserInfoProvider>
            {children}
        </UserInfoProvider>
    )
}
