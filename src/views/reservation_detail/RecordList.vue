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
                        <v-card-text>
                            <v-row class="d-flex justify-content-center mt-5" justify="center">
                                <v-col cols="12">
                                    <v-label>Time by Exercise Record</v-label>
                                    <div class="chart-container">
                                        <ExerciseChart :records="records" />
                                    </div>
                                </v-col>
                            </v-row>
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row>


            <!--운동기록 리스트-->
            <!-- <v-row>
                <v-col>
                    <v-card>
                        <v-card-text>
                            <v-table>
                                <thead>
                                    <tr>
                                        <th style="font-weight: bold; text-align: center;">DATE</th>
                                        <th style="font-weight: bold; text-align: center;">TIME</th>
                                        <th style="font-weight: bold; text-align: center;">PEOPLE</th>
                                        <th style="font-weight: bold; text-align: center;">WOD</th>
                                        <th style="font-weight: bold; text-align: center;">Delete</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="r in filteredReservationList" :key="r.id">
                                        <td style="text-align: center;" @click="viewDetail(r.reservationDetails)">
                                            {{ r.date }}
                                        </td>
                                        <td style="text-align: center;" @click="viewDetail(r.reservationDetails)">
                                            {{ r.time.slice(0, 5) }}
                                        </td>
                                        <td style="text-align: center;" @click="viewDetail(r.reservationDetails)">
                                            {{ r.reservationPeople }} / {{ r.maxPeople }}
                                        </td>
                                        <td style="text-align: center;">
                                            <v-btn :to="{ path: '/wod/select-date' }">view</v-btn>
                                        </td>
                                        <td style="text-align: center;">
                                            <v-btn @click="confirmDelete(r.id)">delete</v-btn>
                                        </td>
                                    </tr>
                                </tbody>
                            </v-table>

                            <ReservationMemberListModal v-model="memberListModal"
                                @update:dialog="memberListModal = $event" :memberList="selectedMemberList" />
                        </v-card-text>
                    </v-card>
                </v-col>
            </v-row> -->

            
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
