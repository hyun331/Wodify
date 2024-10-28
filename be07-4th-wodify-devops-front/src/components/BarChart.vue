<template>
    <Bar :data="data" :options="options" />
</template>

<script>
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
    name: 'BarChart',
    components: {
        Bar,
    },
    props: {
        data: {
            type: Object,
            required: true,
        },
        options: {
            type: Object,
            required: true,
        },
    },
};
</script>