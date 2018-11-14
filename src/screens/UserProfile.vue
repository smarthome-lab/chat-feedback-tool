<template>
<div id="userprofile">
  <nav-bar></nav-bar>
  <div class="content">
  <h1>Benutzerübersicht für {{benutzername}}</h1>
  <table>
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
      <th>Ist online</th>
    </tr>
    <tr>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
      <th>{{kennung}}</th>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
      <th>Test</th>
    </tr>
  </table>
</div>
</div>
</template>

<script>
import {feathersClient} from '../feathers-client'
import {mapGetters, mapMutations} from 'vuex'

export default {
  name: 'userprofile',
  data () {
    return {
      benutzername: this.$route.params.id,
      kennung: this.$route.params.id
    }
  }
}
</script>

<style>
  table{
    width:100%
  }
</style>
