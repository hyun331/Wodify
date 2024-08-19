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
          <v-col cols="12" md="8">
            <!-- 여기서 WodFind 컴포넌트가 라우팅 되어 표시될 것입니다 -->
            <router-view></router-view>
          </v-col>
        </v-row>
      </v-container>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        selectedDate: null,
      };
    },
    methods: {
      onDateSelected(date) {
        const formattedDate = this.formatDate(date);
        this.$router.push({ path: `/wod/find/${formattedDate}` });
      },
      formatDate(date) {
        const year = date.getFullYear();
        const month = ('0' + (date.getMonth() + 1)).slice(-2);
        const day = ('0' + date.getDate()).slice(-2);
  
        return `${year}-${month}-${day}`;
      },
    }
  };
  </script>
  