import * as React from "react";
import Avatar from "@mui/material/Avatar";
import Button from "@mui/material/Button";
import CssBaseline from "@mui/material/CssBaseline";
import TextField from "@mui/material/TextField";
import Grid from "@mui/material/Grid";
import Box from "@mui/material/Box";
import LockOutlinedIcon from "@mui/icons-material/LockOutlined";
import Typography from "@mui/material/Typography";
import Container from "@mui/material/Container";
import { createTheme, ThemeProvider } from "@mui/material/styles";
import { useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import NavBar from "./NavBar";
import UserLoginForm from "./UserLoginForm";

export default function UserRegistrationForm(props) {
  const navigate = useNavigate();
  const [loginClicked, setLoginClicked] = useState(false);
  const [formData, setFormData] = useState({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
  });
  const [errors, setErrors] = useState({
    firstname: "",
    lastname: "",
    email: "",
    password: "",
  });

  const handleInputChange = (event) => {
    setFormData({ ...formData, [event.target.name]: event.target.value });
  };
  console.log(formData);
  const handleRegistration = async (event) => {
    event.preventDefault();

    const newErrors = {};

    if (formData.firstname.trim() === "") {
      newErrors.firstname = "First Name is required";
    }

    if (formData.lastname.trim() === "") {
      newErrors.lastname = "Last Name is required";
    }

    if (!formData.email.match(/^[a-zA-Z0-9]+@[a-zA-Z0-9]+\.[a-zA-Z]{2,4}$/)) {
      newErrors.email = "That doesn't look like an email address";
    }

    if (formData.password.length < 8) {
      newErrors.password = "Password must be at least 8 characters long";
    }

    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }
    console.log(formData);
    try {
      const response = await axios({
        url: "http://localhost:8082/user/signup",
        method: "post",
        data: formData,
      }).then((response) => {
        if (response.data.name !== undefined) {
          // console.log("response from signup", response);
          localStorage.setItem("token", response.data.jwtToken);
          localStorage.setItem("walletId", response.data.walletId);
          localStorage.setItem("balance", response.data.balance);
          localStorage.setItem("fullname", response.data.name);
          navigate("wallet", { replace: true });
          window.location.reload();
        } else {
          toast.error("User with this email already exists.");
          //  console.error("Registration error: Response is undefined");
        }
      });

      // console.log("Registration successful!", response.data);
      // console.log(formData);
    } catch (error) {
      console.log("Registration error:", error);
    }

    setFormData({
      firstname: "",
      lastname: "",
      email: "",
      password: "",
    });
    setErrors({
      firstname: "",
      lastname: "",
      email: "",
      password: "",
    });
  };
  // console.log("setLoginClicked",loginClicked)
  return (
    <>
      <NavBar />
      {!loginClicked && (
          <Container component="main" maxWidth="xs">
            <CssBaseline />
            <Box
              sx={{
                marginTop: 8,
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
              }}
            >
              <Avatar sx={{ m: 1, bgcolor: "secondary.main" }}>
                <LockOutlinedIcon />
              </Avatar>
              <Typography component="h1" variant="h5">
                Sign up
              </Typography>
              <Box
                component="form"
                noValidate
                onSubmit={handleRegistration}
                sx={{ mt: 3 }}
              >
                <Grid container spacing={2}>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      autoComplete="given-name"
                      name="firstname"
                      required
                      fullWidth
                      id="firstname"
                      label="First Name"
                      inputProps={{ "data-testid": "first-name-input" }}
                      value={formData.firstname}
                      onChange={handleInputChange}
                      autoFocus
                      error={!!errors.firstname}
                      helperText={errors.firstname}
                    />
                  </Grid>
                  <Grid item xs={12} sm={6}>
                    <TextField
                      required
                      fullWidth
                      id="lastname"
                      label="Last Name"
                      inputProps={{ "data-testid": "last-name-input" }}
                      name="lastname"
                      value={formData.lastname}
                      onChange={handleInputChange}
                      autoComplete="family-name"
                      error={!!errors.lastname}
                      helperText={errors.lastname}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      required
                      fullWidth
                      id="email"
                      label="Email Address"
                      inputProps={{ "data-testid": "email-address-input" }}
                      name="email"
                      value={formData.email}
                      onChange={handleInputChange}
                      autoComplete="email"
                      error={!!errors.email}
                      helperText={errors.email}
                    />
                  </Grid>
                  <Grid item xs={12}>
                    <TextField
                      required
                      fullWidth
                      name="password"
                      label="Password"
                      inputProps={{ "data-testid": "password-input" }}
                      type="password"
                      id="password"
                      value={formData.password}
                      onChange={handleInputChange}
                      autoComplete="new-password"
                      helperText={errors.password}
                      error={!!errors.password}
                    />
                  </Grid>
                  <Grid item xs={12}></Grid>
                </Grid>
                <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{ mt: 3, mb: 2 }}
                >
                  Sign Up
                </Button>

                <Grid container justifyContent="flex-end">
                  <Grid item>
                    <Button href="" onClick={() => setLoginClicked(true)}>
                      {" "}
                      Already have an account? Sign in
                    </Button>
                  </Grid>
                </Grid>
              </Box>
            </Box>
          </Container>
       
      )}
      {loginClicked && <UserLoginForm setLoginClicked={setLoginClicked} />}
    </>
  );
}
