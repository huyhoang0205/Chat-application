import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  /* config options here */
  // reactStrictMode: false,
  compiler: {
    // styledComponents: true,
    // emotion: true
  },
  output: 'standalone'
};

export default nextConfig;
