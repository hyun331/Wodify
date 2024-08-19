<template>
  <v-app>
    <HeaderComponent/>
    <v-main style="background-color: #D9D9D9;">
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
import HeaderComponent from './components/HeaderComponent.vue';

export default {
  name: 'App',
  components: {
    HeaderComponent
  }
}
</script>

<style>
@import url('https://fonts.googleapis.com/css2?family=Oswald:wght@200..700&family=Rubik+Mono+One&display=swap');
.rubikMonoOne {
  font-family: "Rubik Mono One", monospace;
  font-weight: 400;
  font-style: normal;
  font-size: 120px;

}
</style>
