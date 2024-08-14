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
                    <td>{{r.coachName}}</td>
                    <td><v-btn>view</v-btn></td>
                    <td v-if="r.recordId">{{r.recordSnF}}</td><td v-else><v-btn>create</v-btn></td></tr>
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
import axios from 'axios';
export default {
    
  data() {
    return {
      reservationList: []
    };
  },
  async created() {
      const token = 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MjMxNjA1MDQsImV4cCI6MTcyMzE3ODUwNH0.IwzZ6rz9V7EH58_qaLsykQHqq0FYasqMOdW0fboXzjE';
        try {
          console.log(`${process.env.VUE_APP_API_BASE_URL}`);
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/mylist`,{headers:{Authorization: `Bearer ${token}`}});
            console.log(response)
            this.reservationList = response.data.result.content;
        } catch (error) {
            console.log(error);
        }
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
