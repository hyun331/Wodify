<template>
  <v-container>
    <v-card>
      <v-card-title>
        <v-row>
          <v-col cols="12">
            <v-select v-model="formData.type" :items="typeOptions" label="Type" required outlined dense :disabled="userRole === 'USER'" />
          </v-col>
          <v-col cols="12">
            <v-text-field v-model="formData.title" label="Title" required outlined dense />
          </v-col>
          <v-col cols="12">
            <quill-editor ref="quillEditorRef" v-model:value="formData.contents" :options="editorOptions" @blur="onEditorBlur" @focus="onEditorFocus" @ready="onEditorReady" @change="onEditorChange" class="custom-quill-editor" />
          </v-col>
        </v-row>
      </v-card-title>
      <v-card-actions class="d-flex justify-end">
        <v-btn color="primary" @click="submitForm">저장</v-btn>
        <v-btn color="secondary" @click="cancel">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import { quillEditor } from 'vue3-quill';
import 'quill/dist/quill.snow.css';
import { reactive, ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import axios from 'axios';
import BlotFormatter from 'quill-blot-formatter';
import Quill from 'quill';
import interact from 'interactjs';

// Register BlotFormatter as a module in Quill
Quill.register('modules/blotFormatter', BlotFormatter);

// Custom Video Blot 정의
const BlockEmbed = Quill.import('blots/block/embed');
class VideoBlot extends BlockEmbed {
  static create(value) {
    let node = super.create();
    node.setAttribute('controls', '');
    node.setAttribute('src', value);
    node.style.width = '320px'; // 기본 너비 설정 (작은 크기)
    node.style.height = '180px'; // 기본 높이 설정 (작은 크기)
    node.style.display = 'block'; // block으로 설정하여 부모 요소에 맞춰짐
    node.classList.add('resizable-video'); // 동영상에 고유한 클래스 추가
    return node;
  }
  static value(node) {
    return node.getAttribute('src');
  }
}
VideoBlot.blotName = 'video';
VideoBlot.tagName = 'video';
Quill.register(VideoBlot);

export default {
  components: { quillEditor },
  setup() {
    const router = useRouter();
    const quillEditorRef = ref(null);
    let quillInstance = null;

    const formData = reactive({
      type: null,
      title: '',
      contents: '',
    });

    const typeOptions = ['NOTICE', 'POST'];
    const userRole = localStorage.getItem("role");
    const isLogin = !!localStorage.getItem('token');

    if (userRole === 'USER') {
      formData.type = 'POST';
    }

    const handleMediaUpload = async (acceptType) => {
      if (!quillInstance) {
        console.error("Quill Editor is not initialized yet.");
        return;
      }
      const fileInput = document.createElement('input');
      fileInput.setAttribute('type', 'file');
      fileInput.setAttribute('accept', acceptType);
      fileInput.click();

      fileInput.onchange = async () => {
        const file = fileInput.files[0];
        const mediaUrl = await uploadMedia(file);

        if (mediaUrl) {
          const range = quillInstance.getSelection();
          if (range) {
            const extension = mediaUrl.split('.').pop().toLowerCase();
            const videoExtensions = ['mp4', 'webm', 'ogg'];

            if (videoExtensions.includes(extension)) {
              quillInstance.insertEmbed(range.index, 'video', mediaUrl);
              onMounted(() => makeResizable()); // 동영상 크기 조절 가능하게 설정
            } else {
              quillInstance.insertEmbed(range.index, 'image', mediaUrl);
            }
          }
        }
      };
    };

    const uploadMedia = async (file) => {
      const formData = new FormData();
      formData.append('file', file);
      try {
        const response = await axios.post('http://localhost:8090/post/upload-media', formData, { headers: { 'Content-Type': 'multipart/form-data' } });
        return response.data.result;
      } catch (error) {
        console.error('Error uploading media:', error);
        return null;
      }
    };

    const makeResizable = () => {
      interact('.resizable-video').resizable({
        edges: { left: true, right: true, bottom: true, top: true },
        listeners: {
          move(event) {
            let { x, y } = event.target.dataset;
            x = (parseFloat(x) || 0) + event.deltaRect.left;
            y = (parseFloat(y) || 0) + event.deltaRect.top;
            Object.assign(event.target.style, {
              width: `${event.rect.width}px`,
              height: `${event.rect.height}px`,
              transform: `translate(${x}px, ${y}px)`
            });
            event.target.dataset.x = x;
            event.target.dataset.y = y;
          }
        }
      });
    };

    const onEditorReady = (quill) => {
      quillInstance = quill;
      console.log('Editor ready!', quill);
      makeResizable(); // 에디터가 준비되면 동영상 크기 조절 활성화
    };

    const editorOptions = {
      theme: 'snow',
      placeholder: 'Write your contents here...',
      modules: {
        toolbar: {
          container: [
            [{ header: [1, 2, false] }],
            ['bold', 'italic', 'underline'],
            [{ list: 'ordered' }, { list: 'bullet' }],
            ['link', 'image', 'video'],
          ],
          handlers: {
            image: () => handleMediaUpload('image/*'),
            video: () => handleMediaUpload('video/*'),
          },
        },
        blotFormatter: {},
      },
    };

    const onEditorBlur = (quill) => {
      console.log('Editor blur!', quill);
    };

    const onEditorFocus = (quill) => {
      console.log('Editor focus!', quill);
    };

    const onEditorChange = (event) => {
      formData.contents = event.html;
    };

    const submitForm = async () => {
      const data = new FormData();
      data.append('type', formData.type);
      data.append('title', formData.title);
      data.append('contents', formData.contents);
      try {
        const response = await axios.post('http://localhost:8090/post/create', data, { headers: { 'Content-Type': 'multipart/form-data' } });
        const createdPostId = response.data.result;
        router.push(`/post/detail/${createdPostId}`);
      } catch (error) {
        console.error('Error submitting form:', error);
      }
    };

    const cancel = () => { router.push('/post/list'); };

    return {
      formData, typeOptions, userRole, isLogin, quillEditorRef, editorOptions, onEditorBlur, onEditorFocus, onEditorReady, onEditorChange, submitForm, cancel,
    };
  },
};
</script>

<style scoped>
.custom-quill-editor {
  min-height: 300px; /* 기본 높이 설정 */
}
</style>