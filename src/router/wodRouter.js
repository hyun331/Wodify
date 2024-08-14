import WodSave from "@/views/wod/WodSave.vue"
import WodFind from "@/views/wod/WodFind.vue"
import SelectDate from "@/views/wod/SelectDate.vue"

export const wodRouter = [
    {
        path: '/wod/save',
        name: 'WodSave',
        component: WodSave
    },
    {
        path: '/wod/find',
        component: SelectDate,
        children: [
            {
                path: ':date',
                component: WodFind,
                props: true,
            },
        ],
    }
]