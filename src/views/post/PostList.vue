<template>
  <div class="page-container">
    <div class="box right-align">
      <br>
      <span class="boxLocation">
        {{ this.boxName }}
      </span>
      <br>
    </div>
    <v-container class="container mt-5" fluid>
      <!-- 테이블을 중앙에 정렬 -->
      <v-row justify="center">
        <v-col cols="12">
          <!-- 테이블을 v-card로 감싸기 -->
          <v-card class="custom-card">
            <!-- 검색 영역 -->
            <v-row class="search-bar" align="center" justify="space-between" no-gutters>
              <v-col cols="2" class="pr-1">
                <!-- 검색 기준 선택 -->
                <v-select
                  v-model="searchType"
                  :items="searchOptions"
                  item-title="text"
                  item-value="value"
                  label="Type"
                  dense
                  outlined
                  hide-details
                  class="search-select"
                ></v-select>
              </v-col>
              <v-col cols="8" class="pl-1 pr-2">
                <!-- 검색 입력 필드 -->
                <v-text-field
                  v-model="searchText"
                  label="검색"
                  outlined
                  dense
                  hide-details
                  class="search-text-field"
                ></v-text-field>
              </v-col>
              <v-col cols="2" class="d-flex align-center justify-end">
                <!-- 검색 버튼과 글쓰기 버튼 -->
                <v-btn color="black" dark @click="searchPosts">검색</v-btn>
                <v-btn color="black" dark class="ml-3" @click="goToCreatePost">글쓰기</v-btn>
              </v-col>
            </v-row>

            <!-- 고정된 너비를 가진 테이블 -->
            <div class="table-wrapper">
              <v-simple-table class="fixed-table">
                <thead>
                  <tr>
                    <th class="fixed-cell no-col">No</th>
                    <th class="fixed-cell title-col">제목</th>
                    <th class="fixed-cell author-col">글쓴이</th>
                    <th class="fixed-cell date-col">등록일</th>
                    <th class="fixed-cell comments-col">댓글</th>
                    <th class="fixed-cell likes-col">추천</th>
                  </tr>
                </thead>
                <tbody>
                  <!-- 공지사항 -->
                  <tr v-for="(notice, index) in notices" :key="`notice-${index}`" @click="goToDetail(notice.id)"
                    style="cursor: pointer;">
                    <td class="fixed-cell no-col">공지</td>
                    <td class="fixed-cell title-col">{{ truncateTitle(notice.title, 30) }}</td>
                    <td class="fixed-cell author-col">{{ notice.name }}</td>
                    <td class="fixed-cell date-col">{{ notice.createdTime }}</td>
                    <td class="fixed-cell comments-col">{{ notice.commentCount }}</td>
                    <td class="fixed-cell likes-col">{{ notice.likeCount }}</td>
                  </tr>
                  <!-- POST 타입 글 -->
                  <tr v-for="(post, index) in posts" :key="`post-${index}`" @click="goToDetail(post.id)"
                    style="cursor: pointer;">
                    <td class="fixed-cell no-col">{{ post.id }}</td>
                    <td class="fixed-cell title-col">{{ truncateTitle(post.title, 30) }}</td>
                    <td class="fixed-cell author-col">{{ post.name }}</td>
                    <td class="fixed-cell date-col">{{ post.createdTime }}</td>
                    <td class="fixed-cell comments-col">{{ post.commentCount }}</td>
                    <td class="fixed-cell likes-col">{{ post.likeCount }}</td>
                  </tr>
                </tbody>
              </v-simple-table>
            </div>
          </v-card>
        </v-col>
      </v-row>

      <!-- 무한 스크롤을 위한 트리거 -->
      <div ref="scrollObserver" style="height: 100px;"></div>

      <!-- 화면 오른쪽 하단의 맨위로 버튼 -->
      <v-btn
        color="black"
        dark
        class="scroll-top-btn"
        v-show="showScrollTopButton"
        @click="scrollToTop"
      >
        맨위로
      </v-btn>
    </v-container>
  </div>
</template>

<script setup>
function truncateTitle(title, maxLength) {
  if (title.length > maxLength) {
    return title.slice(0, maxLength) + '...';
  }
  return title;
}
</script>

<script>
import axios from "axios";

export default {
  async created() {
    try {
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/box/name/`);
      this.boxName = response.data.result;
    } catch (error) {
      let errorMessage = "";
      if (error.response && error.response.data) {
        // 서버에서 반환한 에러 메시지가 있는 경우
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
      searchType: '',
            searchOptions: [
                { text: "제목", value: "title" },
                { text: "글쓴이", value: "memberName" }
            ],
      currentPage: 1,
      itemsPerPage: 10,
      searchText: "", // 검색어
      notices: [],
      posts: [],
      totalPages: 1,
      isFetching: false,
      showScrollTopButton: false,
      boxName: "",
    };
  },
  mounted() {
    this.fetchNotices();
    this.fetchPosts();
    this.setupScrollObserver();
    window.addEventListener("scroll", this.handleScroll);
  },
  beforeUnmount() {
    window.removeEventListener("scroll", this.handleScroll);
  },
  methods: {
    async fetchNotices() {
      try {
        const response = await axios.get(
          "http://localhost:8090/post/list/notice"
        );
        this.notices = response.data.result;
      } catch (error) {
        console.error("Error fetching notices:", error);
      }
    },
    async fetchPosts() {
      if (this.isFetching || this.currentPage > this.totalPages) return;
      this.isFetching = true;
      try {
        const response = await axios.get("http://localhost:8090/post/list/page", {
          params: {
            page: this.currentPage - 1,
            size: this.itemsPerPage,
            sort: "id,desc", // 'id' 필드를 기준으로 내림차순 정렬
            searchCategory: this.searchCategory, // 검색 기준 (제목 또는 작성자)
            searchText: this.searchText, // 검색어
          },
        });

        if (this.currentPage === 1) {
          this.posts = response.data.result.content;
        } else {
          this.posts = [...this.posts, ...response.data.result.content];
        }
        this.totalPages = response.data.result.totalPages;
      } catch (error) {
        console.error("Error fetching posts:", error);
      } finally {
        this.isFetching = false;
      }
    },
    searchPosts() {
      // 검색 요청을 트리거할 때 현재 페이지를 초기화하고 데이터 새로 가져오기
      this.currentPage = 1;
      this.posts = [];
      this.fetchPosts();
    },
    setupScrollObserver() {
      const options = {
        root: null,
        rootMargin: "0px",
        threshold: 1.0,
      };

      const observer = new IntersectionObserver((entries) => {
        if (entries[0].isIntersecting && !this.isFetching) {
          this.currentPage += 1;
          this.fetchPosts();
        }
      }, options);

      observer.observe(this.$refs.scrollObserver);
    },
    handleScroll() {
      this.showScrollTopButton = window.scrollY > 200;
    },
    scrollToTop() {
      window.scrollTo({ top: 0, behavior: "smooth" });
    },
    goToCreatePost() {
      this.$router.push("/post/create");
    },
    goToDetail(id) {
      this.$router.push(`/post/detail/${id}`);
    },
  },
};
</script>

<style scoped>
.page-container {
  background-color: #D9D9D9;
  min-height: 100vh;
  padding: 20px;
}

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

.container {
  background-color: transparent;
  padding: 0px;
  max-width: 1200px;
  margin-top: 0px;
  border-radius: 8px;
}

.custom-card {
  background-color: #ffffff;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding-left: 20px;
  padding-right: 20px;
  width: 800px;
  margin: auto;
}

.table-wrapper {
  width: 100%;
  margin-top: 22px;
}

.fixed-table {
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
}

.fixed-cell {
  padding: 5px;
  border: 1px solid #ddd;
  text-align: center;
}

.no-col {
  width: 50px;
}

.title-col {
  width: 400px;
}

.author-col {
  width: 100px;
}

.likes-col {
  width: 45px;
}

.date-col {
  width: 100px;
}

.comments-col {
  width: 45px;
}

.scroll-top-btn {
  position: fixed;
  bottom: 20px;
  right: 20px;
  z-index: 100;
}

.search-bar {
  padding-top: 10px;
  padding-bottom: 10px;
  background-color: #ffffff;
  border-radius: 8px;
}

.search-select,
.search-text-field {
  height: 30px; /* 높이 조절 */
  font-size: 10px; /* 텍스트 크기 조절 */
  padding: 0px 8px; /* 내부 패딩 조절 */
}

.search-select {
  min-width: 130px; /* v-select의 최소 너비 조절 */
}

.search-text-field {
  width: 440px; /* v-text-field를 가능한 길게 확장 */
}

</style>
