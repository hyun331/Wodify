<template>
    <v-container>
        <v-row class="d-flex justify-content-between mt-5">
            <v-col>
                <v-card>
                    <v-card-title class="text-h6 text-center" style="font-family: ONE-Mobile-POP;">
                        명예의 전당 - 운동 시간 차트
                    </v-card-title>
                    <v-card-text>
                        <!-- Render the chart only when chartData is fully ready -->
                        <Bar :data="chartData" :options="chartOptions" />
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</template>

<script>
import axios from 'axios';
import { Bar } from 'vue-chartjs';
import {
    Chart as ChartJS,
    Title,
    Tooltip,
    Legend,
    BarElement,
    CategoryScale,
    LinearScale,
    TimeScale,
} from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale, TimeScale);

export default {
    components: {
        Bar,
    },
    computed: {
        chartData() {
            return {
                labels: this.hallOfFameResDtoList.map(item => item.name),
                datasets: [
                    {
                        label: 'Exercise Time',
                        backgroundColor: '#42A5F5',
                        data: this.hallOfFameResDtoList.map(item => {
                            // Convert LocalTime (HH:mm:ss) to minutes
                            const [hours, minutes, seconds] = item.exerciseTime.split(':').map(Number);
                            return hours * 60 + minutes + seconds / 60;
                        }),
                    },
                ],
            };
        },
    },
    data() {
        return {
            hallOfFameResDtoList: [],
            wodId: null,
            chartOptions: {
                responsive: true,
                scales: {
                    x: {
                        type: 'linear',
                        min: 0,
                        max: 120, // Maximum of 2 hours in minutes
                        ticks: {
                            callback: function (value) {
                                const hours = Math.floor(value / 60);
                                const minutes = value % 60;
                                return `${hours}:${minutes < 10 ? '0' : ''}${minutes} hours`;
                            },
                        },
                        title: {
                            display: true,
                            text: 'Time (HH:mm)',
                        },
                    },
                    y: {
                        beginAtZero: true,
                    },
                },
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Exercise Time Leaderboard',
                    },
                },
                indexAxis: 'y', // This makes the chart horizontal
            },
        };
    },

    async created() {
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/hallOfFame`);
            this.hallOfFameResDtoList = response.data.result.hallOfFameResDtoList;
            this.wodId = response.data.result.wodId;
        } catch (e) {
            console.log(e);
        }
    },
};
</script>

<style scoped>
/* Add any custom styles if needed */
</style>
