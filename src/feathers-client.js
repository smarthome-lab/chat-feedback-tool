import feathers from '@feathersjs/feathers'
import socketio from '@feathersjs/socketio-client'
import auth from '@feathersjs/authentication-client'
import io from 'socket.io-client'

const connection = process.env['NODE_ENV'] === 'production' ? 'https://hsc-backend.herokuapp.com' : 'http://localhost:3030'
const socket = io(connection, {transports: ['websocket']})
const feathersClient = feathers()
  .configure(socketio(socket))
  .configure(auth({ storage: window.localStorage }))

export {
  feathersClient,
  auth,
  socket
}
