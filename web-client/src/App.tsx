import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import OrdersPage from "./pages/OrdersPage";

const App = () => (
    <Router>
        <Routes>
            <Route path="/" element={<LoginPage />} />
            <Route path="/orders" element={<OrdersPage />} />
        </Routes>
    </Router>
);

export default App;