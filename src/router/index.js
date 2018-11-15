import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/screens/HelloWorld'
import Login from '@/screens/Login'
import PageNotFound from '@/screens/PageNotFound'
import Feedback from '@/screens/Feedback'
import UserOverview from '@/screens/UserOverview'
import {feathersClient} from '../feathers-client'
import {store} from '../store/index'

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
      meta: {requiresAuth: true}
    },
    {
      path: '/users',
      name: 'Benutzeruebersicht',
      component: UserOverview,
      meta: {requiresAuth: true}
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
    if (user === null || user === {} || user === undefined) {
      let c = await checkAuth()
      // if the user is still loged in show page else redirect to login
      if (c) {
        next()
        return
      }
      next({path: '/login', query: {redirect: to.fullPath}})
    } else {
      next()
    }
  } else {
    next() // make sure to always call next()!
  }
})

async function checkAuth () {
  return await feathersClient.authenticate().then(async (response) => {
    return await feathersClient.passport.verifyJWT(response.accessToken).then(async (u) => {
      return await feathersClient.service('users').get(u.userId).then((u) => {
        store.state.user = u
        return Promise.resolve(true)
      }).catch(async e => {
        return Promise.resolve(false)
      })
    }).catch(async e => {
      return Promise.resolve(false)
    })
  }).catch(e => {
    return Promise.resolve(false)
  })
}

export default router
