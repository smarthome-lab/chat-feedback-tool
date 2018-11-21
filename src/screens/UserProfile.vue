<template>
<div id="userprofile">
  <nav-bar></nav-bar>
  <div class="content">
  <h1>Benutzerprofil für {{displayedUser.prename}} {{displayedUser.lastname}}</h1>

  <table class="ui violet table tablePart">
    <tr>
      <th>Vorname</th>
      <td>{{displayedUser.prename}}</td>
    </tr>
     <tr>
      <th>Nachname</th>
      <td>{{displayedUser.lastname}}</td>
    </tr>
     <tr>
      <th>E-Mail</th>
      <td>{{displayedUser.email}}</td>
    </tr>
     <tr>
      <th>Kennung</th>
      <td>{{displayedUser.hsid}}</td>
    </tr>
     <tr>
      <th>Status</th>
      <td>{{displayedUser.status}}</td>
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
      <td>{{displayedUser.createdAt}}</td>
    </tr>
    <tr>
      <th>Update am</th>
      <td>{{displayedUser.updatedAt}}</td>
    </tr>
  </table>
  <table class="ui teal table tablePart">
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

export default {
  name: 'userprofile',

  data () {
    return {
      displayedUser: {}
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
          console.log(error)
        })
    }
  }
}
</script>

<style>
table {
  width: 100%;
}
.tablePart {
  width: 30%;
  max-width: 30%;
  margin-right: 10px !important;
  margin-left: 10px !important;
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
</style>
