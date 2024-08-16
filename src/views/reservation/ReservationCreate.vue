<template>
    <div class="page-container">
        <div class="box right-align">
            <br>
            <span class="boxLocation">
                CROSSFIT RICH NANGOK
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
                        <v-text-field
                            v-model="date"
                            label="날짜 입력"
                            type="date"
                            class="mx-2"
                        ></v-text-field>
                    </v-col>
                    <v-col cols="6" class="justify-center align-center">
                        <div v-if="wod && wod.wodDetResDtoList.length > 0" class="bordered">
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
                                <tbody>
                                    <tr v-for="detail in wod.wodDetResDtoList" :key="detail.id">
                                        <td>{{ detail.name }}</td>
                                        <td>{{ detail.contents }}</td>
                                    </tr>
                                </tbody>
                            </v-table>
                        </div>
                    
                        <div v-else class="d-flex justify-center">
                            <v-btn :to="{ path: '/wod/select-date' }">
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
        </v-container>
    </div>
</template>

<script>
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
import axios from 'axios';

export default {
    components: {
        RoundedButtonComponent
    },
    data() {
        return {
            date: "",
            wod: "",
            error: false,
            entries: [
                { time: "", people: "" }
            ],
        };
    },
    watch: {
        date(newDate) {
            if (newDate) {
                this.fetchWod(newDate);
            }
        }
    },
    methods: {
        async fetchWod() {
            try {
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find/`+this.date);
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
        reservation() {
            const reservationData = this.entries.map(entry => ({
                date: this.date,
                wodId: this.wod.id,
                time: entry.time,
                maximumPeople: entry.people
            }));

            alert(JSON.stringify(reservationData, null, 2));
            axios.post(`${process.env.VUE_APP_API_BASE_URL}/reservation/create`, reservationData)
            .then(response => {
                console.log(response);
                alert("예약이 완료되었습니다!");
                this.$router.push("/reservation/list");
            }).catch(error => {
                console.error("예약 실패", error);
            });
        }
    }
}
</script>

<style>
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
</style>
