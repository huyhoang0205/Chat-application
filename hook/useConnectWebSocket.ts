import { useEffect, useState, useRef } from 'react';
import { websocketService } from '@/service/websocket.service';

export const useConnectWebSocket = () => {
  const [connected, setConnected] = useState(false);

  useEffect( () => {
    const token = localStorage.getItem('accessToken');
    if (!token) return;

    websocketService.connect(token, () => {
      setConnected(true);
    });

    return () => {
      websocketService.disconnected();
      setConnected(false);
    };
  }, []); // Chỉ chạy 1 lần
  

  return { connected };
};
