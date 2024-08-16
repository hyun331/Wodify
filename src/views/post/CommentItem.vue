<template>
  <v-card class="mb-2">
    <v-list-item>
      <v-list-item-content>
        <v-row align="center" class="mb-1">
          <v-col cols="auto"><strong>{{ commentCopy.name }}</strong></v-col>
          <v-col cols="auto" class="grey--text text--darken-2">{{ commentCopy.createdTime }}</v-col>
          <v-spacer></v-spacer>
          <v-col cols="auto" class="d-flex justify-end" v-if="!isDeleted">
            <v-btn text small v-if="!isEditing" @click="startReplying" class="ml-2"> 답글 </v-btn>
            <v-btn text small v-if="!isEditing && isAuthor" @click="startEditing" class="ml-2"> 수정 </v-btn>
            <v-btn text small v-if="!isEditing && isAuthor" @click="deleteComment" class="ml-2"> 삭제 </v-btn>
            <v-btn text small v-if="isEditing" @click="saveEdit" class="ml-2"> 저장 </v-btn>
            <v-btn text small v-if="isEditing" @click="cancelEdit" class="ml-2"> 취소 </v-btn>
          </v-col>
        </v-row>

        <div v-if="!isEditing"> {{ this.isDeleted ? '삭제된 글입니다.' : commentCopy.comment }} </div>
        <v-text-field v-else v-model="editedComment" dense hide-details></v-text-field>

        <v-row v-if="isReplying" class="mt-2">
          <v-col cols="10">
            <v-text-field v-model="replyComment" label="답글을 작성하세요..." hide-details dense></v-text-field>
          </v-col>
          <v-col cols="2" class="d-flex justify-end">
            <v-btn small @click="submitReply" color="primary">전송</v-btn>
            <v-btn small @click="cancelReply" color="secondary">취소</v-btn>
          </v-col>
        </v-row>

        <v-list v-if="commentCopy.replies && commentCopy.replies.length">
          <comment-item v-for="reply in commentCopy.replies" :key="reply.id" 
            :comment="reply" :postId="postId" :currentUserId="currentUserId"
            @comment-deleted="onReplyDeleted" @reply-added="onReplyAdded"></comment-item>
        </v-list>
      </v-list-item-content>
    </v-list-item>
  </v-card>
</template>

<script>
import axios from 'axios';

export default {
  props: ['comment', 'postId', 'currentUserId'],
  data() {
    return {
      isEditing: false,
      isReplying: false,
      editedComment: this.comment.comment,
      replyComment: '',
      commentCopy: { ...this.comment },
      isAuthor: false,
      isDeleted: false,
    };
  },
  created() {
    this.checkAuthor();
    this.checkDeleted();
  },
  methods: {
    startEditing() { this.isEditing = true; },
    endEditing() { this.isEditing = false; },
    cancelEdit() { this.isEditing = false; this.editedComment = this.comment.comment; },
    startReplying() { this.isReplying = true; },
    endReplying() { this.isReplying = false; },
    cancelReply() { this.isReplying = false; this.replyComment = ''; },
    onReplyDeleted(replyId) { this.commentCopy.replies = this.commentCopy.replies.filter(reply => reply.id !== replyId); },
    onReplyAdded(newReply) { if (!this.commentCopy.replies.find(reply => reply.id === newReply.id)) this.commentCopy.replies.push(newReply); },
    checkAuthor() { if (this.comment) this.isAuthor = this.comment.memberId === Number(this.currentUserId); },
    checkDeleted() { if (this.comment && this.comment.delYn === 'Y') this.isDeleted = true; },
    async saveEdit() {
      try {
        const response = await axios.patch(`http://localhost:8090/post/comment/update/${this.comment.id}`, {
          comment: this.editedComment,
        });
        if (response.status === 200) {
          this.endEditing();
          this.commentCopy.comment = this.editedComment;
        } else { alert("댓글 수정에 실패했습니다."); }
      } catch (error) {
        console.error("Error updating comment:", error.response ? error.response.data : error.message);
        alert("댓글 수정 중 오류가 발생했습니다.");
      }
    },
    async submitReply() {
      if (!this.replyComment.trim()) { alert("답글을 입력하세요."); return; }
      try {
        const response = await axios.post(`http://localhost:8090/post/comment/create/${this.postId}`, {
          comment: this.replyComment,
          parentId: this.comment.id,
        });

        if (response.status === 201) {
          alert("답글이 등록되었습니다.");
          this.endReplying();
          this.commentCopy.replies.push(response.data.result); // 새 답글을 댓글 목록에 추가
          this.replyComment = '';
        } else { alert("답글 등록에 실패했습니다."); }
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
          } else { alert("댓글 삭제에 실패했습니다."); }
        } catch (error) {
          console.error("Error deleting comment:", error.response ? error.response.data : error.message);
          alert("댓글 삭제 중 오류가 발생했습니다.");
        }
      }
    },
  }
}
</script>