import Header from "./components/header/Header";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import BankingPage from "./pages/bankingPage/BankingPage";
import HandleCallbackPage from "./pages/handleCallbackPage/HandleCallbackPage";
import { ToastContainer } from "react-toastify";
import PaymentPage from "./pages/paymentPay/PaymentPage";
import TransactionHistoryPage from "./pages/transactionHistoryPage/TransactionHistoryPage";
import BalancePage from "./pages/balancePage/BalancePage";

const App = () => {
  return (
    <>
      <BrowserRouter>
        <Header />
        <Routes>
          <Route path="/banking" element={<BankingPage />} />
          <Route path="/success" element={<HandleCallbackPage />} />
          <Route path="/transaction" element={<TransactionHistoryPage />} />
          <Route path="/qrcode" element={<PaymentPage />} />
          <Route path="/balance" element={<BalancePage />} />
        </Routes>
      </BrowserRouter>
      <ToastContainer />
    </>
  );
};
export default App;
