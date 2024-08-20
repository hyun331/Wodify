<template>
    <div class="box right-align">
      <br>
      <span class="boxLocation">
        {{ this.boxName }}
      </span>
      <br>
    </div>
    <v-container>
      <v-row>
        <v-col cols="12" md="4" class="date-picker-col">
          <v-date-picker v-model="selectedDate" @update:model-value="onDateSelected" class="custom-date-picker">
          </v-date-picker>
        </v-col>
        <v-col cols="12" md="8" class="content-col">
          <WodFind v-show="wod" :wod="wod" :key="wod" @wod-deleted="onWodDeleted" />
          <WodSave v-show="!wod" :date="formattedDate" :key="formattedDate" @wod-saved="onWodSaved" />
        </v-col>
      </v-row>
    </v-container>
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
  async created() {
    try {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/box/name/`);
      this.boxName = response.data.result;
    } catch (error) {
      let errorMessage = "";
      if (error.response && error.response.data) {
        errorMessage += `: ${error.response.data.error_message}`;
      } else if (error.message) {
        errorMessage += `: ${error.message}`;
      }
      this.dialogTitle = "박스명 로드 실패";
      this.dialogText = errorMessage;
      this.alertModal = true;
    }
  },
  data() {
    return {
      wod: null,
      selectedDate: new Date(), // 초기 값을 오늘 날짜로 설정
      errorMessage: '',
      formattedDate: this.formatDate(new Date()), // 초기화된 날짜를 포맷
      isLoading: false,
      boxName: "",
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
.box {
  background-color: #797876;
}

.right-align {
  text-align: right;
}

.boxLocation {
  color: white;
  font-weight: 1000;
  font-size: 70px;
  font-family: "Oswald", sans-serif;
}






.date-picker-col {
  flex: 0 0 auto;
  max-width: 300px;
  position: sticky;
  top: 55px;
  min-width: 300px;
}

.custom-date-picker {
  background-color: rgba(255, 255, 255, 0.5);
  margin-top: 20px;
}





.content-container {
  max-width: 1200px;
  margin: 0 auto;
  padding-top: 20px;
}

.content-col {
  flex: 1;
  margin-left: 20px;
  box-sizing: border-box;
}

.fixed-layout {
  display: flex;
  justify-content: flex-start;
  align-items: flex-start;
  flex-wrap: nowrap;
}

</style>
