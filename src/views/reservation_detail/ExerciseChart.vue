<template>
  <div class="chart-container">
    <Line :data="chartData" :options="chartOptions" />
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
      const ids = this.records.map((_, index) => `day${index + 1}`); // 문자열 레이블로 변경
      const times = this.records.map(record => this.convertLocalTimeToSeconds(record.exerciseTime));

      return {
        labels: ids,  // x축에 표시할 레이블을 ids로 설정
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
            type: 'category', // 범주형 스케일로 설정
            title: {
              display: true,
              text: 'record',
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
              stepSize: 600, // 600초 = 10분 단위로 눈금 설정
              callback: (value) => {
                // 초 단위를 분으로 변환
                const minutes = Math.floor(value / 60);
                return `${String(minutes)} MM`; // 10분 단위로 눈금 표시
              }
            },
            grid: {
              lineWidth: 1
            }
          }
        },
        elements: {
          line: {
            borderWidth: 2
          }
        }
      };
    }
  },
  methods: {
    convertLocalTimeToSeconds(localTime) {
      if (!localTime) return 0;
      // HH:mm:ss 형식을 초 단위로 변환
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
  width: 100%;
  height: 300px; /* 차트의 높이를 300px로 설정 */
}
</style>
