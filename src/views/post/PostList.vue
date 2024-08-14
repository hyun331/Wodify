<template>
  <v-container>
    <!-- 상단 글쓰기 버튼 -->
    <v-row justify="end" class="mb-4">
      <v-btn color="primary" @click="goToCreatePost">글쓰기</v-btn>
    </v-row>

    <!-- 테이블 -->
    <v-simple-table style="width: 100%; table-layout: fixed;">
      <thead>
        <tr>
          <th style="border: 1px solid; width: 5%;">No</th>
          <th style="border: 1px solid; width: auto;">제목</th>
          <th style="border: 1px solid; width: 10%;">글쓴이</th>
          <th style="border: 1px solid; width: 5%;">추천</th>
          <th style="border: 1px solid; width: 20%;">등록일</th>
          <th style="border: 1px solid; width: 5%;">댓글</th>
        </tr>
      </thead>
      <tbody>
        <!-- 공지사항 -->
        <tr v-for="(notice, index) in notices" :key="`notice-${index}`" @click="goToDetail(notice.id)"
          style="cursor: pointer;">
          <td style="border: 1px solid;">공지</td>
          <td style="border: 1px solid;">{{ notice.title }}</td>
          <td style="border: 1px solid;">{{ notice.name }}</td>
          <td style="border: 1px solid;">{{ notice.likeCount }}</td>
          <td style="border: 1px solid;">{{ notice.createdTime }}</td>
          <td style="border: 1px solid;">{{ notice.commentCount }}</td>
        </tr>
        <!-- POST 타입 글 -->
        <tr v-for="(post, index) in posts" :key="`post-${index}`" @click="goToDetail(post.id)" style="cursor: pointer;">
          <td style="border: 1px solid;">{{ post.id }}</td>
          <td style="border: 1px solid;">{{ post.title }}</td>
          <td style="border: 1px solid;">{{ post.name }}</td>
          <td style="border: 1px solid;">{{ post.likeCount }}</td>
          <td style="border: 1px solid;">{{ post.createdTime }}</td>
          <td style="border: 1px solid;">{{ post.commentCount }}</td>
        </tr>
      </tbody>
    </v-simple-table>

    <!-- 하단 글쓰기 버튼 -->
    <v-row justify="end" class="mt-4">
      <v-btn color="primary" @click="goToCreatePost">글쓰기</v-btn>
    </v-row>

    <!-- 페이지네이션 -->
    <v-pagination v-model="currentPage" :length="totalPages" :total-visible="5" class="mt-4"
      @input="fetchPosts"></v-pagination>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      currentPage: 1,
      itemsPerPage: 10,
      notices: [],
      posts: [],
      totalPages: 1,
    };
  },
  mounted() {
    this.fetchNotices();
    this.fetchPosts();
  },
  watch: {
    currentPage(newPage) {
      console.log(newPage);
      this.fetchPosts();
    },
  },
  methods: {
    async fetchNotices() {
      try {
        const response = await axios.get('http://localhost:8090/post/list/notice');
        this.notices = response.data.result;
      } catch (error) {
        console.error('Error fetching notices:', error);
      }
    },
    async fetchPosts() {
      try {
        const response = await axios.get('http://localhost:8090/post/list', {
          params: {
            page: this.currentPage - 1,
            size: this.itemsPerPage,
          },
        });

        this.posts = response.data.result.content;
        this.totalPages = response.data.result.totalPages;
      } catch (error) {
        console.error('Error fetching posts:', error);
      }
    },
    goToCreatePost() {
      this.$router.push('/post/create');
    },
    goToDetail(id) {
      this.$router.push(`/post/detail/${id}`);
    },
  },
};
</script>
