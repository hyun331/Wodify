<template>
    <div class="page-container">
        <v-container>
            <!-- 나머지 정보를 표시하는 카드 -->
            <v-row justify="center">
                <v-col>
                    <br><br><br><br><br>
                    <v-card>
                        <!-- 로고와 이름을 표시하는 부분 -->
                        <v-row class="align-center" style="height: 100%; margin-top: 16px;">
                            <v-spacer></v-spacer>
                            <v-col cols="auto" class="text-left">
                                <img :src="boxDetail.logo" alt="Box Logo" class="rounded-logo" />
                            </v-col>
                            <v-col cols="auto" style="flex-grow: 1; text-align: center;">
                                <span class="boxname">
                                    {{ boxDetail.name }}
                                </span>
                            </v-col>
                            <v-spacer></v-spacer>
                        </v-row>
                        <br><br>
                        <v-card-text>
                            <v-table style="table-layout: fixed; width: 100%; overflow: hidden;">
                                <tbody>
                                    <tr v-for="(item, index) in boxDetailList" :key="index">
                                        <td class="title" style="padding-left: 20px;">{{ item.key }} <br><br><br><br> </td>


                                        <td>
                                            <!-- 문자열 처리 -->
                                            <template v-if="item.key !== 'LOGO'">
                                                <div class="text" v-html="formatText(item.value)">
                                                </div>
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
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
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
            console.error("상자 세부정보를 가져오는 중 오류 발생:", error);
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
@import url('https://fonts.googleapis.com/css2?family=Rubik+Mono+One&display=swap');


.title {
    font-family: 'Rubik Mono One', sans-serif;
    font-size: 30px;
    height: 100%;
    position: relative;
    left: 100px; 
}

.text {
    font-size: 25px; /* 글씨 크기 설정 */
    display: flex; /* 플렉스 박스를 사용하여 정렬 */
    align-items: flex-start; /* 위쪽으로 정렬 */
    justify-content: flex-start; /* 왼쪽으로 정렬 */
    height: 90%; /* 셀 높이를 채움 */
    word-wrap: break-word; /* 긴 단어가 줄바꿈되도록 설정 */
    white-space: normal; /* 줄바꿈을 허용하도록 설정 */
    position: relative;
    left: 100px; 
    
}

.container {
    background-color: #D9D9D9;
    min-height: 100vh;
    
}

.rounded-logo {
    max-width: 150px;
    max-height: 150px;
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
    white-space: pre-wrap;
    /* 줄바꿈 유지 */
    padding-top: 10px;
    /* 항목 간 간격 조정 */
    padding-bottom: 10px;
}

.v-table tbody tr+tr {
    padding-top: 20px;
    /* 각 섹션 사이의 간격 조정 */
}

.box {
    background-color: #797876;
}

.right-align {
    text-align: right;
}

.boxLocation {
    color: white;
    font-weight: 1000;
    font-size: 70px;
    font-family: "Oswald", sans-serif;
}

.boxname {
    color: black;
    font-weight: 1000;
    font-size: 70px;
    font-family: "Oswald", sans-serif;
}

.card-actions {
    display: flex;
    justify-content: center; /* 버튼을 중앙에 배치 */
    gap: 20px; /* 버튼 간의 간격을 15px로 설정 */
}

.custom-btn {
    background-color: #D9D9D9; /* 버튼 배경색 설정 */
    color: black; /* 버튼 텍스트 색상 설정 */
    border: none; /* 버튼의 기본 테두리 제거 */
}

.grid-cell {
    display: grid;
    justify-content: start; /* 시작점으로 정렬 */
    padding-left: 200px; /* 오른쪽으로 이동시키기 위해 padding 추가 */
}

.page-container {
    overflow-x: hidden; /* 페이지 전체의 수평 스크롤 숨기기 */
}

.v-table {
    width: 100%;
    table-layout: fixed;
    overflow-x: auto; /* 수평 스크롤이 필요할 때만 표시 */
}
</style>