<template>
    <v-dialog >
        <v-card class="mx-auto" width="500">
            <v-card-title class="text-h4 text-center" style="margin-top: 10px;">
                회원을 탈퇴시키겠습니까?
            </v-card-title>
            <v-card-text style="padding: 20px;">
                <v-row class="d-flex justify-content-center align-center" style="margin-top: 25px;">
                    <RoundedButtonComponent @click="deleteUser()"  text="삭제" class="mx-2" style="background-color: red"/>
                    <RoundedButtonComponent @click="closeModal" text="닫기" class="mx-2"/>
                </v-row>

                
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
import axios from 'axios';
export default{
    props:['email'],
    components:{
        RoundedButtonComponent,

    },
    methods:{
        closeModal() {
            this.$emit('update:dialog', false);
        },
        async deleteUser(){
            await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/member/leave/box?userEmail=${this.email}`);
            this.closeModal();
            window.location.href="/member/list/user";

        }
    }
}

</script>