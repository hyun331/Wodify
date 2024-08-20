<template>
  <div>
    <v-container>
      <v-row justify="center" class="mt-5">
        <v-col cols="12" md="10">
          <v-card>
            <v-card-text>
              <Line :data="chartData" :options="chartOptions" />
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import { Line } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, LineElement, CategoryScale, LinearScale, PointElement);

export default {
  components: {
    Line
  },
  props: {
    records: {
      type: Array,
      required: true
    }
  },
  computed: {
    chartData() {
      const ids = this.records.map(record => record.id);
      const times = this.records.map(record => this.convertLocalTimeToSeconds(record.exerciseTime));

      return {
        labels: ids,
        datasets: [
          {
            label: 'My Exercise Time',
            backgroundColor: '#3C3D3D',
            borderColor: '#000000',
            data: times,
            fill: false
          }
        ]
      };
    },
    chartOptions() {
      return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false
          },
          tooltip: {
            callbacks: {
              label: (context) => {
                // 초 단위를 HH:mm:ss 형식으로 변환
                const timeInSeconds = context.raw;
                const hours = Math.floor(timeInSeconds / 3600);
                const minutes = Math.floor((timeInSeconds % 3600) / 60);
                const seconds = timeInSeconds % 60;
                return `ID: ${context.label}, Time: ${this.formatTime(hours, minutes, seconds)}`;
              }
            }
          }
        },
        scales: {
          x: {
            title: {
              display: true,
              text: 'record',
              color: '#3C3D3D',
              font: {
                family: 'Rubik Mono One',
                size: 16
              }
            },
            ticks: {
              color: '#3C3D3D',
              font: {
                family: 'Rubik Mono One',
                size: 14
              }
            }
          },
          y: {
            title: {
              display: true,
              text: 'Time',
              color: '#3C3D3D',
              font: {
                family: 'Rubik Mono One',
                size: 16
              }
            },
            ticks: {
              color: '#3C3D3D',
              font: {
                family: 'Rubik Mono One',
                size: 12
              },
              stepSize: 7200,
              callback: (value) => {
                // 초 단위에서 HH:00:00 형식으로 변환
                const hours = Math.floor(value / 3600);
                return `${String(hours).padStart(2, '0')}H`;
              }
            },
            grid: {
              lineWidth: 1 // y축의 그리드 라인의 두께 설정
            }
          }
        },
        elements: {
          line: {
            borderWidth: 2 // 선의 두께 설정
          }
        }
      };
    }
  },
  methods: {
    convertLocalTimeToSeconds(localTime) {
      if (!localTime) return 0;
      //HH:mm:ss형식은 차트화를 시킬 수 없으므로 초 단위로 변경
      const [hours, minutes, seconds] = localTime.split(':').map(Number);
      return (hours * 3600) + (minutes * 60) + seconds;
    },
    formatTime(hours, minutes, seconds) {
      const hh = String(hours).padStart(2, '0');
      const mm = String(minutes).padStart(2, '0');
      const ss = String(seconds).padStart(2, '0');
      return `${hh}:${mm}:${ss}`;
    }
  }
};
</script>

<style scoped>
.chart-container {
  width: 200%;
  height: 400px;
}
</style>