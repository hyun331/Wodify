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
            <v-btn color="white" v-bind="props">BOX</v-btn>
            </template>
            <v-list>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">WOD</v-btn>
            </template>
            <v-list>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">RESERVATION</v-btn>
            </template>
            <v-list>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">COMMUNITY</v-btn>
            </template>
            <v-list>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
            <v-btn color="white" v-bind="props">MY PAGE</v-btn>
            </template>
            <v-list>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
                <v-list-item>
                    <v-btn block>BOX</v-btn>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-btn @click="kakaoLogin">LOGIN</v-btn>
        <v-btn :to="{path:'/'}">LOGOUT</v-btn>
    </v-app-bar>

</template>

<script>
// import axios from 'axios';
import { KAKAO_AUTH_URL } from '@/router/Oauth';
export default{
    data(){
        return{
        }
    },
    methods:{
        kakaoLogin(){
            window.location.href=KAKAO_AUTH_URL;
        }

    }

}
</script>