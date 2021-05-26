import React from 'react'
import { Drawer, IconButton } from '@material-ui/core'
import { useRef, useState } from 'react'
import { AiFillSetting, AiOutlineMenu } from 'react-icons/ai'
import { Link } from 'react-router-dom'
import Sidebar from './Sidebar'
import logobus from '../assets/logobus1.png'

const Appbar = () =>{
    const [sidebarToggle, setSidebarToggle] = useState(false)
    const [userMenuToggle, setUserMenuToggle] = useState(false)
    const anchorRef = useRef(null)
    const menuRef = useRef(null)
    return(
        <div className="appbar">
            <IconButton id="toggle-button" onClick={() => setSidebarToggle(!sidebarToggle)}>
                <AiOutlineMenu />
            </IconButton>
            <Link to="/">
                <img src={logobus} alt="logo" height="50px" className="mx-3" />
            </Link>
            <div ref={menuRef} className="position-relative">
                <IconButton
                    ref={anchorRef}
                    onClick={() => setUserMenuToggle(!userMenuToggle)}
                >
                    <AiFillSetting size="30px" />
                </IconButton>
        
            </div>
            <Drawer open={sidebarToggle} onClose={() => setSidebarToggle(false)}>
                <Sidebar close={() => setSidebarToggle(false)} />
            </Drawer>
        </div>
    )
}
export default Appbar;