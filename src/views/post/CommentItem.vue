<template>
  <v-card class="mb-2">
    <v-list-item>
      <v-list-item-content>
        <v-row align="center" class="mb-1">
          <v-col cols="auto"><strong>{{ commentCopy.name }}</strong></v-col>
          <v-col cols="auto" class="grey--text text--darken-2">{{ commentCopy.createdTime }}</v-col>
          <v-spacer></v-spacer>
          <v-col cols="auto" class="d-flex justify-end">
            <v-btn text small v-if="!isEditing" @click="startReplying" class="ml-2">
              답글
            </v-btn>
            <v-btn text small v-if="!isEditing" @click="startEditing" class="ml-2">
              수정
            </v-btn>
            <v-btn text small v-if="!isEditing" @click="deleteComment" class="ml-2">
              삭제
            </v-btn>
            <v-btn text small v-if="isEditing" @click="saveEdit" class="ml-2">
              저장
            </v-btn>
            <v-btn text small v-if="isEditing" @click="cancelEdit" class="ml-2">
              취소
            </v-btn>
          </v-col>
        </v-row>

        <!-- 댓글 내용 -->
        <div v-if="!isEditing">{{ commentCopy.comment }}</div>
        <v-text-field v-else v-model="editedComment" dense hide-details></v-text-field>

        <!-- 답글 작성 필드 -->
        <v-row v-if="isReplying" class="mt-2">
          <v-col cols="10">
            <v-text-field v-model="replyComment" label="답글을 작성하세요..." hide-details dense></v-text-field>
          </v-col>
          <v-col cols="2" class="d-flex justify-end">
            <v-btn small @click="submitReply" color="primary">전송</v-btn>
            <v-btn small @click="cancelReply" color="secondary">취소</v-btn>
          </v-col>
        </v-row>

        <!-- 대댓글 리스트 -->
        <v-list v-if="commentCopy.replies && commentCopy.replies.length">
          <comment-item
            v-for="reply in commentCopy.replies"
            :key="reply.id"
            :comment="reply"
            :postId="postId"
            @comment-deleted="onReplyDeleted"
            @reply-added="onReplyAdded"
          ></comment-item>
        </v-list>
      </v-list-item-content>
    </v-list-item>
  </v-card>
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
        const response = await axios.patch(`http://localhost:8090/post/comment/update/${this.comment.id}`, {
          comment: this.editedComment,
        });
        if (response.status === 200) {
          this.isEditing = false;
          this.commentCopy.comment = this.editedComment;
        } else {
          alert("댓글 수정에 실패했습니다.");
        }
      } catch (error) {
        console.error("Error updating comment:", error.response ? error.response.data : error.message);
        alert("댓글 수정 중 오류가 발생했습니다.");
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
        alert("답글을 입력하세요.");
        return;
      }

      try {
        const response = await axios.post(`http://localhost:8090/post/comment/create/${this.postId}`, {
          comment: this.replyComment,
          parentId: this.comment.id, // 현재 댓글의 ID를 parentId로 전송
        });

        if (response.status === 201) {
          alert("답글이 등록되었습니다.");
          this.commentCopy.replies.push(response.data.result); // 새 답글을 댓글 목록에 추가
          this.isReplying = false;
          this.replyComment = '';
        } else {
          alert("답글 등록에 실패했습니다.");
        }
      } catch (error) {
        console.error("Error submitting reply:", error.response ? error.response.data : error.message);
        alert("답글 등록 중 오류가 발생했습니다.");
      }
    },
    async deleteComment() {
      if (confirm("이 댓글을 삭제하시겠습니까?")) {
        try {
          const response = await axios.patch(`http://localhost:8090/post/comment/delete/${this.comment.id}`);
          if (response.status === 200) {
            alert("댓글이 삭제되었습니다.");
            this.$emit('comment-deleted', this.comment.id);
          } else {
            alert("댓글 삭제에 실패했습니다.");
          }
        } catch (error) {
          console.error("Error deleting comment:", error.response ? error.response.data : error.message);
          alert("댓글 삭제 중 오류가 발생했습니다.");
        }
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
    }
  }
}
</script>
