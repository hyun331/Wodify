<template>
  <div class="page-container">
    <v-container>
      <div>
        <h1 class="title">MY</h1>
        <h1 class="title">RESERVATION</h1>
      </div>
      <v-row justify="center">
        <v-col cols="12" sm="2" md="4">
          <v-card>
            <v-card-text>
              <v-table>
                <tbody>
                  <tr>
                    <td style="font-weight: bold;">DATE</td>
                    <td>{{ reservationDetail.date }}</td>
                  </tr>
                  <tr>
                    <td style="font-weight: bold;">TIME</td>
                    <td>{{ reservationDetail.time }}</td>
                  </tr>
                  <tr>
                    <td style="font-weight: bold;">COACH</td>
                    <td>{{ reservationDetail.coachName }}</td>
                  </tr>
                  <tr>
                    <td style="font-weight: bold;">WOD</td>
                    <td><v-btn>VIEW</v-btn></td>
                  </tr>
                  <tr>
                    <td style="font-weight: bold;">RECORD</td>
                    <td><v-btn @click="reservationRecord">{{ reservationDetailRecordSnF }}</v-btn></td>
                  </tr>
                </tbody>
              </v-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
      <Record></Record>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import Record from './RecordCreate.vue';

export default{
  components:{
    Record,
  },
  data(){
    return{
      reservationDetailId : this.$router.params.id,
      reservationDetail: {},
      reservationDetailRecordSnF: "",
    }
  },
  async created(){
      try{
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservaion-detail/detail/${this.reservationDetailId}`)
        this.reservationDetail = response.data.result;
        if(this.reservationDetail.recordSnF === null){
          this.reservationDetailRecordSnF = "CREATE";
        }else if(this.reservationDetail.recordSnF === "S"){
          this.reservationDetailRecordSnF = "SUCCESS";
        }else if(this.reservationDetail.recordSnF === "N"){
          this.reservationDetailRecordSnF = "FAILURE";
        }
      }catch(e){
        console.log(e)
      }
  },
  methods:{
    reservationRecord(){

    }
  }

}
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