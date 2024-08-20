<template>
    <div class="background-image">
        <div class=’rubikMonoOne’>
            <h1 class="rubikMonoOne">BOX</h1>
            <h1 class="rubikMonoOne">SUCCESS</h1>
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
            window.location.href = '/box/mybox';
        },
        home() {
            window.location.href = '/';
        }
    }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik+Mono+One&display=swap');
html, body {
  margin: 0; /* 기본 여백 제거 */
  padding: 0; /* 기본 패딩 제거 */
  height: 100%; /* 전체 높이 설정 */
}

.background-image {
    background-image: url('');
    background-size: cover; /* 이미지 크기 조절 */
    background-position: center; /* 이미지 위치 조정 */
    min-height: 100vh; /* 화면 전체 높이 설정 */
    width: 100%; /* 화면 전체 너비 설정 */
  }

</style>
