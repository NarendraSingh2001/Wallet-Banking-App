import * as React from "react";
import AppBar from "@mui/material/AppBar";
import Box from "@mui/material/Box";
import Toolbar from "@mui/material/Toolbar";
import Typography from "@mui/material/Typography";
import Logout from "./Logout";
import CssBaseline from "@mui/material/CssBaseline";
import Avatar from "@mui/material/Avatar";
import {green } from "@mui/material/colors";
export default function NavBarWallet() {
  return (
    <>
      <CssBaseline />
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static" sx={{ backgroundColor: "transparent" }}>
          <Toolbar>
            <Avatar sx={{ bgcolor: green[400] }}>{localStorage.getItem("fullname")[0]}</Avatar>{" "}
            <Typography variant="h6" component="div" sx={{ flexGrow: 1 ,color:"black"}}>
              Wallet Pro
            </Typography>
            <Logout />
          </Toolbar>
        </AppBar>
      </Box>
    </>
  );
}
