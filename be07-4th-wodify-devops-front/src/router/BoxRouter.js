import BoxSuccess from "@/views/box/BoxSuccess.vue"
import BoxCreate from "@/views/box/BoxCreate.vue"
import BoxList from "@/views/box/BoxList.vue"
import BoxDetail from "@/views/box/BoxDetail.vue"
import BoxUpdate from "@/views/box/BoxUpdate.vue"
import BoxMyPage from "@/views/box/BoxMyPage.vue"

export const boxRouter = [
    {
        path: '/box/success',
        name: 'BoxSuccess',
        component: BoxSuccess
    },
    {
        path: '/box/create',
        name: 'BoxCreate',
        component: BoxCreate
    },
    {
        path: '/box/list',
        name: 'BoxList',
        component: BoxList
    },
    {
        path: '/box/detail/:id',
        name: 'BoxDetail',
        component: BoxDetail
    },
    {
        path: '/box/update',
        name: 'BoxUpdate',
        component: BoxUpdate
    },
    {
        path: '/box/mybox',
        name: 'BoxMyPage',
        component: BoxMyPage
    },

]