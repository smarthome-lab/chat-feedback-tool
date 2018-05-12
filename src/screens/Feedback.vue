<template>
  <div id="feedback">
    <nav-bar></nav-bar>
    <div class="content">
      <h1>Feedback!</h1>
      <pagination class="pagination" v-bind:page=page v-bind:total="feedbacks.total" v-bind:resultsPerPage=results
                  :onClick=updatePage />

      <sui-card-group :items-per-row="3" stackable>
        <sui-card v-for="feedback in feedbacks.data" :key="feedback.id">
          <sui-card-content>
            <sui-card-meta>{{feedback.createdAt}}</sui-card-meta>
            <sui-card-description>
              <p>
                {{feedback.text}}
              </p>
            </sui-card-description>
          </sui-card-content>
          <sui-card-content extra>
            <div class="tags">
              <sui-dropdown
                multiple
                fluid
                :options="defaultTags"
                placeholder="Tags"
                search
                selection
                allowAdditions
                v-model="currentTags"
                :v-on:change=tagsChanged(feedback.id)
              />
            </div>
            <span slot="right">
            <!--<sui-button negative compact icon="thumbs down outline" />-->
            <sui-button-group>
              <sui-button v-if="feedback.important || feedback.important===null"
                          @click="patchFeedback(feedback.id, {important: true})" positive
                          icon="thumbs up outline"/>
              <sui-button v-if="!feedback.important && feedback.important!==null"
                          @click="patchFeedback(feedback.id, {important: true})"
                          icon="thumbs up outline"/>
              <sui-button-or/>
              <sui-button v-if="!feedback.important || feedback.important===null"
                          @click="patchFeedback(feedback.id, {important: false})" negative
                          icon="thumbs down outline"/>
              <sui-button v-if="feedback.important && feedback.important!==null"
                          @click="patchFeedback(feedback.id, {important: false})"
                          icon="thumbs down outline"/>
            </sui-button-group>
          </span>
          </sui-card-content>
        </sui-card>
      </sui-card-group>
      <pagination class="pagination" v-bind:page=page v-bind:total="feedbacks.total" v-bind:resultsPerPage=results
                  :onClick=updatePage />

    </div>
  </div>
</template>

<script>

  import {feathersClient} from '../feathers-client'
  import {mapGetters, mapMutations} from 'vuex'

  export default {
    name: 'Feedback',
    data() {
      return {
        loading: true,
        loadingImportant: false,
        error: '',
        email: '',
        feedbacks: [],
        page: 1,
        results: 6,
        currentTags: null,
        defaultTags: [
          {key: 'chat', text: 'Chat', value: 'chat'},
          {key: 'groupchat', text: 'Gruppenchat', value: 'groupchat'},
          {key: 'solochat', text: 'Solochat', value: 'solochat'},
          {key: 'profile', text: 'Profil', value: 'profil'},
          {key: 'bug', text: 'bug', value: 'bug'},
          {key: 'enhansmant', text: 'verbesserung', value: 'verbesserung'},
        ],
      }
    },
    methods: {
      test() {
        console.log('feedback current user', this.getUser())
      },
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
        feathersClient.service('feedback').find({
          query: {
            $skip: (this.page - 1) * this.results,
            $limit: this.results,
            $sort: {
              createdAt: -1
            }
          }
        }).then((feedback) => {
          this.feedbacks = feedback
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
      tagsChanged(id, tags){
        console.log(id, tags)
        let i = this.feedbacks.data.findIndex((obj) => obj.id === id)
        //console.log(this.feedbacks.data[i])
      }
    },
    watch: {
      'currentTags': function(val, oldVal){
        console.log('FEEDBACK TAAAAGS WUP WUP', val, oldVal);
      }
    },
    beforeMount() {
      this.loadFeedback()
    },
  }

</script>

<style>
  .tags {
    width: 70% !important;
    display: inline-block;
  }

  .pagination {
    margin-top: 20px !important;
    margin-bottom: 20px !important;
  }

  .content {
    margin-left: 30px;
    margin-right: 30px;
  }
</style>
