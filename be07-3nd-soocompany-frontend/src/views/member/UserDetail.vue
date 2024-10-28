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
        
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/record/list`, {
                params: {
                    memberId: this.memberId 
                }
            });
            this.records = response.data.result.content;
        } catch (e) {
            console.log(e);
        }
    },
    
}
</script>

