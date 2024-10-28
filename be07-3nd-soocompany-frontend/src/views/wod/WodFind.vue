<template>
  <v-container style="min-width: 345px; min-height: 600px;">
    <v-row style="margin:1px">
      <v-text-field class="custom-text-box" style="margin-right:2px" v-model="localWod.date" label="Date" readonly></v-text-field>
      <v-text-field class="custom-text-box" style="margin-right:2px" v-model="localWod.timeCap" label="Time Cap" readonly></v-text-field>
      <v-text-field class="custom-text-box" v-model="localWod.rounds" label="Rounds" readonly outlined></v-text-field>
    </v-row>
    <v-row style="margin:1px">
      <v-textarea class="custom-text-box" v-model="localWod.info" label="Info" readonly auto-grow :rows="2" outlined></v-textarea>
    </v-row>
    <div v-for="(wodDet, index) in localWod.wodDetResDtoList" :key="wodDet.id" class="exercise-group" outlined>
      <v-text-field class="exercise-box" style="border-radius: 4px 4px 0 0;" :label="`Exercise ${index + 1} Name`" v-model="wodDet.name" readonly outlined></v-text-field>
      <v-text-field class="exercise-box" style="border-radius: 0 0 4px 4px;" v-model="wodDet.contents" :label="`Exercise ${index + 1} Contents`" readonly outlined></v-text-field>
    </div>
    <div style="display: flex; justify-content: flex-end;">
      <v-btn color="black" @click="showDeleteConfirmation = true">삭제</v-btn>
    </div>

    <!-- 삭제 확인 모달 -->
    <v-dialog v-model="showDeleteConfirmation" persistent max-width="400px">
      <v-card>
        <v-card-text>정말로 이 WOD 데이터를 삭제하시겠습니까?</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="action-button" text @click="confirmDelete">확인</v-btn>
          <v-btn class="action-button" text @click="showDeleteConfirmation = false">취소</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

    <!-- 삭제 결과 모달 -->
    <v-dialog v-model="showResultModal" persistent max-width="400px">
      <v-card>
        <v-card-title class="headline">결과</v-card-title>
        <v-card-text>{{ resultMessage }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn class="action-button" text @click="closeResultModal">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  props: {
    wod: {
      type: Object,
      required: true,
    },
  },
  data() {
    return {
      localWod: { ...this.wod }, // props로 받은 wod 데이터를 로컬 데이터로 복사
      showDeleteConfirmation: false, // 삭제 확인 모달을 제어하는 변수
      showResultModal: false, // 삭제 후 결과 모달을 제어하는 변수
      resultMessage: '', // 삭제 결과 메시지
    };
  },
  methods: {
    async deleteWod() {
      try {
        const response = await axios.patch(`http://localhost:8090/wod/delete/${this.localWod.date}`);
        if (response.status === 200) {
          this.resultMessage = response.data.status_message; // 서버에서 받은 메시지 설정
          this.showResultModal = true; // 결과 모달 열기
        }
      } catch (error) {
        console.error('Error deleting WOD data:', error);
        this.resultMessage = 'WOD 데이터를 삭제하는 중 오류가 발생했습니다.';
        this.showResultModal = true; // 오류 시에도 결과 모달 열기
      }
    },
    confirmDelete() {
      this.deleteWod(); // 삭제 함수 호출
      this.showDeleteConfirmation = false; // 삭제 확인 모달 닫기
    },
    closeResultModal() {
      this.showResultModal = false; // 결과 모달 닫기
      this.$emit('wod-deleted', this.wod.date); // 삭제 완료 이벤트 emit
    },
  },
};
</script>

<style scoped>
.custom-text-box {
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 4px;
  margin-top: 3px;
  margin-right: 1px;
  margin-left: 1px;
  margin-bottom: 2px;
}

.exercise-group {
  background-color: rgba(255, 255, 255, 0.7);
  border-radius: 4px;
  margin: 2px;
  margin-bottom: 4px;
  position: relative;
}
</style>