<template>
  <div class="col-md-12">

    <!-- Ergebnisse pro Seite -->
    <div class="ui inline field">
      <label>Ergebnisse pro Seite:</label>
      <div class="ui mini input">
        <input class="number" v-on:keyup="updateUsers" v-model="pageSize" :placeholder="results" type="number" min="1" step="1">
      </div>
    </div>

    <!-- Suche -->
    <div class="ui inline field">
      <label>Suche (Vorname, Nachname, Email oder Kennung):</label>
      <div class="ui mini input">
        <input type="text" class="form-control" v-on:keydown.enter="handleNewSearchInput" v-model="search" placeholder="Search">
      </div>
    </div>

    <!-- Filter -->
    <div class="ui inline field">
      <div class="ui mini input">
        <img src="../assets/filter.png" alt="filter" id="filterIcon" width="25px" height="25px" />

        <select v-model="filter.userStatus" @change="handleNewSearchInput">
          <option>Alle</option>
          <option>Aktiv</option>
          <option>Deaktiviert</option>
          <option>Seit 1j deaktiviert</option>
        </select>

        <select v-model="filter.verified" @change="handleNewSearchInput">
          <option>Verifiziert</option>
          <option>Nicht Verifiziert</option>
        </select>

        <select v-model="filter.role" @change="handleNewSearchInput">
          <option>Nutzer</option>
          <option>Admin</option>
        </select>
      </div>
    </div>

    <!-- Seitenauswahl -->
    <pagination class="pagination" v-bind:page=currentPage v-bind:total="userCount" v-bind:resultsPerPage=pageSize
                    :onClick=updatePage />

    <!-- Tabelle -->
    <div class="table-responsive">
      <table class="table table-striped table-bordered" style="width:100%">
          <thead width="400px">
              <tr>
                  <th scope="col">#</th>
                  <th scope="col" @click="sort('lastname')">Name <i class="fas fa-sort-alpha-down float-right"></i></th>
                  <th scope="col" @click="sort('prename')">Vorname <i class="fas fa-sort-alpha-down float-right"></i></th>
                  <th scope="col" @click="sort('email')">Email<i class="fas fa-sort-alpha-down float-right"></i></th>
                  <th scope="col" @click="sort('hsid')">Kennung<i class="fas fa-sort-alpha-down float-right"></i></th>
                  <th scope="col">Zuletzt Online</th>
                  <th scope="col">Deaktiviert</th>
                  <th scope="col">Rolle </th>
              </tr>
          </thead>
          <tbody>
              <tr v-for="(user, index) in (users)" :key="index">
                <td>{{index + 1}}</td>
                <td>{{user.lastname}}</td>
                <td>{{user.prename}}</td>
                <td><a :href="'/users/:' + user.id + ''">{{user.email}}</a></td>
                <td>{{user.hsid}}</td>
                <td>{{user.last_time_online}}</td>
                <td>{{user.status}}</td>
                <td>{{user.role}}</td>
              </tr>
          </tbody>
      </table>
    </div>

    <!-- Seitenauswahl -->
    <pagination class="pagination" v-bind:page=currentPage v-bind:total="userCount" v-bind:resultsPerPage=pageSize
                    :onClick=updatePage />
  </div>
</template>

<script>

import {feathersClient} from '../feathers-client'

export default {
  data: () => ({
    users: [], // all currently available users
    userCount: 0, // number of totally available users (more than users.length due to pagination!)
    currentSort:'lastname',
    currentSortDir:'asc',
    search: '',
    pageSize: 10,
    currentPage: 1,
    filter: {
      userStatus: 'Alle',
      verified: 'Nicht Verifiziert',
      role: 'Nutzer',
    },
  }),
  methods:{
    sort:function(s) {
      if(s === this.currentSort) {
        this.currentSortDir = this.currentSortDir==='asc'?'desc':'asc';
      }
      this.currentSort = s;

      this.users = this.users.sort((a,b) => {
                              let modifier = 1;
                              if(this.currentSortDir === 'desc') modifier = -1;
                              if(a[this.currentSort] < b[this.currentSort]) return -1 * modifier;
                              if(a[this.currentSort] > b[this.currentSort]) return 1 * modifier;
                              return 0;
                            }).filter((row, index) => {
                              let start = (this.currentPage-1)*this.pageSize;
                              let end = this.currentPage*this.pageSize;
                              if(index >= start && index < end) return true;
                            });
    },
    updateUsers() {
      if(this.search===''){
        this.getUsers();
      } else {
        this.searchUsers();
      }
    },
    getUsers() {
      const { $qt } = [null, '', 'hi'];
      feathersClient.service('users').find({
          query: {
            role: this.filter.role === 'Nutzer' ? null : this.filter.role === 'Admin' ? 'admin' : null,
            isVerified: this.filter.verified === 'Verifiziert' ? true : false,
            status: this.filter.userStatus === 'Alle' ? $qt : null,
            $skip: (this.currentPage - 1) * this.pageSize,
            $limit: this.pageSize,
            $sort: {
              lastname: 1
            },
            $select: [ 'id', 'prename', 'lastname', 'email', 'hsid', 'last_time_online', 'role', 'isVerified', 'status' ],
          }
        }).then((users) => {
          this.users = users.data;
          this.userCount = users.total;
        });
    },
    updatePage(nu) {
        if (nu === 'first') {
          this.currentPage = 1;
        } else if (nu === 'last') {
          this.currentPage = Math.ceil(this.userCount / this.pageSize);
        } else {
          this.currentPage = this.currentPage + nu;
        }
        this.updateUsers();
    },
    handleNewSearchInput() {
      this.currentPage = 1; // reset always when input changes

      if(this.search===''){
        this.updateUsers();
      } else {
        this.searchUsers();
      }
    },
    searchUsers() {

      var searchInput = [ this.search,
                          this.search.toLowerCase(),
                          this.search.toUpperCase(),
                          this.search.charAt(0).toUpperCase() + this.search.slice(1) ];

      console.log(this.filter.role === 'Nutzer' ? '' : this.filter.role === 'Admin' ? 'admin' : '')
      const { $qt } = [null, '', 'hi'];
      feathersClient.service('users').find({
          query: {
            role: this.filter.role === 'Nutzer' ? null : this.filter.role === 'Admin' ? 'admin' : null,
            isVerified: this.filter.verified === 'Verifiziert' ? true : false,
            status: this.filter.userStatus === 'Alle' ? $qt : null,
            $skip: (this.currentPage - 1) * this.pageSize,
            $limit: this.pageSize,
            $sort: {
              lastname: 1
            },
            $select: [ 'id', 'prename', 'lastname', 'email', 'hsid', 'last_time_online', 'role', 'isVerified', 'status'  ],
            $or: [
              { prename: { $in: searchInput }},
              { lastname: { $in: searchInput }},
              { email: { $in: searchInput }},
              { hsid: { $in: searchInput }}
            ],
          }
        }).then((users) => {
          this.users = users.data;
          this.userCount = users.total;
        });
    }
  },
  created () {
    this.updateUsers();
  }

}
</script>

<style>
th {
  cursor:pointer;
  /* width: 500px !important; */
  white-space: nowrap;
}
tr {
  white-space: nowrap;
}
</style>
