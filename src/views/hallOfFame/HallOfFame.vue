<template>
    <v-container>
        <div style="line-height: 1;">
            <h1 class="do-hyeon-regular text-center">명예의 전당</h1>
          </div>
   
        <v-row justify="center" class="mt-5">
            <v-col cols="12" md="10">
                <v-card>
                    <v-card-text>
                        <Bar :data="chartData" :options="chartOptions" />
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
        <v-row justify="center">
            <v-col class="d-flex justify-content-end" cols="12" md="10">
                <v-btn @click="showWodModal">WOD</v-btn>
            </v-col>
        </v-row>
        <WodModal 
         v-if="wodId"
        v-model="wodModal" 
        :wodId="this.wodId"
        @update:dialog="wodModal = $event"
        >

        </WodModal>


        <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
        :dialogText="dialogText" />
    </v-container>



    
</template>

<script>
import axios from 'axios';
import { Bar } from 'vue-chartjs';
import WodModal from './WodModal.vue';
import AlertModalComponent from '@/components/AlertModalComponent.vue';
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
        Bar, WodModal, AlertModalComponent
    },
    computed: {
        chartData() {
            return {
                labels: this.hallOfFameResDtoList.map(item => item.name),
                datasets: [
                    {
                        label: 'Exercise Time',
                        backgroundColor: this.hallOfFameResDtoList.map(item => {
                            if (item.rank === 1) {
                                return '#FFBF00';
                            } else if (item.rank === 2) {
                                return '#A3A3A3';
                            } else if (item.rank === 3) {
                                return '#CD7F32';
                            } else {
                                return '#94D82D'; 
                            }
                        }),
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
            wodModal: false,
            alertModal: false,
            dialogTitle:'',
            dialogText:'',

            hallOfFameResDtoList: [],
            wodId: null,
            chartOptions: {
                responsive: true,
                scales: {
                    x: {
                        type: 'linear',
                        min: 0,
                        max: 30, 
                        ticks: {
                            callback: function (value) {
                                const minutes = value % 60;
                                return `${minutes < 10 ? '0' : ''}${minutes} min`;
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
                    tooltip: {
                        callbacks: {
                            label: function (context) {
                                const totalMinutes = context.raw;
                                const hours = Math.floor(totalMinutes / 60);
                                const minutes = Math.floor(totalMinutes % 60);
                                const seconds = Math.floor((totalMinutes * 60) % 60);
                                return `${context.dataset.label}: ${hours}:${minutes < 10 ? '0' : ''}${minutes}:${seconds < 10 ? '0' : ''}${seconds}`;
                            },
                        },
                    },
                },
                indexAxis: 'y',
                barThickness: 15,
            },
        };
    },

    async created() {
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/hallOfFame`);
            this.hallOfFameResDtoList = response.data.result.hallOfFameResDtoList;
            this.wodId = response.data.result.wodId;
        } catch (e) {
            this.dialogTitle='박스에 가입해주세요',
            this.dialogText='박스에 가입되지 않은 회원은 명예의 전당을 이용할 수 없습니다.',

            this.alertModal = true;
            console.log(e.response.data.error_message);

        }
    },
    methods:{
        showWodModal(){
            this.wodModal = true;
        }
    }
};
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap');
.do-hyeon-regular {
  font-family: "Do Hyeon", sans-serif;
  font-weight: 400;
  font-style: normal;
  font-size: 80px;
}

</style>
