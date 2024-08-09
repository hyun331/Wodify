import {createRouter, createWebHistory} from 'vue-router';

import { memberRouter } from './MemberRouter';
import HomeView from '@/views/common/HomeView.vue';
import {reservationRouter} from './reservationRouter';
<<<<<<< HEAD
import { boxRouter } from './BoxRouter';
=======
import { wodRouter } from './wodRouter';
>>>>>>> f44fa03dd1d197d57d5d91e525d3e350bcda7066

const routes = [
    {
        path: '/',
        name: 'HomeView',
        component: HomeView
    },
    ...memberRouter,
    ...reservationRouter,
<<<<<<< HEAD
    ...boxRouter
=======
    ...wodRouter
>>>>>>> f44fa03dd1d197d57d5d91e525d3e350bcda7066
]

const router = createRouter({
    history: createWebHistory(),
    routes
});


export default router;