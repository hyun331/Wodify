<template>
    <v-app-bar absolute dense color="#000000">
        <v-btn :to="{path:'/'}" color="black">
            <img height="40"
            :src="require('@/assets/wod.png')"
            alt="HomeLogo"/>
        </v-btn>
        <v-spacer></v-spacer>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">BOX-공통</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>박스조회-공통</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>내 박스-공통</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>박스 회원 관리-코치</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">WOD-코치</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>와드생성-코치</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>와드조회-코치</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">RESERVATION-공통</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>예약하기-멤버</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>예약생성-코치</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>예약조회-멤버</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>예약조회-코치</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">COMMUNITY-공통</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>게시판-공통</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>공지사항-공통</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">MY PAGE-공통</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>내 정보-공통</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/'}">
                    <v-list-item-title>내 기록-멤버</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-btn @click="kakaoLogin" v-if="!isLogin">LOGIN</v-btn>
        <v-btn @click="kakaoLogout" v-if="isLogin">LOGOUT</v-btn>
    </v-app-bar>

</template>

<script>
// import axios from 'axios';
import { KAKAO_LOGIN_URL } from '@/router/KakaoLoginUrl';
import { KAKAO_LOGOUT_URL } from '@/router/KakaoLogoutUrl';
export default{
    data(){
        return{
            userRole: null,
            isLogin: false,
        }
    },
    created(){
        const token = localStorage.getItem('token');
        if(token){
            this.isLogin = true;
            this.userRole = localStorage.getItem("role");
        }
    },
    methods:{
        kakaoLogin(){
            window.location.href=KAKAO_LOGIN_URL;
        },
        kakaoLogout(){
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('role');
            window.location.href=KAKAO_LOGOUT_URL;
        }

    }

}
</script>