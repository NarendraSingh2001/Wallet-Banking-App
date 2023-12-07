import React from "react";
import {screen, render, fireEvent, waitFor } from "@testing-library/react";
import ViewTransaction from "../components/ViewTransaction";

global.fetch = jest.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve({}),
  })
);
describe("ViewTransaction Component", () => {
  it("renders without crashing", async() => {
    render(<ViewTransaction />);

    await waitFor(()=>{
      expect(screen.getByText(/transaction id/i)).toBeInTheDocument();
      expect(screen.getByText("WalletId")).toBeInTheDocument();
    })
    
  });

  it("fetches and displays transactions", async () => {
    const mockTransactions = [
      {
        transactionId: 1,
        WalletId: "walletId1",
        type: "DEPOSIT",
        amount: 100,
        timestamp: "2023-09-20",
      },
      {
        transactionId: 2,
        WalletId: "walletId2",
        type: "RECEIVE",
        amount: 50,
        timestamp: "2023-09-21",
      },
    ];

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ content: mockTransactions, totalPage: 2 }),
    });

    const { getByText } = render(<ViewTransaction />);

    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        "http://localhost:8082/wallet/info/null?page=0&size=10",

        {
          method: "get",
          headers: {
            Authorization: "Bearer null",
            "Content-Type": "application/json",
          },
        }
      );
    });

    expect(getByText("DEPOSIT")).toBeInTheDocument();
    expect(getByText("RECEIVE")).toBeInTheDocument();
    expect(getByText("WalletId")).toBeInTheDocument();
    expect(getByText("Amount")).toBeInTheDocument();
    expect(getByText("100")).toBeInTheDocument();
    expect(getByText("50")).toBeInTheDocument();
  });

  it("navigates to the nextPage and prevPage", async () => {
    const { getByText } = render(<ViewTransaction />);

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ content: [], totalPage: 3 }),
    });
    const nextPageButton = getByText("»");
    await waitFor(() => {
      fireEvent.click(nextPageButton);
    });
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        "http://localhost:8082/wallet/info/null?page=0&size=10",

        {
          method: "get",
          headers: {
            Authorization: "Bearer null",
            "Content-Type": "application/json",
          },
        }
      );
    });

    global.fetch.mockResolvedValueOnce({
      ok: true,
      json: () => Promise.resolve({ content: [], totalPage: 3 }),
    });
    const previousPageButton = getByText("«");
    await waitFor(() => {
      fireEvent.click(previousPageButton);
    });
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        `http://localhost:8082/wallet/info/null?page=0&size=10`,

        {
          method: "get",
          headers: {
            Authorization: "Bearer null",
            "Content-Type": "application/json",
          },
        }
      );
    });
  });
});