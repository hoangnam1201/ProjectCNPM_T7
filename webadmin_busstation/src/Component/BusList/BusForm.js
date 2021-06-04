import { Button, Drawer, TextField } from "@material-ui/core";
import React, { useContext, useState } from "react";
import styled from "styled-components";
import AppContext from "../AppContext";
import addbus from "../assets/addbus1.png";
import axios from "axios";
import DeleteIcon from "@material-ui/icons/Delete";
import UpIcon from "@material-ui/icons/ArrowUpward";
import TimeLineIcon from "@material-ui/icons/Timeline";
const Label = styled.label`
  font-size: 13px;
  font-weight: 600;
`;

const BusForm = () => {
  const { dispatch } = useContext(AppContext);
  const [busesInput, setBusesInput] = useState({
    id: "",
    operatingTime: "",
    timeDistance: "",
    name: "",
    price: "",
    seats: "",
    busstops: [],
  });
  const [errorMessage, setErrorMessage] = useState(null);
  const [busStopName, setBusSTopName] = useState("");
  const [busStops, setBusStops] = useState([]);
  const [openAddPointForm, setOpenAddPointForm] = useState(false);

  const [addPointInput, setAddPointInput] = useState({
    id: "",
    latitude: "",
    longitude: "",
  });
  const onchangeHandleAddPoint = (e) => {
    setAddPointInput({ ...addPointInput, [e.target.name]: e.target.value });
  };

  const toggleDrawer = (open) => (event) => {
    setOpenAddPointForm(open);

  };
  const onChangeName = async (e) => {
    setBusSTopName(e.target.value);
  };
  const onMoveUp = async (e) => {
    const busStopId = e.target.parentElement.getAttribute("busstopid");
    if (!busStopId) return;
    console.log(busStopId);
    const index = busStops.findIndex((x) => x.id == busStopId);
    const temp = busStops;
    const busStop = temp.splice(index, 1)[0];
    temp.splice(index - 1, 0, busStop);
    console.log(temp);
    setBusStops(temp);
    setBusesInput({ ...busesInput, busstops: temp.map((b) => b.id) });
  };
  const onAddBusStop = async () => {
    const fetch = {
      method: "get",
      url: "https://busapbe.herokuapp.com/api/busstops/search-name",
      headers: {
        Authorization: "Token " + localStorage.getItem("accessToken"),
      },
      params: {
        name: busStopName,
      },
    };
    await axios(fetch)
      .then((response) => {
        if (response.status == 200 && response.data) {
          console.log(response.data._id);
          if (busStops.find((b) => b.id === response.data._id)) {
            console.log("this already exist");
            return;
          }
          setBusStops([
            ...busStops,
            { id: response.data._id, name: response.data.name },
          ]);
          
          setBusesInput({
            ...busesInput,
            busstops: [...busesInput.busstops, response.data._id],
          });
        } else {
        }
      })
      .catch((err) => {
        console.log(err);
      });
  };
  const onDeleteBusStop = (e) => {
    const busStopId = e.target.parentElement.getAttribute("busstopid");
    if (!busStopId) return;
    console.log(e.target.parentElement);
    setBusStops(
      busStops.filter((b) => {
        return b.id != busStopId;
      })
    );
    setBusesInput({
      ...busesInput,
      busstops: busesInput.busstops.filter((b) => b != busStopId),
    });
  };
  const onAddPoint = () => {
  };
  const onchangeHandle = (e) => {
    setBusesInput({ ...busesInput, [e.target.name]: e.target.value });
  };

  const submitHandle = async (e) => {
    try {
      const fetch = {
        method: "post",
        url: "https://busapbe.herokuapp.com/api/buses/add",
        headers: {
          Authorization: "Token " + localStorage.getItem("accessToken"),
        },
        data: busesInput,
      };
      const bus = await axios(fetch);
      dispatch({ type: "CREATE_ONE_BUS", payload: { bus } });
      console.log(bus);
    } catch (err) {
      console.log(err);
    }
  };
  return (
    <>
      <Drawer
        anchor="right"
        onClose={toggleDrawer(false)}
        open={openAddPointForm}
      >
        <div className="detail-form__wrapper">
          <header className="detail-form__header">
            <h5>Add Point</h5>
          </header>
          <form className="m-3">
            <TextField
              className="w-100 mt-2"
              label="ID"
              name="id"
              value={addPointInput.id}
              onChange={onchangeHandleAddPoint}
            />
            <TextField
              className="w-100 mt-2"
              label="Latitude"
              name="latitude"
              value={addPointInput.latitude}
              onChange={onchangeHandleAddPoint}
            />
            <TextField
              className="w-100 mt-2"
              label="Longitude"
              name="longitude"
              value={addPointInput.longitude}
              onChange={onchangeHandleAddPoint}
            />
            <Button
              fullWidth
              className="mt-5"
              variant="contained"
              color="primary"
            >
              ADD
            </Button>
            <Button
              fullWidth
              className="mt-2"
              variant="contained"
              color="primary"
              onClick={() => setOpenAddPointForm(false)}
            >
              Cancel
            </Button>
          </form>
        </div>
      </Drawer>
      <div className="d-flex">
        <form
        // onKeyDown={handleEnterKey}
        >
          <TextField
            className="w-100 mt-3"
            label="ID"
            name="id"
            value={busesInput.id}
            onChange={onchangeHandle}
          />
          <TextField
            className="w-100 mt-3 mb-2"
            label="Operating Time"
            name="operatingTime"
            value={busesInput.operatingTime}
            onChange={onchangeHandle}
          />
          <TextField
            className="w-100 mt-3 mb-2"
            label="Time Distance"
            name="timeDistance"
            value={busesInput.timeDistance}
            onChange={onchangeHandle}
          />
          <TextField
            className="w-100 mt-3 mb-2"
            label="Name"
            name="name"
            value={busesInput.name}
            onChange={onchangeHandle}
          />
          <TextField
            className="w-100 mt-3 mb-2"
            label="Price"
            name="price"
            value={busesInput.price}
            onChange={onchangeHandle}
          />
          <TextField
            className="w-100 mt-3 mb-2"
            label="Seats"
            name="seats"
            value={busesInput.seats}
            onChange={onchangeHandle}
          />
          <div>
            <TextField
              className="w-100 mt-3 mb-2"
              label="Bus stop"
              name="busstops"
              value={busStopName}
              onChange={onChangeName}
            />
            <Button variant="contained" color="primary" onClick={onAddBusStop}>
              add bus stop
            </Button>
            <div max-width="100px">
              {busStops.map((b, index) => {
                return (
                  <div
                    className="d-flex py-3 border-bottom align-items-center"
                    key={b.id}
                    busstopid={b.id}
                  >
                    <p className="w-75 m-0">
                      {index}. {b.name}
                    </p>
                    {index !== 0 ? (
                      <Button variant="contained" onClick={onMoveUp}>
                        <UpIcon />
                      </Button>
                    ) : (
                      <div></div>
                    )}
                    <Button
                      variant="contained"
                      role="button"
                      onClick={onDeleteBusStop}>
                      <DeleteIcon />
                    </Button>
                    <Button variant="contained" onClick={toggleDrawer(true)}>
                      <TimeLineIcon />
                    </Button>
                  </div>
                );
              })}
            </div>
          </div>

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
            Submit
          </Button>
        </form>
        <img className="w-50 h-50" src={addbus} alt="addbus" />
      </div>
    </>
  );
};
export default BusForm;
