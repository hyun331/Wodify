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
                <v-col cols="6" class="justify-center align-center bordered">
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
                        <td>{{detail.name}}</td>
                        <td>{{detail.contents}}</td>
                        </tr>
                        </tbody>
                        </v-table>
                  </v-col>
            </v-row>
            <v-row>
                <v-col cols="12">

                    <v-select
                    v-model="time"
                    :items="timeOptions"
                    item-title="text"  
                    item-value="value"
                    label="시간 선택"
                    class="mx-2"
                    ></v-select>
                </v-col>
            </v-row>
            <v-row class="d-flex justify-center align-center">
                <RoundedButtonComponent
                text="Book Now"
                buttonType="submit"
                />
            </v-row>
        </v-form>
    </v-container>
    </div>
</template>

<script>
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
import axios from 'axios';
export default {    
    components:{
        RoundedButtonComponent
    },
    data() {
        return {
            menu: false,
            date:"",
            wod:"와드 내용",
            time:"",
            timeOptions: [], // 시간 옵션을 여기에 저장
        }
    },
    watch: {
        date(newDate) {
            if (newDate) {
                this.fetchWod(newDate);
                this.fetchTimeOptions(newDate);
            }
        }
    },
    methods: {
        async fetchWod (){
            try {
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find/`+this.date);
                console.log(response);
                this.wod = response.data.result;
            } catch (error) {
                console.log(error);
            }
            

        },
        async fetchTimeOptions() {
            console.log(this.date);
            const dateData = {date:this.date};
            try {
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/reservation/box/list/`,dateData);
                console.log(response.data.result.content);
                this.timeOptions = response.data.result.content.map(item => {
                    return { text: item.time, value: item.id };
                });
                console.log(this.timeOptions)
            } catch (error) {
                console.log(error);
            }
        },
        async reservation() {
            const reservationData = {
                reservationId: this.time // Use the selected value from the v-select
            };
            
            try {
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/create`, reservationData);
                console.log(response.data.result.content);
                this.timeOptions = response.data.result.content.map(item => {
                    return { text: item.time, value: item.id };
                });
                console.log(this.timeOptions);
            } catch (error) {
                console.log(error);
            }
            
            alert("예약 완료 !! Reservation ID: " + reservationData.reservationId);
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
    font-weight:1000;
    font-size: 70px;
    font-family: "Oswald", sans-serif;
}
.page-container {
    /* 전체 페이지의 배경색을 설정합니다 */
    background-color: #D9D9D9; /* 원하는 배경색으로 변경 */
    min-height: 100vh; /* 전체 화면 높이로 설정 */
  }
.reservationHead {
    font-weight: bold;
    font-size: 20px;
}
.flex-between {
    display: flex;
    justify-content: space-between; /* Aligns items at the two ends of the line */
}

.padded {
    padding: 0 10px; /* Adds padding to the left and right sides */
}

.flex-between span:first-child {
    text-align: left;
    margin-right: 10px; /* Adds space between the label and the value */
}

.flex-between span:last-child {
    text-align: right;
    margin-left: 10px; /* Adds space between the value and the label */
}
.bordered {
    border: 1px solid #ccc; /* 테두리 스타일 */
    padding: 10px; /* 여백 추가 */
  }
</style>
