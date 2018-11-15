import Vue from 'vue'
import Vuex from 'vuex'
import feathersClient from '../feathers-client'

Vue.use(Vuex)

export const store = new Vuex.Store({
  state: {
    user: null
  },
  getters: {
    getUser: state => state.user
  },
  mutations: {
    setUser (state, user) {
      state.user = user
    }
  }

})
