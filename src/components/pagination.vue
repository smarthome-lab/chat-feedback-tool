<template>
  <div class="ui pagination menu">


    <!-- Greater then 2 -->
    <a v-if="page>2" @click="onClick('first')" class="item">
      1
    </a>
    <a v-if="page>3" class="disabled item">
      ...
    </a>

    <!-- Show more back buttons if the end is near -->
    <a v-if="(page+1)>(Math.ceil(total/resultsPerPage))" @click="onClick(-4)" class="item">
      {{page-4}}
    </a>
    <a v-if="(page+2)>(Math.ceil(total/resultsPerPage))" @click="onClick(-3)" class="item">
      {{page-3}}
    </a>
    <a v-if="(page+3)>(Math.ceil(total/resultsPerPage))" @click="onClick(-2)" class="item">
      {{page-2}}
    </a>
    <!-- one back three plus -->
    <a v-if="page>1" @click="onClick(-1)" class="item">
      {{page-1}}
    </a>
    <a class="active item">
      {{page}}
    </a>
    <a v-if="(page)<(Math.ceil(total/resultsPerPage))" class="item" @click="onClick(1)">
      {{page+1}}
    </a>
    <a v-if="(page+1)<(Math.ceil(total/resultsPerPage))" class="item" @click="onClick(2)">
      {{page+2}}
    </a>
    <a v-if="(page+2)<(Math.ceil(total/resultsPerPage))" class="item" @click="onClick(3)">
      {{page+3}}
    </a>
    <a v-if="page<1 && (page+3)<(Math.ceil(total/resultsPerPage))" class="item" @click="onClick(4)">
      {{page+4}}
    </a>
    <!-- show Max Page if it is grater then page+4 -->
    <a v-if="(page+4)<(Math.ceil(total/resultsPerPage))" class="disabled item">
      ...
    </a>
    <a v-if="(page+3)<(Math.ceil(total/resultsPerPage))" @click="onClick('last')" class="item">
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
        loading: true, /*
        pagee: this.page,
        totall: this.total,
        resultsPerPagee: this.resultsPerPage */
      }
    },
    methods: {}
  }
</script>

<style>

</style>
