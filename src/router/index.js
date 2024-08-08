import {createRouter, createWebHistory} from 'vue-router';

import { memberRouter } from './MemberRouter';
import HomeView from '@/views/common/HomeView.vue';
import {reservationRouter} from './reservationRouter';

const routes = [
    {
        path: '/',
        name: 'HomeView',
        component: HomeView
    },
    ...memberRouter,
    ...reservationRouter,
    ...wodRouter
]

const router = createRouter({
    history: createWebHistory(),
    routes
});


export default router;