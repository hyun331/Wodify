<template>
    <v-container>
        <div style="line-height: 1;">
            <h1 class="freesentation text-center" style=" font-size:70px;">명예의 전당</h1>
        </div>
        <v-row justify="center">
            <v-tabs v-model="selectedTab" centered color="black" slider-color="yellow">
                <v-tab>와드 수행기록</v-tab>
                <v-tab>출석률</v-tab>
            </v-tabs>
        </v-row>


        <v-row justify="center" class="mt-3">
            <v-col cols="12" md="9">
                <v-card>
                    <v-card-text>
                        <BarChart :data="chartData" :options="chartOptions" />
                    </v-card-text>
                </v-card>
            </v-col>
            <v-col  v-if="selectedTab === 0"  class="d-flex justify-content-end" cols="12" md="9" style="padding-top:0px;">
                <v-btn @click="showWodModal">WOD</v-btn>
            </v-col>
        </v-row>

  
        <WodModal v-if="wodId" v-model="wodModal" :wodId="this.wodId" @update:dialog="wodModal = $event">

        </WodModal>


        <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
            :dialogText="dialogText" />
    </v-container>




</template>

<script>
import axios from 'axios';
import BarChart from '@/components/BarChart.vue';
import WodModal from './WodModal.vue';
import AlertModalComponent from '@/components/AlertModalComponent.vue';

export default {
    components: {
        BarChart, WodModal, AlertModalComponent
    },
    computed: {
        chartData() {
            if (this.selectedTab === 0) {
                // 와드 수행기록 데이터
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
                                const [hours, minutes, seconds] = item.exerciseTime.split(':').map(Number);
                                return hours * 60 + minutes + seconds / 60;
                            }),
                        },
                    ],
                };
            } else {
                // 출석률 데이터
                return {
                    labels: this.attendanceData.map(item => item.name),
                    datasets: [
                        {
                            label: '출석일',
                            backgroundColor: '#FFBF00',
                            data: this.attendanceData.map(item => item.attendanceCount),
                        },
                    ],
                };
            }
        },
        chartOptions() {
            if (this.selectedTab === 0) {
                return {
                    responsive: true,
                    scales: {
                        x: {
                            type: 'linear',
                            min: 0,
                            max: 60, 
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
                };
            } else {
                return {
                    responsive: true,
                    scales: {
                        x:{
                            type: 'linear',
                            min: 0,
                            max: 35, 
                            title: {
                                display: true,
                                text: '출석일 수',
                            },
                            
                            
                        

                        },
                        y: {
                            beginAtZero: true,
                            max: 100,
                            
                        },
                    },
                    plugins: {
                        legend: {
                            position: 'top',
                        },
                    },
                    barThickness: 15,
                };
            }
        },
    },
    data() {
        return {
            selectedTab: 0,



            wodModal: false,
            alertModal: false,
            dialogTitle: '',
            dialogText: '',

            hallOfFameResDtoList: [],
            wodId: null,

            attendanceData:[], 
           
        };
    },

    async created() {
        try {
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/hallOfFame`);
            this.hallOfFameResDtoList = response.data.result.hallOfFameResDtoList;
            this.wodId = response.data.result.wodId;

            const attendanceResponse = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/hallOfFame/attendance-rate`);
            this.attendanceData = attendanceResponse.data.result;
            console.log(this.attendanceData);


        } catch (e) {
            this.dialogTitle = '명예의 전당 확인 불가';
            this.dialogText = e.response?.data?.error_message;

            this.alertModal = true;
            console.error(e.response?.data?.error_message || "???");

        }
    },
    methods: {
        showWodModal() {
            this.wodModal = true;
        }
    }
};
</script>
