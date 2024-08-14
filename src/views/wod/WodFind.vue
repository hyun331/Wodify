<template>
    <v-container v-if="wod">
        <v-card :style="{ width: 'fit-content' }">
            <v-card-title>WOD</v-card-title>
            <v-card-text>
                <p><strong>Date:</strong> {{ wod.date }}</p>
                <p><strong>Box Name:</strong> {{ wod.boxName }}</p>
                <p><strong>Member Name:</strong> {{ wod.memberName }}</p>
                <p><strong>Info:</strong> {{ wod.info }}</p>
                <p><strong>Time Cap:</strong> {{ wod.timeCap }}</p>
                <p><strong>Rounds:</strong> {{ wod.rounds }}</p>
                <p><strong>Created Time:</strong> {{ wod.createdTime }}</p>
            </v-card-text>
            <v-card-text v-for="(wodDet, index) in wod.wodDetResDtoList" :key="wodDet.id" class="mb-4"
                :style="{ width: 'fit-content' }">
                Exercise {{ index + 1 }}
                <p><strong>Name:</strong> {{ wodDet.name }}</p>
                <p><strong>Contents:</strong> {{ wodDet.contents }}</p>
            </v-card-text>
        </v-card>
    </v-container>
    <v-container v-else>
        <v-progress-circular indeterminate color="primary"></v-progress-circular>
        <p>Loading WOD data...</p>
    </v-container>
</template>

<script>
import axios from 'axios';

export default {
    props: ['date'],  // date 파라미터를 props로 받음
    data() {
        return {
            wod: null,  // 초기값을 null로 설정, 로딩 후 데이터를 채웁니다.
        };
    },
    mounted() {
        this.fetchWodData();
    },
    methods: {
        async fetchWodData() {
            try {
                const response = await axios.get(`http://localhost:8090/wod/find/${this.date}`);
                this.wod = response.data.result;
            } catch (error) {
                console.error('Error fetching WOD data:', error);
                alert('WOD 데이터를 불러오는 중 오류가 발생했습니다.');
            }
        },
    },
};
</script>
