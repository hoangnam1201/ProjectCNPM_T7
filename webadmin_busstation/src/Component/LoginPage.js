import React from 'react'
import logo_bus from './assets/logobus1.png'
import './style.scss';
import styled from 'styled-components'
import { Button, TextField } from '@material-ui/core'

const Hr = styled.hr`
    margin: 40px 0;
`
const LoginPage = () =>{
   return <div className="login-page">
        <div className="container-md p-0 h-100">
            <form className="login-form col-12 col-xl-4 col-lg-5 col-md-6 col-sm-6">
                <img alt="logo truong" src={logo_bus} className="w-75 h-75" />
                <Hr />
                <h5 className='text-center primary-logo-color font-weight-bold'>
                    TRANG QUẢN LÝ BUS STATION
                </h5>
                <small className="d-block sub-logo-color text-center">
                    NHÓM SINH VIÊN SƯ PHẠM KỸ THUẬT TP HCM
                </small>
                <h6 className='mt-5'>Thông tin đăng nhập:</h6>
                <form
                    // onKeyDown={handleEnterKey}
                >
                    <TextField
                        className='w-100 mt-3'
                        label="Tài khoản"
                        // value={username}
                        // onChange={e => setUsername(e.target.value)}
                        // error={error.username}
                    />
                    <TextField
                        className='w-100 mt-3 mb-2'
                        type="password"
                        label="Mặt khẩu"
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
}
export default LoginPage;