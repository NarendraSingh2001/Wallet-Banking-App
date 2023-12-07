import React from "react";
import { render, screen, fireEvent, waitFor } from "@testing-library/react";
import UserRegistrationForm from "../components/UserRegistrationForm";
import { MemoryRouter } from "react-router-dom";
import axios from "axios";
jest.mock("axios");
describe("UserRegistrationForm", () => {
  const fillInForm = (values) => {
    fireEvent.change(screen.getByTestId("first-name-input"), {
      target: { name: "firstname", value: values.firstname },
    });

    fireEvent.change(screen.getByTestId("last-name-input"), {
      target: { name: "lastname", value: values.lastname },
    });

    fireEvent.change(screen.getByTestId("email-address-input"), {
      target: { name: "email", value: values.email },
    });

    fireEvent.change(screen.getByTestId("password-input"), {
      target: { name: "password", value: values.password },
    });
  };

  it("renders the UserRegistrationForm component", () => {
    render(
      <MemoryRouter>
        <UserRegistrationForm />
      </MemoryRouter>
    );

    expect(screen.getByText("Sign up")).toBeInTheDocument();
    expect(screen.getByTestId("first-name-input")).toBeInTheDocument();
    expect(screen.getByTestId("last-name-input")).toBeInTheDocument();
    expect(screen.getByTestId("email-address-input")).toBeInTheDocument();
    expect(screen.getByTestId("password-input")).toBeInTheDocument();
  });

  it("submits the form with valid data", async () => {
    render(
      <MemoryRouter>
        <UserRegistrationForm />
      </MemoryRouter>
    );

    fillInForm({
      firstname: "aman",
      lastname: "kumar",
      email: "aman12@example.com",
      password: "password123",
    });
    const mockResponse = {
      data: {
        jwtToken: "token",
        walletId: "1",
        balance: 0,
        name: "aman kumar",
      },
    };
    axios.mockResolvedValue(mockResponse);

    await waitFor(() => {
      fireEvent.click(screen.getByText("Sign Up"));
    });

    expect(axios).toHaveBeenCalledWith({
      url: "http://localhost:8082/user/signup",
      method: "post",
      data: {
        firstname: "aman",
        lastname: "kumar",
        email: "aman12@example.com",
        password: "password123",
      },
    });

  });

  it("displays errors for invalid form data", async () => {
    render(
      <MemoryRouter>
        <UserRegistrationForm />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByText("Sign Up"));

    await screen.findByText("First Name is required");
    await screen.findByText("Last Name is required");
    await screen.findByText("That doesn't look like an email address");
    await screen.findByText("Password must be at least 8 characters long");
  });
});
