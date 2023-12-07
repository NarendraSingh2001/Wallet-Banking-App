import React, { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { flexbox } from "@mui/system";

import "react-toastify/dist/ReactToastify.css";
import {
  Button,
  Container,
  Paper,
  Typography,
  Box,
  TextField,
  FormControl,
} from "@mui/material";
import NavBarWallet from "./NavBarWallet";
function Wallet() {
  const navigate = useNavigate();
  const [userData, setUserData] = useState({
    fullname: "",
    balance: localStorage.getItem("balance"),
  });
  const [recharge, setRecharge] = useState("");
  const [transferData, setTransferData] = useState({
    receiverEmailId: "",
    transferAmount: "",
  });
  useEffect(() => {}, []);
  const handleRecharge = async (amount) => {
    if (amount < 1) {
      toast.error("Invalid recharge amount");
    }
    try {
      console.log(amount);
      const token = localStorage.getItem("token");
      console.log("token", token);
      const walletId = localStorage.getItem("walletId");
      const response = await fetch(
        `http://localhost:8082/wallet/recharge/${walletId}?amount=${amount}`,
        {
          method: "post",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      if (response.ok) {
        const data = await response.json();
        console.log("response from wallet", data);

        const updatedBalance = data;
        localStorage.setItem("balance", updatedBalance);
        setUserData({ ...userData, ["balance"]: updatedBalance });

        setRecharge("");
        console.log(recharge);
        toast.success("wallet recharged successfully");
      }
    } catch (error) {
      toast.error("Error to racharge wallet:", error);
    }
  };

  const handleOnChangeTransfer = (event) => {
    setTransferData({ ...transferData, [event.target.id]: event.target.value });
    console.log(transferData);
  };

  const handleTransfer = async (event) => {
    if (
      !transferData.receiverEmailId.match(
        /^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/
      )
    )
      toast.error("Invalid UserEmail");
    if (transferData.transferAmount < 1) {
      toast.error("Invalid transfer amount");
    }
    console.log(transferData);
    try {
      console.log(transferData);
      const token = localStorage.getItem("token");
      const senderWalletId = localStorage.getItem("walletId");
      const receiverEmailId = transferData.receiverEmailId;
      const amount = transferData.transferAmount;
      console.log("token", token);
      const response = await fetch(
        `http://localhost:8082/wallet/transfer?senderWalletId=${senderWalletId}&receiverEmailId=${receiverEmailId}&amount=${amount}`,
        {
          method: "post",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log(response);

      if (response.ok) {
        const data = await response.json();
        const updatedBalance = data;
        console.log(data);
        localStorage.setItem("balance", updatedBalance);

        setUserData({ ...userData, ["balance"]: updatedBalance });
        setTransferData({
          receiverEmailId: "",
          transferAmount: "",
        });
        toast.success("money transfered successfully");
      } else if (response.status == 404) {
        toast.error("user does't exist");
      } else if (response.status == 409)
        toast.error("better to recharge your wallet instead transferring");
      else toast.error("Insufficient balance in your wallet");
    } catch (error) {
      console.log("Error in transfering money:", error);
    }
  };
  useEffect(() => {
    setUserData({
      fullname: localStorage.getItem("fullname"),
      balance: localStorage.getItem("balance"),
    });
  }, [setUserData, setTransferData]);

  const hadleViewTransaction = () => {
    console.log("view clicked");
    navigate("transaction");
  };
  return (
    <>
      <NavBarWallet />
      <Container sx={{}} maxWidth="sm">
        <Paper elevation={5} sx={{ padding: 3, margin: 3}}>
          <Box marginBottom={1}>
            <Typography
              variant="h5"
              sx={{ margin: 0 }}
              data-testid="wallet-holder"
            >
              Wallet Holder: {userData.fullname}
            </Typography>

            <Typography
              variant="h5"
              sx={{ margin: 0 }}
              data-testid="balance-holder"
            >
              Wallet Balance: Rs {userData.balance}
            </Typography>
          </Box>
          {/* Recharge Form */}

          <FormControl sx={{ marginBottom: 1, width: "100%" }}>
            <TextField
              margin="normal"
              type="number"
              id="amount"
              onChange={(e) => setRecharge(e.target.value)}
              label="Recharge Amount"
              inputProps={{ "data-testid": "recharge-amount-input" }}
              variant="outlined"
              value={recharge}
            />

            <Button
              type="submit"
              variant="contained"
              color="primary"
              onClick={() => handleRecharge(recharge)}
            >
              Recharge
            </Button>
            {/* </Box> */}
          </FormControl>

          {/* Transfer Form */}
          <FormControl sx={{ marginBottom: 3}}>
            <Box sx={{ display: "flex", gap: "16px" }}>
              <TextField
                value={transferData.receiverEmailId}
                id="receiverEmailId"
                onChange={(e) => handleOnChangeTransfer(e)}
                margin="normal"
                type="text"
                label="Email Id"
                variant="outlined"
              />

              <TextField
                value={transferData.transferAmount}
                id="transferAmount"
                onChange={(e) => handleOnChangeTransfer(e)}
                margin="normal"
                type="number"
                label="Transfer Amount"
                variant="outlined"
              />
            </Box>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              onClick={() => handleTransfer()}
            >
              Transfer
            </Button>
          </FormControl>

          {/* Account Statement */}
          <Box sx={{  }}>
            <Button
              variant="outlined"
              color="primary"
              onClick={hadleViewTransaction}
            >
              View Account Statement
            </Button>
          </Box>
        </Paper>
      </Container>
    </>
  );
}

export default Wallet;
