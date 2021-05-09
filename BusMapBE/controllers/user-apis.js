const User = require('./../models/user')
const SHA256 = require('crypto-js/sha256')

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

module.exports = (app) => {

    app.get('/api/users', (req, res) => {
        getUsers(req, res)
    })

    app.get('/api/user-username/:username', (req, res) => {
        User.findOne({ username: req.params.username }, (err, user) => {
            if (err)
                return res.json(err)
            if (!user)
                return res.json(false)
            return res.json(true)
        })
    })

    app.post('/api/user', (req, res) => {
        User.findById(req.body.id, (err, user) => {
            if (err)
                return res.json(err)
            return res.json(user)

        })
    })

    app.get('/api/user-email/:email', (req, res) => {
        User.findOne({ email: req.params.email }, (err, user) => {
            if (err)
                return res.json(err)
            if (!user)
                return res.json(false)
            return res.json(true)
        })
    })

    app.post('/api/user-login', (req, res) => {
        User.findOne({ username: req.body.username, password: SHA256(req.body.password).toString() }, (err, user) => {
            if (err) {
                res.status(500).json(err)
            } else {
                res.json(user)
            }
        })
    })

    app.post('/api/user-loginEmail', (req, res) => {
        User.findOne({ email: req.body.email, password: SHA256(req.body.password).toString() }, (err, user) => {
            if (err) {
                res.status(500).json(err)
            } else {
                res.json(user)
            }
        })
    })

    app.post('/api/user-checkpassword', (req, res) => {
        User.findOne({ username: req.body.username, password: SHA256(req.body.password) }, (err, user) => {
            if (err || user == null) {
                res.json(false)
            } else (
                res.json(true)
            )

        })
    })

    app.post('/api/users', (req, res) => {
        console.log(req)
        const newUser = {
            fullname: req.body.fullname,
            email: req.body.email,
            username: req.body.username,
            password: SHA256(req.body.password),
            role: 'user'
        }
        User.create(newUser, (err, user) => {
            if (err) {
                res.json(err)
            } else {
                getUsers(req, res)
            }
        })
    })
    app.post('/api/admins', (req, res) => {
        console.log(req.body)
        const newUser = {
            email: req.body.email,
            username: req.body.username,
            password: SHA256(req.body.password),
            role: 'admin'
        }
        User.create(newUser, (err, user) => {
            if (err) {
                res.json(err)
            } else {
                getUsers(req, res)
            }
        })
    })

    app.put('/api/user-email/:id', (req, res) => {
        User.findOneAndUpdate({ _id: req.params.id }, { email: req.body.email }, { new: true }, (err, user) => {
            if (err) {
                res.json(err)
            } else {
                res.json(user)
            }
        })
    })

    app.put('/api/user-password/:id', (req, res) => {
        User.findOneAndUpdate({ _id: req.params.id }, { password: SHA256(req.body.password) }, { new: true }, (err, user) => {
            if (err) {
                res.json(err)
            } else {
                res.json(user)
            }
        })
    })

    app.put('/api/user/:id', (req, res) => {
        User.findOneAndUpdate({ _id: req.params.id },
            { email: req.body.email, fullname: req.body.fullname, username: req.body.username },
            { new: true },
            (err, user) => {
            if (err) {
                res.json(err)
            } else {
                res.json(user)
            }
        })
    })

    app.delete('/api/users/:id', (req, res) => {
        User.deleteOne({ _id: req.params.id }, (err, user) => {
            if (err) {
                return res.json(err)
            }
            return getUsers(req, res)

        })
    })


}