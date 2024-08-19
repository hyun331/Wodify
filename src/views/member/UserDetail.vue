<template>
    <MemberDetailComponent 
    :memberInfo="memberInfo"
    :isMemberBaseUrl="isMemberBaseUrl"
    :isOnHold="isOnHold"
    :records="records"
    :userRole="userRole"
    :memberId="memberId"
    pageType="User Page"
    />
</template>

<script>

import MemberDetailComponent from '@/components/MemberDetailComponent.vue';
import axios from 'axios';
export default {
    components: {
        MemberDetailComponent
    },
    data() {
        return {
            memberInfo: {},
            isMemberBaseUrl: false,
            isOnHold: false,
            records: [],
            // userRole: null,
            memberId: null, 
        }
    },
    async created() {
        this.memberId = this.$route.params.id;
        
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/detail/${this.memberId}`);


            this.memberInfo = response.data.result;
            
            if (this.memberInfo.imagePath === '/assets/memberBaseImg.png') {
                this.isMemberBaseUrl = true;
            }
        } catch (e) {
            console.log(e);
        }
        // try {
        //     const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/isonhold`);
        //     console.log(response.data.result);

        //     this.isOnHold = response.data.result;
        // } catch (e) {
        //     console.log(e);
        // }
        try {
            // const token = localStorage.getItem('token');
            // if (token) {
            //     this.isLogin = true;
            //     this.userRole = localStorage.getItem("role");
            // }
            // 운동 기록 데이터를 가져오는 API 호출 (로그인한 사용자의 ID로 필터링)
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/record/list`, {
                params: {
                    memberId: this.memberId // memberId를 요청에 추가
                }
            });
            this.records = response.data.result.content;
        } catch (e) {
            console.log(e);
        }
    },
    
}
</script>

