const mongoose = require('mongoose')

const userShema = new mongoose.Schema({
    fullname: String,
    email: String,
    username: String,
    password: String,
    role: String
}, { collection: 'user' })

const User = mongoose.model('user', userShema)
module.exports = User