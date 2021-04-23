const mongoose = require('mongoose')
const Buses = require('../models/buses')
const BusStop = require('./../models/bus-stop')

const getBusStops = (req, res) => {
    BusStop.find({}, (err, busstops) => {
        if (err) {
            res.json(err)
        } else {
            res.json(busstops)
        }
    })
}

module.exports = (app) => {
    app.get('/api/busstops', (req, res) => {
        getBusStops(req, res)
    })
    app.get('/api/busstops/:id', (req, res) => {
        BusStop.findById(req.params.id).populate('buses').then(busstop => {
            res.json(busstop)
        }).catch(err => {
            res.json(err)
        })
    })

    app.post('/api/busstops', (req, res) => {
        const newBusStop = {
            name: req.body.busstopName,
            locationName: req.body.locationName,
            latitude: req.body.latitube,
            longitude: req.body.longitube,
            buses: []
        }
        BusStop.create(newBusStop, (err, busStop) => {
            if (err) {
                res.json(err)
            } else {
                getBusStops(req, res)
            }

        })
    })
    app.delete('/api/busstops/:id', (req, res) => {
        BusStop.findOneAndDelete({ _id: req.params.id }, (err, busStop) => {
            if (err) {
                res.json(err)
            } else {
                Buses.updateMany({ busstops: mongoose.Types.ObjectId(busStop._id) },
                {$pull:{busstops: busStop._id}}, (err, buses) => {
                    if (err)
                        return res.json(err)
                    return getBusStops(req, res)
                })
            }
        })
    })
}