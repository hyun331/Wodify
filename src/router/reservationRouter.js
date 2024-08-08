import ReservationList from '@/views/reservation/ReservationList.vue'
import ReservationDetailCreate from '@/views/reservation_detail/ReservationDetailCreate.vue'

export const reservationRouter = [
    {
        path: '/reservation/list',
        name: 'ReservationList',
        component: ReservationList
    },
    {
        path: '/reservation-detail/create',
        name: 'ReservationDetailCreate',
        component: ReservationDetailCreate
    }
]