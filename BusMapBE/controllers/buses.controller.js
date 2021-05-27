const mongoose = require('mongoose')
const Buses = require('../models/buses')
const BusStop = require('../models/bus-stop')
const User = require('../models/user')
const getBuses = (req, res) => {
    Buses.find({}, (err, busses) => {
        if (err)
            res.json(err)
        else
            res.json(busses)
    })
}
module.exports = function () {

    getFavoriteBuses =async (id, res) => {
        User.findById(id, (err, user) => {
            if (err) return res.status(400).json(err)
            Buses.aggregate([
                {
                    $match: {}
                },
                {
                    $addFields: {
                        isFavorite: { $in: ['$_id', user.favoriteBuses] }
                    }
                }
            ]).exec((err, buses) => {
                if (err) return res.status(400).json(err)
                res.json(buses)
            })
        })
    }

    this.getAll = async (req, res) => {
        getBuses(req, res)
    }
    this.getAllNameAndId = async (req, res) => {
        Buses.find({}, (err, busses) => {
            if (err) return res.json(err)
            if (!busses) return res.json(null)
            let arrName = busses.map(b => b.name)
            let arrId = busses.map(b => b.id)
            res.json([...arrName, ...arrId])
        })
    }
    this.getById = async (req, res) => {
        Buses.findById(req.params.id).populate('busstops').then(buses => {
            res.json(buses)
        }).catch(err => {
            res.json(err)
        })
    }
    this.getByName = async (req, res) => {
        Buses.findOne({ name: req.query.name }).populate('busstops').then(buses => {
            res.json(buses)
        }).catch(err => {
            res.json(err)
        })
    }

    this.searchByIdOrName = async (req, res) => {
        const regex = new RegExp(req.query.value, 'i')
        Buses.find({ $or: [{ id: { $regex: regex } }, { name: { $regex: regex } }] }).then(buses => {
            res.json(buses)
        }).catch(err => {
            res.json(err)
        })
    }

    this.getFavoriteBusesByIdUser = async (req, res) => {

        const userId = req.userData.userId
        getFavoriteBuses(userId, res)
    }


    this.addPointAfterId = async (req, res) => {

        const busesId = req.params.id
        const BeforBusStopId = req.query.id
        const newBusStop = {
            name: 'point',
            locationName: req.params.id + '-point',
            latitude: req.body.latitube,
            longitude: req.body.longitube,
            buses: [busesId]
        }

        BusStop.create(newBusStop, (err, point) => {
            if (err) return res.json(err)
            Buses.findById(busesId, (err, buses) => {
                if (err | buses == null) return res.json(err ? err : ' id is not exist')
                let index = buses.busstops.indexOf(BeforBusStopId) + 1;
                Buses.updateOne({ _id: mongoose.Types.ObjectId(busesId) }, {
                    $push: { busstops: { $each: [point._id], $position: index } }
                }, (err, buses) => {
                    if (err) return res.json(err)
                    getBuses(req, res)
                })
            })
        })
    }

    this.addPointAfterIndex = async (req, res) => {

        const busesId = req.params.id
        const index = req.query.index
        const newBusStop = {
            name: 'point',
            locationName: req.params.id + '-point',
            latitude: req.body.latitube,
            longitude: req.body.longitube,
            buses: [busesId]
        }

        BusStop.create(newBusStop, (err, point) => {
            if (err) return res.json(err)
            Buses.updateOne({ _id: mongoose.Types.ObjectId(busesId) }, {
                $push: { busstops: { $each: [point._id], $position: index } }
            }, (err, buses) => {
                if (err) return res.json(err)
                getBuses(req, res)
            })
        })
    }


    this.add = async (req, res) => {
        const busstops = req.body.busstops
        const newBuses = {
            id: req.body.id,
            operatingTime: req.body.operatingTime,
            timeDistance: req.body.timeDistance,
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
    }

    this.update = async (req, res) => {
        const busstops = req.body.busstops
        const id = req.params.id
        const newBuses = {
            id: req.body.id,
            operatingTime: req.body.operatingTime,
            timeDistance: req.body.timeDistance,
            name: req.body.name,
            price: req.body.price,
            seats: req.body.seats,
            busstops: busstops
        }
        Buses.findOneAndUpdate({ _id: id },
            {
                name: newBuses.name,
                price: newBuses.price,
                seats: newBuses.seats,
                busstops: newBuses.busstops
            }, { new: true }, (err, buses) => {
                if (err) {
                    return res.json(err)
                }
                BusStop.updateMany({ buses: mongoose.Types.ObjectId(id) },
                    { $pull: { buses: id } }, (err, result) => {
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
                    })
            })
    }

    this.delete = async (req, res) => {
        Buses.findOneAndDelete({ _id: req.params.id }, (err, buses) => {
            if (err) {
                res.json(err)
            } else {
                BusStop.updateMany(
                    { buses: mongoose.Types.ObjectId(buses._id) },
                    { $pull: { buses: buses._id } }).then(busstop => {
                        return getBuses(req, res)
                    }).catch(err => res.json(err))
            }
        })
    }
}