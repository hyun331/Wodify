<template>
    <v-container style="min-width: 345px; min-height: 600px;">
        <!-- 헤더 부분 -->
        <v-row class="rubikMonoOne" style="font-size: 50px; margin-left: 3px">
            <v-col class="auto-width" style="padding: 0; margin-right: 15px;">
                wod
            </v-col>
            <v-col class="auto-width" style="padding: 0;">
                save
            </v-col>
        </v-row>

        <!-- 입력 필드들 -->
        <v-text-field class="custom-text-box" v-model="wodSaveReqDto.date" label="Date" placeholder="yyyy-mm-dd"
            readonly></v-text-field>
        <v-text-field class="custom-text-box" v-model="wodSaveReqDto.timeCap" label="TimeCap"
            placeholder="HH:mm:ss"></v-text-field>
        <v-text-field class="custom-text-box" v-model="wodSaveReqDto.rounds" label="Rounds"
            placeholder="number"></v-text-field>
        <v-textarea class="custom-text-box" v-model="wodSaveReqDto.info" label="Info" placeholder="여기에 설명을 입력하세요..."
            rows="5" outlined></v-textarea>

        <!-- 동적 입력 필드 -->
        <div v-for="(wodDetSaveReqDto, index) in wodSaveReqDto.wodDetSaveReqDtoList" :key="index">
            <v-text-field class="custom-text-box" v-model="wodDetSaveReqDto.name" :label="`Name ${index + 1}`"
                placeholder="Enter exercise name"></v-text-field>
            <v-text-field class="custom-text-box" v-model="wodDetSaveReqDto.contents" :label="`Contents ${index + 1}`"
                placeholder="Enter exercise contents"></v-text-field>
        </div>

        <!-- 버튼들 -->
        <div style="display: flex; justify-content: flex-end; margin-top: 10px;">
            <v-btn @click="addWodDet" color="black" style="margin-right: 10px;">종목+</v-btn>
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
            wodSaveReqDto: {
                date: this.date || "", // props로 받은 date를 사용
                timeCap: "00:30:00",
                rounds: "3",
                info: "WOD 설명 텍스트입니다.",
                wodDetSaveReqDtoList: [],
            },
        };
    },
    methods: {
        addWodDet() {
            this.count++;
            this.wodSaveReqDto.wodDetSaveReqDtoList.push({
                name: `종목명${this.count}`,
                contents: `종목내용${this.count}`,
            });
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
}
</style>