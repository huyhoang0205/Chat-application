'use client';

import { useEffect, useState } from 'react';
import { useUserStore } from '@/store/useUserStore';
import { authService } from '@/service/auth.service';
import {Box, keyframes} from '@mui/material'
import { usePathname, useRouter } from 'next/navigation';

interface AuthProviderProps {
  children: React.ReactNode;
}

const PUBLIC_ROUTES = ["/login", "/register"];

export default function AuthProvider({ children }: AuthProviderProps) {
  const [isInitialized, setIsInitialized] = useState(false);
  const { user } = useUserStore();

  const pathname = usePathname();
  const router = useRouter();

  useEffect(() => {
    const initAuth = async () => {
      const token = localStorage.getItem("accessToken");
      const isPublicRoute = PUBLIC_ROUTES.some((route) =>
        pathname.startsWith(route),
      );

      // 1. TRƯỜNG HỢP: Đã có token nhưng lại mò vào trang Public (/login, /register...)
      // -> Tự động đá về trang Dashboard (hoặc trang chủ)
      if (token && isPublicRoute) {
        router.push("/");
        return;
      }

      // 2. TRƯỜNG HỢP: Không có token và đang ở trang Public
      // -> Cho qua luôn, không cần gọi API check user
      if (!token && isPublicRoute) {
        setIsInitialized(true);
        return;
      }

      // 3. TRƯỜNG HỢP: Không có token mà lại vào trang bảo mật
      // -> Xóa state user và đá về trang login
      if (!token && !isPublicRoute) {
        useUserStore.getState().clearUser();
        router.push("/login");
        return;
      }

      // 4. TRƯỜNG HỢP: Có token và đang ở trang bảo mật
      // -> Tiến hành gọi API lấy thông tin user
      try {
        const response = await authService.myInfo();
        if (response?.data) {
          useUserStore.getState().setUser(response.data);
          setIsInitialized(true);
        } else {
          throw new Error("Invalid user data");
        }
      } catch (error) {
        console.error("Token không hợp lệ hoặc hết hạn:", error);
        localStorage.removeItem("accessToken");
        localStorage.removeItem("refreshToken");
        useUserStore.getState().clearUser();
        router.push("/login");
      }
    };
    initAuth();
  }, [pathname, user, router]);


  // Trên client, chờ initialization
  if (!isInitialized) {
    const spin = keyframes`
                0% {
                    transform: rotate(0deg);
                }
                100% {
                    transform: rotate(360deg);
                }
            `;
    return (
      <Box
        sx={{
          display: 'flex',
          justifyContent: 'center',
          alignItems: 'center',
          minHeight: '100vh',
          backgroundColor: '#36393f',
        }}
      >
        <Box
          sx={{
            width: '40px',
            height: '40px',
            border: '4px solid #404249',
            borderTop: '4px solid #5865f2',
            borderRadius: '50%',
            animation: `${spin} 1s linear infinite`,
          }}
        />
      </Box>
    );
  }

  return <>{children}</>;
}
