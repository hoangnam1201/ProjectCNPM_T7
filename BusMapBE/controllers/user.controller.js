const User = require('./../models/user')
const SHA256 = require('crypto-js/sha256')
const Buses = require('../models/buses')

const getUsers = (req, res) => {
    User.find({}, (err, users) => {
        if (err) {
            res.status(500).json(err)
        }
        else {
            res.json(users)
        }
    })
}

getFavoriteBuses = async (id, res) => {
    User.findById(id, (err, user) => {
        if (err) return res.status(400).json(err)
        console.log(user.favoriteBuses)
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

module.exports = function () {

    this.getAll = async (req, res) => {
        getUsers(req, res)
    }

    this.getByUsername = async (req, res) => {
        User.findOne({ username: req.query.username }, (err, user) => {
            if (err)
                return res.json(err)
            if (!user)
                return res.json(false)
            return res.json(true)
        })
    }

    this.getInfo = async (req, res) => {
        const userId = req.userData.userId
        User.findById(userId, (err, user) => {
            if (err)
                return res.json(err)
            return res.json(user)

        })
    }

    this.getFavoriteBusesOfUser = async (req, res) => {
        const userId = req.userData.userId
        User.findOne({ _id: userId }).populate('favoriteBuses').then(user => {
            let result = user.favoriteBuses.map(b => {
                return { buses: b, isOwner: true }
            })
            return res.json(result)
        }).catch(err => res.json(err))
    }

    this.addFavoriteBuses = async (req, res) => {
        const userId = req.userData.userId
        User.findOneAndUpdate({ _id: userId }, { $push: { favoriteBuses: req.body.idBuses } }, { new: true }, (err, result) => {
            if (err) return res.json(err)
            return getFavoriteBuses(req.params.id, res)
        })
    }
    this.deleteFavoriteBuses = async (req, res) => {
        const userId = req.userData.userId
        User.findOneAndUpdate({ _id: userId }, { $pull: { favoriteBuses: req.body.idBuses } }, (err, result) => {
            if (err) return res.json(err)
            return getFavoriteBuses(req.params.id, res)
        })
    }

    this.add = async (req, res) => {
        if (!req.body.fullname || !req.body.email || !req.body.username || !req.body.password) {
            return res.status(400).json('input required')
        }

        const newUser = {
            fullname: req.body.fullname,
            email: req.body.email,
            username: req.body.username,
            password: SHA256(req.body.password),
            role: 'user'
        }
        User.create(newUser, (err, user) => {
            if (err) {
                res.status(500).json(err)
            } else {
                res.json(user)
            }
        })
    }
    this.updatePassword = async (req, res) => {
        const userId = req.userData.userId
        User.findOneAndUpdate({ _id: userId }, { password: SHA256(req.body.password).toString() }, { new: true }, (err, user) => {
            if (err) {
                res.json(err)
            } else {
                res.json(user)
            }
        })
    }

    this.updateInfor = async (req, res) => {
        const userId = req.userData.userId
        User.findOneAndUpdate({ _id: userId },
            { email: req.body.email, fullname: req.body.fullname, username: req.body.username },
            { new: true },
            (err, user) => {
                if (err) {
                    res.json(err)
                } else {
                    res.json(user)
                }
            })
    }

    this.delete = async (req, res) => {
        User.deleteOne({ _id: req.params.id }, (err, user) => {
            if (err) {
                return res.json(err)
            }
            return getUsers(req, res)

        })
    }


}