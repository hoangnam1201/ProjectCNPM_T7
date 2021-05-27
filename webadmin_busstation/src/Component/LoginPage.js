import React, { useContext, useState } from "react";
import logo_bus from "./assets/logobus1.png";
import "./style.scss";
import styled from "styled-components";
import { Button, TextField } from "@material-ui/core";
import AppContext from "./AppContext";
import { useHistory } from "react-router";
import axios from "axios";

const Hr = styled.hr`
  margin: 40px 0;
`;
const LoginPage = () => {
  const { dispatch } = useContext(AppContext);
  const [userInput, setUserInput] = useState({ username: "", password: "" });
  const onChangeHandle = (e) => {
    setUserInput({ ...userInput, [e.target.name]: e.target.value });
  };
  const submitHandle = async (e) => {
    try {
      e.preventDefault();
      console.log(userInput);
      const fetch = {
        method: "post",
        url: "https://busapbe.herokuapp.com/api/auth/login",
        data: userInput,
      };
      const response = await axios(fetch);
      console.log(response.data);
      const { accessToken, refreshToken } = response.data;
      localStorage.setItem("accessToken", accessToken);
      localStorage.setItem("refreshToken", refreshToken);
      // dispatch({type: "CURRENT_USER",payload: {username}})

      const fetch2 = {
        method: "post",
        url: "http://localhost:3002/api/users/get-infor",
        Headers: {
          authorization: localStorage.getItem("accessToken"),
        },
      };
      console.log(fetch2);
      axios(fetch2)
        .then((response) => {
          console.log(response.data);
        })
        .catch((err) => {
          console.log(err);
        });
    } catch (error) {
      console.log(error);
    }

    // e.preventDefault();
    // const fetch = {
    //   method: "post",
    //   url: "https://busapbe.herokuapp.com/api/auth/login",
    //   data: userInput,
    // };
    // axios(fetch)
    //   .then((response) => {
    //     console.log(response);
    //     const { accessToken, refreshToken } = response.data;
    //     localStorage.setItem("accessToken", accessToken);
    //     localStorage.setItem("refreshToken", refreshToken);

    //     const fetch2 = {
    //       method: "post",
    //       url: "https://busapbe.herokuapp.com/api/users/get-infor",
    //       Headers: {
    //         Authorization:
    //           "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiI2MDgyZjA4ZDRhZmJhMTAwMjI2MDZmNjIiLCJpYXQiOjE2MjIxMzQ0NDMsImV4cCI6MTYyMjEzNDc0M30.Jsk16Fs41TfKSnc9ZpuSVGpYOGbT8qml5gYqCWsDrbA",
    //       },
    //     };
    //     console.log(fetch2);
    //     axios(fetch2)
    //       .then((response) => {
    //         console.log(response.data);
    //       })
    //       .catch((err) => {
    //         console.log(err);
    //       });
    //   })
    //   .catch((err) => {
    //     console.log(err);
    //   });
  };

  return (
    <div className="login-page">
      <div className="container-md p-0 h-100">
        <form className="login-form col-12 col-xl-4 col-lg-5 col-md-6 col-sm-6">
          <img alt="logo truong" src={logo_bus} className="w-75 h-75" />
          <Hr />
          <h5 className="text-center primary-logo-color font-weight-bold">
            TRANG QUẢN LÝ BUS STATION
          </h5>
          <small className="d-block sub-logo-color text-center">
            NHÓM SINH VIÊN SƯ PHẠM KỸ THUẬT TP HCM
          </small>
          <h6 className="mt-5">Thông tin đăng nhập:</h6>
          <form
          // onKeyDown={handleEnterKey}
          >
            <TextField
              className="w-100 mt-3"
              label="Tài khoản"
              name="username"
              value={userInput.username}
              onChange={onChangeHandle}
              // value={username}
              // onChange={e => setUsername(e.target.value)}
              // error={error.username}
            />
            <TextField
              className="w-100 mt-3 mb-2"
              type="password"
              label="Mặt khẩu"
              name="password"
              value={userInput.password}
              onChange={onChangeHandle}
              // value={password}
              // onChange={e => setPassword(e.target.value)}
              // error={error.password}
            />
            {/* {error.message &&
                        <div className="alert alert-danger p-2 mt-2">
                            {error.message}
                        </div>
                    } */}
            <Button
              fullWidth
              className="mt-2"
              variant="contained"
              color="primary"
              // onClick={handleLogin}
              // onKeyDown={handleEnterKey}
              // disabled={loading}
              onClick={submitHandle}
            >
              Đăng nhập
            </Button>
          </form>
          <Hr />
          <footer className="text-center">
            <small>©2021 Đại học Sư Phạm Kỹ Thuật thành phố Hồ Chí Minh</small>
          </footer>
        </form>
      </div>
    </div>
  );
};
export default LoginPage;
