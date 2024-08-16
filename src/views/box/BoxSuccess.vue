<template>
    <div class="background-image">
        <div class=’rubikMonoOne’>
            <h1 class="title">BOX</h1>
            <h1 class="title">SUCCESS</h1>
        </div>
      <br>
      <v-card-title class="text-center text-h4">
        등록이 완료되었습니다!
      </v-card-title>
      <v-container fluid class="d-flex align-center justify-center" style="height: 80vh;">
        <v-row class="d-flex justify-center" style="margin-top: auto; margin-bottom: 25vh;">
          <v-col cols="6" sm="4" class="d-flex justify-center">
            <v-btn
              color="black"
              @click="myBox"
              block
              large
              style="height: 70px; font-size: 1.5rem; max-width: 200px;"
            >
              MY BOX
            </v-btn>
          </v-col>
          <v-col cols="6" sm="4" class="d-flex justify-center">
            <v-btn
              color="black"
              @click="home"
              block
              large
              style="height: 70px; font-size: 1.5rem; max-width: 200px;"
            >
              HOME
            </v-btn>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </template>

<script>
export default {
    data() {
        return {
        };
    },
    methods: {
        myBox() {
            window.location.href = '/box/detail';
        },
        home() {
            window.location.href = '/';
        }
    }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik+Mono+One&display=swap');
.title {
    font-family: 'Rubik Mono One', sans-serif;
  }
.background-image {
    background-image: url('');
    background-size: cover;
    background-position: center;
    min-height: 100vh;
  }

</style>
