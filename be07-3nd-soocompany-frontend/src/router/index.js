import {createRouter, createWebHistory} from 'vue-router';

import { memberRouter } from './MemberRouter';
import HomeView from '@/views/common/HomeView.vue';
import {reservationRouter} from './reservationRouter';
import { wodRouter } from './wodRouter';
import { boxRouter } from './BoxRouter';
import { hallOfFameRouter } from './HallOfFame';
import { postRouter } from './postRouter';

const routes = [
    {
        path: '/',
        name: 'HomeView',
        component: HomeView
    },
    ...memberRouter,
    ...reservationRouter,
    ...wodRouter,
    ...boxRouter,
    ...hallOfFameRouter,
    ...postRouter
]

const router = createRouter({
    history: createWebHistory(),
    routes
});


export default router;