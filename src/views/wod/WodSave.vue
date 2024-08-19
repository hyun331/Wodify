<template>
    <v-container>
        <h1>Wod 등록</h1>
        <v-text-field v-model="wodSaveReqDto.date" label="Date" placeholder="yyyy-mm-dd"></v-text-field>
        <v-text-field v-model="wodSaveReqDto.timeCap" label="TimeCap" placeholder="HH:mm:ss"></v-text-field>
        <v-text-field v-model="wodSaveReqDto.rounds" label="Rounds" placeholder="number"></v-text-field>
        <v-textarea v-model="wodSaveReqDto.info" label="Info" placeholder="여기에 설명을 입력하세요..." rows="5" outlined></v-textarea>

        <div v-for="(wodDetSaveReqDto, index) in wodSaveReqDto.wodDetSaveReqDtoList" :key="index">
            <v-text-field v-model="wodDetSaveReqDto.name" :label="`Name ${index + 1}`" placeholder="Enter exercise name"></v-text-field>
            <v-text-field v-model="wodDetSaveReqDto.contents" :label="`Contents ${index + 1}`" placeholder="Enter exercise contents"></v-text-field>
        </div>

        <v-btn @click="addWodDet" color="secondary">Add WodDet</v-btn>
        <v-btn @click="submitForm" color="primary">Submit</v-btn>
    </v-container>
</template>

<script>
import axios from 'axios';

export default {
    props: ['date'],  // date를 props로 받아서 사용
    data() {
        return {
            count: 0,
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
        addWodDet() {
            this.count++;
            this.wodSaveReqDto.wodDetSaveReqDtoList.push({ name: `종목명${this.count}`, contents: `종목내용${this.count}` });
        },
        async submitForm() {
            try {
                const response = await axios.post('http://localhost:8090/wod/save', this.wodSaveReqDto);
                if (response.status === 201) {
                    alert("WOD가 성공적으로 등록되었습니다.");
                    this.$emit('wod-saved', this.date);
                } else {
                    alert('WOD 등록에 실패했습니다.');
                }
            } catch (error) {
                console.error('Error submitting WOD form:', error.response ? error.response.data : error.message);
                alert('WOD 등록 중 오류가 발생했습니다.');
            }
        }
    }
};
</script>
