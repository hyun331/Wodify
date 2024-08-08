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
                    {{ wod }}
                </v-col>
            </v-row>
            <v-row>
                <v-col cols="12">
                    <v-select
                    v-model="time"
                    :items="timeOptions"
                    label="시간 선택"
                    class="mx-2"
                ></v-select>
                </v-col>
            </v-row>
            <v-row class="d-flex justify-center align-center">
                <button class="rounded-button" type="submit">
                    <i class="fas fa-map-marker-alt"></i>
                    <span class="button-text">Book Now</span>
                  </button>
            </v-row>
        </v-form>
        
        
    </v-container>
    </div>
</template>

<script>
export default {
    data() {
        return {
            date:"",
            wod:"와드 내용",
            time:"",
            timeOptions: [] // 시간 옵션을 여기에 저장
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
        fetchWod (date){
            this.wod = `${date} 와드`;
        },
        fetchTimeOptions(date) {
            this.timeOptions = [2,3,4,5,date];
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
.rounded-btn {
    border-radius: 50px;
    padding: 10px 20px;
}
.rounded-button {
    background-color: #000000; /* 버튼 배경색 */
    border: none;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    border-radius: 50px; /* 둥근 정도 조절 */
    cursor: pointer;
}
.button-text {
    /* 텍스트 스타일 */
    color: white; /* 텍스트 색상 */
    font-size: 16px;
    /* 필요한 다른 스타일 추가 */
  }
  .rounded-button:hover {
    border: 2px solid white; /* 버튼에 흰색 테두리 추가 */
    background-color: rgb(48, 48, 48); /* 버튼 배경색을 조금 어두운 색으로 변경 */
    outline: none; /* 기본 포커스 아웃라인 제거 */
}
</style>
