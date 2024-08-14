import WodSave from "@/views/wod/WodSave.vue"
import WodFind from "@/views/wod/WodFind.vue"

export const wodRouter = [
    {
        path: '/wod/save',
        name: 'WodSave',
        component: WodSave
    },
    {
        path: '/wod/find/:date',
        name: 'WodFind',
        component: WodFind,
        props: true,
    },
]