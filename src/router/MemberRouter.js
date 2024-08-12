import TestMember from "@/views/member/TestMember.vue"
import OAuth2RedirectHandler from './OAuth2RedirectHandler';
import ChooseRegister from "@/views/member/ChooseRegister.vue";
import UserRegister from "@/views/member/UserRegister.vue";
import CoachRegister from "@/views/member/CoachRegister.vue";

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

    {
        path:"/member/user-register",
        name: "UserRegister",
        component: UserRegister
    },

    {
        path:"/member/coachRegister",
        name: "CoachRegister",
        component: CoachRegister
    }


]