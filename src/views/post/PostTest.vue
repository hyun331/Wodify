<template>
  <v-app>
  <v-container>
    <v-data-table
      :headers="headers"
      :items="rowData"
      class="elevation-1"
      :header-class="'custom-header'"
    />
  </v-container>
</v-app>
</template>

<script setup>
import { ref } from 'vue';

const headers = [
  { text: '번호', value: 'id' },
  { text: '분류', value: 'type' },
  { text: '제목', value: 'title' },
  { text: '작성자', value: 'name' },
  { text: '좋아요 수', value: 'likeCount' },
  { text: '댓글 수', value: 'commentCount' },
  { text: '작성일', value: 'createdTime' },
];

const rowData = ref([
  { id: 1, type: '공지사항', title: '첫 번째 게시물', name: '관리자', likeCount: 5, commentCount: 2, createdTime: '2024-08-19 12:00' },
  { id: 2, type: '이벤트', title: '두 번째 게시물', name: '홍길동', likeCount: 3, commentCount: 1, createdTime: '2024-08-19 13:00' }
]);
</script>
<style>
.custom-header {
  background-color: #f5f5f5;
  color: #333;
  font-weight: bold;
  text-align: center;
}
</style>