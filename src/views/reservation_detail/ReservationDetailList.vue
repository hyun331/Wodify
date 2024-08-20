<template>
  <div class="page-container">


    <v-container>
      <div style="line-height: 1;">
        <h1 class="rubikMonoOne">MY</h1>
        <h1 class="rubikMonoOne">RESERVATION</h1>
      </div>

      <v-row style="margin-top: 20px;" justify="end">
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
                    <th style="font-weight: bold; text-align: center;">COACH</th>
                    <th style="font-weight: bold; text-align: center;">RECORD</th>
                    <th style="font-weight: bold; text-align: center;">Delete</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="r in filteredReservationList" :key="r.id" @click="goToDetail(r.id)"
                    style="cursor: pointer;">
                    <td style="text-align: center;">{{ r.date }}</td>
                    <td style="text-align: center;">{{ r.time.slice(0, 5) }}</td>
                    <td style="text-align: center;">{{ r.coachName }}</td>
                    <td style="text-align: center;" v-if="r.recordId">{{ r.recordSnF }}</td>
                    <td style="text-align: center;" v-else><v-btn size=small>create</v-btn></td>
                    <td style="text-align: center;">
                      <v-btn @click.stop="confirmDelete(r.id)" size="small">delete</v-btn>
                    </td>
                  </tr>
                </tbody>
              </v-table>
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
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import AlertModalComponent from '@/components/AlertModalComponent.vue';
export default {
  components: {
    AlertModalComponent
  },
  data() {
    return {
      reservationList: [],
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
      deleteModal: false,
      reservationIdToDelete: null,
    };
  },
  async created() {
    try {
      this.loadList();
      window.addEventListener('scroll', this.scrollPagination);
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
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/mylist`, { params });
        const additionalData = response.data.result.content;
        this.isLastPage = response.data.isLastPage;
        this.reservationList = [...this.reservationList, ...additionalData]
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
    goToDetail(id) {
      this.$router.push(`/reservation-detail/detail/${id}`);
    },
    confirmDelete(id) {
      this.reservationIdToDelete = id; // Store the ID of the reservation to delete
      this.dialogTitle = "예약 삭제 확인";
      this.deleteModal = true; // Show the delete confirmation modal
    },
    async cancel() {
      try {
        await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/delete/${this.reservationIdToDelete}`);
        window.location.reload();
      } catch (error) {
        let errorMessage = "";
        if (error.response && error.response.data) {
          // 서버에서 반환한 에러 메시지가 있는 경우
          errorMessage += `: ${error.response.data.error_message}`;
        } else if (error.message) {
          errorMessage += `: ${error.message}`;
        }
        this.dialogTitle = "예약 삭제 실패";
        this.dialogText = errorMessage;
        this.alertModal = true;
        console.log(error);
      }
    }
  }
};
</script>

<style scope></style>
