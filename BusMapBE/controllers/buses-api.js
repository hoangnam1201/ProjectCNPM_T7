const mongoose = require('mongoose')
const Buses = require('./../models/buses')
const BusStop = require('./../models/bus-stop')
const getBuses = (req, res) => {
    Buses.find({}, (err, busses) => {
        if (err)
            res.json(err)
        else
            res.json(busses)
    })
}
module.exports = (app) => {
    app.get('/api/buses', (req, res) => {
        getBuses(req, res)
    })
    app.get('/api/buses/:id', (req, res) => {
        Buses.findById(req.params.id).populate('busstops').then(buses => {
            res.json(buses)
        }).catch(err => {
            res.json(err)
        })
    })
    app.post('/api/buses', (req, res) => {
        const busstops = req.body.busstops
        const newBuses = {
            name: req.body.name,
            price: req.body.price,
            seats: req.body.seats,
            busstops: busstops
        }
        Buses.create(newBuses, (err, buses) => {
            if (err) {
                res.json(err)
            } else {
                BusStop.updateMany(
                    { _id: { $in: busstops } },
                    { $push: { buses: buses._id } },
                    (err, busstop) => {
                        if (err) {
                            res.json(err)
                        } else {
                            getBuses(req, res)
                        }
                    })
            }
        })
    })

    app.delete('/api/buses/:id', (req, res) => {
        Buses.findOneAndDelete({ _id: req.params.id }, (err, buses) => {
            if (err) {
                res.json(err)
            } else {
                    console.log(buses)
                BusStop.updateMany(
                    { buses: mongoose.Types.ObjectId(buses._id) },
                    { $pull: { buses: buses._id } }).then(busstop => {
                        return getBuses(req, res)
                    }).catch(err => res.json(err))
            }
        })
    })
}