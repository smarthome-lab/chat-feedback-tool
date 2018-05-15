<template>
  <div id="feedback">
    <nav-bar></nav-bar>
    <div class="content">
      <h1>Feedback!</h1>
      <!-- Setting -->
      <div class="ui compact segment">
        <div class="ui small form">
          <div class="ui inline field">
            <label>Suche:</label>
            <div class="ui mini input">
              <input type="text" v-on:keydown.enter="loadFeedback" v-model="searchInput" placeholder="Tag1, Tag2, Tag3">
            </div>
          </div>
          <div class="ui inline field">
            <label>Ergebnisse pro Seite:</label>
            <div class="ui mini input">
              <input class="number" v-on:keydown.enter="updateResultsPerPage" v-model="resultsInput" :placeholder="results" type="text">
            </div>
          </div>
          <div class="ui inline field">
            <label>Ergebnisse pro Spalte:</label>
            <div class="ui mini input">
              <input class="number ui large input" v-model="columnsInput" :placeholder="columns" type="text">
            </div>
          </div>
          <div class="ui inline field">
            <label>Zeige:</label>
            <div class="ui tiny input">
              <sui-dropdown
                placeholder="Gender"
                selection
                :options="showOptions"
                v-model="showInput"
              />
            </div>
          </div>
        </div>
      </div>
      <!-- Main Content -->
      <div>
        <pagination class="pagination" v-bind:page=page v-bind:total="feedbacks.total" v-bind:resultsPerPage=results
                    :onClick=updatePage />

        <sui-card-group :items-per-row="columns" stackable>
          <feedback-card v-for="feedback in feedbacks.data" v-bind:feedback=feedback :patchFeedback=patchFeedback />
        </sui-card-group>

        <pagination class="pagination" v-bind:page=page v-bind:total="feedbacks.total" v-bind:resultsPerPage=results
                    :onClick=updatePage />
      </div>
    </div>
  </div>
</template>

<script>

  import {feathersClient} from '../feathers-client'
  import {mapGetters, mapMutations} from 'vuex'
  import FeedbackCard from '../components/feedback-card'

  export default {
    name: 'Feedback',
    components: {
      FeedbackCard,
    },
    data() {
      return {
        loading: true,
        loadingImportant: false,
        error: '',
        email: '',
        feedbacks: [],
        page: 1,
        results: 6,
        resultsInput: '',
        columns: 3,
        columnsInput: '',
        showInput: 4,
        showOptions: [{
            text: 'Nur Positiv',
            value: 1,
          }, {
            text: 'Nur Negativ',
            value: 2,
          },{
          text: 'Nur ohne Wertung',
          value: 3,
          }, {
          text: 'Alles',
          value: 4,
        }],
        searchInput: '',
      }
    },
    methods: {
      ...mapGetters([
        'getUser'
      ]),
      ...mapMutations([
        'setUser',
      ]),
      updatePage(nu) {
        if (nu === 'first') {
          this.page = 1
        } else if (nu === 'last') {
          this.page = Math.ceil(this.feedbacks.total / this.results)
        } else {
          this.page = this.page + nu
        }
        this.loadFeedback()
      },
      loadFeedback() {
        this.loading = true
        //make Querry for
        let important = this.makeQueryForImportant()
        feathersClient.service('feedback').find({
          query: {
            $skip: (this.page - 1) * this.results,
            $limit: this.results,
            $sort: {
              createdAt: -1
            },
            $or: important,
            tags: this.searchInput !== '' ? { $contains: this.searchInput.split(', ')}: undefined
          }
        }).then((feedback) => {
          this.feedbacks= feedback
          this.loading = false
          console.log('Feedback!', this.feedbacks)
        });
      },
      patchFeedback(id, data) {
        this.loadingImportant = true
        feathersClient.service('feedback').patch(id, data).then((feed) => {
          this.loadingImportant = false
          //Update the object within the array
          let i = this.feedbacks.data.findIndex((obj) => obj.id === feed.id)
          this.feedbacks.data.splice(i, 1, feed)
        }).catch(e => {
          this.loadingImportant = false
          console.log(e)
        });
      },
      makeQueryForImportant(){
        let q = []

        switch (this.showInput){
          case 1: q.push({important: true}); break;
          case 2: q.push({important: false}); break;
          case 3: q.push({important: null}); break;
          case 4:
            q.push({important: true})
            q.push({important: false})
            q.push({important: null})
            break;
        }
        return q
      },
      updateResultsPerPage (val, oldVal) {
        let nu =parseInt(this.resultsInput)
        //Check if a value is given and a number
        if(!Number.isInteger(nu))return
        // Reset to first page if the current page is greater then max to prevent bugs
        if(this.page > Math.ceil(this.feedbacks.total / nu)) this.updatePage('first');
        this.results=nu
        this.loadFeedback()
      },
    },
    watch: {
      'columnsInput': {
        handler: function (val, oldVal) {
          //Check if a value is given and a number
          if(!Number.isInteger(parseInt(val)))return

          this.columns=parseInt(val)
        },
      },
      /*
      'resultsInput': {
        handler: function (val, oldVal) {
          let nu =parseInt(val)
          //Check if a value is given and a number
          if(!Number.isInteger(nu))return
          // Reset to first page if the current page is greater then max to prevent bugs
          if(this.page > Math.ceil(this.feedbacks.total / nu)) this.updatePage('first');
          this.results=nu
          //this.loadFeedback()
        },
      },
      'showInput':{
        handler: function (val, oldVal) {
          //Reload current feedbacks
          //this.loadFeedback()
        }
      },
      'searchInput':{
        handler: function (val, oldVal) {
          console.log(val.split(', '))
          this.loadFeedback()
        }
      }*/
    },
    beforeMount() {
      this.loadFeedback()
    },
  }

</script>

<style scoped>

  .pagination {
    margin-top: 20px !important;
    margin-bottom: 20px !important;
  }

  .content {
    margin-left: 30px;
    margin-right: 30px;
  }
  .number{
    width: 40px !important;
    text-align: center!important;
  }
</style>
