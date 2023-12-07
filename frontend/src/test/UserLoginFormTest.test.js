import React from "react";
import {
  render,
  fireEvent,
  waitFor,
  screen,
} from "@testing-library/react";
import axios from "axios";
import { MemoryRouter } from "react-router-dom";
import UserLoginForm from "../components/UserLoginForm";

jest.mock("axios");

describe("UserLoginForm", () => {
  it("renders the login form correctly", () => {
     render(
      <MemoryRouter>
        <UserLoginForm />
      </MemoryRouter>
    );

    expect(screen.getByText("Sign in")).toBeInTheDocument();
    expect(screen.getByTestId("email-address-input")).toBeInTheDocument();
    expect(screen.getByTestId("password-input")).toBeInTheDocument();
    expect(screen.getByText("Sign In")).toBeInTheDocument();
  });
  it("handles form submission correctly", async () => {
    const mockResponse = {
      data: {
        jwtToken: "token",
        walletId: "wallet-id",
        balance: 1000,
        name: "Narendra Singh",
      },
    };
    axios.mockResolvedValue(mockResponse);
render(
      <MemoryRouter>
        <UserLoginForm />
      </MemoryRouter>
    );

    const emailInput = screen.getByTestId("email-address-input");
    fireEvent.change(emailInput, {
      target: { value: "test@example.com" },
    });

    const passwordInput = screen.getByTestId("password-input");
    fireEvent.change(passwordInput, {
      target: { value: "password123" },
    });

    await waitFor(() => { fireEvent.click(screen.getByText("Sign In"))});
  
    await waitFor(() => {
      expect(axios).toHaveBeenCalledWith({
        url: "http://localhost:8082/user/signin",
        method: "post",
        data: {
          email: "test@example.com",
          password: "password123",
        },
      });
    });
    expect(emailInput.value).toBe("");
    expect(passwordInput.value).toBe("");

  });
  it("displays errors for invalid form data", async () => {
    render(
      <MemoryRouter>
        <UserLoginForm />
      </MemoryRouter>
    );
    fireEvent.click(screen.getByText("Sign In"));
    await screen.findByText("That doesn't look like an email address");
    await screen.findByText("Password must be at least 8 characters long");
  });
});
