import React, { useState, useEffect } from "react";
import {
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Paper,
} from "@mui/material";
import { Button } from "@mui/material";
import { toast } from "react-toastify";
import NavBarWallet from "./NavBarWallet";

function ViewTransaction() {
  const [transactions, setTransactions] = useState([]);
  const [currentPage, setCurrentPage] = useState(0);
  const [total, setTotal] = useState(0);
  const pageSize = 10;

  useEffect(() => {
    ViewTransactions();
  }, [currentPage]);

  const ViewTransactions = async () => {
    try {
      const walletId = localStorage.getItem("walletId");
      const token = localStorage.getItem("token");
      const response = await fetch(
        `http://localhost:8082/wallet/info/${walletId}?page=${currentPage}&size=${pageSize}`,
        {
          method: "get",
          headers: {
            Authorization: `Bearer ${token}`,
            "Content-Type": "application/json",
          },
        }
      );
      console.log(response);
      if (response.ok) {
        console.log(response);
        const responseData = await response.json();
        const transactionsData = responseData.content;
        console.log(transactionsData);
        setTotal(responseData.totalPage);
        console.log(total);
        setTransactions(transactionsData);
      }
    } catch (error) {
      toast.error("Error to fetch transactions:");
    }
  };

  const handleNextPage = () => {
    setCurrentPage(currentPage + 1);
  };

  const handlePreviousPage = () => {
      setCurrentPage(currentPage - 1);
  };

  const getTextColor = (type) => {
    switch (type) {
      case "DEPOSIT":
        return "green";
      case "RECEIVE":
        return "blue";
      case "SEND":
        return "red";
      default:
        return "";
    }
  };
  // console.log(total);
  return (
    <div>
      <NavBarWallet/>
      <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }}>
          <TableHead>
            <TableRow style={{ backgroundColor: "#eeeeee" }}>
              <TableCell style={{ fontSize: "16px" }}>Transaction ID</TableCell>
              <TableCell style={{ fontSize: "16px" }}>WalletId</TableCell>
              <TableCell style={{ fontSize: "16px" }}>Type</TableCell>
              <TableCell style={{ fontSize: "16px" }}>Amount</TableCell>
              <TableCell style={{ fontSize: "16px" }}>Timestamp</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {transactions.map((transaction) => (
              <TableRow key={transaction.transactionId}>
                <TableCell>{transaction.id}</TableCell>
                <TableCell>{transaction.email}</TableCell>
                <TableCell style={{ color: getTextColor(transaction.type) }}>
                  {transaction.type}
                </TableCell>
                <TableCell>{transaction.amount}</TableCell>
                <TableCell>{transaction.timestamp}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>
      <div>
        <Button
          variant="contained"
          onClick={handlePreviousPage}
          disabled={currentPage === 0}
        >
          &laquo;
        </Button>
        <Button
          sx={{ margin: 1 }}
          variant="contained"
          onClick={handleNextPage}
          disabled={currentPage === total - 1}
        >
          &raquo;
        </Button>
      </div>
    </div>
  );
}

export default ViewTransaction;
