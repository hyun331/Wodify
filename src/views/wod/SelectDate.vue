<template>
    <div>
      <v-container>
        <v-row>
          <v-col cols="12" md="4">
            <v-date-picker 
              v-model="selectedDate" 
              @update:model-value="onDateSelected">
            </v-date-picker>
        </v-col>
        <v-col cols="12" md="8" class="content-col">
          <WodFind v-show="wod" :wod="wod" :key="wod" @wod-deleted="onWodDeleted" />
          <WodSave v-show="!wod" :date="formattedDate" :key="formattedDate" @wod-saved="onWodSaved" />
        </v-col>
      </v-row>
      <v-row>
      <v-col cols="12" md="8" class="content-col">
        <WodFind v-show="wod" :wod="wod" :key="wod" @wod-deleted="onWodDeleted" />
        <WodSave v-show="!wod" :date="formattedDate" :key="formattedDate" @wod-saved="onWodSaved" />
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
  min-height: 100vh;
  /* 변경: 100vh에서 min-height: 100vh로 수정 */
  position: relative;
  overflow: auto;
  /* 변경: overflow: hidden에서 overflow: auto로 수정 */
}

.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding-top: 20px;
}

.fixed-layout {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  flex-wrap: nowrap;
}

.date-picker-col {
  flex: 0 0 auto;
  max-width: 300px;
  position: sticky;
  top: 55px;
  min-width: 300px;
}

.content-col {
  flex: 1;
  margin-left: 20px;
  box-sizing: border-box;
}

.custom-date-picker {
  background-color: rgba(255, 255, 255, 0.5);
  margin-top: 20px;
}
</style>
