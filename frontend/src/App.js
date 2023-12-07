import React, { useEffect } from "react";
import "./App.css";
import {
  BrowserRouter as Router,
  Route,
  Routes,

} from "react-router-dom";
import UserRegistrationForm from "./components/UserRegistrationForm";
import Wallet from "./components/Wallet";
import ViewTransaction from "./components/ViewTransaction";
import { ToastContainer } from "react-toastify";

function App() {
  const response = localStorage.getItem("token");
  // const navigate=useNavigate();
  useEffect(() => {}, [response]);

  return (
    <Router>
      <Routes>
        {!response ? (
          <Route path="/" element={<UserRegistrationForm />} />
        ) : null}{" "}
        {response ? <Route path="/wallet" element={<Wallet />} /> : null}
        <Route
          path="/wallet/transaction"
          element={response && <ViewTransaction />}
        />
      </Routes>
      <ToastContainer />
    </Router>
  );
}

export default App;
