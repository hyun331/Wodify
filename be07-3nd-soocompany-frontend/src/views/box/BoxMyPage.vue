<template>
    <div class="page-container">
        <v-container>
            <v-row class="align-left justify-start" style="height: 100%; margin-top: 16px;">
                <v-col cols="auto" class="text-left">
                    <img :src="boxDetail.logo" alt="Box Logo" class="rounded-logo" />
                </v-col>
                <v-col class="d-flex align-center">
                    <h1 class="black-han-sans-regular">{{ boxDetail.name }}</h1>
                </v-col>
            </v-row>
            <v-row justify="center">
                <v-col cols="60" md="48" lg="42">
                    <v-card class="dynamic-card">
                        <v-card-text>
                            <v-table class="v-table" style="table-layout: fixed; width: 100%; overflow: hidden;">
                                <tbody>
                                    <tr v-for="(item, index) in boxDetailList" :key="index">
                                        <td class="title" style="padding-left: 20px;">{{ item.key }}</td>
                                        <td>
                                            <template v-if="item.key !== 'LOGO'">
                                                <div class="text" v-html="formatText(item.value)"></div>
                                            </template>
                                        </td>
                                    </tr>
                                </tbody>
                            </v-table>
                        </v-card-text>
                        <v-card-actions v-if="userRole === 'CEO'" class="card-actions">
                            <v-spacer></v-spacer>
                            <v-btn small class="hover-btn mx-auto custom-btn" @click="updateBox">UPDATE</v-btn>
                            <v-btn small class="hover-btn mx-auto custom-btn" @click="showDeleteDialog">DELETE</v-btn>
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
        :dialogText="dialogText" @click="home" />
    <BoxCoachRegister v-model="boxCoachRegister" @update:dialog="boxCoachRegister = $event" />
</template>

<script>
import axios from 'axios';
import AlertModalComponent from '@/components/AlertModalComponent.vue';
import BoxCoachRegister from './BoxCoachRegister.vue';

export default {
    components: {
        AlertModalComponent, BoxCoachRegister
    },
    data() {
        return {

            alertModal: false,
            dialogTitle: '',
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
            if (this.userRole == 'USER') {
                this.dialogTitle = '등록된 박스가 존재하지 않습니다';
                this.dialogText = '박스에 가입해주세요';
                this.alertModal = true;

            } else if (this.userRole == 'COACH') {

                this.boxCoachRegister = true;
            } else {
                //CEO
                this.dialogTitle = '등록된 박스가 존재하지 않습니다';
                this.dialogText = '박스에 생성해주세요';
                this.alertModal = true;
            }
        }
    },
    methods: {
        home() {
            this.$router.push('/');
        },
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
            this.$router.push('/');
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
        },
        // 줄바꿈 문자를 <br> 태그로 변환하는 메서드
        formatText(text) {
            if (!text) return '';
            return text.replace(/\n/g, '<br>');
        }
    }
}
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Black+Han+Sans&family=Jua&display=swap');

.page-container {
    overflow-x: hidden;
    /* 페이지 전체의 수평 스크롤 숨기기 */
}

.flex-card-row {
    display: flex;
    justify-content: center;
    flex: 1;
}

.flex-card-col {
    flex: 1;
    display: flex;
    justify-content: center;
}

.dynamic-card {
    width: 100%;
    min-height: 500px;
    /* 원하는 최소 높이 설정 */
    display: flex;
    flex-direction: column;
    overflow: hidden;
    /* 내부 스크롤 숨기기 */
}

.v-table {
    width: 100%;
    table-layout: fixed;
    overflow-x: auto;
    /* 수평 스크롤이 필요할 때만 표시 */
}

.title {
    font-family: 'Rubik Mono One', sans-serif;
    font-size: 30px;
    height: 100%;
    position: relative;
    left: 10px;
}

.text {
    font-size: 25px;
    /* 글씨 크기 설정 */
    display: flex;
    /* 플렉스 박스를 사용하여 정렬 */
    align-items: flex-start;
    /* 위쪽으로 정렬 */
    justify-content: flex-start;
    /* 왼쪽으로 정렬 */
    height: auto;
    /* 셀 높이를 자동으로 조정 */
    word-wrap: break-word;
    /* 긴 단어가 줄바꿈되도록 설정 */
    white-space: normal;
    /* 줄바꿈을 허용하도록 설정 */
    margin-bottom: 20px;
    /* 항목 간의 간격 조정 */
}

.data-value {
    white-space: pre-wrap;
    /* 줄바꿈 유지 */
    padding-top: 60px;
    /* 항목 간 간격 조정 */
    padding-bottom: 60px;
}

.v-table {
    width: 100%;
    /* 테이블이 부모 요소의 전체 너비를 차지하도록 설정 */
    table-layout: auto;
    /* 테이블 열 너비를 자동 조정 */
}

.v-table tbody tr+tr {
    padding-top: 50px;
    /* 각 섹션 사이의 간격 조정 */
}

.dynamic-card {
    min-height: 200px;
    /* 기본 카드 높이 설정 */
    display: flex;
    flex-direction: column;
    /* 내용에 따라 카드가 늘어나도록 설정 */
    overflow: hidden;
    /* 카드 내용이 넘치지 않도록 설정 */
}

.v-card-text {
    flex: 1;
    /* 카드 내용이 카드의 남은 공간을 차지하도록 설정 */
}

.rounded-logo {
    max-width: 150px;
    max-height: 150px;
    border-radius: 50%;
    border: 2px solid #ccc;
    float: left;
}

.black-han-sans-regular {
    font-family: "Black Han Sans", sans-serif;
    font-weight: 400;
    font-style: normal;
    font-size: 130px;
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

.card-actions {
    display: flex;
    justify-content: center;
    /* 버튼을 중앙에 배치 */
    gap: 20px;
    /* 버튼 간의 간격을 15px로 설정 */
}

.custom-btn {
    background-color: #D9D9D9;
    /* 버튼 배경색 설정 */
    color: black;
    /* 버튼 텍스트 색상 설정 */
    border: none;
    /* 버튼의 기본 테두리 제거 */
}
</style>
