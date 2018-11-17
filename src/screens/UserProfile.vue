<template>
<div id="userprofile">
  <nav-bar></nav-bar>
  <div class="content">
  <h1>Benutzerprofil für {{displayedUser.prename}}</h1>

  <table class="ui celled table" width="95%">
    <thead class="bottom aligned">
      <tr>
      <th>Vorname</th>
      <th>Name</th>
      <th>E-Mail</th>
      <th>Kennung</th>
      <th>Status</th>
      <th>Entfernung zur HS(Meter)</th>
      <th>Rolle</th>
      <th>Verifiziert</th>
      <th>Verifizierungs-Token</th>
      <th>Rücksetzungs-Token</th>
      <th>Ablaufdatum der Verifizerung</th>
      <th>Zuletzt online</th>
      <th>Ist online?</th>
      <th>Standort erlaubt?</th>
      <th>Prüfzeit</th>
      <th>Erstellt am</th>
      <th>Update am</th>
    </tr></thead>
    <tbody>
      <tr>
        <td>{{displayedUser.prename}}</td>
        <td>{{displayedUser.lastname}}</td>
        <td>{{displayedUser.email}}</td>
        <td>{{displayedUser.hsid}}</td>
        <td>{{displayedUser.status}}</td>
        <td>{{displayedUser.meter_to_hs}}</td>
        <td>{{displayedUser.role}}</td>
        <td>{{displayedUser.isVerified}}</td>
        <td>{{displayedUser.verifyToken}}</td>
        <td>{{displayedUser.resetToken}}</td>
        <td>{{displayedUser.verifyExpires}}</td>
        <td>{{displayedUser.last_time_online}}</td>
        <td>{{displayedUser.isOnline}}</td>
        <td>{{displayedUser.location_is_allowed}}</td>
        <td>{{displayedUser.location_check_time}}</td>
        <td>{{displayedUser.createdAt}}</td>
        <td>{{displayedUser.updatedAt}}</td>
      </tr>
    </tbody>
  </table>
</div>
</div>
</template>

<script>
import { feathersClient } from "../feathers-client";
import { mapGetters, mapMutations } from "vuex";

export default {
  name: "userprofile",

  data() {
    return {
      displayedUser: {}
    };
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      feathersClient
        .service("users")
        .get(this.$route.params.id)
        .then(user => {
          console.log(JSON.stringify(user));
          this.displayedUser = user;
        })
        .catch(error => {
          console.log(error);
        });
    }
  }
};
</script>

<style>
table {
  width: 100%;
}
</style>
