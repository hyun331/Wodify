<template>
  <div class="background-image">
    <v-container class="content-container">
      <v-row class="fixed-layout">
        <v-col cols="12" md="4" class="date-picker-col">
          <div class="date-picker-container">
            <v-date-picker
              v-model="selectedDate"
              @update:model-value="onDateSelected"
              class="custom-date-picker"
            >
              <template v-slot:header></template>
            </v-date-picker>
          </div>
        </v-col>
        <v-col cols="12" md="8" class="content-col">
          <WodFind v-show="wod" :wod="wod" :key="wod" @wod-deleted="onWodDeleted" />
          <WodSave
            v-show="!wod"
            :date="formattedDate"
            :key="formattedDate"
            @wod-saved="onWodSaved"
          />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
import WodFind from './WodFind.vue';
import WodSave from './WodSave.vue';

export default {
  components: {
    WodFind,
    WodSave,
  },
  data() {
    return {
      wod: null,
      selectedDate: new Date(), // 초기 값을 오늘 날짜로 설정
      errorMessage: '',
      formattedDate: this.formatDate(new Date()), // 초기화된 날짜를 포맷
      isLoading: false,
    };
  },
  mounted() {
    // 페이지 로드 시 오늘 날짜로 데이터 조회
    this.fetchWodData(this.formattedDate);
  },
  methods: {
    onDateSelected(date) {
      this.formattedDate = this.formatDate(date);
      this.fetchWodData(this.formattedDate);
    },
    formatDate(date) {
      const year = date.getFullYear();
      const month = ('0' + (date.getMonth() + 1)).slice(-2);
      const day = ('0' + date.getDate()).slice(-2);
      return `${year}-${month}-${day}`;
    },
    async fetchWodData(date) {
      try {
        this.isLoading = true;
        this.wod = null;
        const response = await axios.get(`http://localhost:8090/wod/find/${date}`);

        if (response.status === 200) {
          this.wod = response.data.result;
        } else {
          this.wod = null;
        }
      } catch (error) {
        if (error.response && error.response.status === 404) {
          this.wod = null;
        } else {
          this.errorMessage = 'WOD 데이터를 불러오는 중 오류가 발생했습니다.';
          console.error('Error fetching WOD data:', error.response ? error.response.data : error.message);
        }
      } finally {
        this.isLoading = false;
      }
    },
    onWodSaved(date) {
      this.fetchWodData(date);
    },
    onWodDeleted(date) {
      this.fetchWodData(date);
    },
  },
};
</script>


<style scoped>
.background-image {
  background-image: url('@/assets/background.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
}

.content-container {
  max-width: 1200px;
  margin: 0 auto; /* 가로 중앙 정렬 */
  padding-top: 20px; /* 상단에 위치하게 설정 */
}

.fixed-layout {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  flex-wrap: nowrap; /* 줄 바꿈을 방지 */
}

.date-picker-col {
  flex: 0 0 auto;
  max-width: 300px;
  position: sticky;
  top: 55px; /* 상단에서의 간격 설정 */
  min-width: 300px; /* 최소 너비 설정 */
}

.content-col {
  flex: 1;
  margin-left: 20px; /* v-date-picker와 컴포넌트 간 20px 간격 설정 */
  box-sizing: border-box;
}

.custom-date-picker {
  background-color: rgba(255, 255, 255, 0.5);
  margin-top: 20px;
}
</style>
