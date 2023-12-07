import React from "react";
import { render, fireEvent, waitFor, screen } from "@testing-library/react";
import Wallet from "../components/Wallet";
import { MemoryRouter } from "react-router-dom";
import { toast } from "react-toastify";

jest.mock("react-toastify", () => ({
  error: jest.fn((message) => message),
  success: jest.fn((message) => message),
  toast: {
    error: jest.fn(),
    success: jest.fn(),
  },
}));

describe("Wallet Component", () => {
  beforeEach(() => {
    localStorage.setItem("fullname", "Narendra Singh");
    localStorage.setItem("walletId", "6501a220be84cb2861314294");
    localStorage.setItem("balance", "1000");
    localStorage.setItem("token", "token1234");
  });

  afterEach(() => {
    localStorage.clear();
  });

  it("renders wallet details correctly", () => {
    render(
      <MemoryRouter>
        <Wallet />
      </MemoryRouter>
    );

    expect(screen.getByTestId("wallet-holder")).toHaveTextContent(
      "Wallet Holder: Narendra Singh"
    );
    expect(screen.getByTestId("balance-holder")).toHaveTextContent(
      "Wallet Balance: Rs 1000"
    );
  });

  it("handles wallet recharge correctly", async () => {
    global.fetch = jest.fn().mockResolvedValueOnce({
      ok: true,
      json: async () => 1050,
    });
    render(
      <MemoryRouter>
        <Wallet />
      </MemoryRouter>
    );

    const rechargeAmount = 50;
    const rechargeButton = screen.getByText("Recharge");
    fireEvent.change(screen.getByLabelText("Recharge Amount"), {
      target: { value: rechargeAmount },
    });

    await waitFor(() => {
      fireEvent.click(rechargeButton);
    });
    await waitFor(() => {
      expect(toast.success).toHaveBeenCalledWith(
        "wallet recharged successfully"
      );
      expect(screen.getByText("Wallet Balance: Rs 1050")).toBeInTheDocument();
      expect(
        screen.getByText("Wallet Holder: Narendra Singh")
      ).toBeInTheDocument();
    });
  });

  it("handles wallet transfer correctly", async () => {
    render(
      <MemoryRouter>
        <Wallet />
      </MemoryRouter>
    );
    global.fetch = jest.fn().mockResolvedValueOnce({
      ok: true,
      json: async () => 800,
    });

    const receiverEmailId = "test@example.com";
    const amount = 200;
    const transferButton = screen.getByRole("button", { name: "Transfer" });

    fireEvent.change(screen.getByLabelText("Email Id"), {
      target: { value: receiverEmailId },
    });

    fireEvent.change(screen.getByLabelText("Transfer Amount"), {
      target: { value: amount },
    });
    await waitFor(() => {
      fireEvent.click(transferButton);
    });
    await waitFor(() => {
      expect(toast.success).toHaveBeenCalledWith(
        "money transfered successfully"
      );

      expect(screen.getByText("Wallet Balance: Rs 800")).toBeInTheDocument();
      expect(screen.getByLabelText("Email Id")).toHaveValue("");
      expect(screen.getByLabelText("Transfer Amount")).toHaveValue(null);
    });
  });

  describe("Wallet Component error with toastify", () => {
    it("should display error toast for invalid recharge amount", async () => {
      render(
        <MemoryRouter>
          <Wallet />
        </MemoryRouter>
      );

      const rechargeAmount = -50;
      const rechargeButton = screen.getByText("Recharge");
      fireEvent.change(screen.getByLabelText("Recharge Amount"), {
        target: { value: rechargeAmount },
      });

      await waitFor(() => {
        fireEvent.click(rechargeButton);
      });

      expect(toast.error).toHaveBeenCalledWith("Invalid recharge amount");
    });
    it("should display error toast for invalid email", async () => {
      render(
        <MemoryRouter>
          <Wallet />
        </MemoryRouter>
      );

      const receiverEmailId = "invalid-email";
      const amount = 200;
      const transferButton = screen.getByRole("button", { name: "Transfer" });

      fireEvent.change(screen.getByLabelText("Email Id"), {
        target: { value: receiverEmailId },
      });

      fireEvent.change(screen.getByLabelText("Transfer Amount"), {
        target: { value: amount },
      });
      await waitFor(() => {
        fireEvent.click(transferButton);
      });
      await waitFor(() => {
        expect(toast.error).toHaveBeenCalledWith("Invalid UserEmail");
      });
    });
    it("should display error toast for invalid transfer amount", async () => {
      render(
        <MemoryRouter>
          <Wallet />
        </MemoryRouter>
      );

      const receiverEmailId = "test@example.com";
      const amount = -200;
      const transferButton = screen.getByRole("button", { name: "Transfer" });

      fireEvent.change(screen.getByLabelText("Email Id"), {
        target: { value: receiverEmailId },
      });

      fireEvent.change(screen.getByLabelText("Transfer Amount"), {
        target: { value: amount },
      });
      await waitFor(() => {
        fireEvent.click(transferButton);
      });
      await waitFor(() => {
        expect(toast.error).toHaveBeenCalledWith("Invalid transfer amount");
      });
    });
    it("should display an error message when user does not exist (status 404)", async () => {
      global.fetch = jest.fn(() =>
        Promise.resolve({
          status: 404,
        })
      );

      render(
        <MemoryRouter>
          <Wallet />
        </MemoryRouter>
      );
      const transferButton = screen.getByRole("button", { name: "Transfer" });

      await waitFor(() => {
        fireEvent.click(transferButton);
      });

      await waitFor(() =>{
        expect(toast.error).toHaveBeenCalledWith("user does't exist")
      }
      );
    });
    it("should display an error message when transferring money to himself", async () => {
      global.fetch = jest.fn(() =>
        Promise.resolve({
          status: 409,
        })
      );

      render(
        <MemoryRouter>
          <Wallet />
        </MemoryRouter>
      );

      fireEvent.click(screen.getByText("Transfer"));

      await waitFor(() =>{
        expect(toast.error).toHaveBeenCalledWith(
          "better to recharge your wallet instead transferring"
        )}
      );
    });

    it("should display an error message for insufficient balance", async () => {
      global.fetch = jest.fn(() =>
        Promise.resolve({
          status: 400,
        })
      );
      render(
        <MemoryRouter>
          <Wallet />
        </MemoryRouter>
      );

      fireEvent.click(screen.getByText("Transfer"));

      await waitFor(() =>{
        expect(toast.error).toHaveBeenCalledWith(
          "Insufficient balance in your wallet"
        )
      
      }
      );
    });
  });
});
