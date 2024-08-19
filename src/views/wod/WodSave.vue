<template>
    <v-container style="min-width: 345px; min-height: 600px; max-width: 800px;">
        <v-row class="rubikMonoOne" style="font-size: 50px; margin-left: 3px">
            <v-col class="auto-width" style="padding: 0; margin-right: 15px;">
                wod
            </v-col>
            <v-col class="auto-width" style="padding: 0;">
                save
            </v-col>
        </v-row>

        <!-- 입력 필드들 -->
        <v-row style="margin:1px">
            <v-text-field 
            class="custom-text-box" 
            style="margin-right:2px" 
            v-model="wodSaveReqDto.date" 
            label="Date"
            placeholder="yyyy-mm-dd" 
            readonly></v-text-field>
            <!-- TimeCap 입력 -->
            <v-text-field 
            class="custom-text-box" 
            style="margin-right:2px" 
            v-model="wodSaveReqDto.timeCap"
            label="TimeCap" 
            placeholder="HH:mm:ss" 
            @click="timePickerVisible = true" 
            readonly></v-text-field>
            <!-- TimeCap 모달 -->
            <v-dialog v-model="timePickerVisible" persistent width="400px">
                <v-card class="dark-card">
                    <v-card-text class="time-picker-content">
                        <v-row>
                            <v-col>
                                <v-select v-model="selectedHour" :items="hours" label="HH"></v-select>
                            </v-col>
                            <v-col>
                                <v-select v-model="selectedMinute" :items="minutes" label="MM"></v-select>
                            </v-col>
                            <v-col>
                                <v-select v-model="selectedSecond" :items="seconds" label="SS"></v-select>
                            </v-col>
                        </v-row>
                    </v-card-text>
                    <v-card-actions class="time-picker-actions">
                        <v-spacer></v-spacer>
                        <v-btn text @click="confirmTime" class="bold-button">OK</v-btn>
                        <v-btn text @click="timePickerVisible = false" class="bold-button">Cancel</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>

            <!-- Rounds 입력 -->
            <v-text-field 
            class="custom-text-box" 
            v-model="wodSaveReqDto.rounds" 
            label="Rounds"
            placeholder="Select rounds" 
            @click="roundsPickerVisible = true" 
            readonly></v-text-field>

            <!-- Rounds 모달 -->
            <v-dialog v-model="roundsPickerVisible" persistent width="180px">
                <v-card class="dark-card">
                    <v-card-text class="time-picker-content">
                        <v-select v-model="selectedRounds" :items="roundsOptions" label="Rounds"></v-select>
                    </v-card-text>
                    <v-card-actions class="time-picker-actions">
                        <v-spacer></v-spacer>
                        <v-btn text @click="confirmRounds" class="bold-button">OK</v-btn>
                        <v-btn text @click="roundsPickerVisible = false" class="bold-button">Cancel</v-btn>
                    </v-card-actions>
                </v-card>
            </v-dialog>
        </v-row>
        <v-row style="margin:1px">
            <v-textarea 
            class="custom-text-box" 
            v-model="wodSaveReqDto.info" 
            label="Info" 
            placeholder="여기에 설명을 입력하세요..."
            auto-grow
            :rows="2"
            outlined></v-textarea>
        </v-row>
        <!-- 동적 입력 필드 -->
        <div v-for="(wodDetSaveReqDto, index) in wodSaveReqDto.wodDetSaveReqDtoList" :key="index" class="exercise-group" outlined>
            <!-- X 버튼 -->
            <v-btn icon @click="removeWodDet(index)" class="remove-btn">
                <v-icon>mdi-close</v-icon>
            </v-btn>
            <v-text-field 
            class="exercise-box" 
            style="border-radius: 4px 4px 0 0;" 
            v-model="wodDetSaveReqDto.name"
            :label="`Name ${index + 1}`" 
            placeholder="Enter exercise name"></v-text-field>
            <v-text-field 
            class="exercise-box" 
            style="border-radius: 0 0 4px 4px;" 
            v-model="wodDetSaveReqDto.contents"
            :label="`Contents ${index + 1}`" 
            placeholder="Enter exercise contents"></v-text-field>
        </div>

        <!-- 버튼들 -->
        <div style="display: flex; justify-content: flex-end;">
            <v-btn @click="addRandomWodDet" color="black" style="margin-right: 2px;">랜덤+</v-btn>
            <v-btn @click="addWodDet" color="black" style="margin-right: 2px;">종목+</v-btn>
            <v-btn @click="submitForm" color="black">등록</v-btn>
        </div>
    </v-container>
</template>

<script>
import axios from "axios";

export default {
    props: ["date"], // 라우트에서 전달받은 date를 props로 받음
    data() {
        return {
            count: 0,
            timePickerVisible: false,
            roundsPickerVisible: false,
            selectedHour: '00',
            selectedMinute: '00',
            selectedSecond: '00',
            selectedRounds: 0,
            hours: Array.from({ length: 24 }, (_, i) => String(i).padStart(2, '0')), // 00 ~ 23
            minutes: Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0')), // 00 ~ 59
            seconds: Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0')), // 00 ~ 59
            roundsOptions: Array.from({ length: 20 }, (_, i) => (i + 1).toString()), // 1 ~ 20
            wodSaveReqDto: {
                date: this.date || "", // props로 받은 date를 사용
                timeCap: null,
                rounds: null,
                info: '',
                wodDetSaveReqDtoList: [],
            },
        };
    },
    methods: {
        confirmTime() {
            if (this.selectedHour !== null && this.selectedMinute !== null && this.selectedSecond !== null) {
                this.wodSaveReqDto.timeCap = `${this.selectedHour}:${this.selectedMinute}:${this.selectedSecond}`;
            }
            this.timePickerVisible = false;
        },
        confirmRounds() {
            if (this.selectedRounds !== null) {
                this.wodSaveReqDto.rounds = this.selectedRounds;
            }
            this.roundsPickerVisible = false;
        },
        async addWodDet() {
            this.count++;
            this.wodSaveReqDto.wodDetSaveReqDtoList.push({
                name: `종목명${this.count}`,
                contents: `종목내용${this.count}`,
            });
        },
        removeWodDet(index) {
            // 특정 인덱스의 항목을 제거
            this.wodSaveReqDto.wodDetSaveReqDtoList.splice(index, 1);
            this.count--; // count 감소
        },
        async addRandomWodDet() {
            try {
                // 1. repository의 데이터 갯수 가져오기
                const countResponse = await axios.get("http://localhost:8090/wod/count");
                const size = countResponse.data.result;
                console.log(size);

                // 2. 난수 생성 및 id 계산
                let randomIndex = Math.floor(Math.random() * size);
                if (randomIndex === 0) randomIndex = size;

                // 3. 해당 id의 데이터 가져오기
                const randomDataResponse = await axios.get(`http://localhost:8090/wod/random/${randomIndex}`);
                console.log(randomDataResponse.data.result);
                const { name, contents } = randomDataResponse.data.result;

                // 4. 가져온 데이터 화면에 추가
                this.wodSaveReqDto.wodDetSaveReqDtoList.push({
                    name,
                    contents,
                });
            } catch (error) {
                console.error("Error fetching random WOD data:", error);
            }
        },
        async submitForm() {
            try {
                const response = await axios.post(
                    "http://localhost:8090/wod/save",
                    this.wodSaveReqDto
                );
                if (response.status === 201) {
                    alert("WOD가 성공적으로 등록되었습니다.");
                    this.$emit('wod-saved', this.date);
                } else {
                    alert("WOD 등록에 실패했습니다.");
                }
            } catch (error) {
                console.error(
                    "Error submitting WOD form:",
                    error.response ? error.response.data : error.message
                );
                alert("WOD 등록 중 오류가 발생했습니다.");
            }
        },
    },
};
</script>

<style scoped>
.auto-width {
    flex: 0 1 auto;
    width: auto;
    display: inline-block;
}

.custom-text-box {
    background-color: rgba(255, 255, 255, 0.5);
    border-radius: 4px;
    margin-bottom: 2px;
}

.time-picker-content {
    padding: 0px;
    /* 전체 패딩을 줄여 간격을 최적화 */
    margin-left: -10px;
    margin-right: -10px;
    margin-bottom: -15px;
}

.time-picker-actions {
    padding-top: 0;
    /* 버튼 영역의 상단 간격을 줄임 */
    padding-bottom: 0px;
    /* 버튼 영역의 하단 간격을 줄임 */
    margin-top: -30px;
    /* 위와 아래 간격을 줄여 버튼과 콘텐츠 간격 최소화 */
    margin-right: 7px;
}

/* 어두운 배경색을 적용한 카드 */
.dark-card {
    background-color: #333;
    /* 다크 그레이 배경 */
    color: white;
    /* 텍스트 색상을 밝게 설정 */
}

/* 버튼의 글씨를 두껍게 만듦 */
.bold-button {
    font-weight: bold;
}

.exercise-group {
    background-color: rgba(255, 255, 255, 0.2);
    border-radius: 9px;
    margin: 2px;
    margin-bottom: 4px;
    position: relative;
}

.exercise-box {
    background-color: rgba(255, 255, 255, 0.5);
}

.remove-btn {
    position: absolute;
    top: 3px;
    right: 3px;
    background-color: transparent;
    color: #000000;
    z-index: 10;
}
</style>
