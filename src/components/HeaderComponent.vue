<template>
    <v-app-bar absolute color="#000000">
        <v-btn :to="{ path: '/' }" color="black">
            <img height="40" :src="require('@/assets/wod.png')" alt="HomeLogo" />
        </v-btn>
        <v-spacer></v-spacer>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
                <v-btn color="white" v-bind="props" class="rubikMonoOne">BOX</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{ path: '/box/list' }">
                    <v-list-item-title>박스조회</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{ path: '/box/mybox' }">
                    <v-list-item-title>내 박스</v-list-item-title>
                </v-list-item>
                <v-list-item v-if="userRole === 'CEO'" :to="{ path: '/box/create' }">
                    <v-list-item-title>박스 생성</v-list-item-title>
                </v-list-item>
                <v-list-item v-if="userRole === 'COACH' || userRole === 'CEO'" :to="{ path: '/member/list/user' }">
                    <v-list-item-title>박스 회원 관리</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu v-if="userRole === 'COACH' || userRole === 'CEO'" open-on-hover>
            <template v-slot:activator="{ props }">
                <v-btn color="white" v-bind="props" class="rubikMonoOne">WOD</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{path:'/wod/select-date'}">
                    <v-list-item-title>와드조회 ∙ 등록</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
                <v-btn color="white" v-bind="props" class="rubikMonoOne">RESERVATION</v-btn>
            </template>
            <v-list>
                <v-list-item v-if="userRole === 'USER'" :to="{ path: '/reservation-detail/create' }">
                    <v-list-item-title>예약하기</v-list-item-title>
                </v-list-item>
                <v-list-item v-if="userRole === 'COACH' || userRole === 'CEO'" :to="{ path: '/reservation/create' }">
                    <v-list-item-title>예약생성</v-list-item-title>
                </v-list-item>
                <v-list-item v-if="userRole === 'USER'" :to="{ path: '/reservation-detail/list' }">
                    <v-list-item-title>예약조회</v-list-item-title>
                </v-list-item>
                <v-list-item v-if="userRole === 'COACH' || userRole === 'CEO'" :to="{ path: '/reservation/list' }">
                    <v-list-item-title>예약조회</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
                <v-btn color="white" v-bind="props" class="rubikMonoOne">COMMUNITY</v-btn>
            </template>
            <v-list>
                <v-list-item :to="{ path: '/post/list' }">
                    <v-list-item-title>게시판</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{ path: '/hallOfFame' }">
                    <v-list-item-title>명예의 전당</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover v-if="userRole === 'COACH' || userRole === 'CEO'">
            <template v-slot:activator="{ props }">
                <v-btn color="white" v-bind="props" class="rubikMonoOne" :to="{ path: '/member/detail' }">MY PAGE</v-btn>
            </template>
        </v-menu>
        <v-menu open-on-hover v-if="userRole === 'USER'">
            <template v-slot:activator="{ props }">
                <v-btn color="white" v-bind="props" class="rubikMonoOne">MY PAGE</v-btn>
            </template>
            <v-list >
                <v-list-item :to="{ path: '/member/detail' }">
                    <v-list-item-title>내 정보</v-list-item-title>
                </v-list-item>
                <v-list-item :to="{path:'/record/list'}">
                    <v-list-item-title>내 기록</v-list-item-title>
                </v-list-item>
            </v-list>
        </v-menu>
        <v-menu open-on-hover>
            <template v-slot:activator="{ props }">
                <v-btn icon color="white" v-bind="props" class="rubikMonoOne"><v-icon>mdi-bell</v-icon></v-btn>
                <span class="notification-icon">
                    <i class="fa fa-bell"></i>
                    <span v-if="liveAlert > 0" class="notification-count">{{ liveAlert }}</span>
                </span>
            </template>
            <v-list>
                <!-- Check if there are notifications -->
                <v-list-item v-if="notifications.length === 0">
                    <v-list-item-title>No notifications</v-list-item-title>
                </v-list-item>

                <!-- Loop through notifications -->
                <v-list-item v-for="(notification, index) in notifications" :key="index"
                    @click="handleNotificationClick(index)">
                    <v-list-item-content>
                        <v-list-item-title>{{ notification.message }}</v-list-item-title>
                        <!-- Optional: Display the date or any other info -->
                        <v-list-item-subtitle>{{ notification.createdTime.slice(0,10) }}</v-list-item-subtitle>
                    </v-list-item-content>
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
import { EventSourcePolyfill } from 'event-source-polyfill';
export default {
    data() {
        return {
            userRole: null,
            isLogin: false,
            liveAlert: 0,
            msg: "",
            notifications: [], // 받은 알림 저장
            showNotifications: true, // 토글 드랍다운
        }
    },
    created() {
        const token = localStorage.getItem('token');
        if (token) {
            this.isLogin = true;
            this.userRole = localStorage.getItem("role");
        }
        const savedNotifications = localStorage.getItem('notifications');
        if (savedNotifications) {
            this.notifications = JSON.parse(savedNotifications);
            this.liveAlert = this.notifications.length; // Update alert count
        }
        this.connectToEventSource(token)

    },
    methods: {
        connectToEventSource(token) {
            if (this.isLogin) {
                let sse = new EventSourcePolyfill(`${process.env.VUE_APP_API_BASE_URL}/subscribe`, { headers: { Authorization: `Bearer ${token}` } });
                sse.addEventListener('connect', (event) => { console.log(event) });
                sse.addEventListener('reservation', (event) => {
                    this.liveAlert++;

                    let data = JSON.parse(event.data);

                    const newNotification = {
                        memberName: data.memberName,
                        date: data.date,
                        createdTime: data.createdTime,
                        message: this.userRole === 'USER'
                            ? `대기 중이던 ${data.date}일자 수업에 예약이 확정되었습니다.`
                            : `회원 ${data.memberName}님이 ${data.date}에 예약을 완료했습니다.`
                    };
                    localStorage.removeItem('notifications');
                    this.notifications.push(newNotification);
                    localStorage.setItem('notifications', JSON.stringify(this.notifications));
                });
                sse.addEventListener('reservationDetail', (event) => {
                    this.liveAlert++;

                    let data = JSON.parse(event.data);

                    if(this.userRole === 'USER'){ 
                        this.msg = `${data.date}일자 수업이 한시간 남았습니다.`;
                    }

                    const newNotification = {
                        memberName: data.memberName,
                        date: data.date,
                        createdTime: data.createdTime,
                        message: this.msg
                    };
                    
                    localStorage.removeItem('notifications');
                    this.notifications.push(newNotification);
                    localStorage.setItem('notifications', JSON.stringify(this.notifications));

                });
                sse.onerror = (error) => {
                    console.error('Error event:', error);
                };
            }
        },
        kakaoLogin() {
            window.location.href = KAKAO_LOGIN_URL;
        },
        kakaoLogout() {
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            localStorage.removeItem('role');
            localStorage.removeItem('notifications');
            window.location.href = KAKAO_LOGOUT_URL;
        },
        toggleNotificationDropdown() {
            console.log("toggle");
            this.showNotifications = !this.showNotifications;
            console.log(this.showNotifications);
        },
        handleNotificationClick(index) {
            if (this.userRole === 'USER') {
                this.$router.push('/reservation-detail/list');
            } else {
                this.$router.push('/reservation/list');
            }
            // Redirect to /reservation/list

            // Remove the clicked notification
            this.notifications.splice(index, 1);

            // Update localStorage
            localStorage.setItem('notifications', JSON.stringify(this.notifications));

            // Update liveAlert count
            this.liveAlert = this.notifications.length;
        },

    }

}
</script>
<style scoped>
.notification-icon {
    position: relative;
    cursor: pointer;
}

.notification-count {
    position: absolute;
    top: -5px;
    right: -5px;
    background: red;
    color: white;
    border-radius: 50%;
    padding: 2px 6px;
    font-size: 12px;
}
</style>
