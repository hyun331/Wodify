<template>
    <div class="page-container">
        <div class="box right-align">
            <br>
            <span class="boxLocation">
                {{ this.boxName }}
            </span>
            <br>
        </div>

        <v-container>
            <br>
            <v-row class="reservationHead">
                <v-col cols="6" class="d-flex justify-center align-center">
                    DATE
                </v-col>
                <v-col cols="6" class="d-flex justify-center align-center">
                    WOD
                </v-col>
            </v-row>
            <v-form @submit.prevent="reservation">
                <v-row>
                    <v-col cols="6" class="d-flex justify-center align-center">
                        <div class="date-picker-container">
                            <v-date-picker v-model="selectedDate" @update:model-value="onDateSelected"
                                class="custom-date-picker">
                                <template v-slot:header></template>
                            </v-date-picker>
                        </div>
                    </v-col>
                    <v-col cols="6" class="justify-center align-center">
                        <div v-if="wod && wod.wodDetResDtoList.length > 0" class="bordered wod">
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

                        <div v-else class="d-flex justify-center wod">
                            <v-btn :to="{ path: '/wod/select-date' }" class="freesentation">
                                wod 생성
                            </v-btn>
                        </div>
                    </v-col>
                </v-row>

                <v-row v-for="(entry, index) in entries" :key="index">
                    <v-col cols="6">
                        <v-text-field label="Time" type="time" v-model="entry.time" placeholder="시간을 입력해주세요" required>
                        </v-text-field>
                    </v-col>
                    <v-col cols="5">
                        <v-text-field label="People" v-model="entry.people" placeholder="인원을 입력해주세요" required>
                        </v-text-field>
                    </v-col>
                    <v-col cols="1">
                        <v-btn @click="removeEntry(index)" icon>
                            -
                        </v-btn>
                    </v-col>
                </v-row>
                <v-row class="d-flex justify-center align-center">
                    <RoundedButtonComponent text="Add Time Slot" buttonType="button" @click="addEntry" class="mx-2" />
                    <RoundedButtonComponent text="Book Create" buttonType="submit" class="mx-2" />
                </v-row>

            </v-form>

            <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
                :dialogText="dialogText" />
        </v-container>
    </div>
</template>

<script>
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
import AlertModalComponent from '@/components/AlertModalComponent.vue';
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
            let errorMessage = "";
            if (error.response && error.response.data) {
                // 서버에서 반환한 에러 메시지가 있는 경우
                errorMessage += `: ${error.response.data.error_message}`;
            } else if (error.message) {
                errorMessage += `: ${error.message}`;
            }
            this.dialogTitle = "박스명 로드 실패";
            this.dialogText = errorMessage;
            this.alertModal = true;
        }
    },
    data() {
        return {
            selectedDate: new Date(),
            formattedDate: "",
            date: "",
            wod: "",
            error: false,
            entries: [
                { time: "", people: "" }
            ],
            alertModal: false,
            dialogTitle: "",
            dialogText: "",
            boxName: "",
        };
    },
    watch: {
        date(newDate) {
            if (newDate) {
                this.fetchWod(newDate);
            }
        },
        selectedDate(selectDate) {
            if (selectDate) {
                this.formattedDate = this.formatDate(selectDate);
                this.fetchWod(this.formattedDate);
            }
        }
    },
    methods: {
        formatDate(date) {
            const year = date.getFullYear();
            const month = ('0' + (date.getMonth() + 1)).slice(-2);
            const day = ('0' + date.getDate()).slice(-2);
            return `${year}-${month}-${day}`;
        },
        async fetchWod(date) {
            try {
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find/${date}`);
                this.wod = response.data.result;
                if (!this.wod) {
                    throw new Error("No data found");
                }
            } catch (error) {
                if (error.response && error.response.status === 404) {
                    this.error = true; // 에러 상태를 true로 설정하여 WodSave 컴포넌트를 표시합니다.
                    this.wod = "";
                } else {
                    this.errorMessage = 'WOD 데이터를 불러오는 중 오류가 발생했습니다.';
                    console.error('Error fetching WOD data:', error);
                }
            }
        },
        addEntry() {
            this.entries.push({ time: "", people: "" });
        },
        removeEntry(index) {
            this.entries.splice(index, 1);
        },
        async reservation() {
            if (!this.formattedDate) {
                this.dialogTitle = "입력사항을 모두 입력해주세요";
                this.dialogText = "입력사항을 모두 입력해주세요";
                this.alertModal = true;
                return;
            }

            const reservationData = this.entries.map(entry => ({
                date: this.formattedDate,
                wodId: this.wod.id,
                time: entry.time,
                maximumPeople: entry.people
            }));

            alert(JSON.stringify(reservationData, null, 2));
            await axios.post(`${process.env.VUE_APP_API_BASE_URL}/reservation/create`, reservationData)
                .then(response => {
                    console.log(response);
                    alert("예약이 완료되었습니다!");
                    this.$router.push("/reservation/list");
                }).catch(error => {
                    let errorMessage = "";
                    if (error.response && error.response.data) {
                        // 서버에서 반환한 에러 메시지가 있는 경우
                        errorMessage += `: ${error.response.data.error_message}`;
                    } else if (error.message) {
                        errorMessage += `: ${error.message}`;
                    }
                    this.dialogTitle = "예약 실패";
                    this.dialogText = errorMessage;
                    this.alertModal = true;
                });
        }
    }
}
</script>

<style scoped>
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

.page-container {
    /* 전체 페이지의 배경색을 설정합니다 */
    background-color: #D9D9D9;
    /* 원하는 배경색으로 변경 */
    min-height: 100vh;
    /* 전체 화면 높이로 설정 */
}

.reservationHead {
    font-weight: bold;
    font-size: 20px;
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
}
</style>
