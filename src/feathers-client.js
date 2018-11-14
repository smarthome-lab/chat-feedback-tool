import feathers from '@feathersjs/feathers'
import socketio from '@feathersjs/socketio-client'
import auth from '@feathersjs/authentication-client'
import io from 'socket.io-client'

const connection = 'https://hsc-backend-staging.herokuapp.com'
const socket = io(connection, {transports: ['websocket']})
const feathersClient = feathers()
  .configure(socketio(socket))
  .configure(auth({ storage: window.localStorage }))

export {
  feathersClient,
  auth,
  socket
}
