<template>
  <div class="page-container">
    <v-container>
      <div>
        <h1 class="menufont">MY</h1>
        <h1 class="menufont">RESERVATION</h1>
      </div>
      <br>
      <v-row>
        <v-col>
          <v-card>
            <v-card-text>
              <v-table>
                <tbody>
                  <tr>
                    <td class="menufont">DATE</td>
                    <td>{{ reservation.date }}</td>
                  </tr>
                  <tr>
                    <td class="menufont">TIME</td>
                    <td>{{ reservation.time }}</td>
                  </tr>
                  <tr>
                    <td class="menufont">COACH</td>
                    <td>{{ reservation.coachName }}</td>
                  </tr>
                  <tr>
                    <td class="menufont">WOD</td>
                    <td><v-btn @click="showWod">VIEW</v-btn></td>
                  </tr>
                  <tr>
                    <td class="menufont">RECORD</td>
                    <td><v-btn @click="handleRecord">{{ recordButtonLabel }}</v-btn></td>
                  </tr>
                </tbody>
              </v-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <!-- 컴포넌트 처리할 거는 일단 미루고 다 쑤셔 넣었다.-->

      <ReservationWod v-model="isWod" :wod="wod" @update:dialog="isWod = $event">
      </ReservationWod>

      <v-row v-if="isNotExisted"> <!-- 운동기록 등록-->
        <v-col>
          <v-card>
            <v-card-text>
              <v-form @submit.prevent="createRecord">
                <v-row>
                  <v-col cols="4"><v-label class="menufont">SUCCESS OR FAILURE</v-label></v-col>
                  <v-col cols="8">
                    <v-radio-group v-model="registerData.snf" inline>
                      <v-radio label="SUCCESS" value="S"></v-radio>
                      <v-radio label="FAILURE" value="N"></v-radio>
                    </v-radio-group>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="2"><v-label class="menufont">TIME</v-label></v-col>
                  <v-col cols="5">
                    <v-text-field v-model="registerData.exerciseTime" :active="menu2" :focus="menu2"
                      label="Exersice Time" readonly>
                      <v-menu v-model="menu2" :close-on-content-click="false" activator="parent"
                        transition="scale-transition">
                        <v-time-picker use-seconds v-if="menu2" v-model="registerData.exerciseTime"
                          full-width></v-time-picker>
                      </v-menu>
                    </v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="2"><v-label class="menufont">COMMENT</v-label></v-col>
                  <v-col cols="9">
                    <v-text-field v-model="registerData.comments" required></v-text-field>
                  </v-col>
                </v-row>
                <v-row v-for=" (wodDetDto, index) in wod.wodDetResDtoList" :key="wodDetDto.id">
                  <v-col cols="5">
                    <v-label class="menufont">name : {{ wodDetDto.name }}</v-label>
                    <br>
                    <v-label class="menufont">contents : {{ wodDetDto.contents }}</v-label>
                  </v-col>
                  <v-col cols="7">
                    <v-text-field v-model="registerData.recordDetSaveReqDtoList[index].detailComments"
                      label="Detail Comments" required></v-text-field>
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
                    <td class="menufont">SUCCESS OR FAILURE</td>
                    <td>{{ this.record.snf === 'S' ? 'SUCCESS' : 'FAILURE' }}</td>
                  </tr>
                  <tr>
                    <td class="menufont">TIME</td>
                    <td>{{ this.record.exerciseTime }}</td>
                  </tr>
                  <tr>
                    <td class="menufont">COMMENT</td>
                    <td>{{ this.record.comments }}</td>
                  </tr>
                </tbody>
                <tr v-for=" recordDetDto in record.recordResDetDtoList" :key="recordDetDto.id">
                  <td class="menufont">name : {{ recordDetDto.wodName }}</td>
                  <td class="menufont">contents : {{ recordDetDto.wodContents }}</td>
                  <td class="menufont">comment : {{ recordDetDto.detailComments }}</td>
                </tr>
              </v-table>
              <v-row justify="end">
                <v-btn @click="changeModifyBtn" color="grey-lighten-1">MODIFY</v-btn>
                <v-btn @click="showDeleteRecordModal" color="grey-lighten-1">DELETE</v-btn>
              </v-row>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <v-row v-if="isModify"> <!-- 운동기록 수정-->
        <v-col>
          <v-card>
            <v-card-text>
              <v-form @submit.prevent="modifyRecord">
                <v-row>
                  <v-col cols="4"><v-label class="menufont">SUCCESS OR FAILURE</v-label></v-col>
                  <v-col cols="8">
                    <v-radio-group v-model="modifyData.snf" inline>
                      <v-radio label="SUCCESS" value="S"></v-radio>
                      <v-radio label="FAILURE" value="N"></v-radio>
                    </v-radio-group>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="2"><v-label class="menufont">TIME</v-label></v-col>
                  <v-col cols="5">
                    <v-text-field v-model="modifyData.exerciseTime" :active="menu2" :focus="menu2" label="Exersice Time"
                      readonly>
                      <v-menu v-model="menu2" :close-on-content-click="false" activator="parent"
                        transition="scale-transition">
                        <v-time-picker use-seconds v-if="menu2" v-model="modifyData.exerciseTime" full-width></v-time-picker>
                      </v-menu>
                    </v-text-field>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="2"><v-label class="menufont">COMMENT</v-label></v-col>
                  <v-col cols="9">
                    <v-text-field v-model="modifyData.comments" required></v-text-field>
                  </v-col>
                </v-row>
                <v-row v-for=" (wodDetDto, index) in wod.wodDetResDtoList" :key="wodDetDto.id">
                  <v-col cols="5">
                    <v-label class="menufont">name : {{ wodDetDto.name }}</v-label>
                    <br>
                    <v-label class="menufont">contents : {{ wodDetDto.contents }}</v-label>
                  </v-col>
                  <v-col cols="7">
                    <v-text-field v-model="modifyData.recordDetSaveReqDtoList[index].detailComments"
                      label="Detail Comments" required></v-text-field>
                  </v-col>
                </v-row>
                <v-row justify="end">
                  <v-btn type="submit" color="grey-lighten-1">MODIFY</v-btn>
                </v-row>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>

      <DeleteRecordModal v-model="deleteRecord" :recordId="reservation.recordId" @update:dialog="deleteRecord = $event">
      </DeleteRecordModal>

    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import DeleteRecordModal from './DeleteRecordModal.vue';
import ReservationWod from './ReservationDetailWodModal.vue'

export default {
  components: {
    DeleteRecordModal, ReservationWod
  },
  data() {
    return {
      reservationDetailId: this.$route.params.id,
      reservation: {}, // 예약내역 조회

      record: {}, // 조회용 record

      isNotExisted: false,
      isExisted: false,    // 조회용 체크

      menu2: false, // 시계

      wod: {},  // 와드내역-디테일 조회

      registerData: {
        snf: "",
        exerciseTime: null,
        comments: "",
        recordDetSaveReqDtoList: []
      }, // 등록용 데이터

      modifyData: {
        snf: "",
        exerciseTime: null,
        comments: "",
        recordDetSaveReqDtoList: []
      }, // 수정용 데이터

      isModify: false,

      isWod: false,

      deleteRecord: false

    };
  },
  computed: {
    recordButtonLabel() { // 예약내역에 운동기록이 있나 없나 표시
      if (this.reservation.recordSnF === 'S') { // 있음
        return 'SUCCESS';
      } else if (this.reservation.recordSnF === 'N') { // 있음
        return 'FAILURE';
      } else {                                        // 없음
        return 'CREATE';
      }
    },
  },
  async created() { // 예약내역 상세조회
    try {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/detail/${this.reservationDetailId}`)
      this.reservation = response.data.result;
      const wodResponse = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find/${this.reservation.date}`);
      this.wod = wodResponse.data.result;
      const wodDetails = this.wod.wodDetResDtoList;
      this.registerData.recordDetSaveReqDtoList = wodDetails.map(() => ({ wodDetailId: null, detailComments: "" }));
      this.modifyData.recordDetSaveReqDtoList = wodDetails.map(() => ({ wodDetailId: null, detailComments: "" }));
    } catch (e) {
      console.log(e)
    }
  },
  methods: {
    handleRecord() { // 운동기록 있냐에 따른 화면구성(있음->조회, 없음->등록) // 구현완료 // 수정필요
      if (this.reservation.recordSnF) {
        this.isExisted = true;
        this.isModify = false;
        this.viewRecord();
      } else {
        this.isNotExisted = true;
      }
    },
    async createRecord() { // 운동기록 없을 때 등록 // 구현완료 // 수정필요
      try {
        let wodDetails = this.wod.wodDetResDtoList
        this.registerData.recordDetSaveReqDtoList = wodDetails.map((wodDetDto, index) => ({
          wodDetailId: wodDetDto.id,
          detailComments: this.registerData.recordDetSaveReqDtoList[index].detailComments
        }));
        let recordData = {
          snf: this.registerData.snf,
          exerciseTime: this.registerData.exerciseTime,
          comments: this.registerData.comments,
          reservationDetailId: this.reservationDetailId,
          recordSaveReqDetDtoList: this.registerData.recordDetSaveReqDtoList
        };
        console.log(recordData);
        await axios.put(`${process.env.VUE_APP_API_BASE_URL}/record/create`, recordData);
        window.location.reload(); // 스무스하게 바꾸고 싶은데.. 일단 보류
      } catch (e) {
        console.log(e)
      }
    },
    async viewRecord() { // 운동기록 있을 때 조회 // 구현완료 // 수정필요
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/record/detail/${this.reservation.recordId}`)
        this.record = response.data.result;
        console.log(this.record);
      } catch (e) {
        console.log(e)
      }
    },


    changeModifyBtn() { // 운동기록 수정 -> 화면구성 변경
      this.isModify = true;
      this.isExisted = false;
    },
    async modifyRecord() { // 운동기록 수정 // 구현완료 // 수정 필요
      try {
        const wodDetails = this.wod.wodDetResDtoList
        this.modifyData.recordDetSaveReqDtoList = wodDetails.map((wodDetDto, index) => ({
          wodDetailId: wodDetDto.id,
          detailComments: this.modifyData.recordDetSaveReqDtoList[index].detailComments
        }));
        let mRecordData = {
          snf: this.modifyData.snf,
          exerciseTime: this.modifyData.exerciseTime,
          comments: this.modifyData.comments,
          recordUpdateReqDtoList: this.modifyData.recordDetSaveReqDtoList
        };
        console.log(mRecordData);
        await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/record/update/${this.reservation.recordId}`, mRecordData);
        window.location.reload(); // 스무스하게 바꾸고 싶은데.. 일단 보류
      } catch (e) {
        console.log(e)
      }
    },

    showWod() {
      console.log("hoho");
      console.log(this.reservation.date);
      console.log("hoho");
      this.isWod = true;
    },
    showDeleteRecordModal() {
      this.deleteRecord = true;
    }
  }
}

</script>

<style>
.page-container {
  background-color: #D9D9D9;
}

.menufont {
  font-family: 'Rubik Mono One', sans-serif;
}
</style>