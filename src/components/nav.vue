<template>
  <div id="head">
    <sui-menu secondary pointing>
      <a class="vertically fitted item" href="#">
        <img class="ui mini image" src="../assets/logo.png"/>
      </a>

      <div class="right menu">

        <div class="ui item">
          <img class="ui avatar image" :src="`https://api.adorable.io/avatars/200/${getUser().email}.png`" alt="PB" />
          <sui-dropdown floating :text="`${getUser().prename} ${getUser().lastname}`">
            <sui-dropdown-menu>
              <sui-dropdown-item @click="logout">Abmelden</sui-dropdown-item>
            </sui-dropdown-menu>

          </sui-dropdown>
        </div>
      </div>
    </sui-menu>
  </div>
</template>

<script>
  import {feathersClient, auth} from '../feathers-client'
  import {mapGetters, mapMutations} from 'vuex'
  export default {
    name: 'Nav',
    data() {
      return {
        loading: true,
        error: '',
        email: '',
        password: '',
      }
    },
    methods: {
      test() {
        console.log('feedback current user', this.getUser())
      },
      logout(){
        this.setUser(null)
        feathersClient.logout();
        this.$router.push({ path: 'login' })
      },
      ...mapGetters([
        'getUser'
      ]),
      ...mapMutations([
        'setUser',
      ])
    }
  }
</script>

<style>

</style>
