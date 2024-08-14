<template>
  <v-container>
    <div>
      <h1>MY</h1>
      <h1>RESERVATION</h1>
    </div>
    <br>
    <v-row>
      <v-col>
        <v-card>
          <v-card-text>
            <v-table>
              <tbody>
                <tr>
                  <td>DATE</td>
                  <td>{{ reservation.date }}</td>
                </tr>
                <tr>
                  <td>TIME</td>
                  <td>{{ reservation.time }}</td>
                </tr>
                <tr>
                  <td>COACH</td>
                  <td>{{ reservation.coachName }}</td>
                </tr>
                <tr>
                  <td>WOD</td>
                  <td><v-btn @click="viewWod">VIEW</v-btn></td>
                </tr>
                <tr>
                  <td>RECORD</td>
                  <td><v-btn @click="handleRecord">{{ recordButtonLabel }}</v-btn></td>
                </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

     <!-- 컴포넌트 처리할 거는 일단 미루고 다 쑤셔 넣었다.-->

    <v-row v-if="isNotExisted"> <!-- 운동기록 등록-->
        <v-col>
            <v-card>
                <v-card-text>
                    <v-form @submit.prevent="createRecord">
                        <v-row>
                            <v-col cols="4"><v-label>SUCCESS OR FAILURE</v-label></v-col>
                            <v-col cols="8">
                                <v-radio-group v-model="snf" inline>
                                <v-radio label="SUCCESS" value="S"></v-radio>
                                <v-radio label="FAILURE" value="N"></v-radio>
                                </v-radio-group>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="2"><v-label>TIME</v-label></v-col>
                            <v-col cols="5">
                                <v-text-field
                                    v-model="time"
                                    :active="menu2"
                                    :focus="menu2"
                                    label="Exersice Time"
                                    readonly
                                  >
                                    <v-menu
                                      v-model="menu2"
                                      :close-on-content-click="false"
                                      activator="parent"
                                      transition="scale-transition"
                                    >
                                      <v-time-picker
                                        use-seconds
                                        v-if="menu2"
                                        v-model="time"
                                        full-width
                                      ></v-time-picker>
                                    </v-menu>
                                  </v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="2"><v-label>COMMENT</v-label></v-col>
                            <v-col cols="9">
                                <v-text-field v-model="comments" required></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row justify="end">
                            <v-btn type="submit" color="grey-lighten-1">CREATE</v-btn>
                        </v-row>
                    </v-form>
                </v-card-text>
            </v-card>
        </v-col>
      </v-row>

      <v-row v-if="isExisted"> <!--운동기록 조회-->
        <v-col>
            <v-card>
                <v-card-text>
                  <v-table>
                    <tbody>
                      <tr>
                        <td>SUCCESS OR FAILURE</td>
                        <td>{{ recordSnF }}</td>
                      </tr>
                      <tr>
                        <td>TIME</td>
                        <td>{{ this.record.exerciseTime }}</td>
                      </tr>
                      <tr>
                        <td>COMMENT</td>
                        <td>{{ this.record.comments }}</td>
                      </tr>
                    </tbody>
                  </v-table>
                    <v-row justify="end">
                        <v-btn @click="modifyRecord" color="grey-lighten-1">MODIFY</v-btn>
                        <v-btn color="grey-lighten-1">DELETE</v-btn>
                    </v-row>
                </v-card-text>
            </v-card>
        </v-col>
      </v-row>

      
     
  </v-container>
</template>

<script>
import axios from 'axios';
export default {
  data() {
    return {
      reservationDetailId : this.$route.params.id,
      reservation: {},

      record: {}, // 조회용 record

      isNotExisted: false,
      isExisted: false,
      
      menu2:false,

      snf:"",
      time: null,
      comments:"",

      isModify: false,
    };
  },
  computed: {
  recordButtonLabel() {
      if (this.reservation.recordSnF === 'S') {
        return 'SUCCESS';
      } else if (this.reservation.recordSnF === 'N') {
        return 'FAILURE';
      } else {
        return 'CREATE';
      }
    },

    recordSnF() {
      if (this.record.snf  === 'S') {
        return 'SUCCESS';
      } else if (this.record.snf  === 'N'){
        return 'FAILURE';
      } else{
        return 'NULL';
      }
    },
  },
  async created(){
      try{
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/detail/${this.reservationDetailId}`)
        this.reservation = response.data.result;
      }catch(e){
        console.log(e)
      }
  },
  methods: {
    viewWod() { // 와드 보는 기능 구현
    },
    handleRecord() {
      if (this.reservation.recordId) {
        this.isExisted = true;
        this.viewRecord();
      } else {
        this.isNotExisted = true;
      }
    },
    async createRecord() { // record 없을 때 생성
      try{
        const registerData = {
          snf: this.snf,
          exerciseTime: this.time,
          comments: this.comments,
          reservationDetailId: this.reservationDetailId
        }
        await axios.put(`${process.env.VUE_APP_API_BASE_URL}/record/create`, registerData);
        window.location.reload(); // 스무스하게 바꾸고 싶은데.. 일단 보류
      }catch(e){
        console.log(e)
      }
    },
    async viewRecord(){
      try{
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/record/detail/${this.reservation.recordId}`)
        this.record = response.data.result;
      }catch(e){
        console.log(e)
      }
    },
    async modifyRecord(){
      try{
        console.log("ha")
      }catch(e){
        console.log(e)
      }
    },
  }
}

</script>