import { Client, IMessage, StompSubscription } from "@stomp/stompjs";


class WebSocketService {
    private client: Client | null = null;
    private connectionInProgress = false;
    private subscribeQueue: Array<{ destination: string; callback: (data: any) => void }> = [];
    public isConnected: boolean = false;

    connect(token: string, onConnected: () => void) {

        if (this.connectionInProgress || (this.client && this.client.connected)) {
            return;
        }

        this.connectionInProgress = true;
    
        console.log('[WebSocket] Khởi tạo kết nối mới...');

        this.client = new Client({
            brokerURL: 'ws://localhost:8080/ws',
            // typeof window !== 'undefined' 
            // ? `${window.location.protocol === 'https:' ? 'wss' : 'ws'}://${window.location.host}/ws` 
            // : 'ws://localhost:8080/ws',
            connectHeaders: {
                Authorization: `Bearer ${token}`,
            },
            reconnectDelay: 5000,
            heartbeatIncoming: 10000,
            heartbeatOutgoing: 10000,
            debug: (str) => {
                console.log('[STOMP]:::', str);
            },
            onConnect: (frame) => {
                this.isConnected = true;
                this.connectionInProgress = false;
                console.log('[WebSocket] Connected successfully');

                this.subscribeQueue.forEach(item => {
                    console.log(`[WS] Đang thực hiện subscribe bù cho: ${item.destination}`);
                    this.client?.subscribe(item.destination, (frame) => item.callback(JSON.parse(frame.body)));
                });
                this.subscribeQueue = []; 
            },
            onStompError: (error) => {
                console.error('[WebSocket] STOMP error:::', error)
                this.connectionInProgress = false;
            },
            onWebSocketError: error => {
                console.error('[WebSocket] Connection error:::', error);
                this.connectionInProgress = false;
            }
        });
        this.client.activate();
    }

    subscribe(destination : string , callback: (message: any) => void)  {
        if(this.client?.connected) {
            return this.client.subscribe(destination, frame => {
                const messages = JSON.parse(frame.body);
                callback(messages);
            })
        }
        console.log(`[WS] Chưa có kết nối, đang đưa ${destination} vào hàng đợi...`);
        this.subscribeQueue.push({ destination, callback });

        return {
            unsubscribe: () => {
                // Xóa khỏi hàng đợi nếu chưa kịp sub đã unsub
                this.subscribeQueue = this.subscribeQueue.filter(item => item.destination !== destination);
        }
    };
    }


    disconnected() {
        if(this.client) {
            console.log('[WebSocket] Đang ngắt kết nối cũ...');
            this.client.deactivate();
            this.client = null;
        }
        this.isConnected = false;
        this.connectionInProgress = false;
    }
}

export const websocketService = new WebSocketService();