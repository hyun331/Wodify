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
                  <td><v-btn> {{ recordButtonLabel }}</v-btn></td>
                </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>

    <v-row>
        <v-col>
            <v-card>
                <v-card-text>
                    <v-form @submit.prevent="createRecord">
                        <v-row>
                            <v-col cols="4"><v-label>SUCCESS OR FAILURE</v-label></v-col>
                            <v-col cols="8">
                                <v-radio-group v-model="record.snf" inline>
                                <v-radio label="SUCCESS" value="S"></v-radio>
                                <v-radio label="FAILURE" value="N"></v-radio>
                                </v-radio-group>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="2"><v-label>TIME</v-label></v-col>
                            <v-col cols="5">
                                <v-text-field placeholder="--:--:--" v-model="record.exerciseTime" required></v-text-field>
                            </v-col>
                        </v-row>
                        <v-row>
                            <v-col cols="2"><v-label>COMMENT</v-label></v-col>
                            <v-col cols="9">
                                <v-text-field v-model="record.comments" required></v-text-field>
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
  </v-container>
</template>

<script>
// import axios from 'axios';
export default {
  data() {
    return {
      // reservationDetailId : this.$router.params.id,
      reservation: {},
      record: {}
    };
  },
  computed: {
  // recordButtonLabel() {
  //     if (this.reservation.recordSnF === 'S') {
  //       return 'SUCCESS';
  //     } else if (this.reservation.recordSnF === 'N') {
  //       return 'FAILURE';
  //     } else {
  //       return 'CREATE';
  //     }
  //   },
  },
  async created(){
      // try{
      //   const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservaion-detail/detail/${this.reservationDetailId}`)
      //   this.reservation = response.data.result;
      // }catch(e){
      //   console.log(e)
      // }
  },
  methods: {
      viewWod() { // 와드 보는 기능 구현
      },

      createRecord() { // record 없을 때 생성
      },
  }
}

</script>