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
        <input type="text" class="form-control" v-model="search" v-on:keyup="handleNewSearchInput" placeholder="Search">
      </div>
    </div>

    <!-- Filter -->
    <div class="ui inline field">
      <div class="ui mini input">
        <img src="../assets/filter.png" alt="filter" id="filterIcon" width="25px" height="25px" />

        <div style="width: 140px;">
          <label for="userStatusFilter">Acc. Status</label>
          <select v-model="filter.userStatus" @change="handleNewSearchInput" id="userStatusFilter">
          <option>Egal</option>
          <option>Aktiv</option>
          <option>Deaktiviert</option>
          <option>Seit 1j deaktiviert</option>
        </select>

        </div>

        <div style="width: 120px;">
        <label for="verifiedFilter">Verifiziert</label>
        <select v-model="filter.verified" @change="handleNewSearchInput" id="verifiedFilter">
          <option>Egal</option>
          <option>Verifiziert</option>
          <option>Nicht Verifiziert</option>
        </select>
        </div>

        <div style="width: 80px;">
        <label for="roleFilter">Rolle</label>
        <select v-model="filter.role" @change="handleNewSearchInput" id="roleFilter">
          <option>Egal</option>
          <option>Keine Rolle</option>
          <option>Admin</option>
        </select>
        </div>
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
            <tr class="displayTable" v-for="(user, index) in (users)" :key="index" @click="linkTo(user.id)">
              <td >{{user.lastname}}</td>
              <td>{{user.prename}}</td>
              <td>{{user.email}}</td>
              <td>{{user.hsid}}</td>
              <td>{{user.last_time_online ? formatTime(user.last_time_online) : undefined}}</td>
              <td>{{!user.is_activated}}</td>
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
import { feathersClient } from '../feathers-client'
import moment from 'moment'

export default {
  data: () => ({
    users: [], // all currently available users
    userCount: 0, // number of totally available users (more than users.length due to pagination!)
    currentSort: 'lastname',
    currentSortDir: 'asc',
    search: '',
    pageSize: 10,
    currentPage: 1,
    filter: {
      userStatus: 'Aktiv',
      verified: 'Egal',
      role: 'Egal'
    },
    results: undefined
  }),
  methods: {
    sort: function (s) {
      if (s === this.currentSort) {
        this.currentSortDir = this.currentSortDir === 'asc' ? 'desc' : 'asc'
      }
      this.currentSort = s

      this.users = this.users
        .sort((a, b) => {
          let modifier = 1
          if (this.currentSortDir === 'desc') modifier = -1
          if (a[this.currentSort] < b[this.currentSort]) return -1 * modifier
          if (a[this.currentSort] > b[this.currentSort]) return 1 * modifier
          return 0
        })
        .filter((row, index) => {
          let start = (this.currentPage - 1) * this.pageSize
          let end = this.currentPage * this.pageSize
          if (index >= start && index < end) return true
        })
    },
    updateUsers () {
      const searchObject = {
        query: {
          $skip: (this.currentPage - 1) * this.pageSize,
          $limit: this.pageSize,
          $sort: {
            lastname: 1
          },
          $select: [
            'id',
            'prename',
            'lastname',
            'email',
            'hsid',
            'last_time_online',
            'role',
            'isVerified',
            'is_activated'
          ]
        }
      }
      if (this.search !== '') {
        const searchInput = [
          this.search,
          this.search.toLowerCase(),
          this.search.toUpperCase(),
          this.search.charAt(0).toUpperCase() + this.search.slice(1)
        ]

        Object.assign(searchObject.query,
          { $or: [
            { prename: { $in: searchInput } },
            { lastname: { $in: searchInput } },
            { email: { $in: searchInput } },
            { hsid: { $in: searchInput } }
          ] })
      }
      this.addFilters(searchObject)
      console.log(searchObject)
      feathersClient
        .service('users')
        .find(searchObject)
        .then(users => {
          this.users = users.data
          this.userCount = users.total
        }).catch(error => {
          console.error(JSON.stringify(error))
        })
    },
    updatePage (nu) {
      if (nu === 'first') {
        this.currentPage = 1
      } else if (nu === 'last') {
        this.currentPage = Math.ceil(this.userCount / this.pageSize)
      } else {
        this.currentPage = this.currentPage + nu
      }
      this.updateUsers()
    },
    handleNewSearchInput () {
      this.currentPage = 1 // reset always when input changes
      this.updateUsers()
    },
    addFilters (searchObject) {
      if (this.filter.role !== 'Egal') {
        Object.assign(searchObject.query, {
          role: this.filter.role === 'Keine Rolle' ? null : 'admin'
        })
      }
      if (this.filter.verified !== 'Egal') {
        Object.assign(searchObject.query, {
          isVerified: this.filter.verified === 'Verifiziert'
        })
      }
      if (this.filter.userStatus !== 'Egal') {
        if (this.filter.userStatus === 'Aktiv' || this.filter.userStatus === 'Deaktiviert') {
          Object.assign(searchObject.query, {
            is_activated: this.filter.userStatus === 'Aktiv'
          })
        } else if (this.filter.userStatus === 'Seit 1j deaktiviert') {
          Object.assign(searchObject.query, {
            is_activated: false,
            updatedAt: {
              $lt: moment(new Date().getTime() - 31536000000).format('YYYY-MM-DD[T]HH:mm') // One Year
            }
          })
        }
      }
    },
    linkTo (userId) {
      this.$router.push({ path: `/users/${userId}` })
    },
    formatTime: (timeStamp) => moment(timeStamp).format('DD.MM.YYYY hh:mm:ss')
  },
  created () {
    this.updateUsers()
  }
}
</script>

<style>
th {
  cursor: pointer;
  /* width: 500px !important; */
  white-space: nowrap;
}
tr {
  white-space: nowrap;
}

.displayTable:hover {
  background-color: rgb(243, 250, 243) !important;
  cursor: pointer;
}
</style>
