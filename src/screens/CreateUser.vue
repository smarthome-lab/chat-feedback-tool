<template>
<div id="createUser">
  <nav-bar></nav-bar>
  <div class="content">
  <h1>Benutzererstellung</h1>
  <div id="createUserButtonContainer">
    <div v-if="this.patchPressed" class="ui active inline loader"></div>
    <button  v-if="!this.patchPressed" class="ui positive inverted button createUserButton" @click="saveEdit" >Speichern</button>
    <button v-if="!this.patchPressed" class="ui negative basic button createUserButton" @click="abortEdit" >Abbrechen</button>
  </div>
  <div v-if="this.showErrorString" class='ui error message' style="max-width: 60%;">
    <div class='content'>
      <div class='header'>Es sind einige Fehler aufgetreten</div>
      <ul class='list'>
        <li class='content' v-bind:key="error" v-for="error in errorStringArray">
          {{error}}
        </li>
      </ul>
    </div>
  </div>
  <table class="ui red table userCreateTablePart">
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
        password: '',
        temporary_password: ''
      },
      errorStringArray: [],
      showErrorString: false,
      errorPrename: false,
      errorLastname: false,
      errorEmail: false,
      errorHSID: false,
      patchPressed: false
    }
  },
  created () {

  },
  methods: {
    saveEdit () {
      this.patchPressed = true
      this.createUser.password = Math.random().toString(36).slice(-8)
      this.createUser.temporary_password = this.createUser.password

      if (!this.errorCheck()) {
        this.patchPressed = false
        return
      }

      this.createUser.email = this.createUser.email.toLowerCase()
      this.createUser.hsid = this.createUser.hsid.toLowerCase()
      feathersClient
        .service('users')
        .create(this.createUser)
        .then(result => {
          // Sprung zum Profil basierend auf ID
          this.patchPressed = false
          this.$router.push({ path: `/users/${result.id}` })
        })
        .catch(error => {
          this.patchPressed = false
          this.showErrorString = true
          const errorMessage = JSON.stringify(error)
          let isKnownError = false
          if (errorMessage.indexOf('email must be unique') !== -1) {
            this.errorStringArray.push('Die angegebene E-Mail existiert bereits')
            isKnownError = true
          }
          if (errorMessage.indexOf('hsid must be unique') !== -1) {
            this.errorStringArray.push('Die angegebene Kennung existiert bereits')
            isKnownError = true
          }
          if (isKnownError === false) {
            this.errorStringArray.push('Es ist ein unerwarteter Fehler aufgetreten')
          }
        })
    },
    errorCheck () {
      this.errorStringArray = []
      this.showErrorString = false
      this.errorPrename = false
      this.errorLastname = false
      this.errorEmail = false
      this.errorHSID = false
      if (!this.createUser.prename || !this.createUser.lastname || !this.createUser.email || !this.createUser.hsid) {
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
      }
      if (this.createUser.email.indexOf('hs-coburg.de') === -1) {
        this.errorStringArray.push('Bitte E-Mail überprüfen')
        this.errorEmail = true
      }
      const rege = this.createUser.hsid.match(/[a-z0-9A-Z]{8}/)
      if (rege === null || rege[0] !== this.createUser.hsid) {
        this.errorStringArray.push('Bitte Kennung überprüfen')
        this.errorHSID = true
      }

      if (this.errorPrename || this.errorLastname || this.errorEmail || this.errorHSID) {
        this.showErrorString = true
        return false
      }
      return true
    },
    abortEdit () {
      this.errorStringArray = []
      this.showErrorString = false
      this.errorPrename = false
      this.errorLastname = false
      this.errorEmail = false
      this.errorHSID = false
      this.patchPressed = false
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

.userCreateTablePart {
  width: 50%;
  max-width: 50%;
  margin-left: auto !important;
  margin-right: auto !important;
  clear: both;
  cursor: default;
}

.userCreateTablePart th {
  text-align: right;
  max-width: 50%;
  width: 40%;
  line-height: 50px;
  cursor: default;
}

.createUserButton {
  width: 150px;
  height: 50px;
  display: inline-block !important;
  margin-bottom: 15px !important;
}

#createUserButtonContainer {
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
