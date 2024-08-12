import {createRouter, createWebHistory} from 'vue-router';

import { memberRouter } from './MemberRouter';
import HomeView from '@/views/common/HomeView.vue';
import {reservationRouter} from './reservationRouter';
import { boxRouter } from './BoxRouter';
import { wodRouter } from './wodRouter';

const routes = [
    {
        path: '/',
        name: 'HomeView',
        component: HomeView
    },
    ...memberRouter,
    ...reservationRouter,
    ...boxRouter,
    ...wodRouter
]

const router = createRouter({
    history: createWebHistory(),
    routes
});


export default router;