<template>
  <div id="feedback">
    <nav-bar></nav-bar>
    <div class="content">
      <h1>Feedback!</h1>
      <pagination class="pagination" v-bind:page=page v-bind:total="feedbacks.total" v-bind:resultsPerPage=results
                  :onClick=updatePage />

      <sui-card-group :items-per-row="3" stackable>
        <feedback-card v-for="feedback in feedbacks.data" v-bind:feedback=feedback :patchFeedback=patchFeedback />
      </sui-card-group>

      <pagination class="pagination" v-bind:page=page v-bind:total="feedbacks.total" v-bind:resultsPerPage=results
                  :onClick=updatePage />

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
        feathersClient.service('feedback').find({
          query: {
            $skip: (this.page - 1) * this.results,
            $limit: this.results,
            $sort: {
              createdAt: -1
            }
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
</style>
