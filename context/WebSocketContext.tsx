'use client';

import { websocketService } from "@/service/websocket.service";
import { useChatStore } from "@/store/useChatStore";
import { ChatMessageResponse, PresenceEvent } from "@/type";
import { StompSubscription } from "@stomp/stompjs";
import { createContext, useContext, useEffect, useRef, useState } from "react";

interface WebSocketContextType {
    connected: boolean;
}

const WebSocketContext = createContext<WebSocketContextType | null>(null);

export const WebSocketProvider = ({
  children,
}: {
  children: React.ReactNode;
}) => {
  const [connected, setConnected] = useState(false);
  
  const messageSubRef = useRef<
    StompSubscription | { unsubscribe: () => void } | null
  >(null);
  const presenceSubRef = useRef<
    StompSubscription | { unsubscribe: () => void } | null
  >(null);
  useEffect(() => {
    const token = localStorage.getItem("accessToken");
    if (!token) return;

    // Kết nối WebSocket thông qua service
    websocketService.connect(token, () => {
      setConnected(true);

      messageSubRef.current = websocketService.subscribe(
        "/user/queue/messages",
        (message: ChatMessageResponse) => {
          const { conversationId } = message;
          // Chỉ cập nhật vào đúng ngăn kéo của conversationId đó
          useChatStore.getState().addMessage(conversationId, message);
        },
      );

      presenceSubRef.current = websocketService.subscribe(
        "/topic/presence",
        (presence: PresenceEvent) => {
          useChatStore.getState().updateStatusInConversations(presence);
        },
      );
    });

    return () => {
      messageSubRef.current?.unsubscribe();
      presenceSubRef.current?.unsubscribe();
      websocketService.disconnect();
      setConnected(false);
    };
  }, []);

  return (
    <WebSocketContext.Provider value={{ connected }}>
      {children}
    </WebSocketContext.Provider>
  );
};

export const useWebSocket = () => {
  const context = useContext(WebSocketContext);
  if (!context) {
    throw new Error("useWebSocket must be used within a WebSocketProvider");
  }
  return context;
};