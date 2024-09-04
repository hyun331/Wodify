<template>
  <div class="background">
    <v-container>
      <div>
        <h1 class="rubikMonoOne">MY</h1>
        <h1 class="rubikMonoOne">RESERVATION</h1>
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
                <v-row class="menufont">
                  <v-col cols="3"><label>SUCCESS OR FAILURE</label></v-col>
                  <v-col cols="8">
                    <v-radio-group v-model="registerData.snf" inline>
                      <v-radio label="SUCCESS" value="S"></v-radio>
                      <v-radio label="FAILURE" value="F"></v-radio>
                    </v-radio-group>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="3"><label class="menufont">EXERCISE TIME</label></v-col>
                  <v-col cols="5">
                    <v-text-field class="custom-text-box" style="margin-right:2px" v-model="registerData.exerciseTime"
                      label="ExerciseTime" placeholder="HH:mm:ss" @click="timePickerVisible = true"
                      readonly></v-text-field>
                    <v-dialog v-model="timePickerVisible" persistent width="400px">
                      <v-card class="dark-card">
                        <v-card-text class="time-picker-content">
                          <v-row>
                            <v-col>
                              <v-select v-model="selectedHour" :items="hours" label="HH"></v-select>
                            </v-col>
                            <v-col>
                              <v-select v-model="selectedMinute" :items="minutes" label="MM"></v-select>
                            </v-col>
                            <v-col>
                              <v-select v-model="selectedSecond" :items="seconds" label="SS"></v-select>
                            </v-col>
                          </v-row>
                        </v-card-text>
                        <v-card-actions class="time-picker-actions">
                          <v-spacer></v-spacer>
                          <v-btn text @click="confirmTime" class="bold-button">OK</v-btn>
                          <v-btn text @click="timePickerVisible = false" class="bold-button">Cancel</v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="3"><label class="menufont">COMMENT</label></v-col>
                  <v-col cols="8">
                    <v-textarea v-model="registerData.comments" required auto-grow :rows="5"></v-textarea>
                  </v-col>
                </v-row>
                <hr style="border: solid 0.8px #BDBDBD">
                <br>
                <v-row v-for=" (wodDetDto, index) in wod.wodDetResDtoList" :key="wodDetDto.id">
                  <v-col cols="3">
                    <label class="menufont">{{ wodDetDto.name }} - {{ wodDetDto.contents }}</label>
                  </v-col>
                  <v-col cols="8">
                    <v-text-field v-model="registerData.recordDetSaveReqDtoList[index].detailComments"
                      label="Detail Comments" required></v-text-field>
                  </v-col>
                </v-row>
                <v-row justify="end">
                  <v-btn type="submit">CREATE</v-btn>
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
                    <td class="menufont">EXERCISE TIME</td>
                    <td>{{ this.record.exerciseTime }}</td>
                  </tr>
                  <tr>
                    <td class="menufont">COMMENT</td>
                    <td>{{ this.record.comments }}</td>
                  </tr>
                  <tr v-for=" recordDetDto in record.recordResDetDtoList" :key="recordDetDto.id">
                    <td class="menufont">{{ recordDetDto.wodName }} - {{ recordDetDto.wodContents }}</td>
                    <td>{{ recordDetDto.detailComments }}</td>
                  </tr>
                </tbody>
              </v-table>
              <br>
              <v-row justify="end">
                <v-btn @click="changeModifyBtn">MODIFY</v-btn>
                <v-btn @click="showDeleteRecordModal">DELETE</v-btn>
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
                  <v-col cols="3"><label class="menufont">SUCCESS OR FAILURE</label></v-col>
                  <v-col cols="8">
                    <v-radio-group v-model="modifyData.snf" inline>
                      <v-radio label="SUCCESS" value="S"></v-radio>
                      <v-radio label="FAILURE" value="F"></v-radio>
                    </v-radio-group>
                  </v-col>
                </v-row>
                <v-row>
                  <v-col cols="3"><label class="menufont">EXERCISE TIME</label></v-col>
                  <v-col cols="5">
                    <v-text-field class="custom-text-box" style="margin-right:2px" v-model="modifyData.exerciseTime"
                      label="ExerciseTime" placeholder="HH:mm:ss" @click="mtimePickerVisible = true"
                      readonly></v-text-field>
                    <v-dialog v-model="mtimePickerVisible" persistent width="400px">
                      <v-card class="dark-card">
                        <v-card-text class="time-picker-content">
                          <v-row>
                            <v-col>
                              <v-select v-model="selectedHour" :items="hours" label="HH"></v-select>
                            </v-col>
                            <v-col>
                              <v-select v-model="selectedMinute" :items="minutes" label="MM"></v-select>
                            </v-col>
                            <v-col>
                              <v-select v-model="selectedSecond" :items="seconds" label="SS"></v-select>
                            </v-col>
                          </v-row>
                        </v-card-text>
                        <v-card-actions class="time-picker-actions">
                          <v-spacer></v-spacer>
                          <v-btn text @click="confirmmTime" class="bold-button">OK</v-btn>
                          <v-btn text @click="mtimePickerVisible = false" class="bold-button">Cancel</v-btn>
                        </v-card-actions>
                      </v-card>
                    </v-dialog>
                  </v-col>
                </v-row>
                <hr style="border: solid 0.8px #BDBDBD">
                <br>
                <v-row>
                  <v-col cols="3"><label class="menufont">COMMENT</label></v-col>
                  <v-col cols="8">
                    <v-textarea v-model="modifyData.comments" required auto-grow :rows="5"></v-textarea>
                  </v-col>
                </v-row>
                <v-row v-for=" (wodDetDto, index) in wod.wodDetResDtoList" :key="wodDetDto.id">
                  <v-col cols="3">
                    <label class="menufont">{{ wodDetDto.name }} - {{ wodDetDto.contents }}</label>
                  </v-col>
                  <v-col cols="8">
                    <v-text-field v-model="modifyData.recordDetSaveReqDtoList[index].detailComments"
                      label="Detail Comments" required></v-text-field>
                  </v-col>
                </v-row>
                <v-row justify="end">
                  <v-btn type="submit">MODIFY</v-btn>
                </v-row>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>



      <DeleteRecordModal v-model="deleteRecord" :recordId="reservation.recordId" @update:dialog="deleteRecord = $event">
      </DeleteRecordModal>

      <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
        :dialogText="dialogText" />

    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import DeleteRecordModal from './DeleteRecordModal.vue';
import ReservationWod from './ReservationDetailWodModal.vue'
import AlertModalComponent from '@/components/AlertModalComponent.vue';

export default {
  components: {
    DeleteRecordModal, ReservationWod, AlertModalComponent
  },
  data() {
    return {
      reservationDetailId: this.$route.params.id,
      reservation: {}, // 예약내역 조회

      record: {}, // 조회용 record

      isNotExisted: false, // 등록용 체크
      isExisted: false,    // 조회용 체크

      wod: {},  // 와드내역-디테일 조회

      timePickerVisible: false, // 등록용 시간 체크

      selectedHour: '00',
      selectedMinute: '00',
      selectedSecond: '00',
      hours: Array.from({ length: 24 }, (_, i) => String(i).padStart(2, '0')), // 00 ~ 23
      minutes: Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0')), // 00 ~ 59
      seconds: Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0')), // 00 ~ 59

      registerData: {
        snf: "",
        exerciseTime: null,
        comments: "",
        recordDetSaveReqDtoList: []
      }, // 등록용 데이터


      mtimePickerVisible: false, // 수정용 시간 체크

      modifyData: {
        snf: "",
        exerciseTime: null,
        comments: "",
        recordDetSaveReqDtoList: []
      }, // 수정용 데이터

      isModify: false,

      isWod: false,

      deleteRecord: false,


      alertModal: false,
      dialogTitle: "",
      dialogText: "",

    };
  },
  computed: {
    recordButtonLabel() { // 예약내역에 운동기록이 있나 없나 표시
      if (this.reservation.recordSnF === 'S') { // 있음
        return 'SUCCESS';
      } else if (this.reservation.recordSnF === 'F') { // 있음
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
        this.isNotExisted = false;
        this.viewRecord();
      } else {
        this.isNotExisted = true;
        this.isExisted = false;
        this.isModify = false;
      }
    },
    confirmTime() {
      if (this.selectedHour !== null && this.selectedMinute !== null && this.selectedSecond !== null) {
        this.registerData.exerciseTime = `${this.selectedHour}:${this.selectedMinute}:${this.selectedSecond}`;
      }
      this.timePickerVisible = false;
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
        let errorMessage = "";
        if (e.response && e.response.data) {
          errorMessage += `: ${e.response.data.error_message}`;
        } else if (e.message) {
          errorMessage += `: ${e.message}`;
        }
        this.dialogTitle = "기록할 수 없습니다";
        this.dialogText = errorMessage;
        this.alertModal = true;
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

    confirmmTime() {
      if (this.selectedHour !== null && this.selectedMinute !== null && this.selectedSecond !== null) {
        this.modifyData.exerciseTime = `${this.selectedHour}:${this.selectedMinute}:${this.selectedSecond}`;
      }
      this.mtimePickerVisible = false;
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

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@600&family=Rubik:wght@500&display=swap');

.rubik-button {
  font-family: "Rubik", sans-serif;
  font-optical-sizing: auto;
  font-weight: 400;
  font-style: normal;
}

/* color="grey-lighten-1" class="rubik-button" */

.background {
  min-height: 100vh;
  margin: 0;
  background-image: url("@/assets/background.png");
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.menufont {
  font-family: 'Rubik Mono One', sans-serif;
  font-size: 17px
}

.custom-text-box {
  background-color: rgba(255, 255, 255, 0.5);
  border-radius: 4px;
  margin-bottom: 2px;
}

.dark-card {
  background-color: #333;
  /* 다크 그레이 배경 */
  color: white;
  /* 텍스트 색상을 밝게 설정 */
}

.time-picker-content {
  padding: 0px;
  /* 전체 패딩을 줄여 간격을 최적화 */
  margin-left: -10px;
  margin-right: -10px;
  margin-bottom: -15px;
}

.time-picker-actions {
  padding-top: 0;
  /* 버튼 영역의 상단 간격을 줄임 */
  padding-bottom: 0px;
  /* 버튼 영역의 하단 간격을 줄임 */
  margin-top: -30px;
  /* 위와 아래 간격을 줄여 버튼과 콘텐츠 간격 최소화 */
  margin-right: 7px;
}

.bold-button {
  font-weight: bold;
}
</style>