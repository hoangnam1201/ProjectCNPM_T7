const route = require('express').Router()
const authMiddle = require('../middleware/auth.middleware')
const BusStopController = require('../controllers/bus-stops.controller')
const _busStopController = new BusStopController()

route.get('/get-all',authMiddle.verifyToken, _busStopController.getAll)
route.get('/get-by-id/:id',authMiddle.verifyToken, _busStopController.getById)
route.get('/search-name',authMiddle.verifyToken,_busStopController.searchName)
route.get('/get-name',authMiddle.verifyToken, _busStopController.getName)
route.get('/get-all-name',authMiddle.verifyToken, _busStopController.getAllName)
route.post('/around',authMiddle.verifyToken, _busStopController.getAround)
route.post('/add',[authMiddle.verifyToken, authMiddle.admin], _busStopController.add)
route.put('/update/:id',[authMiddle.verifyToken, authMiddle.admin], _busStopController.update)
route.delete('/delete/:id',[authMiddle.verifyToken, authMiddle.admin], _busStopController.delete)

module.exports = route