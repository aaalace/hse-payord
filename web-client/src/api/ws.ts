import type {Order} from "../pages/OrdersPage.tsx";

export interface OrderRequestDTO {
    userId: string;
    price: number;
}

let socket: WebSocket | null = null;

type MessageCallback = (data: Order) => void;

let onMessageCallback: MessageCallback | null = null;

export function connectWebSocket(
    userId: string,
    onSuccess: () => void,
    onError: (e: Event) => void,
    onMessage: MessageCallback
) {
    socket = new WebSocket(`ws://localhost:80/order/ws/order?userId=${userId}`);

    socket.onopen = () => {
        console.log("WebSocket connected");
        onMessageCallback = onMessage;
        onSuccess();
    };

    socket.onerror = (e) => {
        console.error("WebSocket error", e);
        onMessageCallback = null;
        onError(e);
    };

    socket.onclose = () => {
        console.log("WebSocket closed");
    };

    socket.onmessage = (event) => {
        try {
            const data = JSON.parse(event.data) as Order;
            if (onMessageCallback) onMessageCallback(data);
        } catch (e) {
            console.error("Invalid WebSocket message", e);
        }
    };
}

export function sendOrder(data: OrderRequestDTO) {
    if (socket && socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(data));
    } else {
        console.warn("WebSocket not connected.");
    }
}