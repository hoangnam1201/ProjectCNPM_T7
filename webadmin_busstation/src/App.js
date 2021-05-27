import React from 'react'
import LoginPage from './Component/LoginPage'
import './Component/style.scss';
import Sidebar from './Component/Appbar/Sidebar'
import {Switch, Route} from 'react-router-dom'
import HomePage from './Component/HomePage'
import Appbar from './Component/Appbar';
import BusList from './Component/BusList';
import BusForm from './Component/BusList/BusForm';
import UserList from './Component/UserList';
import BusStopList from './Component/BusStopList';
import BusStopForm from './Component/BusStopList/BusStopForm';
function App() {
  return (
    <div className="wrapper">
        {/* <LoginPage/> */}
        <Sidebar class="persist"/>
        <Appbar/>
        <div className="content">
          <main>
            <div className="container flex-fill">
              <Switch>
                <Route exact path="/">
                    <HomePage/>
                </Route>
                <Route path="/bus">
                    <BusList/>
                </Route>
                <Route path="/bus:/create">
                    <BusForm/>
                </Route>
                 <Route path="/user">
                    <UserList/>
                </Route>
                <Route path="/busstop">
                    <BusStopList/>
                </Route>
                <Route path="/busstop:/create">
                    <BusStopForm/>
                </Route>
              </Switch>
            </div>
        </main>
        </div>
    </div>
  );
}

export default App;