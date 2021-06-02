import { Button, TextField } from "@material-ui/core";
import React, { useContext, useState } from "react";
import styled from "styled-components";
import AppContext from "../AppContext";
import addbus from "../assets/addbus1.png";
import axios from "axios";
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
  const [busStops, setBusStop] = useState([]);

  const onChangeName = (e) => {
    setBusSTopName(e.target.value);
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
          console.log(response);
          setBusStop([
            ...busStops,
            { id: response.data._id, name: response.data.name },
          ]);
          setBusesInput({
            ...busesInput,
            busstops: busStops.map((b) => b.id),
          });
          console.log(busesInput.busstops);
          console.log(busStops);
        } else {
          console.log(response.status);
        }
      })
      .catch((err) => {
        console.log(err);
      });
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
          <Button variant="contained" color="primary">
            add point
          </Button>
          {busStops.map((b) => {
            return <p key={b.id}>{b.name}</p>;
          })}
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
  );
};
export default BusForm;
