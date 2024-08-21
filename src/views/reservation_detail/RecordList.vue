<template>
    <div>
        <v-container>
            <div>
                <h1 class="rubikMonoOne">MY</h1>
                <h1 class="rubikMonoOne">RECORD</h1>
            </div>
            <br>
            <v-row>
                <v-col>
                    <v-card>
                        <v-card-title>
                          <v-row class="d-flex justify-center align-center">
                            <v-col class="text-center">
                              운동 기록
                            </v-col>
                          </v-row>
                        </v-card-title>
                        <hr>
                        <v-card-text style="font-size: 20px;">
                            <ExerciseChart :records="records" />
                        </v-card-text>
                      </v-card>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
import ExerciseChart from './ExerciseChart.vue';
import axios from 'axios';
export default {
    components: {
        ExerciseChart
    },
    data() {
        return {
            memberInfo: {},
            records: [],
            userRole: null,
            memberId: null, // 추가: 로그인한 사용자의 ID

        }
    },
    async created() {
        try {
            const token = localStorage.getItem('token');
            if (token) {
                this.isLogin = true;
                this.userRole = localStorage.getItem("role");
            }
            const response1 = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/detail`);
            this.memberInfo = response1.data.result;
            this.memberId = this.memberInfo.id; // 추가: 로그인한 사용자의 ID 저장
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
