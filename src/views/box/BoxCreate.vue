<template>
    <v-container>
      <v-row justify="center">
        <!-- sm: small screen, md: medium screen -->
        <v-col cols="12" sm="10" md="8">
          <v-card>
            <v-card-title class="text-h5 text-center">
              박스 생성/수정
            </v-card-title>
            <v-card-text>
              <v-form @submit.prevent="submitForm">
                <v-text-field
                  label="박스명"
                  v-model="boxName"
                  placeholder="박스 이름을 입력해주세요."
                  prepend-icon="mdi-home"
                  required
                ></v-text-field>
  
                <v-text-field
                  label="박스주소"
                  v-model="boxAddress"
                  placeholder="박스의 주소를 입력해주세요."
                  prepend-icon="mdi-map-marker"
                  required
                ></v-text-field>
  
                <v-file-input
                  label="박스로고"
                  prepend-icon="mdi-file-image"
                  @change="onFileChange"
                ></v-file-input>
  
                <v-textarea
                  label="이용시간"
                  v-model="useTime"
                  placeholder="박스 이용시간을 입력해주세요."
                  rows="2"
                  prepend-icon="mdi-clock-outline"
                ></v-textarea>
  
                <v-textarea
                  label="가격표"
                  v-model="price"
                  placeholder="₩ 가격을 입력해주세요."
                  rows="2"
                  prepend-icon="mdi-currency-krw"
                ></v-textarea>
  
                <v-textarea
                  label="소개글"
                  v-model="description"
                  placeholder="박스를 소개하는 문구를 적어주세요."
                  rows="4"
                  prepend-icon="mdi-pencil-outline"
                ></v-textarea>
  
                <v-btn type="submit" color="primary" block>등록</v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  export default {
    data() {
      return {
        boxName: '',
        boxAddress: '',
        useTime: '',
        price: '',
        description: '',
        boxLogo: null,
      };
    },
    methods: {
      onFileChange(file) {
        this.boxLogo = file;
      },
      submitForm() {
        console.log({
          boxName: this.boxName,
          boxAddress: this.boxAddress,
          useTime: this.useTime,
          price: this.price,
          description: this.description,
          boxLogo: this.boxLogo,
        });
        // 서버로 데이터를 전송하거나 다른 작업을 처리합니다.
      },
    },
  };
  </script>
  
  <style scoped>
  /* 필요에 따라 추가적인 스타일링을 여기에 적용하세요 */
  </style>
  