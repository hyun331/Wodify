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
</style>
