import {
  Button,
  Drawer,
  List,
  ListItem,
  TableCell,
  TableRow,
  TextField,
} from "@material-ui/core";
import React, { useContext, useState, useEffect } from "react";
import AppContext from "../AppContext";
import axios from "axios";
import ConfirmDelete from "../common/ConfirmDeleteForm";
import DeleteIcon from "@material-ui/icons/Delete";

import EditIcon from "@material-ui/icons/Edit";
const Row = ({ bus }) => {
  const { dispatch } = useContext(AppContext);
  const [openToEditForm, setOpenToEditForm] = useState(false);
  const [busToEdit, setBusToEdit] = useState(bus);
  const [deleteDialog, setDeleteDialog] = useState(false);
  const [deleting, setDeleting] = useState(false);
  const toggleDrawer = (open) => (event) => {
    setOpenToEditForm(open);
  };
  const updateBus = async () => {
    try {
      const fetch = {
        method: "put",
        url: `https://busapbe.herokuapp.com/api/buses/update/:${bus._id}`,
        headers: {
          Authorization: "Token " + localStorage.getItem("accessToken"),
        },
        body: {
          id: busToEdit.id,
          operatingTime: busToEdit.operatingTime,
          timeDistance: busToEdit.timeDistance,
          name: busToEdit.name,
          price: busToEdit.price,
          seats: busToEdit.seats,
          busstops: [busToEdit.busstops],
        },
        data: busToEdit,
      };
      await axios(fetch);
      dispatch({ type: "UPDATE_ONE_BUS", payload: { ...busToEdit } });
      setOpenToEditForm(false);
    } catch (err) {
      console.log(err);
    }
  };
  const handleDelete = () => {
    setDeleteDialog(true);
  };
  const deleteBuses = async () => {
    try {
      const fetch = {
        method: "delete",
        url: `https://busapbe.herokuapp.com/api/buses/delete/${bus._id}`,
        headers: {
          Authorization: "Token " + localStorage.getItem("accessToken"),
        },
      };
      await axios(fetch);
      dispatch({ type: "DELETE_ONE_BUS", payload: { _id: bus._id } });
      setDeleting(false);
      setDeleteDialog(false);
    } catch (err) {
      console.log(err);
    }
  };

  const onDelete = async () => {
    setDeleting(true);
    deleteBuses();
  };

  return (
    <>
      <ConfirmDelete
        open={deleteDialog}
        label="Chuyến xe"
        warning="Xóa chuyến vĩnh viễn. Không thể khôi phục."
        name={bus.name}
        onClose={() => setDeleteDialog(false)}
        loading={deleting}
        onSubmit={onDelete}
      />
      <Drawer
        anchor="right"
        open={openToEditForm}
        onClose={toggleDrawer(false)}
      >
        <div className="detail-form__wrapper">
          <header className="detail-form__header">
            <h5>Update chuyến xe</h5>
          </header>
          <form
          // onKeyDown={handleEnterKey}
          >
            <TextField
              className="w-100 mt-3"
              label="ID"
              name="id"
              value={busToEdit.id}
              onChange={(e) => {
                setBusToEdit({ ...busToEdit, id: e.target.value });
              }}
            />
            <TextField
              className="w-100 mb-2"
              label="Operating Time"
              name="operatingTime"
              value={busToEdit.operatingTime}
              onChange={(e) => {
                setBusToEdit({ ...busToEdit, operatingTime: e.target.value });
              }}
            />
            <TextField
              className="w-100 mb-2"
              label="Time Distance"
              name="timeDistance"
              value={busToEdit.timeDistance}
              onChange={(e) => {
                setBusToEdit({ ...busToEdit, timeDistance: e.target.value });
              }}
            />
            <TextField
              className="w-100 mb-2"
              label="Name"
              name="name"
              value={busToEdit.name}
              onChange={(e) => {
                setBusToEdit({ ...busToEdit, name: e.target.value });
              }}
            />
            <TextField
              className="w-100 mb-2"
              label="Price"
              name="price"
              value={busToEdit.price}
              onChange={(e) => {
                setBusToEdit({ ...busToEdit, price: e.target.value });
              }}
            />
            <TextField
              className="w-100 mb-2"
              label="Seats"
              name="seats"
              value={busToEdit.seats}
              onChange={(e) => {
                setBusToEdit({ ...busToEdit, seats: e.target.value });
              }}
            />
            <TextField
              className="w-100 mb-2"
              label="Bus stop"
              name="busstops"
              value={busToEdit.busstops}
              onChange={(e) => {
                setBusToEdit({ ...busToEdit, busstops: [e.target.value] });
              }}
            />

            <Button
              fullWidth
              className="mt-2"
              variant="contained"
              color="primary"
              onClick={updateBus}
            >
              Update
            </Button>
            <Button
              fullWidth
              className="mt-2"
              variant="contained"
              color="primary"
              onClick={() => setOpenToEditForm(false)}
            >
              Cancel
            </Button>
          </form>
        </div>
      </Drawer>
      <TableRow hover style={{ transform: "scale(1)" }}>
        <TableCell align="center" className="border-right">
          {bus.id}
        </TableCell>
        <TableCell align="center" className="border-right">
          {bus.operatingTime}
        </TableCell>
        <TableCell align="center" className="border-right">
          {bus.timeDistance}
        </TableCell>
        <TableCell align="center" className="border-right">
          {bus.name}
        </TableCell>
        <TableCell align="center" className="border-right">
          {bus.price}
        </TableCell>
        <TableCell align="center" className="border-right">
          {bus.seats}
        </TableCell>
        <TableCell align="center" className="border-right">
          <Button
            // onClick={() => handleDelete(bus._id)}
            color="primary"
            variant="contained"
            onClick={toggleDrawer(true)}
          >
            <EditIcon />
          </Button>
        </TableCell>
        <TableCell align="center" className="border-right">
          <Button
            onClick={() => handleDelete(bus._id)}
            color="secondary"
            variant="contained"
          >
            <DeleteIcon />
          </Button>
        </TableCell>
      </TableRow>
    </>
  );
};
export default Row;
