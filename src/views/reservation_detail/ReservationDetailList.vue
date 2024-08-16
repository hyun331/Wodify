<template>
  <div class="page-container">


    <v-container>
      <div style="line-height: 1;">
        <h1 class="rubikMonoOne">MY</h1>
        <h1 class="rubikMonoOne">RESERVATION</h1>
      </div>

      <v-row style="margin-top: 20px;">
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
                    <th style="font-weight: bold;">COACH</th>
                    <th style="font-weight: bold;">RECORD</th>
                    <th style="font-weight: bold;">Delete</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="r in filteredReservationList" :key="r.id" @click="goToDetail(r.id)"
                    style="cursor: pointer;">
                    <td>{{ r.date }}</td>
                    <td>{{ r.time.slice(0, 5) }}</td>
                    <td>{{ r.coachName }}</td>
                    <td v-if="r.recordId">{{ r.recordSnF }}</td>
                    <td v-else><v-btn size=small>create</v-btn></td>
                    <td><v-btn @click.stop="cancel(r.id)" size=small>delete</v-btn></td>
                  </tr>
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
      reservationList: [],
      filteredReservationList: [],
      startDate: "",
      endDate: "",
      pageSize: 5,
      currentPage: 0,
      isLastPage: false,
      isLoading: false,
    };
  },
  async created() {
    try {
      this.loadList();
      this.filteredReservationList = this.reservationList;
    } catch (error) {
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
        const additionalData = response.data.result.content.map(p => ({ ...p, quantity: 0 }));
        this.isLastPage = response.data.isLastPage;
        this.reservationList = [...this.reservationList, ...additionalData]
        this.filteredReservationList = this.reservationList;
        this.currentPage++;
        console.log(response.data);
        this.isLoading = false;

      } catch (e) {
        console.log(e);
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
    async cancel(id) {
      try {
        await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/delete/` + id);
        window.location.reload();
      } catch (error) {
        console.log(error);
      }
    }
  }
};
</script>

<style></style>
