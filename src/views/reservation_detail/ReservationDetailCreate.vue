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
                <v-col cols="6" class="d-flex justify-center align-center">
                    {{ wod.date }}
                    {{ wod.timeCap }}
                    {{ wod.rounds }}
                    {{ wod.info }}
                    <v-table>
                        <thead><tr><th>name</th><th>contents</th></tr></thead>
                        <tbody>
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
                        item-text="text"  
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
        <v-date-picker v-model="vdate" @input="menu = false" :max="maxDate"></v-date-picker>
        
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
            vdate: null,
            date:"",
            wod:"와드 내용",
            time:"",
            timeOptions: [], // 시간 옵션을 여기에 저장
            token:"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MjMxOTU1NTAsImV4cCI6MTcyMzIxMzU1MH0.hdvFCt9lzy0UjjaTc0QukWubTUZUYd2ko0o_EsPRn-E",
            buttonTest: "test\nbutton"
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
        async fetchWod (date){
            const dateData = {date:this.date}
            console.log(date)
            try {
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find`, {
                    params: dateData,
                    headers: { 
                        Authorization: `Bearer ${this.token}`
                    }
                });
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
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/reservation/box/list/`, 
                    dateData,
                    {
                        headers: { 
                            Authorization: `Bearer ${this.token}`
                        }
                    }
                );
                console.log(response.data.result.content);
                this.timeOptions = response.data.result.content.map(item => {
                    return { text: item.time, value: item.id };
                });
                console.log(this.timeOptions)
            } catch (error) {
                console.log(error);
            }
        },
        reservation() {
            const reservationData = {
                date:this.date,
                time:this.time
            }
            alert("예약 완료 !!" +reservationData.date);
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
</style>
