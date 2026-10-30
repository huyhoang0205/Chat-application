import AuthProvider from "@/component/auth/AuthProvider";
import { WebSocketProvider } from "@/context/WebSocketContext";

export default function Layout({ children }: { children: React.ReactNode }) {
  return (
    <main>
      <AuthProvider>
        <WebSocketProvider>{children}</WebSocketProvider>
      </AuthProvider>
    </main>
  );
}
