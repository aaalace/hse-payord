import { useEffect, useState } from "react";
import api from "../api/api";
import {
    connectWebSocket,
    sendOrder,
} from "../api/ws.ts";
import {useNavigate} from "react-router-dom";

export interface Order {
    id: string;
    amount: number;
    status: string;
}

const OrdersPage = () => {
    const navigate = useNavigate();

    const [wsReady, setWsReady] = useState(false);
    const [orders, setOrders] = useState<Order[]>([]);
    const [balance, setBalance] = useState<number | null>(null);

    const userId = localStorage.getItem("userId");

    useEffect(() => {
        if (!userId) {
            return;
        }

        connectWebSocket(
            userId,
            () => setWsReady(true),
            () => setWsReady(false),
            async (msg: Order) => {
                setOrders((prev) => {
                    const index = prev.findIndex((o) => o.id === msg.id);
                    if (index === -1) {
                        return [
                            ...prev,
                            {
                                id: msg.id,
                                amount: msg.amount,
                                status: msg.status
                            }
                        ];
                    } else {
                        const updated = [...prev];
                        updated[index] = {
                            id: msg.id,
                            amount: msg.amount,
                            status: msg.status
                        };
                        return updated;
                    }
                });
                const balanceResp = await api.get(`/payment/v1/balance/${userId}`);
                setBalance(balanceResp.data.data.balance);
            }
        );

        const fetchData = async () => {
            const balanceResp = await api.get(`/payment/v1/balance/${userId}`);
            setBalance(balanceResp.data.data.balance);

            const ordersResp = await api.get(`/order/v1/order/${userId}`);
            setOrders(ordersResp.data.data);
        };

        void fetchData();
    }, [userId]);

    const createOrder = () => {
        if (!wsReady || !userId) return;

        const amount = Number(prompt("Введите стоимость заказа", "10"));
        if (!amount || isNaN(amount)) return;

        sendOrder({ userId, price: amount });
    };

    const topUp = async () => {
        const amount = Number(prompt("Введите сумму пополнения", "100"));
        if (!amount || !userId || isNaN(amount)) return;

        await api.post(`/payment/v1/balance/deposit`, { userId, amount });
        const updatedAmount = await waitForBalanceUpdate(userId);
        setBalance(updatedAmount);
    };

    const waitForBalanceUpdate = async (userId: string, maxAttempts = 5, delay = 1000): Promise<number> => {
        for (let attempt = 0; attempt < maxAttempts; attempt++) {
            const res = await api.get(`/payment/v1/balance/${userId}`);
            if (!balance && res.data.data.balance > 0 || balance && res.data.data.balance > balance) {
                return res.data.data.balance;
            }
            await new Promise((r) => setTimeout(r, delay));
        }
        throw new Error("Баланс не обновился вовремя");
    };

    const checkOrderStatus = async (orderId: string): Promise<void> => {
        const res = await api.get(`/order/v1/order/${orderId}/status`);
        alert("Статус заказа: " + res.data.data.status);
    };

    return (
        <div>
            <h2>Пользователь: {userId} <span style={{color: "blue", textDecoration: "underline", fontSize: "18px", cursor: "default"}}
                                             onClick={() => navigate("/")}>[ смена пользователя ]</span></h2>
            <h3>Баланс: {balance}</h3>

            <button onClick={createOrder} disabled={!wsReady}>
                Создать заказ
            </button>
            <button onClick={topUp}>Пополнить баланс</button>

            <h3>Заказы:</h3>
            <ul>
                {orders.map((order) => (
                    <li key={order.id}>
                        {order.id} — Сумма: {order.amount} - Статус: {order.status}
                        &nbsp;
                        <button onClick={() => checkOrderStatus(order.id)}>
                            Узнать статус
                        </button>
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default OrdersPage;