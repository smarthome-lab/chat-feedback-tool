<template>
  <div class="ui pagination menu">

    <!-- Get to the first Page -->
    <a v-if="page>2" @click="onClick('first')" class="item">
      1
    </a>
    <a v-if="page>2" class="item">
      ...
    </a>

    <!-- default pages -->
    <a v-if="page>1" @click="onClick(-1)" class="item">
      {{page-1}}
    </a>
    <a class="active item">
      {{page}}
    </a>
    <a v-if="(page+1)<=(Math.ceil(total/resultsPerPage))"  @click="onClick(+1)" class="item">
      {{page+1}}
    </a>
    <a v-if="(page+2)<=(Math.ceil(total/resultsPerPage))" @click="onClick(+2)" class="item">
      {{page+2}}
    </a>
    <a v-if="(page+3)<=(Math.ceil(total/resultsPerPage))"  @click="onClick(+3)" class="item">
      {{page+3}}
    </a>
    <a v-if="page===1 && (page+4)<=(Math.ceil(total/resultsPerPage))"  @click="onClick(+4)" class="item">
      {{page+4}}
    </a>

    <!-- Get to the last Page -->
    <a v-if="page+4<=(Math.ceil(total/resultsPerPage))" class="item">
      ...
    </a>
    <a v-if="page+4<=(Math.ceil(total/resultsPerPage))" @click="onClick('last')" class="item">
      {{Math.ceil(total/resultsPerPage)}}
    </a>

  </div>
</template>

<script>

  export default {
    name: 'Pagination',
    props: {
      page: {
        type: Number,
        default: 1
      },
      total: {
        type: Number
      },
      resultsPerPage: {
        type: Number,
        default: 10
      },
      onClick: {
        type: Function,
      }
    },
    data() {
      return {
        loading: true
      }
    },
    methods: {}
  }
</script>

<style>

</style>
