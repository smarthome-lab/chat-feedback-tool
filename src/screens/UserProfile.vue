<template>
<div id="userprofile">
  <nav-bar></nav-bar>
  <div class="content">
  <h1>Benutzerprofil für {{displayedUser.prename}}</h1>

  <table class="ui celled table">
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
      <th>Ist online</th>
    </tr></thead>
    <tbody>
      <tr>
        <td>{{displayedUser.prename}}</td>
        <td>{{displayedUser.lastname}}</td>
        <td>{{displayedUser.email}}</td>
        <td>{{displayedUser.hsid}}</td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
        <td></td>
      </tr>
    </tbody>
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
      displayedUser: {}, 

    }
  },
  created(){
      console.log('created');
      this.loadData();
    } , 
  methods:{
    loadData(){
      console.log('LoadDAta');
        feathersClient.passport.verifyJWT(response.accessToken).then((u)=> {
          feathersClient.service('users').get(u.userId).then((user) => {
            if(user.userId === this.$route.params.id){
               console.log(JSON.stringify(user));
                 this.displayedUser = user;
            }else{
              console.log('something wrong with request');
            }

          })
        })

    },

    


  } 
}


</script>

<style>
  table{
    width:100%
  }
</style>
