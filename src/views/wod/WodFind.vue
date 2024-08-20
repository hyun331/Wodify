<template>
  <v-container style="min-width: 345px; min-height: 600px;">
    <!-- <v-row class="rubikMonoOne" style="font-size: 50px; margin-left: 3px">
      <v-col class="auto-width" style="padding: 0; margin-right: 15px;"> Wod </v-col>
      <v-col class="auto-width" style="padding: 0;"> find </v-col>
    </v-row> -->

    <v-row style="margin:1px">
      <v-text-field 
      class="custom-text-box" 
      style="margin-right:2px" 
      v-model="localWod.date" 
      label="Date" 
      readonly></v-text-field>
      <v-text-field 
      class="custom-text-box" 
      style="margin-right:2px" 
      v-model="localWod.timeCap" 
      label="Time Cap" 
      readonly></v-text-field>
      <v-text-field 
      class="custom-text-box" 
      v-model="localWod.rounds" 
      label="Rounds" 
      readonly
      outlined></v-text-field>
    </v-row>
    <v-row style="margin:1px">
      <v-textarea 
      class="custom-text-box" 
      v-model="localWod.info" 
      label="Info" 
      readonly 
      auto-grow
      :rows="2"
      outlined></v-textarea>
    </v-row>
    <div v-for="(wodDet, index) in localWod.wodDetResDtoList" :key="wodDet.id" class="exercise-group" outlined>
      <v-text-field 
      class="exercise-box" 
      style="border-radius: 4px 4px 0 0;" 
      :label="`Exercise ${index + 1} Name`"
      v-model="wodDet.name" 
      readonly 
      outlined></v-text-field>
      <v-text-field 
      class="exercise-box" 
      style="border-radius: 0 0 4px 4px;" 
      v-model="wodDet.contents" 
      :label="`Exercise ${index + 1} Contents`"
      readonly 
      outlined></v-text-field>
    </div>
    <!-- <v-text-field label="Box Name" v-model="localWod.boxName" readonly outlined class="custom-field"></v-text-field> -->
    <!-- <v-text-field label="Member Name" v-model="localWod.memberName" readonly outlined class="custom-field"></v-text-field> -->
    <!-- <v-text-field label="Created Time" v-model="localWod.createdTime" readonly outlined class="custom-field"></v-text-field> -->
    <div style="display: flex; justify-content: flex-end;">
      <v-btn color="black" @click="deleteWod">삭제</v-btn>
    </div>
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
        this.$emit('wod-deleted', this.wod.date);
      } catch (error) {
        console.error('Error deleting WOD data:', error);
        this.errorMessage = 'WOD 데이터를 삭제하는 중 오류가 발생했습니다.';
      }
    },
  },
};
</script>
<style scoped>
/*
.auto-width {
  flex: 0 1 auto;
  width: auto;
  display: inline-block;
}
.custom-field {
  background-color: rgba(255, 255, 255, 0.5);
}
*/
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