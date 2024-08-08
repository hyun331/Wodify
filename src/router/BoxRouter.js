import BoxSuccess from "@/views/box/BoxSuccess.vue"
import BoxCreate from "@/views/box/BoxCreate.vue"

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

]