<template>
  <v-container>
    <v-form ref="form" @submit.prevent="submitForm">
      <v-row>
        <v-col cols="12" md="6" v-if="userRole !== 'USER'">
          <v-select 
            v-model="formData.type" 
            :items="typeOptions" 
            label="Type" 
            required 
            :disabled="userRole === 'USER'"
          ></v-select>
        </v-col>

        <v-col cols="12" md="6">
          <v-text-field v-model="formData.title" label="Title" required></v-text-field>
        </v-col>

        <v-col cols="12">
          <v-textarea v-model="formData.contents" label="Contents" required></v-textarea>
        </v-col>

        <!-- 이미지 업로드 부분 -->
        <v-col cols="12">
          <input type="file" multiple @change="handleFileUpload" />
        </v-col>

        <!-- 미리보기 및 삭제 버튼 -->
        <v-row>
          <v-col v-for="(file, index) in newFiles" :key="index" cols="12" sm="6" md="4">
            <v-img :src="file.preview" aspect-ratio="1.75"></v-img>
            <v-btn color="error" @click="removeNewFile(index)">Remove</v-btn>
          </v-col>
        </v-row>

        <v-col cols="12" class="d-flex justify-end">
          <v-btn color="primary" type="submit" class="mr-4">Submit</v-btn>
          <v-btn color="secondary" @click="cancel">Cancel</v-btn>
        </v-col>
      </v-row>
    </v-form>
  </v-container>
</template>

<script>
import axios from 'axios';

export default {
  data() {
    return {
      formData: {
        type: null,
        title: '',
        contents: '',
      },
      newFiles: [], // 이미지 파일과 미리보기 URL을 담을 배열
      typeOptions: ['NOTICE', 'POST'], // 가능한 타입 선택지
      userRole: null, // 사용자의 역할을 저장
      isLogin: false, // 로그인 상태를 저장
    };
  },
  created() {
    // 로그인 상태와 사용자 역할을 localStorage에서 가져옴
    const token = localStorage.getItem('token');
    if (token) {
      this.isLogin = true;
      this.userRole = localStorage.getItem("role");

      // userRole이 'user'일 경우 type을 'POST'로 설정
      if (this.userRole === 'USER') {
        this.formData.type = 'POST';
      }
    }
  },
  methods: {
    handleFileUpload(event) {
      const files = event.target.files;
      for (let i = 0; i < files.length; i++) {
        const reader = new FileReader();
        reader.onload = (e) => {
          this.newFiles.push({
            file: files[i], // 원본 파일 객체
            preview: e.target.result // 미리보기 URL
          });
        };
        reader.readAsDataURL(files[i]);
      }
    },
    removeNewFile(index) {
      this.newFiles.splice(index, 1); // 선택된 파일을 배열에서 제거
    },
    async submitForm() {
      const data = new FormData();
      data.append('type', this.formData.type);
      data.append('title', this.formData.title);
      data.append('contents', this.formData.contents);

      // 선택된 이미지 파일들을 FormData에 추가
      this.newFiles.forEach(file => {
        data.append('files', file.file);
      });

      try {
        // 백엔드로 POST 요청
        const response = await axios.post('http://localhost:8090/post/create', data, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        });
        console.log('Response:', response.data);

        this.$router.push('/post/list');
      } catch (error) {
        console.error('Error submitting form:', error);
      }
    },
    cancel() {
      // /post/list로 이동
      this.$router.push('/post/list');
    },
  },
};
</script>
