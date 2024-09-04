<template>
  <v-list-item style="margin-bottom: -5px;padding-right: -30px; margin-left: -10px;margin-top: -10px;">
    <v-list-item-content>
      <v-row align="center">
        <v-col cols="auto">
          <strong>{{ commentCopy.name }}</strong> {{ commentCopy.createdTime }}
        </v-col>
        <v-col style="width: 100px;"></v-col>
        <v-col cols="auto" style="text-align: right;">
          <v-btn v-if="!isEditing" @click="startReplying" class="action-button"
            style="min-width: 30px; max-width: 30px; padding: 5px;">
            <v-icon small>mdi-message-reply</v-icon>
          </v-btn>
          <v-btn v-if="!isEditing" @click="startEditing" class="action-button"
            style="min-width: 30px; max-width: 30px; padding: 5px;">
            <v-icon small>mdi-pencil</v-icon>
          </v-btn>
          <v-btn v-if="!isEditing" @click="showDeleteConfirmationModal" class="action-button"
            style="min-width: 30px; max-width: 30px; padding: 5px;">
            <v-icon small>mdi-delete</v-icon>
          </v-btn>
          <v-btn v-if="isEditing" @click="saveEdit" class="action-button"
            style="min-width: 30px; max-width: 30px; padding: 5px;">
            <v-icon>mdi-check-circle</v-icon>
          </v-btn>
          <v-btn v-if="isEditing" @click="cancelEdit" class="action-button"
            style="min-width: 30px; max-width: 30px; padding: 5px;">
            <v-icon>mdi-close-circle</v-icon>
          </v-btn>
        </v-col>
      </v-row>

      <!-- 삭제 확인 모달 -->
      <v-dialog v-model="showDeleteModal" persistent max-width="400px">
        <v-card>
          <v-card-text>정말로 댓글을 삭제하시겠습니까?</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn class="action-button" text @click="confirmDelete">확인</v-btn>
            <v-btn class="action-button" text @click="closeDeleteModal">취소</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- 댓글 수정 및 등록 결과 모달 -->
      <v-dialog v-model="showResultModal" persistent max-width="400px">
        <v-card>
          <v-card-text>{{ resultMessage }}</v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="primary" text @click="closeResultModal">확인</v-btn>
          </v-card-actions>
        </v-card>
      </v-dialog>

      <!-- 댓글 내용 -->
      <div v-if="!isEditing">{{ commentCopy.comment }}</div>
      <v-text-field v-else v-model="editedComment" dense hide-details></v-text-field>

      <!-- 답글 작성 필드 -->
      <v-row v-if="isReplying">
        <v-col cols="10">
          <v-text-field class="field-style" v-model="replyComment" label="Write a reply..." hide-details
            dense></v-text-field>
        </v-col>
        <v-col cols="2" class="d-flex justify-end">
          <v-btn small @click="submitReply" class="action-button"
            style="min-width: 30px; max-width: 30px; padding: 5px;">
            <v-icon>mdi-check-circle</v-icon></v-btn>
          <v-btn small @click="cancelReply" class="action-button"
            style="min-width: 30px; max-width: 30px; padding: 5px;">
            <v-icon>mdi-close-circle</v-icon></v-btn>
        </v-col>
      </v-row>

      <!-- 대댓글 리스트 -->
      <v-list v-if="commentCopy.replies && commentCopy.replies.length">
        <comment-item style="margin-right:0px;" v-for="reply in commentCopy.replies" :key="reply.id" :comment="reply"
          :postId="postId" @comment-deleted="onReplyDeleted" @reply-added="onReplyAdded"></comment-item>
      </v-list>
    </v-list-item-content>
  </v-list-item>
</template>

<script>
import axios from 'axios';

export default {
  props: ['comment', 'postId'],
  data() {
    return {
      isEditing: false,
      isReplying: false,
      editedComment: this.comment.comment,
      replyComment: '', // 답글 내용을 담는 변수
      commentCopy: { ...this.comment },
      showDeleteModal: false, // 삭제 확인 모달 제어 변수
      showResultModal: false, // 작업 결과 모달 제어 변수
      resultMessage: "", // 모달에 표시될 메시지
    };
  },
  methods: {
    startEditing() {
      this.isEditing = true;
    },
    cancelEdit() {
      this.isEditing = false;
      this.editedComment = this.comment.comment;
    },
    async saveEdit() {
      try {
        const response = await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/post/comment/update/${this.comment.id}`, {
          comment: this.editedComment,
        });
        if (response.status === 200) {
          this.isEditing = false;
          this.commentCopy.comment = this.editedComment;
          this.resultMessage = response.data.status_message;
        } else {
          this.resultMessage = response.data.error_message;
          this.showResultModal = true; // 결과 모달 열기
        }
      } catch (error) {
        console.error("Error updating comment:", error.response ? error.response.data : error.message);
        this.resultMessage = "댓글 수정 중 오류가 발생했습니다.";
        this.showResultModal = true; // 결과 모달 열기
      }
    },
    startReplying() {
      this.isReplying = true;
    },
    cancelReply() {
      this.isReplying = false;
      this.replyComment = '';
    },
    async submitReply() {
      if (!this.replyComment.trim()) {
        this.resultMessage = "답글을 입력하세요.";
        this.showResultModal = true; // 결과 모달 열기
        return;
      }

      try {
        const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/post/comment/create/${this.postId}`, {
          comment: this.replyComment,
          parentId: this.comment.id, // 현재 댓글의 ID를 parentId로 전송
        });

        if (response.status === 201) {
          this.resultMessage = response.data.status_message;
          this.commentCopy.replies.push(response.data.result); // 새 답글을 댓글 목록에 추가
          this.isReplying = false;
          this.replyComment = '';
        } else {
          this.resultMessage = response.data.error_message;
        }
      } catch (error) {
        console.error("Error submitting reply:", error.response ? error.response.data : error.message);
        this.resultMessage = "답글 등록 중 오류가 발생했습니다.";
      }
    },
    confirmDelete() {
      this.closeDeleteModal(); // 모달 닫기
      this.deleteComment(); // 실제 삭제 로직 호출
    },
    showDeleteConfirmationModal() {
      this.showDeleteModal = true; // 삭제 확인 모달 열기
    },
    closeDeleteModal() {
      this.showDeleteModal = false; // 삭제 확인 모달 닫기
    },
    async deleteComment() {
      try {
        const response = await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/post/comment/delete/${this.comment.id}`);
        if (response.status === 200) {
          this.resultMessage = response.data.status_message;
          this.$emit('comment-deleted', this.comment.id);
        } else {
          this.resultMessage = response.data.error_message;
        }
      } catch (error) {
        console.error("Error deleting comment:", error.response ? error.response.data : error.message);
        this.resultMessage = "댓글 삭제 중 오류가 발생했습니다.";
      } finally {
        this.showResultModal = true; // 결과 모달 열기
      }
    },
    onReplyDeleted(replyId) {
      this.commentCopy.replies = this.commentCopy.replies.filter(reply => reply.id !== replyId);
    },
    onReplyAdded(newReply) {
      // 이미 추가된 댓글을 다시 추가하지 않도록 수정
      if (!this.commentCopy.replies.find(reply => reply.id === newReply.id)) {
        this.commentCopy.replies.push(newReply);
      }
    },
    closeResultModal() {
      this.showResultModal = false; // 작업 결과 모달 닫기
    },
  },
}
</script>

<style scope>
.action-button {
  background-color: #3a3a3a !important;
  color: #ffffff !important;
  width: 10px;
  /* 버튼의 가로 크기 고정 */
  height: 10px;
  /* 버튼의 세로 크기 고정 */
  min-width: 10px;
  /* 최소 가로 크기 고정 */
  max-width: 10px;
  /* 최대 가로 크기 고정 */
  padding: 0;
  /* 버튼 내부의 패딩을 제거 */
  margin: 0;
  margin-left: 1px;
  /* 필요 시 마진을 제거 */
  line-height: 20px;
  /* 텍스트가 중앙에 위치하도록 설정 */
  text-align: center;
  /* 텍스트 가운데 정렬 */
  font-size: 1px;
  /* 텍스트 크기 조정 */
  display: flex;
  justify-content: center;
  align-items: center;
}

.text-right {
  text-align: right;
}
</style>
