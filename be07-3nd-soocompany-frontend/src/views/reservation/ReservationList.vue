<template>
  <div class="page-container">
    <div class="box right-align">
      <br>
      <span class="boxLocation">
        {{ this.boxName }}
      </span>
      <br>
    </div>

    <v-container>
      <div>
        <h1 class="rubikMonoOne" style="margin-top: 10px;">RESERVATION</h1>
      </div>


      <v-row justify="end">
        <v-col cols="4">
          <v-text-field v-model="startDate" label="Start Date" type="date" class="mx-2"></v-text-field>
        </v-col>
        <v-col cols="4">
          <v-text-field v-model="endDate" label="End Date" type="date" class="mx-2"></v-text-field>
        </v-col>
        <v-btn @click="searchByDateRange" icon style="margin-top: 16px; margin-right: 20px;">
          <v-icon>mdi-magnify</v-icon>
        </v-btn>
      </v-row>

      <v-row>
        <v-col>
          <v-card>
            <v-card-text>

              <v-table>
                <thead>
                  <tr>
                    <th style="font-weight: bold; text-align: center;">DATE</th>
                    <th style="font-weight: bold; text-align: center;">TIME</th>
                    <th style="font-weight: bold; text-align: center;">PEOPLE</th>
                    <th style="font-weight: bold; text-align: center;">WOD</th>
                    <th style="font-weight: bold; text-align: center;">Delete</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="r in filteredReservationList" :key="r.id">
                    <td style="text-align: center;" @click="viewDetail(r.reservationDetails)">
                      {{ r.date }}
                    </td>
                    <td style="text-align: center;" @click="viewDetail(r.reservationDetails)">
                      {{ r.time.slice(0, 5) }}
                    </td>
                    <td style="text-align: center;" @click="viewDetail(r.reservationDetails)">
                      {{ r.reservationPeople }} / {{ r.maxPeople }}
                    </td>
                    <td style="text-align: center;">
                      <v-btn @click="showWodModal(r.wod_id)">view</v-btn>
                    </td>
                    <!-- Updated delete button event -->
                    <td style="text-align: center;">
                      <v-btn @click="confirmDelete(r.id)">delete</v-btn>
                    </td>
                  </tr>
                </tbody>
              </v-table>

              <ReservationMemberListModal v-model="memberListModal" @update:dialog="memberListModal = $event"
                :memberList="selectedMemberList" />


            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
      <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
        :dialogText="dialogText" />

      <v-dialog v-model="deleteModal" max-width="400px">
        <v-card>
          <v-card-title class="headline">{{ dialogTitle }}</v-card-title>
          <v-card-text>정말로 예약을 삭제하시겠습니까?</v-card-text>
          <v-card-actions>
            <v-btn text @click="deleteModal = false">아니오</v-btn>
            <v-btn class="hover-btn" text @click="cancel">예</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>
      <WodModal v-if="wodId" v-model="wodModal" :wodId="this.wodId" @update:dialog="wodModal = $event">

      </WodModal>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import ReservationMemberListModal from './ReservationMemberListModal.vue';
import AlertModalComponent from '@/components/AlertModalComponent.vue';
import WodModal from './WodModal.vue';
export default {
  components: {
    ReservationMemberListModal,
    AlertModalComponent,
    WodModal
  },
  data() {
    return {
      reservationList: [],
      memberListModal: false,
      selectedMemberList: [],
      filteredReservationList: [],
      startDate: "",
      endDate: "",
      pageSize: 10,
      currentPage: 0,
      isLastPage: false,
      isLoading: false,
      alertModal: false,
      dialogTitle: "",
      dialogText: "",
      boxName: "",
      deleteModal: false,
      reservationIdToDelete: null,
      wodModal: false,
      wodId: "",
    };
  },
  async created() {
    try {
      this.loadList();
      window.addEventListener('scroll', this.scrollPagination);

      console.log(this.filteredReservationList);
    } catch (error) {
      let errorMessage = "";
      if (error.response && error.response.data) {
        // 서버에서 반환한 에러 메시지가 있는 경우
        errorMessage += `: ${error.response.data.error_message}`;
      } else if (error.message) {
        errorMessage += `: ${error.message}`;
      }
      this.dialogTitle = "예약 로드 실패";
      this.dialogText = errorMessage;
      this.alertModal = true;
      console.log(error);
    }

    try {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/box/name/`);
      this.boxName = response.data.result;
    } catch (error) {
      let errorMessage = "";
      if (error.response && error.response.data) {
        // 서버에서 반환한 에러 메시지가 있는 경우
        errorMessage += `: ${error.response.data.error_message}`;
      } else if (error.message) {
        errorMessage += `: ${error.message}`;
      }
      this.dialogTitle = "박스명 로드 실패";
      this.dialogText = errorMessage;
      this.alertModal = true;
    }
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.scrollPagination);
  },
  methods: {
    showWodModal(wodId) {
      this.wodId = wodId;
      this.wodModal = true;
    },
    async loadList() {
      try {
        if (this.isLoading || this.isLastPage) return;
        this.isLoading = true;
        let params = {
          size: this.pageSize,
          page: this.currentPage
        }
        if (this.startDate && this.endDate) {
          params.startDate = this.startDate;
          params.endDate = this.endDate;
        }
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservation/box/list/`, { params });
        const additionalData = response.data.result.content;
        this.isLastPage = response.data.isLastPage;
        this.reservationList = [...this.reservationList, ...additionalData];
        this.filteredReservationList = this.reservationList;
        this.currentPage++;
        console.log(response.data);
        this.isLoading = false;

      } catch (error) {
        let errorMessage = "";
        if (error.response && error.response.data) {
          // 서버에서 반환한 에러 메시지가 있는 경우
          errorMessage += `: ${error.response.data.error_message}`;
        } else if (error.message) {
          errorMessage += `: ${error.message}`;
        }
        this.dialogTitle = "예약 로드 실패";
        this.dialogText = errorMessage;
        this.alertModal = true;
        console.log(error);
      }
    },
    scrollPagination() {
      // 현재 화면 + 스크롤로 이동한 화면 > 전체화면 -N 의 조건이 성립되면 추가 데이터 로드
      const isBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - 100;
      if (isBottom && !this.isLastPage && !this.isLoading) {
        this.loadList();
      }
    },
    searchByDateRange() {
      console.log(this.startDate);
      console.log(this.endDate);
      this.reservationList = [];
      this.currentPage = 0;
      this.isLastPage = false;
      this.isLoading = false;
      this.loadList();
    },
    viewDetail(memberList) {
      this.selectedMemberList = memberList;
      this.memberListModal = true;
    },
    confirmDelete(id) {
      this.memberListModal = false;
      this.reservationIdToDelete = id; // Store the ID of the reservation to delete
      this.deleteModal = true; // Show the delete confirmation modal
    },
    async cancel() {
      try {
        await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/reservation/delete/${this.reservationIdToDelete}`);
        window.location.reload();
      } catch (error) {
        let errorMessage = "";
        if (error.response && error.response.data) {
          // 서버에서 반환한 에러 메시지가 있는 경우
          errorMessage += `: ${error.response.data.error_message}`;
        } else if (error.message) {
          errorMessage += `: ${error.message}`;
        }
        this.dialogTitle = "예약 취소 실패";
        this.dialogText = errorMessage;
        this.alertModal = true;
        console.log(error);
      }
    }
  }
};
</script>

<style scoped>
.bold {
  font-weight: bold;
}

.box {
  background-color: #797876;
  text-align: right;
}

.boxLocation {
  color: white;
  font-weight: 1000;
  font-size: 70px;
  font-family: "Oswald", sans-serif;
}
</style>
