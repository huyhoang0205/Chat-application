import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";
import AuthProvider from '@/component/auth/AuthProvider'
import {AppRouterCacheProvider}  from '@mui/material-nextjs/v16-appRouter'

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
});

export const metadata: Metadata = {
  title: "Chat App",
  description: "Real-time chat application",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="vi"
    >
      <body className={`${geistSans.variable} ${geistMono.variable} antialiased min-h-full flex flex-col`}>
      <AppRouterCacheProvider options={{enableCssLayer: true, key: 'css'}}>
        <AuthProvider>        
            {children}
        </AuthProvider>
      </AppRouterCacheProvider>
      </body>
    </html>
  );
}
