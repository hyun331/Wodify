<template>
    
    <v-container>

        <v-row class="d-flex mt-5" justify="center">
            <v-col cols="12" sm="8" md="9">
                <v-card>
                    <v-card-title>
                        <v-row>
                            <v-col v-if="isMemberBaseUrl" class="profile-image-container">

                                <v-img :src="require('@/assets/memberBaseImg.png')" alt="member Image"
                                    class="memberImg" contain></v-img>




                            </v-col>
                            <v-col v-else class="profile-image-container">
                                <v-img :src="memberInfo.imagePath" class="memberImg" contain></v-img>
                            </v-col>
                            <v-col class="rubikMonoOne" cols="9" style="font-size: 80px;">
                                {{ pageType }}
                            </v-col>
                        </v-row>

                    </v-card-title>

                    <v-row justify="end" v-if="memberId === memberInfo.id">
                        <v-col cols="1"><v-btn @click="changeMemberInfo">수정</v-btn></v-col>
                        <v-col cols="2"><v-btn @click="deleteMember">탈퇴</v-btn></v-col>
                    </v-row>

                    <v-card-text style="font-size: 20px;">

                        <hr>

                        <v-row>
                            <v-col>
                                <v-label class="rubikMonoOne" style="font-size: 25px;">NAME</v-label>
                            </v-col>
                            <v-col>
                                {{ memberInfo.name }}
                            </v-col>

                        </v-row>

                        <v-row>
                            <v-col><v-label class="rubikMonoOne" style="font-size: 25px;">EMAIL</v-label></v-col>
                            <v-col>
                                {{ memberInfo.email }}

                            </v-col>
                        </v-row>

                        <v-row>
                            <v-col><v-label class="rubikMonoOne" style="font-size: 25px;">PHONE</v-label></v-col>
                            <v-col>

                                {{ memberInfo.phone }}

                            </v-col>
                        </v-row>

                        <v-row>
                            <v-col><v-label class="rubikMonoOne" style="font-size: 25px;">ADDRESS</v-label></v-col>
                            <v-col>

                                {{ memberInfo.address }}

                            </v-col>
                        </v-row>
                        <hr>

                        <v-row>
                            <v-col><v-label class="rubikMonoOne" style="font-size: 25px;">DEAD
                                    LIFT</v-label></v-col>
                            <v-col>

                                {{ memberInfo.deadLift }}

                            </v-col>
                        </v-row>

                        <v-row>
                            <v-col><v-label class="rubikMonoOne" style="font-size: 25px;">SQUAT</v-label></v-col>
                            <v-col>

                                {{ memberInfo.squat }}

                            </v-col>
                        </v-row>

                        <v-row>
                            <v-col><v-label class="rubikMonoOne" style="font-size: 25px;">BENCH
                                    PRESS</v-label></v-col>
                            <v-col>

                                {{ memberInfo.benchPress }}

                            </v-col>
                        </v-row>
                        <hr>
                        <v-row>
                            <v-col cols="6">
                                <v-label class="rubikMonoOne" style="font-size: 25px;">BOX</v-label></v-col>
                            <v-col cols="4">

                                {{ memberInfo.boxName }}

                            </v-col>
                            <v-col cols="2" v-if="pageType === 'My Page' && userRole === 'USER'">
                                <v-btn v-if="!isOnHold" @click="showHoldingModal">정지</v-btn>
                                <v-btn @click="showUnholdingModal" v-else>정지 해제</v-btn>
                            </v-col>

                            <v-col cols="2" v-if="pageType === 'My Page' && userRole === 'COACH'">
                                <v-btn v-if="memberInfo.boxName == null" @click="showBoxCoachRegisterModal">박스
                                    등록</v-btn>

                                <v-btn v-else @click="showBoxCoachRegisterModal">박스 변경</v-btn>
                            </v-col>

                        </v-row>
                        <br>
                    </v-card-text>
                </v-card>
                <br><br><br>

                <v-card v-if="userRole != 'USER'  && pageType=='My Page'">

                </v-card>

                <!-- 차트 -->
                <v-card v-else>

                    <v-card-title>
                      <v-row class="d-flex justify-center align-center">
                        <v-col class="text-center">
                          운동 기록
                        </v-col>
                      </v-row>
                    </v-card-title>
                    <hr>
                    <v-card-text style="font-size: 20px;">
                        <exercise-time-chart :records="records" />
                    </v-card-text>
                  </v-card>
                  

            </v-col>
        </v-row>
        <HoldingModal v-model="holding" @update:dialog="holding = $event">
        </HoldingModal>
        <UnholdingModal v-model="unholding" @update:dialog="unholding = $event">
        </UnholdingModal>

        <BoxCoachRegister v-model="boxCoachRegister" @update:dialog="boxCoachRegister = $event">
        </BoxCoachRegister>

        <ChangeMemberInfoModal v-model="changeMemberInfoModal" @update:dialog="changeMemberInfoModal = $event"
        :name="memberInfo.name"
        :email="memberInfo.email"
        :phone="memberInfo.phone"
        :address="memberInfo.address"
        :benchPress="memberInfo.benchPress"
        :squat="memberInfo.squat"
        :deadLift="memberInfo.deadLift"
        >
        </ChangeMemberInfoModal>
    </v-container>

</template>

<script>
import HoldingModal from '@/views/member/HoldingModal.vue';
import UnholdingModal from '@/views/member/UnholdingModal.vue';
import ExerciseTimeChart from '@/views/member/ExerciseTimeChart.vue';
import BoxCoachRegister from '@/views/box/BoxCoachRegister.vue';
import ChangeMemberInfoModal from '@/views/member/ChangeMemberInfoModal.vue';
import axios from 'axios';
import { KAKAO_LOGOUT_URL } from '@/router/KakaoLogoutUrl';


export default {
props: ['memberInfo', 'isMemberBaseUrl', 'isOnHold', 'records', 'userRole', 'memberId', 'pageType'],
components: {
    HoldingModal,
    UnholdingModal,
    ExerciseTimeChart,
    BoxCoachRegister,
    ChangeMemberInfoModal,
},
data() {
    return {
        holding: false,
        unholding: false,
        boxCoachRegister: false,
        changeMemberInfoModal: false,
    }
},

methods: {
    showHoldingModal() {
        this.holding = true;
    },
    showUnholdingModal() {
        this.unholding = true;
    },
    showBoxCoachRegisterModal() {
        this.boxCoachRegister = true;
    },
    changeMemberInfo(){
        this.changeMemberInfoModal = true;

    },
    async deleteMember(){
        await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/member/delete`);
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        localStorage.removeItem('role');
        localStorage.removeItem('notifications');
        window.location.href = KAKAO_LOGOUT_URL;
    }
}
}
</script>

<style scoped>


.memberImg {
border-radius: 50%;
width: 100%;
height: 100%;


}

.profile-image-container {
position: relative;

background-image: url('@/assets/profileBoarder.png');
background-size: cover;
background-position: center;
border-radius: 50%;
display: flex;
justify-content: center;
align-items: center;
overflow: hidden;
/* 필요 시 추가적으로 오버플로우 숨김 */
padding: 10px;
margin: 20px;
}
</style>