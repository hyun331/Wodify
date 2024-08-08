<template>
    <v-app-bar>
        <v-container>
            <v-row align="center">
                <v-col class="d-flex justify-start">
                    <h1>HEADER</h1>
                    <v-btn @click="kakaoLogin">LOGIN</v-btn>
                </v-col>
            </v-row>
        </v-container>

    </v-app-bar>
    
</template>
<script>
// import axios from 'axios';
import { KAKAO_AUTH_URL } from '@/router/Oauth';
export default{
    methods:{
        kakaoLogin(){
            window.location.href=KAKAO_AUTH_URL;
        }

    }

}
</script>