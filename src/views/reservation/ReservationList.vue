<template>
    <div class="page-container">
    

    <v-container>
      <div>
        <h1 class="title">MY</h1>
        <h1 class="title">RESERVATION</h1>
        </div>
      <v-row>
        <v-col>
          <v-card>
            <v-card-text>
              <v-table >
                <thead>
                    <tr>
                      <th style="font-weight: bold;">DATE</th>
                      <th style="font-weight: bold;">TIME</th>
                      <th style="font-weight: bold;">COACH</th>
                      <th style="font-weight: bold;">WOD</th>
                      <th style="font-weight: bold;">RECORD</th></tr>
                </thead>
                <tbody>
                    <tr v-for="r in reservationList" :key="r.id">
                    <td>{{r.date}}</td>
                    <td>{{r.time}}</td>
                    <td>{{r.coach}}</td>
                    <td>{{r.wodId}}</td>
                    <td></td></tr>
                </tbody>
            </v-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
export default {
    
  data() {
    return {
      reservationList: []
    };
  },
  created() {
    
  }
};
</script>

<style>
.title {
  font-family: "Roboto", sans-serif;
  font-size: 100px;
  line-height: 1.0; /* 전체 텍스트의 줄 간격 설정 */
}

.page-container {
    /* 전체 페이지의 배경색을 설정합니다 */
    background-color: #D9D9D9; /* 원하는 배경색으로 변경 */
    min-height: 100vh; /* 전체 화면 높이로 설정 */
  }

.bold {
  font-weight: bold;
}
</style>
