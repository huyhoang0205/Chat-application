'use client'

import { useUserStore } from "@/store/useUserStore"
import { useRouter } from "next/navigation";
import { useEffect } from "react";

export const useAuthGuard = () => {
    const {user} = useUserStore();
    const router = useRouter()

    useEffect(() => {
        const token = localStorage.getItem('accessToken');
        if(!token || !user) {
            router.push('/login')
        }
    }, [user, router]);
    return {isAuthenticated: !!user}
}