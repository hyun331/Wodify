<template>
  <v-container>
    <v-card>
      <v-card-title>
        <v-row align="center">
          <v-col cols="auto">
            <v-select v-model="post.type" :items="typeOptions" label="Type" required outlined dense
              :disabled="userRole === 'USER'" style="margin-top: 15px; width: 200px" />
          </v-col>
          <v-spacer />
          <v-col cols="auto">
            <v-row justify="end" style="margin-right: 8px">
              <v-btn class="custom-btn" style="margin-right: 8px" @click="isModalOpen = true">기록</v-btn>
              <v-btn class="custom-btn" style="margin-right: 8px" @click="updatePost">저장</v-btn>
              <v-btn class="custom-btn" @click="goBackToDetail">취소</v-btn>
            </v-row>
          </v-col>
        </v-row>
      </v-card-title>
      <v-card-text>
        <v-row>
          <v-col cols="12">
            <v-text-field v-model="post.title" label="Title" required outlined dense />
          </v-col>
          <v-col cols="12">
            <quill-editor ref="quillEditorRef" v-model:value="post.contents" :options="editorOptions"
              @blur="onEditorBlur" @focus="onEditorFocus" @ready="onEditorReady" @change="onEditorChange"
              class="custom-quill-editor" />
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
    <ModalRecord v-if="isModalOpen" @close="isModalOpen = false" @insert="insertIntoEditor" />

    <!-- 알림 모달 -->
    <v-dialog v-model="isAlertDialogOpen" max-width="500px">
      <v-card>
        <v-card-title class="headline">{{ alertDialogTitle }}</v-card-title>
        <v-card-text>{{ alertDialogMessage }}</v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="primary" @click="closeAlertDialog">확인</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </v-container>
</template>

<script>
import { quillEditor } from "vue3-quill";
import "quill/dist/quill.snow.css";
import { reactive, ref, onMounted } from "vue";
import { useRouter } from "vue-router";
import axios from "axios";
import ModalRecord from './ModalRecord.vue';
import BlotFormatter from "quill-blot-formatter";
import Quill from "quill";
import interact from "interactjs";

Quill.register("modules/blotFormatter", BlotFormatter);

const BlockEmbed = Quill.import("blots/block/embed");
class VideoBlot extends BlockEmbed {
  static create(value) {
    let node = super.create();
    node.setAttribute("controls", "");
    node.setAttribute("src", value);
    node.style.width = "320px";
    node.style.height = "180px";
    node.style.display = "block";
    node.classList.add("resizable-video");
    return node;
  }
  static value(node) {
    return node.getAttribute("src");
  }
}
VideoBlot.blotName = "video";
VideoBlot.tagName = "video";
Quill.register(VideoBlot);

export default {
  components: { quillEditor, ModalRecord },
  props: ['id'], // post ID를 prop으로 받아옴
  setup(props) {
    const router = useRouter();
    const quillEditorRef = ref(null);
    const isModalOpen = ref(false); // 모달 열림 여부를 관리하는 변수
    const quillInstance = ref(null); // Quill 인스턴스
    const post = reactive({
      type: "",
      title: "",
      contents: "",
    });

    const typeOptions = ["NOTICE", "POST"];
    const userRole = localStorage.getItem("role");
    const isLogin = !!localStorage.getItem("token");

    const loadPostData = async () => {
      try {
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/post/detail/${props.id}`);
        Object.assign(post, response.data.result);
      } catch (error) {
        console.error('Error loading post:', error);
        alert('Failed to load post data.');
      }
    };

    onMounted(() => {
      loadPostData(); // 컴포넌트가 마운트될 때 게시글 데이터를 로드
    });

    const handleMediaUpload = async (acceptType) => {
      if (!quillInstance.value) {
        console.error("Quill Editor is not initialized yet.");
        return;
      }
      const fileInput = document.createElement("input");
      fileInput.setAttribute("type", "file");
      fileInput.setAttribute("accept", acceptType);
      fileInput.click();

      fileInput.onchange = async () => {
        const file = fileInput.files[0];
        const mediaUrl = await uploadMedia(file);

        if (mediaUrl) {
          const range = quillInstance.value.getSelection();
          if (range) {
            const extension = mediaUrl.split(".").pop().toLowerCase();
            const videoExtensions = ["mp4", "webm", "ogg"];

            if (videoExtensions.includes(extension)) {
              quillInstance.value.insertEmbed(range.index, "video", mediaUrl);
              makeResizable();
            } else {
              quillInstance.value.insertEmbed(range.index, "image", mediaUrl);
            }
          }
        }
      };
    };

    const uploadMedia = async (file) => {
      const formData = new FormData();
      formData.append("file", file);
      try {
        const response = await axios.post(
          `${process.env.VUE_APP_API_BASE_URL}/post/upload-media`,
          formData,
          { headers: { "Content-Type": "multipart/form-data" } }
        );
        return response.data.result;
      } catch (error) {
        console.error("Error uploading media:", error);
        return null;
      }
    };

    const makeResizable = () => {
      interact(".resizable-video").resizable({
        edges: { left: true, right: true, bottom: true, top: true },
        listeners: {
          move(event) {
            let { x, y } = event.target.dataset;
            x = (parseFloat(x) || 0) + event.deltaRect.left;
            y = (parseFloat(y) || 0) + event.deltaRect.top;
            Object.assign(event.target.style, {
              width: `${event.rect.width}px`,
              height: `${event.rect.height}px`,
              transform: `translate(${x}px, ${y}px)`,
            });
            event.target.dataset.x = x;
            event.target.dataset.y = y;
          },
        },
      });
    };

    const onEditorReady = (quill) => {
      quillInstance.value = quill; // Quill 인스턴스 저장
      console.log("Editor ready!", quill);
      makeResizable();
    };

    const editorOptions = {
      theme: "snow",
      placeholder: "Write your contents here...",
      modules: {
        toolbar: {
          container: [
            [{ header: [1, 2, false] }],
            ["bold", "italic", "underline"],
            [{ list: "ordered" }, { list: "bullet" }],
            ["link", "image", "video"],
          ],
          handlers: {
            image: () => handleMediaUpload("image/*"),
            video: () => handleMediaUpload("video/*"),
          },
        },
        blotFormatter: {},
      },
    };

    const onEditorBlur = (quill) => {
      console.log("Editor blur!", quill);
    };

    const onEditorFocus = (quill) => {
      console.log("Editor focus!", quill);
    };

    const onEditorChange = (event) => {
      post.contents = event.html;
    };

    const updatePost = async () => {
      const formData = new FormData();
      formData.append('title', post.title);
      formData.append('contents', post.contents);
      formData.append('type', post.type);

      try {
        const response = await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/post/update/${props.id}`, formData, {
          headers: { 'Content-Type': 'multipart/form-data' }
        });

        if (response.status === 200) {
          goBackToDetail();
        } else {
          alert('Failed to update post.');
        }
      } catch (error) {
        console.error('Error updating post:', error);
        alert('An error occurred while updating the post.');
      }
    };


    const goBackToDetail = () => { router.push(`/post/detail/${props.id}`); };

    // 모달에서 전달된 데이터를 Quill Editor에 삽입
    const insertIntoEditor = (data) => {
      const quill = quillInstance.value; // Quill 인스턴스 가져오기

      // WOD 데이터 삽입
      if (data.wod) {
        let wodText = `
          WOD\n
          Time Cap: ${data.wod.timeCap}
          Rounds: ${data.wod.rounds}
          Info: ${data.wod.info}
          `;

        data.wod.wodDetResDtoList.forEach((detail) => {
          wodText += `
            ${detail.name}: ${detail.contents}
            `;
        });

        quill.insertText(quill.getLength(), wodText); // WOD 데이터를 먼저 삽입
      }

      // Record 데이터 삽입
      if (data.record) {
        const recordText = `
          Record\n
          SNF: ${data.record.snf === "Y" ? "성공" : "실패"}
          Time: ${data.record.time.slice(0, 5)}
          Comments: ${data.record.comments}
          `;

        quill.insertText(quill.getLength(), recordText); // Record 데이터를 나중에 삽입
      }

      isModalOpen.value = false; // 모달 닫기
    };

    return {
      post,
      typeOptions,
      userRole,
      isLogin,
      quillEditorRef,
      editorOptions,
      onEditorBlur,
      onEditorFocus,
      onEditorReady,
      onEditorChange,
      updatePost,
      goBackToDetail,
      isModalOpen,
      insertIntoEditor,
    };
  },
};
</script>

<style scoped>
.background-wrapper {
  background: linear-gradient(135deg, #1c1c1c, #3a3a3a);
  /* 그라데이션 배경 */
  padding: 20px;
  min-height: 100vh;
  /* 화면 전체 높이 */
}

.custom-quill-editor {
  min-height: 530px;
  /* 기본 높이 설정 */
}

.custom-btn {
  background-color: black !important;
  color: white !important;
  font-weight: 600;
  /* 글씨 두께를 조금 두껍게 설정 */
}

.v-card-title {
  padding: 0 16px;
}
</style>