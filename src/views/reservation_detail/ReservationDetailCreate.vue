<template>
    <div>
        <div class="box right-align">
            <br>
            <span class="boxLocation">
                {{ this.boxName }}
            </span>
            <br>
        </div>

        <v-container>
            <br>
            <v-row class="reservationHead" justify="center">
                <v-col cols="5" class="d-flex justify-center align-center">
                    DATE
                </v-col>
                <v-col cols="5" class="d-flex justify-center align-center">
                    WOD
                </v-col>
            </v-row>
            <v-form @submit.prevent="reservation">
                <v-row class="d-flex justify-center">
                    <v-col cols="5" class="d-flex justify-center align-center">
                        <div class="date-picker-container">
                            <v-date-picker v-model="selectedDate" @update:model-value="onDateSelected"
                                style="width: 400px;" class="custom-date-picker">
                                <template v-slot:header></template>
                            </v-date-picker>
                        </div>
                    </v-col>
                    <v-col cols="5" class="justify-center align-center">
                        <div class="bordered wod">
                            <div class="flex-between padded">
                                <span>date</span>
                                <span>{{ wod.date }}</span>
                            </div>
                            <div class="flex-between padded">
                                <span>timeCap</span>
                                <span>{{ wod.timeCap }}</span>
                            </div>
                            <div class="flex-between padded">
                                <span>rounds</span>
                                <span>{{ wod.rounds }}</span>
                            </div>
                            <div class="wod-info-container">{{ wod.info }}</div>
                            <v-table>
                                <tbody style="background-color: #D9D9D9;">
                                    <tr v-for="detail in wod.wodDetResDtoList" :key="detail.id">
                                        <td>{{ detail.name }}</td>
                                        <td>{{ detail.contents }}</td>
                                    </tr>
                                </tbody>
                            </v-table>
                        </div>
                    </v-col>
                </v-row>
                <v-row class="d-flex justify-center align-center">
                    <v-col cols="9">
                        <v-select v-model="time" :items="timeOptions" item-title="text" item-value="value" label="시간 선택"
                            class="mx-2"></v-select>
                    </v-col>
                </v-row>
                <v-row class="d-flex justify-center align-center">
                    <RoundedButtonComponent text="Book Now" buttonType="submit" />
                </v-row>
            </v-form>
            <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
                :dialogText="dialogText" />

            <v-dialog v-model="waitingModal" max-width="400px">
                <v-card>
                    <v-card-title class="headline">{{ dialogTitle }}</v-card-title>
                    <v-card-text>{{ dialogText }}</v-card-text>
                    <v-card-actions>
                        <v-btn class="hover-btn" text @click="waiting">예</v-btn>
                        <v-btn class="hover-btn" text @click="waitingModal = false">아니오</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </v-container>
    </div>
</template>

<script>
import AlertModalComponent from '@/components/AlertModalComponent.vue';
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
import { KAKAO_LOGIN_URL } from '@/router/KakaoLoginUrl';
import axios from 'axios';
export default {
    components: {
        RoundedButtonComponent,
        AlertModalComponent
    },
    async created() {
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/box/name/`);
            this.boxName = response.data.result;
        } catch (error) {
            if (error.response && error.response.data) {
                if(error.response.status === 403) {
                    this.dialogTitle = "로그인이 필요합니다";
                    this.dialogText = "";
                    this.alertModal = true;
                    this.kakaoLogin();
                } else {
                    this.dialogTitle = "박스명 로드 실패";
                    this.dialogText = "등록된 박스가 존재하지 않습니다";
                    this.alertModal = true;
                }
                // 서버에서 반환한 에러 메시지가 있는 경우
            }
        }
    },
    data() {
        return {
            selectedDate: new Date(),
            formattedDate: "",
            menu: false,
            date: "",
            wod: "와드 내용",
            time: "",
            timeOptions: [], // 시간 옵션을 여기에 저장
            alertModal: false,
            dialogTitle: "",
            dialogText: "",
            waitingModal: false,
            boxName: "",
        }
    },
    watch: {
        date(newDate) {
            if (newDate) {
                this.fetchWod(newDate);
                this.fetchTimeOptions(newDate);
            }
        },
        selectedDate(selectDate) {
            if (selectDate) {
                this.formattedDate = this.formatDate(selectDate);
                this.fetchWod(this.formattedDate);
                this.fetchTimeOptions(this.formattedDate);
            }
        }
    },
    methods: {
        kakaoLogin() {
            window.location.href = KAKAO_LOGIN_URL;
        },
        formatDate(date) {
            const year = date.getFullYear();
            const month = ('0' + (date.getMonth() + 1)).slice(-2);
            const day = ('0' + date.getDate()).slice(-2);
            return `${year}-${month}-${day}`;
        },
        async fetchWod(date) {
            try {
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find/${date}`);
                console.log(response);
                this.wod = response.data.result;
            } catch (error) {
                let errorMessage = "";
                if (error.response && error.response.data) {
                    // 서버에서 반환한 에러 메시지가 있는 경우
                    errorMessage += `: ${error.response.data.error_message}`;
                } else if (error.message) {
                    errorMessage += `: ${error.message}`;
                }
                this.dialogTitle = "와드 로드 실패";
                this.dialogText = errorMessage;
                this.alertModal = true;
                console.log(error);
            }


        },
        async fetchTimeOptions() {
            const dateData = { date: this.formattedDate };
            try {
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/reservation/box/list/`, dateData);
                console.log(response.data.result.content);
                this.timeOptions = response.data.result.content.map(item => {
                    return { text: item.time, value: item.id };
                });
                console.log(this.timeOptions)
            } catch (error) {
                let errorMessage = "";
                if (error.response && error.response.data) {
                    // 서버에서 반환한 에러 메시지가 있는 경우
                    errorMessage += `: ${error.response.data.error_message}`;
                } else if (error.message) {
                    errorMessage += `: ${error.message}`;
                }
                this.dialogTitle = "예약 시간 로드 실패";
                this.dialogText = errorMessage;
                this.alertModal = true;
                console.log(error);
            }
        },
        async reservation() {

            if (!this.formattedDate || !this.time) {
                this.dialogTitle = "입력사항을 모두 입력해주세요";
                this.dialogText = "입력사항을 모두 입력해주세요";
                this.alertModal = true;
                return;
            }

            const reservationData = {
                reservationId: this.time // Use the selected value from the v-select
            };

            await axios.post(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/create`, reservationData)
                .then(response => {
                    console.log(response.data.result.content);

                    this.dialogTitle = "예약이 완료되었습니다";
                    this.dialogText = "";
                    this.alertModal = true;
                    this.$watch(
                        () => this.alertModal,
                        (newVal) => {
                            if (!newVal) {
                                this.$router.push("/reservation-detail/list");
                            }
                        }
                    );
                }).catch(error => {
                    if (error.response && error.response.status === 409) {
                        this.dialogTitle = "예약 인원이 초과하였습니다.";
                        this.dialogText = "대기하시겠습니까?";
                        this.waitingModal = true;
                    } else {

                        let errorMessage = "";
                        if (error.response.data) {
                            // 서버에서 반환한 에러 메시지가 있는 경우
                            errorMessage += `: ${error.response.data.error_message}`;
                        } else if (error.message) {
                            errorMessage += `: ${error.message}`;
                        }
                        this.dialogTitle = "예약이 정상적으로 처리되지 않았습니다";
                        this.dialogText = errorMessage;
                        this.alertModal = true;
                    }
                    console.log(error);
                });

        },
        async waiting() {
            console.log(this.time);

            try {
                await axios.post(`${process.env.VUE_APP_API_BASE_URL}/waiting/add`, { reservationId: this.time });
                this.dialogTitle = "대기자 명단에 등록되었습니다.";
                this.dialogText = "예약이 취소되면 알림을 받게 됩니다.";
                this.waitingModal = false;
                this.alertModal = true;
            } catch (error) {
                this.waitingModal = false;
                let errorMessage = "";
                if (error.response && error.response.data) {
                    // 서버에서 반환한 에러 메시지가 있는 경우
                    errorMessage += `: ${error.response.data.error_message}`;
                } else if (error.message) {
                    errorMessage += `: ${error.message}`;
                }
                this.dialogTitle = "대기자 명단 등록 실패";
                this.dialogText = errorMessage;
                this.alertModal = true;
                console.log(error);

            }
        }
    }
}
</script>

<style scoped>
::v-deep .v-field__input {
    background-color: rgba(255, 255, 255, 0.4);
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


.reservationHead {
    font-weight: bold;
    font-size: 20px;
}

.flex-between {
    display: flex;
    justify-content: space-between;
    /* Aligns items at the two ends of the line */
}

.padded {
    padding: 0 10px;
    /* Adds padding to the left and right sides */
}

.flex-between span:first-child {
    text-align: left;
    margin-right: 10px;
    /* Adds space between the label and the value */
}

.flex-between span:last-child {
    text-align: right;
    margin-left: 10px;
    /* Adds space between the value and the label */
}

.wod-info-container {
    margin: 10px;
    text-align: center;
    padding: 10px;
    border-radius: 40px;
    background-color: #f8f8f8;
}

.custom-date-picker {
    background-color: rgba(255, 255, 255, 0.5);
    margin-top: 20px;
}

.wod {
    margin-top: 20px;
    background-color: #D9D9D9;
    border-radius: 5px;
    padding: 10px;
}
</style>
