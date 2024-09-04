<template>
    <div>
        <v-container>
            <!-- 차트 -->
            <v-row class="d-flex justify-content-center mt-5" justify="center">
                <v-col cols="12">
                    <v-label>Time by reservation</v-label>
                    <div class="chart-container">
                        <exercise-time-chart :records="records" />
                    </div>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
import ExerciseTimeChart from '@/views/member/ExerciseTimeChart.vue';
export default {
    props: ['records'],
    components: {
        ExerciseTimeChart,
    },
}

</script>

<style>
.chart-container {
    width: 100%;
    /* 차트의 너비를 100%로 설정 */
    height: 400px;
    /* 원하는 높이로 설정, 예: 400px */
}
</style>