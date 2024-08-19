<template>
    <div>
        <v-container>

            <v-row class="d-flex mt-5" justify="center">
                <v-col cols="12" sm="8" md="9">
                    <v-card>
                        <v-card-title>
                            <v-row>
                                <v-col v-if="isMemberBaseUrl">
                                    <v-img :src="require('@/assets/memberBaseImg.png')" alt="member Image" class="memberImg"
                                         contain></v-img>
                                </v-col>
                                <v-col v-else>
                                    <v-img :src="memberInfo.imagePath" class="memberImg"></v-img>
                                </v-col>
                                <v-col class="rubikMonoOne" style="font-size: 80px;">
                                    {{ pageType }}
                                </v-col>
                            </v-row>

                        </v-card-title>
                        <v-card-text style="font-size: 20px;">

                            <hr>

                            <v-row  >
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
                                <v-col cols="2" v-if="pageType ==='My Page'">
                                    <v-btn v-if="!isOnHold" @click="showHoldingModal">정지</v-btn>
                                    <v-btn @click="showUnholdingModal" v-else>정지 해제</v-btn>
                                </v-col>

                            </v-row>
                            <br>
                        </v-card-text>
                    </v-card>




                    <!-- 차트 -->
                    <v-row class="d-flex justify-content-center mt-5" justify="center">
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
import HoldingModal from '@/views/member/HoldingModal.vue';
import UnholdingModal from '@/views/member/UnholdingModal.vue';
import ExerciseTimeChart from '@/views/member/ExerciseTimeChart.vue';
// import axios from 'axios';


export default {
    props: ['memberInfo', 'isMemberBaseUrl', 'isOnHold', 'records', 'userRole', 'memberId', 'pageType'],
    components: {
        HoldingModal,
        UnholdingModal,
        ExerciseTimeChart
    },
    data() {
        return {
            holding: false,

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

.memberImg{
    border-radius: 50%; 
    width: 150px; 
    height: 150px;
    border: 3px solid #000;
}


</style>