<template>
  <div class="page-container">
    <div class="box right-align">
      <br>
      <span class="boxLocation">
        CROSSFIT RICH NANGOK
      </span>
      <br>
    </div>

    <v-container>
      <div>
        <h1 class="rubikMonoOne" style="margin-top: 10px;">RESERVATION</h1>
      </div>

      <!-- Date Search Fields -->
      <v-row>
        <v-col cols="4">
          <v-text-field v-model="startDate" label="Start Date" type="date" class="mx-2"></v-text-field>
        </v-col>
        <v-col cols="4">
          <v-text-field v-model="endDate" label="End Date" type="date" class="mx-2"></v-text-field>
        </v-col>
        <v-col cols="4">
          <v-btn @click="searchByDateRange">Search</v-btn>
        </v-col>
      </v-row>

      <v-row>
        <v-col>
          <v-card>
            <v-card-text>

              <v-table>
                <thead>
                  <tr>
                    <th style="font-weight: bold;">DATE</th>
                    <th style="font-weight: bold;">TIME</th>
                    <th style="font-weight: bold;">PEOPLE</th>
                    <th style="font-weight: bold;">WOD</th>
                    <th style="font-weight: bold;">Delete</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="r in filteredReservationList" :key="r.id" @click="viewDetail(r.reservationDetails)"
                    style="cursor: pointer;">
                    <td>{{ r.date }}</td>
                    <td>{{ r.time.slice(0, 5) }}</td>
                    <td>{{ r.reservationPeople }} / {{ r.maxPeople }}</td>
                    <td><v-btn :to="{ path: '/wod/find/' + r.date }">view</v-btn></td>
                    <td><v-btn @click.stop="cancel(r.id)">delete</v-btn></td>
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
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import ReservationMemberListModal from './ReservationMemberListModal.vue';
import AlertModalComponent from '@/components/AlertModalComponent.vue';

export default {
  components: {
    ReservationMemberListModal,
    AlertModalComponent
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
  },
  beforeUnmount() {
    window.removeEventListener('scroll', this.scrollPagination);
  },
  methods: {
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
    async cancel(id) {
      try {
        await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/reservation/delete/` + id);
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

<style>
.bold {
  font-weight: bold;
}
</style>
