<template>
<div id="createUser">
  <nav-bar></nav-bar>
  <div class="content">
  <h1>Benutzererstellung</h1>
  <div id="editUserButtonContainer">
    <button class="ui positive inverted button editUserButton" @click="saveEdit" >Speichern</button>
    <button class="ui negative basic button editUserButton" @click="abortEdit" >Abbrechen</button>
  </div>
  <div v-if="this.showErrorString" class='ui error message'>
    <div class='content'>
      <div class='header'>Es sind einige Fehler aufgetreten</div>
      <ul class='list'>
        <li class='content' v-bind:key="error" v-for="error in errorStringArray">
          {{error}}
        </li>
      </ul>
    </div>
  </div>
  <table class="ui red table tablePart">
    <tr>
      <th>Vorname</th>
      <td><input v-model="createUser.prename" v-bind:class="{
        ui: true,
        fluid: true,
        input: true,
        editField: true,
        errorInField: errorPrename
      }"/></td>
    </tr>
     <tr>
      <th>Nachname</th>
      <td><input v-model="createUser.lastname" v-bind:class="{
        ui: true,
        fluid: true,
        input: true,
        editField: true,
        errorInField: errorLastname
      }"/></td>
    </tr>
     <tr>
      <th>E-Mail</th>
      <td><input v-model="createUser.email" v-bind:class="{
        ui: true,
        fluid: true,
        input: true,
        editField: true,
        errorInField: errorEmail
      }"/></td>
    </tr>
     <tr>
      <th>Kennung</th>
      <td><input v-model="createUser.hsid" v-bind:class="{
        ui: true,
        fluid: true,
        input: true,
        editField: true,
        errorInField: errorHSID
      }"/></td>
    </tr>
  </table>
</div>
</div>
</template>

<script>
import { feathersClient } from '../feathers-client'

export default {
  name: 'userprofile',

  data () {
    return {
      createUser: {
        prename: '',
        lastname: '',
        email: '',
        hsid: '',
        password: ''
      },
      errorStringArray: [],
      showErrorString: false,
      errorPrename: false,
      errorLastname: false,
      errorEmail: false,
      errorHSID: false
    }
  },
  created () {

  },
  methods: {
    saveEdit () {
      this.createUser.password = Math.random().toString(36).slice(-8)
      if (!this.createUser.prename || !this.createUser.lastname || !this.createUser.email || !this.createUser.hsid) {
        this.errorStringArray = []
        this.showErrorString = false
        this.errorPrename = false
        this.errorLastname = false
        this.errorEmail = false
        this.errorHSID = false
        if (!this.createUser.prename) {
          this.errorStringArray.push('Bitte Vornamen eintragen')
          this.errorPrename = true
        }
        if (!this.createUser.lastname) {
          this.errorStringArray.push('Bitte Nachnamen eintragen')
          this.errorLastname = true
        }
        if (!this.createUser.email) {
          this.errorStringArray.push('Bitte E-Mail eintragen')
          this.errorEmail = true
        }
        if (!this.createUser.hsid) {
          this.errorStringArray.push('Bitte Hochschul-Kennung eintragen')
          this.errorHSID = true
        }
        if (this.createUser.email.indexOf('hs-coburg.de') === -1) {
          this.errorStringArray.push('Bitte E-Mail überprüfen')
          this.errorEmail = true
        }
        const rege = this.createUser.hsid.match(/[a-z0-9A-Z]{8}/)
        console.log(rege)
        if (rege === null || rege[0] !== this.createUser.hsid) {
          this.errorStringArray.push('Bitte Kennung überprüfen')
          this.errorHSID = true
        }
        this.showErrorString = true
        return
      }
      this.createUser.email = this.createUser.email.toLowerCase()
      this.createUser.hsid = this.createUser.hsid.toLowerCase()
      feathersClient
        .service('users')
        .create(this.createUser)
        .then(result => {
          // Sprung zum Profil basierend auf ID
          console.log(result)
          this.$router.push({ path: `/users/${result.id}` })
        })
        .catch(error => {
          console.error(JSON.stringify(error))
        })
    },
    abortEdit () {
      // User kommt auf Homepage zurück
      this.$router.push({ path: `/users` })
    }
  }
}
</script>

<style>
table {
  width: 100%;
}

.content {
  margin-left: 10px !important;
}

.tablePart {
  width: 32%;
  max-width: 32%;
  margin-right: 10px !important;
  float: left;
  cursor: default;
}

.tablePart th {
  text-align: right;
  max-width: 50%;
  width: 40%;
  line-height: 50px;
  cursor: default;
}

.editUserButton {
  width: 150px;
  height: 50px;
  display: inline-block !important;
  margin-bottom: 15px !important;
}

#editUserButtonContainer {
  width: 97%;
  max-width: 97%;
  text-align: right;
  display: block !important;
  margin-right: 30px;
}

.errorInField {
  border-color: #e0b4b4!important;
  color: #9f3a38!important;
  background: #fff6f6!important;
}

.editField {
  max-width: 95%;
  width: 95%;
}

</style>
