import {createRouter, createWebHistory} from 'vue-router';
import { memberRouter } from './member/MemberRouter';
import { boxRouter } from './BoxRouter';
const routes = [
    ...memberRouter,
    ...boxRouter
    

]

const router = createRouter({
    history: createWebHistory(),
    routes
});


export default router;