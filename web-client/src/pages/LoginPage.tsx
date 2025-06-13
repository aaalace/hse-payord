import {useState} from "react";
import {useNavigate} from "react-router-dom";
import api from "../api/api";

const LoginPage = () => {
    const navigate = useNavigate();

    const [userId, setUserId] = useState("");

    const handleLogin = async () => {
        try {
            await api.get(`/payment/v1/balance/${userId}`);
            localStorage.setItem("userId", userId);
            navigate("/orders");
        } catch {
            try {
                await api.post(`/payment/v1/balance/open/${userId}`);
                alert("Новый аккаунт создан");
                localStorage.setItem("userId", userId);
                navigate("/orders");
            } catch (e) {
                alert(`Ошибка при создании баланса, ${e}`);
            }
        }
    };

    return (
        <div>
            <h2>Введите ваш User ID</h2>
            <input
                value={userId}
                onChange={(e) => setUserId(e.target.value)}
                placeholder="userId"
            />
            <button onClick={handleLogin}>Продолжить</button>
        </div>
    );
};

export default LoginPage;