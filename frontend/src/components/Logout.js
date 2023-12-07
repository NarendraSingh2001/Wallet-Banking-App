import React, { useEffect, useState } from "react";
import Button from "@mui/material/Button";
import Dialog from "@mui/material/Dialog";
import DialogActions from "@mui/material/DialogActions";
import DialogContent from "@mui/material/DialogContent";
import DialogContentText from "@mui/material/DialogContentText";
import DialogTitle from "@mui/material/DialogTitle";
import { useNavigate } from "react-router-dom";
function Logout() {
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();
  const handleLogout = async () => {
    console.log("clicked");
    try {
      const token = localStorage.getItem("token");

      console.log("token", token);
      const response = await fetch(`http://localhost:8082/wallet/logout`, {
        method: "post",
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "application/json",
        },
      });
      if (response.ok) {
        console.log(response);
        localStorage.clear();
        navigate("/", { replace: true });

        setOpen(false);
        window.location.reload();
      }
    } catch (error) {
      console.error("Error in to logout:", error);
    }
  };
  useEffect(() => {}, [handleLogout]);

  return (
    <div>
       
      <Button
        variant="contained"
        color="primary"
        onClick={() => setOpen(true)}
        data-testid="logout-button"
        // sx={{bgcolor:deepOrange[700],
        //   "&:hover":{bgcolor:deepOrange[600]}
        // }}
       
      >
        Logout
      </Button>
      <Dialog  open={open} onClose={() => setOpen(false)}>
        <DialogTitle>Logout Confirmation</DialogTitle>
        <DialogContent>
          <DialogContentText>
            Are you sure you want to logout?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button
            onClick={() => setOpen(false)}
            color="primary"
            data-testid="cancel-button"
          >
            Cancel
          </Button>
          <Button
            onClick={() => handleLogout()}
            color="primary"
            data-testid="logout-confirmation-button"
          >
            Logout
          </Button>
        </DialogActions>
      </Dialog>
    </div>
  );
}

export default Logout;
