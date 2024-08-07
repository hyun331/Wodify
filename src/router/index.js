import {createRouter, createWebHistory} from 'vue-router';
import { memberRouter } from './MemberRouter';
import HelloWorld from '@/components/HelloWorld.vue';
const routes = [
    ...memberRouter,
    

    // 홈화면
    {
        path:"/",
        name: "HelloWorld",
        component: HelloWorld
    }
    

]


const router = createRouter({
    history: createWebHistory(),
    routes
});


export default router;