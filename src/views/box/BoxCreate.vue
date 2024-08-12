<template>
  <div class="page-container">
  <v-app class="app-background">
  <v-container fluid>
    <div>
      <h1 class="title">BOX CREATE</h1>
      </div><br><br>
    <v-row justify="center">
      <v-col cols="12" sm="12" md="10">
        <v-card>
          <v-card-title class="text-h5 text-center">
            BOX CREATION
          </v-card-title>
          <v-card-text>
            <v-form @submit.prevent="boxCreate">
              <v-text-field label="BOX NAME" v-model="boxName" placeholder="박스 이름을 입력해주세요." prepend-icon="mdi-home"
                required></v-text-field>

              <v-text-field label="BOX ADDRESS" v-model="boxAddress" placeholder="박스의 주소를 입력해주세요."
                prepend-icon="mdi-map-marker" required></v-text-field>

              <v-file-input label="BOX LOGO" accept="image/*" @change="fileUpdate" required></v-file-input>

              <v-textarea label="TIME" v-model="useTime" placeholder="박스 이용시간을 입력해주세요." rows="2"
                prepend-icon="mdi-clock-outline"></v-textarea>

              <v-textarea label="PRICE" v-model="price" placeholder="₩ 가격을 입력해주세요." rows="2"
                prepend-icon="mdi-currency-krw"></v-textarea>

              <v-textarea label="INTRODUCTION" v-model="intro" placeholder="박스를 소개하는 문구를 적어주세요." rows="4"
                prepend-icon="mdi-pencil-outline"></v-textarea>

              <v-btn type="submit" color="black" block>CREATE</v-btn>
            </v-form>
          </v-card-text>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</v-app>
</div>
</template>

<script>
// import axios from 'axios';
export default {
  data() {
    return {
      boxName: '',
      boxAddress: '',
      useTime: '',
      price: '',
      intro: '',
      boxLogo: null,
    };
  },
  methods: {
    fileUpdate(event) {
      this.boxLogo = event.target.files[0]
    },
    async boxCreate() {
      try {
        let registerData = new FormData();
        registerData.append("boxName", this.boxName);
        registerData.append("boxAddress", this.boxAddress);
        registerData.append("useTime", this.useTime);
        registerData.append("price", this.price);
        registerData.append("intro", this.intro);

        // await axios.post(`${process.env.VUE_APP_API_BASE_URL}/box/create`, registerData);
        // this.$router.push('/box/success');
      } catch (e) {
        alert("박스등록에 실패했습니다.");
      }
    },
  },
};

</script>


<style scoped>
.app-background {
  background-color: #D9D9D9; 
  min-height: 100vh; 
  margin: 0;
  padding: 0;
}
</style>