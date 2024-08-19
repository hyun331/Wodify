<template>
    <div class="background">
        <v-container>

            <v-row class="d-flex justify-content-between mt-5" justify="center">
                <v-col cols="12" sm="6" md="8">

                    <v-row>
                        <v-col v-if="isMemberBaseUrl">
                            <v-img :src="require('@/assets/memberBaseImg.png')" alt="member Image"
                                style="border-radius: 50%; width: 100px; height: 100px;" contain></v-img>
                        </v-col>
                        <v-col v-else>
                            <v-img :src="memberInfo.imagePath"
                                style="border-radius: 100%; width: 100px; height: 100px;"></v-img>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">NAME</v-label></v-col>
                        <v-col cols="10">

                            <v-text-field> {{ memberInfo.name }}
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">EMAIL</v-label></v-col>
                        <v-col cols="10">

                            <v-text-field> {{ memberInfo.email }}
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">PHONE</v-label></v-col>
                        <v-col cols="10">

                            <v-text-field> {{ memberInfo.phone }}
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">ADDRESS</v-label></v-col>
                        <v-col cols="10">

                            <v-text-field> {{ memberInfo.address }}
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">DEAD
                                LIFT</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field>{{ memberInfo.deadLift }}
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">SQUAT</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field> {{ memberInfo.squat }}
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">BENCH
                                PRESS</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field> {{ memberInfo.benchPress }}
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">BOX</v-label></v-col>
                        <v-col cols="5">

                            {{ memberInfo.boxName }}

                        </v-col>
                        <v-col cols="3">
                            <v-btn v-if="!isOnHold" @click="showHoldingModal">정지</v-btn>
                            <v-btn @click="showUnholdingModal" v-else>정지 해제</v-btn>
                        </v-col>
                    </v-row>


                    <!-- 차트 -->
                    <v-row class="d-flex justify-content-center mt-5" justify="center" v-if="userRole === 'USER'">
                        <v-col cols="12">
                            <v-label>Time by reservation</v-label>
                            <div class="chart-container">
                                <exercise-time-chart :records="records" />
                            </div>
                        </v-col>
                    </v-row>


                </v-col>
            </v-row>
            <HoldingModal v-model="holding" @update:dialog="holding = $event">
            </HoldingModal>
            <UnholdingModal v-model="unholding" @update:dialog="unholding = $event">
            </UnholdingModal>
        </v-container>
    </div>
</template>

<script>
import HoldingModal from './HoldingModal.vue';
import UnholdingModal from './UnholdingModal.vue';
import ExerciseTimeChart from './ExerciseTimeChart.vue';
import axios from 'axios';
export default {
    components: {
        HoldingModal,
        UnholdingModal,
        ExerciseTimeChart
    },
    data() {
        return {
            memberInfo: {},
            isMemberBaseUrl: false,
            isOnHold: false,
            holding: false,
            unholding: false,
            records: [],
            userRole: null,
            memberId: null, // 추가: 로그인한 사용자의 ID
        }
    },
    async created() {
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/detail`);
            console.log(response.data.result);

            this.memberInfo = response.data.result;
            this.memberId = this.memberInfo.id; // 추가: 로그인한 사용자의 ID 저장
            if (this.memberInfo.imagePath === '/assets/memberBaseImg.png') {
                this.isMemberBaseUrl = true;
            }
        } catch (e) {
            console.log(e);
        }
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/isonhold`);
            console.log(response.data.result);

            this.isOnHold = response.data.result;
        } catch (e) {
            console.log(e);
        }
        try {
            const token = localStorage.getItem('token');
            if (token) {
                this.isLogin = true;
                this.userRole = localStorage.getItem("role");
            }
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
    methods: {
        showHoldingModal() {
            this.holding = true;
        },
        showUnholdingModal() {
            this.unholding = true;
        }
    }
}
</script>

<style scoped>
.chart-container {
    width: 100%;
    /* 차트의 너비를 100%로 설정 */
    height: 400px;
    /* 원하는 높이로 설정, 예: 400px */
}
</style>