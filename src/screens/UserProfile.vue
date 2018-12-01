<template>
<div id="userprofile">
  <nav-bar></nav-bar>
  <div class="content">
  <h1>Benutzerprofil für {{displayedUser.prename}} {{displayedUser.lastname}}</h1>
  <div v-if="this.editSuccessful" class="ui positive message">
    <i class="close icon" @click="closeNotification"></i>
    <div class="header">
      Nutzer erfolgreich aktualisiert!
    </div>
  </div>
  <div v-if="this.editAborted" class="ui info message">
    <i class="close icon" @click="closeNotification"></i>
    <div class="header">
      Bearbeitung wurde abgebrochen. Keine Daten wurden verändert!
    </div>
  </div>
  <div v-if="this.resetPasswordDone" class="ui info message">
    <i class="close icon" @click="closeNotification"></i>
    <div class="header">
      Passwort von {{displayedUser.prename}} {{displayedUser.lastname}} wurde zurückgesetzt!
    </div>
  </div>
  <div v-if="this.showErrorString" class='ui error message'>
    <i class="close icon" @click="closeNotification"></i>
    <div class='content'>
      <div class='header'>Beim update sind Fehler aufgetreten</div>
      <ul class='list'>
        <li class='content' v-bind:key="error" v-for="error in errorStringArray">
          {{error}}
        </li>
      </ul>
    </div>
  </div>
  <div id="editUserButtonContainer">
    <button class="ui primary inverted button editUserButton" @click="editUser" v-if="!this.editMode" >Benutzer editieren</button>
    <button class="ui positive inverted button editUserButton" @click="saveEdit" v-if="this.editMode" >Speichern</button>
    <button class="ui negative basic button editUserButton" @click="abortEdit" v-if="this.editMode" >Abbrechen</button>
  </div>
  <table class="ui red table tablePart">
    <tr>
      <th>Vorname</th>
      <td v-show="!this.editMode">{{displayedUser.prename}}</td>
      <td><input v-model="displayedUser.prename" v-show="this.editMode" v-bind:class="{ui: true, fluid: true, input: true, editField: true, errorInField: errorPrename}"/></td>
    </tr>
     <tr>
      <th>Nachname</th>
      <td v-show="!this.editMode">{{displayedUser.lastname}}</td>
      <td><input v-model="displayedUser.lastname" v-show="this.editMode" v-bind:class="{ui: true, fluid: true, input: true, editField: true, errorInField: errorLastname}"/></td>
    </tr>
     <tr>
      <th>E-Mail</th>
      <td v-show="!this.editMode">{{displayedUser.email}}</td>
      <td><input v-model="displayedUser.email" v-show="this.editMode" v-bind:class="{ui: true, fluid: true, input: true, editField: true, errorInField: errorEmail}"/></td>
    </tr>
     <tr>
      <th>Kennung</th>
      <td v-show="!this.editMode">{{displayedUser.hsid}}</td>
      <td><input v-model="displayedUser.hsid" v-show="this.editMode" v-bind:class="{ui: true, fluid: true, input: true, editField: true, errorInField: errorHSID}"/></td>
    </tr>
     <tr>
      <th>Status</th>
      <td>{{displayedUser.status}}</td>
    </tr>
     <tr>
      <th>Nutzer Aktiviert</th>
      <td>
        <div class="ui toggle checkbox">
          <input type="checkbox" v-model="displayedUser.is_activated" @change="changeDeactivated" name="public">
          <label>(Aus = Nutzer Deaktiviert)</label>
        </div>
      </td>
    </tr>
    <tr v-if="this.editMode">
      <th>Passwort</th>
      <td><button class="ui red inverted button" @click="resetPasswordOfUser">Zurücksetzen</button></td>
    </tr>
  </table>
  <table class="ui black table tablePart">
    <tr>
      <th>Entfernung zur HS in Meter</th>
      <td>{{displayedUser.meter_to_hs}}</td>
    </tr>
     <tr>
      <th>Rolle</th>
      <td>{{displayedUser.role}}</td>
    </tr>
     <tr>
      <th>Standort erlaubt?</th>
      <td>{{displayedUser.location_is_allowed}}</td>
    </tr>
    <tr>
      <th>Zuletzt Online</th>
      <td>{{displayedUser.last_time_online ? formatTime(displayedUser.last_time_online) : undefined}}</td>
    </tr>
    <tr>
      <th>Ist Online?</th>
      <td>{{displayedUser.isOnline}}</td>
    </tr>
    <tr>
      <th>Erstellt am</th>
      <td>{{formatTime(displayedUser.createdAt)}}</td>
    </tr>
    <tr>
      <th>Update am</th>
      <td>{{formatTime(displayedUser.updatedAt)}}</td>
    </tr>
  </table>
  <table class="ui grey table tablePart">
    <tr>
      <th>User Id</th>
      <td>{{displayedUser.id}}</td>
    </tr>
    <tr>
      <th>Verifiziert</th>
      <td>{{displayedUser.isVerified}}</td>
    </tr>
     <tr>
      <th>Verifizierungs-Token</th>
      <td>{{displayedUser.verifyToken}}</td>
    </tr>
     <tr>
      <th>Rücksetzungs-Token</th>
      <td>{{displayedUser.resetToken}}</td>
    </tr>
     <tr>
      <th>Ablaufdatum der Verifizerung</th>
      <td>{{displayedUser.verifyExpires}}</td>
    </tr>
     <tr>
      <th>Letzter Standort Check</th>
      <td>{{displayedUser.location_check_time}}</td>
    </tr>
  </table>
</div>
</div>
</template>

<script>
import { feathersClient } from '../feathers-client'
import moment from 'moment'

export default {
  name: 'userprofile',

  data () {
    return {
      displayedUser: {},
      editMode: false,
      editSuccessful: false,
      editAborted: false,
      resetPasswordDone: false,
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
    this.loadData()
  },
  methods: {
    loadData () {
      feathersClient
        .service('users')
        .get(this.$route.params.id)
        .then(user => {
          this.displayedUser = user
        })
        .catch(error => {
          console.error(JSON.stringify(error))
        })
    },
    editUser () {
      this.editMode = true
      this.editSuccessful = false
      this.editAborted = false
    },
    saveEdit () {
      if (!this.errorCheck()) {
        return
      }

      this.displayedUser.email = this.displayedUser.email.toLowerCase()
      this.displayedUser.hsid = this.displayedUser.hsid.toLowerCase()

      feathersClient
        .service('users')
        .patch(this.displayedUser.id, this.displayedUser)
        .then(result => {
          this.loadData()
          this.editMode = false
          this.editSuccessful = true
        })
        .catch(error => {
          this.closeNotification()
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
      if (!this.displayedUser.prename || !this.displayedUser.lastname || !this.displayedUser.email || !this.displayedUser.hsid) {
        if (!this.displayedUser.prename) {
          this.errorStringArray.push('Bitte Vornamen eintragen')
          this.errorPrename = true
        }
        if (!this.displayedUser.lastname) {
          this.errorStringArray.push('Bitte Nachnamen eintragen')
          this.errorLastname = true
        }
        if (!this.displayedUser.email) {
          this.errorStringArray.push('Bitte E-Mail eintragen')
          this.errorEmail = true
        }
        if (!this.displayedUser.hsid) {
          this.errorStringArray.push('Bitte Hochschul-Kennung eintragen')
          this.errorHSID = true
        }
      }
      if (this.displayedUser.email.indexOf('hs-coburg.de') === -1) {
        this.errorStringArray.push('Bitte E-Mail überprüfen')
        this.errorEmail = true
      }
      const rege = this.displayedUser.hsid.match(/[a-z0-9A-Z]{8}/)
      if (rege === null || rege[0] !== this.displayedUser.hsid) {
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
      this.closeNotification()
      this.editMode = false
      this.loadData()
      this.editSuccessful = false
      this.editAborted = true
    },
    closeNotification () {
      this.editSuccessful = false
      this.editAborted = false
      this.resetPasswordDone = false
      this.showErrorString = false
    },
    resetPasswordOfUser () {
      this.closeNotification()
      this.resetPasswordDone = true
      // TODO: reset Password
    },
    formatTime: (timeStamp) => moment(timeStamp).format('DD.MM.YYYY hh:mm:ss'),
    changeDeactivated () {
      feathersClient
        .service('users')
        .patch(this.displayedUser.id, { is_activated: this.displayedUser.is_activated })
        .then(result => {
          this.loadData()
          this.closeNotification()
          this.editMode = false
          this.editSuccessful = true
        })
        .catch(error => {
          console.error(JSON.stringify(error))
        })
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

.editField {
  max-width: 95%;
  width: 95%;
}

.errorInField {
  border-color: #e0b4b4!important;
  color: #9f3a38!important;
  background: #fff6f6!important;
}

</style>
