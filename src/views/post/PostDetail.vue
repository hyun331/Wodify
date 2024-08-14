<template>
    <v-container>
        <!-- 상단의 글목록, 수정, 삭제, 좋아요 버튼과 정보 필드를 한 줄에 표시 -->
        <v-row v-if="post" align="center">
            <v-col cols="auto"> <v-text-field label="ID" v-model="post.id" readonly></v-text-field> </v-col>
            <v-col cols="auto"> <v-text-field label="Type" v-model="post.type" readonly></v-text-field> </v-col>
            <v-col cols="auto"> <v-text-field label="Title" v-model="post.title" readonly></v-text-field> </v-col>
            <v-col cols="auto"> <v-text-field label="Like Count" v-model="post.likeCount" readonly></v-text-field> </v-col>
            <v-col cols="auto"> <v-btn color="primary" @click="goBackToList">글목록</v-btn> </v-col>
            <v-col cols="auto"> <v-btn color="warning" @click="goToEditPost">수정</v-btn> </v-col>
            <v-col cols="auto"> <v-btn color="error" @click="deletePost">삭제</v-btn> </v-col>
            <v-col cols="auto">
                <v-btn color="pink" @click="likePost">
                    <v-icon left>mdi-thumb-up</v-icon> 좋아요 {{ post.likeCount }}
                </v-btn>
            </v-col>
        </v-row>

        <div v-if="post">
            <!-- 이미지 슬라이드 -->
            <v-carousel v-if="post.files && post.files.length">
                <v-carousel-item v-for="file in post.files" :key="file.id">
                    <v-img :src="file.url"></v-img>
                </v-carousel-item>
            </v-carousel>

            <v-text-field label="Contents" v-model="post.contents" readonly></v-text-field>
            <v-text-field label="Created Time" v-model="post.createdTime" readonly></v-text-field>

            <!-- 댓글 작성 섹션 -->
            <v-row align="center">
                <v-text-field v-model="newComment" label="Write a comment..." hide-details dense></v-text-field>
                <v-col cols="2" class="d-flex justify-end">
                    <v-btn icon @click="submitComment" color="primary"> <v-icon>mdi-send</v-icon> </v-btn>
                </v-col>
            </v-row>

            <!-- 댓글 표시 -->
            <h2>Comments</h2>
            <v-list>
                <comment-item v-for="comment in post.comments" 
                :key="comment.id" 
                :comment="comment" 
                :postId="post.id"
                @comment-deleted="removeComment" 
                ></comment-item>
            </v-list>
        </div>
    </v-container>
</template>

<script>
import axios from 'axios';
import CommentItem from './CommentItem.vue';

export default {
    props: ['id'],
    data() {
        return {
            post: null,
            newComment: '',
        };
    },
    mounted() {
        this.fetchPostDetail();
    },
    methods: {
        async fetchPostDetail() {
            try {
                const response = await axios.get(`http://localhost:8090/post/detail/${this.id}`);
                this.post = response.data.result;
            } catch (error) {
                console.error('Error fetching post detail:', error);
            }
        },
      goBackToList() {
            this.$router.push('/post/list');
        },
        goToEditPost() {
            this.$router.push(`/post/update/${this.id}`);
        },
        async deletePost() {
            try {
                const response = await axios.patch(`http://localhost:8090/post/delete/${this.id}`);
                if (response.status === 200) {
                    alert('게시물이 삭제되었습니다.');
                    this.goBackToList(); // 삭제 후 목록으로 돌아가기
                } else {
                    alert('게시물 삭제에 실패했습니다.');
                }
            } catch (error) {
                console.error('Error deleting post:', error);
                alert('게시물 삭제 중 오류가 발생했습니다.');
            }
        },
        async likePost() {
            try {
                const response = await axios.post(`http://localhost:8090/post/like/${this.id}`);
                if (response.status === 200) {
                    this.post.likeCount = response.data.result; // 서버에서 반환된 새로운 좋아요 수로 업데이트
                } else {
                    alert("좋아요에 실패했습니다.");
                }
            } catch (error) {
                console.error('Error liking post:', error);
                alert("좋아요 중 오류가 발생했습니다.");
            }
        },
        async submitComment() {
            if (!this.newComment.trim()) {
                alert("댓글을 입력하세요.");
                return;
            }

            const commentData = { comment: this.newComment, parentId: null };

            try {
                const response = await axios.post(`http://localhost:8090/post/comment/create/${this.id}`, commentData);

                if (response.status === 201) {
                    alert("댓글이 성공적으로 등록되었습니다.");
                    this.post.comments.push(response.data.result); // 새로운 댓글을 댓글 목록에 추가
                    this.newComment = ''; // 댓글 입력란 초기화
                } else {
                    alert("댓글 등록에 실패했습니다.");
                }
            } catch (error) {
                console.error('Error submitting comment:', error);
                alert("댓글 등록 중 오류가 발생했습니다.");
            }
        },
        removeComment(commentId) {
            this.post.comments = this.post.comments.filter(comment => comment.id !== commentId);
        },
    },
    components: {
        CommentItem,
    },
};
</script>
