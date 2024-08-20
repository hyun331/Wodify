<template>
    <div class="page-container">
        <div class="box right-align">
            <br>
            <span class="boxLocation">
                {{ boxDetail.name }}
            </span>
            <br>
        </div>
        <v-container>
            <!-- 로고를 표시하는 부분 -->
            <v-row justify="center">
                <v-col cols="12" md="8" class="text-center">
                    <img :src="boxDetail.logo" alt="Box Logo" class="rounded-logo" />
                </v-col>
            </v-row>

            <!-- 나머지 정보를 표시하는 카드 -->
            <v-row justify="center">
                <v-col>
                    <v-card>
                        <v-card-text>
                            <v-table style="table-layout: fixed; width: 100%;">
                                <tbody>
                                    <tr v-for="(item, index) in boxDetailList" :key="index">
                                        <td>{{ item.key }}</td>
                                        <td>
                                            <!-- 문자열 처리 -->
                                            <template v-if="item.key !== 'LOGO'">
                                                <div>
                                                    {{ item.value }}
                                                </div>
                                            </template>
                                        </td>
                                    </tr>
                                </tbody>
                            </v-table>
                        </v-card-text>
                        <v-card-actions v-if="userRole === 'CEO'">
                            <v-spacer></v-spacer>
                            <v-btn small class="hover-btn mx-auto" @click="updateBox">UPDATE</v-btn>
                            <v-btn small class="hover-btn mx-auto" @click="showDeleteDialog">DELETE</v-btn>
                        </v-card-actions>
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
        <OrderListComponent :isAdmin="false" />

        <!-- 삭제 확인 다이얼로그 -->
        <v-dialog v-model="deleteDialog" max-width="500px">
            <v-card>
                <v-card-title class="headline">DELETE</v-card-title>
                <v-card-text>
                    정말 삭제하시겠습니까?
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn class="hover-btn mx-auto" @click="confirmDelete">삭제</v-btn>
                    <v-btn class="hover-btn mx-auto" @click="closeDeleteDialog">취소</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>

        <v-dialog v-model="successDialog" max-width="500px">
            <v-card>
                <v-card-title class="headline">삭제 완료</v-card-title>
                <v-card-text>
                    BOX가 성공적으로 삭제되었습니다.
                </v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn class="hover-btn mx-auto" @click="closeSuccessDialog">확인</v-btn>
                </v-card-actions>
            </v-card>
        </v-dialog>
    </div>
    <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
        :dialogText="dialogText" />
    <BoxCoachRegister v-model="boxCoachRegister" @update:dialog="boxCoachRegister = $event" 
         />
</template>

<script>
import axios from 'axios';
import AlertModalComponent from '@/components/AlertModalComponent.vue';
import BoxCoachRegister from './BoxCoachRegister.vue';

export default {
    components:{
        AlertModalComponent, BoxCoachRegister
    },
    data() {
        return {

            alertModal: false,
            dialogTitle:'',
            dialogText: '',

            boxCoachRegister: false, 

            boxDetailId: this.$route.params.id,
            boxDetail: {},
            boxDetailList: [],
            deleteDialog: false,
            successDialog: false,
            userRole: null,
        }
    },
    async created() {
        try {
            const token = localStorage.getItem('token');
            if (token) {
                this.userRole = localStorage.getItem("role");
            }
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/box/mybox`);
            this.boxDetail = response.data.result;
            this.boxDetailList = [
                { key: "TIME", value: this.boxDetail.operatingHours },
                { key: "PRICE", value: this.boxDetail.fee },
                { key: "ADDRESS", value: this.boxDetail.address },
                { key: "INTRO", value: this.boxDetail.intro },
            ];
        } catch (error) {
            console.error(this.userRole)
            console.error("등록된 상자가 존재하지 않습니다:", error);
            if(this.userRole == 'USER'){
                this.dialogTitle = '등록된 박스가 존재하지 않습니다';
                this.dialogText = '박스에 가입해주세요';
                this.alertModal = true;

            }else if(this.userRole == 'COACH'){

                this.boxCoachRegister = true;
            }else{
                //CEO
                this.dialogTitle = '등록된 박스가 존재하지 않습니다';
                this.dialogText = '박스에 생성해주세요';
                this.alertModal = true;
            }
        }
    },
    methods: {
        updateBox() {
            this.$router.push('/box/update');
        },
        showDeleteDialog() {
            this.deleteDialog = true; // 다이얼로그 표시
        },
        closeDeleteDialog() {
            this.deleteDialog = false; // 다이얼로그 숨기기
        },
        closeSuccessDialog() {
            this.successDialog = false; // 삭제 성공 다이얼로그 숨기기
        },
        async confirmDelete() {
            try {
                const token = localStorage.getItem('token');
                if (token) {
                    this.isLogin = true;
                    this.userRole = localStorage.getItem("role");
                }
                await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/box/delete`);
                this.closeDeleteDialog();
                this.successDialog = true;
            } catch (error) {
                console.error("상자 삭제 중 오류 발생:", error);
            }
        }
    }
}
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik+Mono+One&display=swap');

.title {
    font-family: 'Rubik Mono One', sans-serif;
}

.container {
    background-color: #D9D9D9;
    min-height: 100vh;
}

.rounded-logo {
    max-width: 200px;
    max-height: 200px;
    border-radius: 50%;
    border: 2px solid #ccc;
    float: left;
}

.hover-btn {
    background-color: white;
    color: black;
    transition: background-color 0.3s ease, color 0.3s ease;
}

.hover-btn:hover {
    background-color: black;
    color: white;
}

.data-value {
    white-space: pre-wrap; /* 줄바꿈 유지 */
    padding-top: 10px; /* 항목 간 간격 조정 */
    padding-bottom: 10px;
}

.v-table tbody tr + tr {
    padding-top: 20px; /* 각 섹션 사이의 간격 조정 */
}
</style>