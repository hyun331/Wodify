import TestMember from "@/views/member/TestMember.vue"
import OAuth2RedirectHandler from './OAuth2RedirectHandler';
import ChooseRegister from "@/views/member/ChooseRegister.vue";

export const  memberRouter = [
    {
        path: "/member/test",
        name: "TestMember",
        component: TestMember
    },
    {
        path:"/member/auth/kakao/callback",
        name: "OAuth2RedirectHandler",
        component: OAuth2RedirectHandler
    },

    {
        path:"/member/choose/register",
        name: "ChooseRegister",
        component: ChooseRegister
    },


]