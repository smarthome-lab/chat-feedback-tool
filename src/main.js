// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import SuiVue from 'semantic-ui-vue'
import 'semantic-ui-css/semantic.min.css'
import Vuex from 'vuex'
import 'es6-promise/auto'
import { store } from './store/index';
import Nav from './components/nav'
import Pagination from './components/pagination'

Vue.config.productionTip = false
Vue.use(SuiVue)
Vue.component('nav-bar', Nav);
Vue.component('pagination', Pagination);



/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>',
  store
})
