<template>
  <v-container style="min-width: 345px; min-height: 600px;">
    <v-row>
      <v-col cols="12">
        <v-row class="rubikMonoOne" style="font-size: 50px; margin-left: 3px">
          <v-col class="auto-width" style="padding: 0; margin-right: 15px;">
            Wod
          </v-col>
        </v-row>
        <v-text-field label="Date" v-model="localWod.date" readonly outlined class="custom-field"></v-text-field>
        <v-text-field label="Box Name" v-model="localWod.boxName" readonly outlined class="custom-field"></v-text-field>
        <v-text-field label="Member Name" v-model="localWod.memberName" readonly outlined class="custom-field"></v-text-field>
        <v-text-field label="Info" v-model="localWod.info" readonly outlined class="custom-field"></v-text-field>
        <v-text-field label="Time Cap" v-model="localWod.timeCap" readonly outlined class="custom-field"></v-text-field>
        <v-text-field label="Rounds" v-model="localWod.rounds" readonly outlined class="custom-field"></v-text-field>
        <v-text-field label="Created Time" v-model="localWod.createdTime" readonly outlined class="custom-field"></v-text-field>
        <div v-for="(wodDet, index) in localWod.wodDetResDtoList" :key="wodDet.id">
          <v-text-field :label="`Exercise ${index + 1} Name`" v-model="wodDet.name" readonly outlined class="custom-field"></v-text-field>
          <v-text-field :label="`Exercise ${index + 1} Contents`" v-model="wodDet.contents" readonly outlined class="custom-field"></v-text-field>
        </div>
        <div style="display: flex; justify-content: flex-end;">
          <v-btn color="black" @click="deleteWod">삭제</v-btn>
        </div>
      </v-col>
    </v-row>
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
    };
  },
  methods: {
    async deleteWod() {
      try {
        await axios.patch(`http://localhost:8090/wod/delete/${this.localWod.date}`);
        console.log('Delete successful');
      } catch (error) {
        console.error('Error deleting WOD data:', error);
        this.errorMessage = 'WOD 데이터를 삭제하는 중 오류가 발생했습니다.';
      }
    },
  },
};
</script>

<style scoped>
.custom-field {
  background-color: rgba(255, 255, 255, 0.5);
}
</style>
