import Vue from 'vue'
import Router from 'vue-router'
import UserProfile from '@/screens/UserProfile'
import Login from '@/screens/Login'
import PageNotFound from '@/screens/PageNotFound'
import Feedback from '@/screens/Feedback'
import UserOverview from '@/screens/UserOverview'
import { feathersClient } from '../feathers-client'
import { store } from '../store/index'
import CreateUser from '@/screens/CreateUser'

Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Login
    },
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/feedback',
      name: 'Feedback',
      component: Feedback,
      meta: { requiresAuth: true }
    },
    {
      path: '/users/:id',
      name: 'UserProfile',
      component: UserProfile,
      meta: { requiresAuth: true }
    },
    {
      path: '/users',
      name: 'Benutzeruebersicht',
      component: UserOverview,
      meta: { requiresAuth: true }
    },
    {
      path: '/createUser',
      name: 'Benutzererstellung',
      component: CreateUser,
      meta: { requiresAuth: true }
    },
    {
      path: '*',
      name: '404',
      component: PageNotFound
    }
  ]
})

router.beforeEach(async (to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    let user = store.getters.getUser

    console.log('Wechsle Route', to, from, user)
    // this route requires auth, check if logged in
    // if not, redirect to login page.
    if (user === null || !user.id || user === undefined) {
      let c = await checkAuth()
      // if the user is still loged in show page else redirect to login
      if (c) {
        next()
        return
      }
      next({ path: '/login', query: { redirect: to.fullPath } })
    } else {
      next()
    }
  } else {
    next() // make sure to always call next()!
  }
})

async function checkAuth () {
  const authResponse = await feathersClient.authenticate().catch(error => {
    console.log(error)
    return false
  })
  if (!authResponse) {
    return false
  }
  const passportUser = await feathersClient.passport.verifyJWT(authResponse.accessToken)
  if (!passportUser.userId) {
    return false
  }
  const storedUser = await feathersClient.service('users').get(passportUser.userId)
  store.state.user = storedUser
  return true
}

export default router
