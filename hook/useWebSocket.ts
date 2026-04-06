import { websocketService } from "@/service/websocket.service";
import { useEffect, useRef, useState } from "react";
import {useUserStore} from '@/store/useUserStore'

export const useWebSocket = (destination: string , onMessage?: (message : any) => void ) => {
    // const [connected, setConnected] = useState(false);
    const subscriptionRef = useRef<any>(null)
    const onMessageRef = useRef(onMessage);
    const {user} = useUserStore();

    useEffect(() => {
        onMessageRef.current = onMessage;
    }, [onMessage])
    
    useEffect(() => {
        if (!user || !destination) return;
            subscriptionRef.current = websocketService.subscribe(destination , (message) =>  onMessageRef.current?.(message))
        return () => {
            subscriptionRef.current?.unsubscribe()
            // setConnected(false);
        }
    },[destination])
    // return {connected}
}