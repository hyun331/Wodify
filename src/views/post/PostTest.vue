<template>
    <b-container fluid>
      <!-- 게시판 제목 및 검색 -->
      <b-row>
        <b-col>
          <h2>게시판 목록</h2>
        </b-col>
        <b-col class="text-end">
          <b-form-input
            v-model="search"
            type="text"
            placeholder="Search"
            class="mb-2"
          ></b-form-input>
        </b-col>
      </b-row>
  
      <!-- 게시판 목록 테이블 -->
      <b-table
        :items="filteredPosts"
        :fields="fields"
        responsive="sm"
        striped
        hover
        bordered
        selectable
        sticky-header="300px"
        :per-page="5"
        :current-page="currentPage"
        selected-variant="primary"
        class="table-hover-custom"
        @row-selected="onRowSelected"
      >
        <!-- 액션 버튼 열 -->
        <template #cell(actions)="data">
          <b-button variant="primary" size="sm" @click="viewPost(data.item.id)">
            <i class="bi bi-eye"></i> 보기
          </b-button>
          <b-button variant="warning" size="sm" @click="editPost(data.item.id)">
            <i class="bi bi-pencil"></i> 수정
          </b-button>
          <b-button variant="danger" size="sm" @click="deletePost(data.item.id)">
            <i class="bi bi-trash"></i> 삭제
          </b-button>
        </template>
      </b-table>
  
      <!-- 페이지네이션 -->
      <b-pagination
        v-model="currentPage"
        :total-rows="filteredPosts.length"
        :per-page="5"
        align="center"
        class="mt-3"
      ></b-pagination>
    </b-container>
  </template>
  
  <script setup>
  import { ref, computed } from 'vue'
  
  // 테이블 필드 설정
  const fields = [
    { key: 'id', label: '번호', sortable: true },
    { key: 'title', label: '제목', sortable: true },
    { key: 'author', label: '작성자', sortable: true },
    { key: 'created_at', label: '작성일', sortable: true },
    { key: 'actions', label: '액션', sortable: false }
  ]
  
  // 게시판 데이터
  const posts = ref([
    { id: 1, title: '첫 번째 게시글', author: '홍길동', created_at: '2024-08-15' },
    { id: 2, title: '두 번째 게시글', author: '이몽룡', created_at: '2024-08-14' },
    { id: 3, title: '세 번째 게시글', author: '성춘향', created_at: '2024-08-13' }
  ])
  
  // 검색 기능
  const search = ref('')
  const filteredPosts = computed(() => {
    if (!search.value) return posts.value
    return posts.value.filter(post =>
      Object.values(post).some(value =>
        String(value).toLowerCase().includes(search.value.toLowerCase())
      )
    )
  })
  
  // 페이지네이션
  const currentPage = ref(1)
  
  // 선택된 행 처리
  const onRowSelected = (items) => {
    console.log('Selected rows:', items)
  }
  
  // 액션 버튼 핸들러
  const viewPost = (id) => {
    alert(`게시글 ${id} 보기`)
  }
  
  const editPost = (id) => {
    alert(`게시글 ${id} 수정`)
  }
  
  const deletePost = (id) => {
    alert(`게시글 ${id} 삭제`)
  }
  </script>
  
  <style scoped>
  h2 {
    margin-bottom: 1rem;
  }
  
  /* 테이블 행 커서 및 호버 효과 */
  .table-hover-custom tbody tr {
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .table-hover-custom tbody tr:hover {
    background-color: #f8f9fa;
  }
  </style>
  