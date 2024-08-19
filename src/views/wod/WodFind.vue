<template>
    <v-container>
      <v-row>
        <v-col cols="12" v-if="error">
          <!-- 에러 발생 시 WodSave 컴포넌트를 표시하고, 선택한 날짜를 전달 -->
          <WodSave :date="formattedDate" />
        </v-col>
        <v-col cols="12" v-else-if="wod">
          <v-card :style="{ width: 'fit-content' }">
            <v-card-title>WOD</v-card-title>
            <v-card-text>
              <p><strong>Date:</strong> {{ wod.date }}</p>
              <p><strong>Box Name:</strong> {{ wod.boxName }}</p>
              <p><strong>Member Name:</strong> {{ wod.memberName }}</p>
              <p><strong>Info:</strong> {{ wod.info }}</p>
              <p><strong>Time Cap:</strong> {{ wod.timeCap }}</p>
              <p><strong>Rounds:</strong> {{ wod.rounds }}</p>
              <p><strong>Created Time:</strong> {{ wod.createdTime }}</p>
            </v-card-text>
            <v-card-text v-for="(wodDet, index) in wod.wodDetResDtoList" :key="wodDet.id" class="mb-4"
              :style="{ width: 'fit-content' }">
              Exercise {{ index + 1 }}
              <p><strong>Name:</strong> {{ wodDet.name }}</p>
              <p><strong>Contents:</strong> {{ wodDet.contents }}</p>
            </v-card-text>
          </v-card>
        </v-col>
        <v-col cols="12" v-else>
          <v-progress-circular indeterminate color="primary"></v-progress-circular>
          <p>Loading WOD data...</p>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  import axios from 'axios';
  import WodSave from '@/views/wod/WodSave.vue'; // WodSave 컴포넌트를 가져옵니다.
  
  export default {
    props: ['date'],
    components: {
      WodSave,
    },
  
  data() {
    return {
      localWod: { ...this.wod }, // props로 받은 wod 데이터를 로컬 데이터로 복사
    };
  },
  methods: {
    async deleteWod() {
      try {
        await axios.patch(`http://localhost:8090/wod/delete/${this.localWod.date}`);
        console.log('Delete successful');
        this.$emit('wod-deleted', this.wod.date);
      } catch (error) {
        console.error('Error deleting WOD data:', error);
        this.errorMessage = 'WOD 데이터를 삭제하는 중 오류가 발생했습니다.';
      }
    },
    watch: {
      '$route.params.date': 'fetchWodData',
    },
    mounted() {
      this.fetchWodData();
    },
    methods: {
      async fetchWodData() {
        try {
          this.error = false;
          this.wod = null;
  
          const response = await axios.get(`http://localhost:8090/wod/find/${this.date}`);
          this.wod = response.data.result;
  
          if (!this.wod) {
            throw new Error("No data found");
          }
        } catch (error) {
          if (error.response && error.response.status === 404) {
            this.error = true; // 에러 상태를 true로 설정하여 WodSave 컴포넌트를 표시합니다.
          } else {
            this.errorMessage = 'WOD 데이터를 불러오는 중 오류가 발생했습니다.';
            console.error('Error fetching WOD data:', error);
          }
        }
      },
      formatDate(date) {
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);
  
        return `${year}-${month}-${day}`;
      },
    },
  },
}
  </script>
  