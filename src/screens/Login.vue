<template>
  <div>
    <div class="background">
      <sui-grid centered vertical-align="middle">
        <sui-grid-column>

          <h2 is="sui-header" color="red" image>
            <img class="ui image" src="../assets/logo.png" />
            <sui-header-content>Log-in to the HSChat Admin Tool</sui-header-content>
          </h2>

          <sui-form>
            <sui-segment stacked>
              <sui-form-field>
                <sui-input
                  type="email"
                  placeholder="E-mail address"
                  icon="user"
                  icon-position="left"
                  v-model="email" />
              </sui-form-field>
              <sui-form-field>
                <sui-input
                  type="password"
                  placeholder="Password"
                  icon="lock"
                  icon-position="left"
                  v-model="password" />
              </sui-form-field>
              <sui-button @click="login" size="large" color="red" fluid>
                <sui-icon v-if="loading==true" name="notched circle" loading />
                <span v-if="loading==false">Login</span>
              </sui-button>
            </sui-segment>
          </sui-form>
          <sui-message error v-if="error!==''">{{error}}</sui-message>
        </sui-grid-column>
      </sui-grid>
    </div>
  </div>
</template>

<script>
import { feathersClient } from '../feathers-client'
import { mapGetters, mapMutations } from 'vuex'

export default {
  name: 'Login',
  data () {
    return {
      loading: false,
      error: '',
      email: '',
      password: ''
    }
  },
  methods: {
    login () {
      this.loading = true
      feathersClient
        .authenticate({
          strategy: 'local',
          email: this.email,
          password: this.password
        })
        .then(response => {
          feathersClient.passport.verifyJWT(response.accessToken).then(u => {
            feathersClient
              .service('users')
              .get(u.userId)
              .then(u => {
                if (u.role === 'admin' || u.role === 'moderator') {
                  console.log('eingeloggt!', u)
                  this.loading = false
                  this.$router.push({ path: 'feedback' })
                  this.setUser(u)
                } else {
                  this.loading = false
                  this.error = 'Nur Admins dürfen sich hier einloggen sorry…'
                }
              })
          })
        })
        .catch(e => {
          // Show login page (potentially with `e.message`)
          this.loading = false
          this.error = 'Die Email-Adresse oder das Passwort sind falsch'
          console.error('Authentication error', e)
        })
    },
    ...mapGetters(['getUser']),
    ...mapMutations(['setUser'])
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.background {
  background-color: #fefefe !important;
  height: 100vh;
  margin: 1em 0;
}
.grid {
  height: 100%;
}
.column {
  max-width: 450px;
  text-align: center !important;
}
</style>
