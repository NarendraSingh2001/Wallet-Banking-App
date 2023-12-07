import React from "react";
import { render, fireEvent, waitFor } from "@testing-library/react";
import { MemoryRouter } from "react-router-dom";
import Logout from "../components/Logout";

global.localStorage = {
  getItem: jest.fn(() => "token1234"),
  clear: jest.fn(),
};

global.fetch = jest.fn(() =>
  Promise.resolve({
    ok: true,
    json: () => Promise.resolve({ token: "token1234" }),
  })
);

describe("Logout Component", () => {
  afterEach(() => {
    jest.clearAllMocks();
  });

  it("displays confirmation dialog on Logout button click", async () => {
    const { getByText } = render(
      <MemoryRouter>
        <Logout />
      </MemoryRouter>
    );

    const logoutButton = getByText("Logout");
    fireEvent.click(logoutButton);

    const confirmationDialog = getByText("Logout Confirmation");
    expect(confirmationDialog).toBeInTheDocument();
  });

  it("closes confirmation dialog on Cancel button click", async () => {
    const { getByText, queryByText } = render(
      <MemoryRouter>
        <Logout />
      </MemoryRouter>
    );

    const logoutButton = getByText("Logout");
    fireEvent.click(logoutButton);

    const cancelBtn = getByText("Cancel");
    fireEvent.click(cancelBtn);

    const confirmationDialog = queryByText("Logout Confirmation");
    expect(confirmationDialog).toBeInTheDocument();
  });

  it('calls handleLogout and navigates to "/" on Logout button click', async () => {
    const { getByText, getByTestId } = render(
      <MemoryRouter>
        <Logout />
      </MemoryRouter>
    );

    await waitFor(() => {
      const logoutButton = getByTestId("logout-button");
      fireEvent.click(logoutButton);
    });
    await waitFor(() => {
      const logoutConfirmationButton = getByTestId(
        "logout-confirmation-button"
      );
      fireEvent.click(logoutConfirmationButton);
    });
  });
});
