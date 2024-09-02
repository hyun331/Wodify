<template>
  <v-app class="lyrics">
    <HeaderComponent/>
    <v-main v-if="userRole=='USER'" class="userBackground">
      <router-view/>
    </v-main>

    <v-main v-else style="background-color: #D9D9D9;">
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
  },
  data(){
    return{
      userRole : localStorage.getItem('role'),

    }
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

.userBackground {
  min-height: 100vh;
  margin: 0;
  background-image: url("@/assets/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}
.lyrics{
  overflow: scroll;
}
</style>
