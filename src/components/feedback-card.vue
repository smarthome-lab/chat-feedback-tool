<template>
  <sui-card :key="feedback.id">
    <sui-card-content>
      <sui-card-meta>{{time}}</sui-card-meta>
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
          v-model.lazy="feedback.tags"
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
</template>

<script>
  import moment from 'moment'

  export default {
    name: "feedback-card",
    props: {
      feedback: {
        type: Object,
        required: true
      },
      patchFeedback:{
        type: Function,
        required: true
      }
    },
    data() {
      return {
        //feedback: this.feedback,
        loading: false,
        loadingImportant: false,
        error: '',
        currentTags: this.feedback.tags,
        defaultTags: [
          {key: 'Chat', text: 'Chat', value: 'chat'},
          {key: 'Gruppenchat', text: 'Gruppenchat', value: 'groupchat'},
          {key: 'Solochat', text: 'Solochat', value: 'solochat'},
          {key: 'Profil', text: 'Profil', value: 'profil'},
          {key: 'bug', text: 'bug', value: 'bug'},
          {key: 'Verbesserung', text: 'Verbesserung', value: 'verbesserung'},
          {key: 'lob', text: 'lob', value: 'lob'},
        ],
      }
    },
    computed: {
      time: function () {
        return moment(this.feedback.createdAt).format('DD.MM.YYYY [um] hh:mm')
      }
    },
    methods: {
    },
    watch: {
      'feedback.tags': {
        handler: function (val, oldVal) {
          //Check if there is something to be updated
          if(this.feedback.tags.toString()===oldVal.toString()){
            return
          }
          //Update the tags
          this.patchFeedback(this.feedback.id, {tags: this.feedback.tags})
        }, deep: true
      }
    }
  }
</script>

<style scoped>
  .tags {
    width: 70% !important;
    display: inline-block;
  }
</style>
