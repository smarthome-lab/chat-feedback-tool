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
  <table class="ui violet table tablePart">
    <tr>
      <th>Vorname</th>
      <td v-show="!this.editMode">{{displayedUser.prename}}</td>
      <td><input v-model="displayedUser.prename" v-show="this.editMode" class="ui fluid input editField"/></td>
    </tr>
     <tr>
      <th>Nachname</th>
      <td v-show="!this.editMode">{{displayedUser.lastname}}</td>
      <td><input v-model="displayedUser.lastname" v-show="this.editMode" class="ui fluid input"/></td>
    </tr>
     <tr>
      <th>E-Mail</th>
      <td v-show="!this.editMode">{{displayedUser.email}}</td>
      <td><input v-model="displayedUser.email" v-show="this.editMode" class="ui fluid input"/></td>
    </tr>
     <tr>
      <th>Kennung</th>
      <td v-show="!this.editMode">{{displayedUser.hsid}}</td>
      <td><input v-model="displayedUser.hsid" v-show="this.editMode" class="ui fluid input"/></td>
    </tr>
     <tr>
      <th>Status</th>
      <td v-show="!this.editMode">{{displayedUser.status}}</td>
      <td><input v-model="displayedUser.status" v-show="this.editMode" class="ui fluid input"/></td>
    </tr>
     <tr>
      <th>Deaktiviert</th>
      <td>
        <div class="ui toggle checkbox">
          <input type="checkbox" name="public">
          <label>(An = Nutzer Deaktiviert)</label>
        </div>
      </td>
    </tr>
    <tr v-if="this.editMode">
      <th>Passwort</th>
      <td><button class="ui red inverted button" @click="resetPasswordOfUser">Zurücksetzen</button></td>
    </tr>
  </table>
  <table class="ui red table tablePart">
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
      <td>{{displayedUser.last_time_online}}</td>
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
  <table class="ui teal table tablePart">
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

  <button class="ui primary inverted button editUserButton" @click="editUser" v-if="!this.editMode" >Benutzer editieren</button>
  <button class="ui positive inverted button editUserButton" @click="saveEdit" v-if="this.editMode" >Speichern</button>
  <button class="ui negative inverted button editUserButton" @click="abortEdit" v-if="this.editMode" >Abbrechen</button>
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
      resetPasswordDone: false
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
          console.log(JSON.stringify(user))
          this.displayedUser = user
        })
        .catch(error => {
          console.error(error)
        })
    },
    editUser () {
      this.editMode = true
      this.editSuccessful = false
      this.editAborted = false
    },
    saveEdit () {
      console.log(`UPDATE FOR ÌD: ${this.displayedUser.id} OBJ: ${JSON.stringify(this.displayedUser)}`)
      feathersClient
        .service('users')
        .patch(this.displayedUser.id, this.displayedUser)
        .then(result => {
          console.log(result)
          this.loadData()
          this.editMode = false
          this.editSuccessful = true
        })
        .catch(error => {
          console.error(error)
        })
    },
    abortEdit () {
      this.editMode = false
      this.loadData()
      this.editSuccessful = false
      this.editAborted = true
    },
    closeNotification () {
      this.editSuccessful = false
      this.editAborted = false
      this.resetPasswordDone = false
    },
    resetPasswordOfUser () {
      this.resetPasswordDone = true
      // TODO: reset Password
    },
    formatTime: (timeStamp) => moment(timeStamp).format('DD.MM.YYYY hh:mm:ss')
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
  width: 30%;
  max-width: 30%;
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
  clear: both;
  float: none;
  display: block !important;
  margin-bottom: 15px !important;
}
</style>
