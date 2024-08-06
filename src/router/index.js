import {createRouter, createWebHistory} from 'vue-router';
import { memberRouter } from './member/MemberRouter';
import {reservationRouter} from './reservationRouter';
const routes = [
    ...memberRouter,
    ...reservationRouter
    

]


const router = createRouter({
    history: createWebHistory(),
    routes
});


export default router;