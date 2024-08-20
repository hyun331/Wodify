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
                    </v-card>
                </v-col>
            </v-row>
        </v-container>
        <OrderListComponent :isAdmin="false" />
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
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/box/detail/${this.boxDetailId}`);
                this.boxDetail = response.data.result;
                this.boxDetailList = [
                    { key: "ADDRESS", value: this.boxDetail.address },
                    { key: "TIME", value: this.boxDetail.operatingHours },
                    { key: "PRICE", value: this.boxDetail.fee },
                    { key: "INTRO", value: this.boxDetail.intro },
                ];
            } catch (error) {
                console.error("상자 세부정보를 가져오는 중 오류 발생:", error);
            }
        },
        methods: {
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
    overflow-x: hidden; /* 페이지 전체의 수평 스크롤 숨기기 */
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
    min-height: 500px; /* 원하는 최소 높이 설정 */
    display: flex;
    flex-direction: column;
    overflow: hidden; /* 내부 스크롤 숨기기 */
}

.v-table {
    width: 100%;
    table-layout: fixed;
    overflow-x: auto; /* 수평 스크롤이 필요할 때만 표시 */
}

.title {
    font-family: 'Rubik Mono One', sans-serif;
    font-size: 30px;
    height: 100%;
    position: relative;
    left: 10px; 
}

.text {
    font-size: 25px; /* 글씨 크기 설정 */
    display: flex; /* 플렉스 박스를 사용하여 정렬 */
    align-items: flex-start; /* 위쪽으로 정렬 */
    justify-content: flex-start; /* 왼쪽으로 정렬 */
    height: auto; /* 셀 높이를 자동으로 조정 */
    word-wrap: break-word; /* 긴 단어가 줄바꿈되도록 설정 */
    white-space: normal; /* 줄바꿈을 허용하도록 설정 */
    margin-bottom: 20px; /* 항목 간의 간격 조정 */
}

.data-value {
    white-space: pre-wrap; /* 줄바꿈 유지 */
    padding-top: 60px; /* 항목 간 간격 조정 */
    padding-bottom: 60px;
}

.v-table {
    width: 100%; /* 테이블이 부모 요소의 전체 너비를 차지하도록 설정 */
    table-layout: auto; /* 테이블 열 너비를 자동 조정 */
}

.v-table tbody tr+tr {
    padding-top: 50px; /* 각 섹션 사이의 간격 조정 */
}

.dynamic-card {
    min-height: 200px; /* 기본 카드 높이 설정 */
    display: flex;
    flex-direction: column; /* 내용에 따라 카드가 늘어나도록 설정 */
    overflow: hidden; /* 카드 내용이 넘치지 않도록 설정 */
}

.v-card-text {
    flex: 1; /* 카드 내용이 카드의 남은 공간을 차지하도록 설정 */
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
</style>
