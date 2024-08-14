import ReservationList from '@/views/reservation/ReservationList.vue'
import ReservationCreate from '@/views/reservation/ReservationCreate.vue'
import ReservationDetailCreate from '@/views/reservation_detail/ReservationDetailCreate.vue'
import ReservationDetailRecord from '@/views/reservation_detail/ReservationDetailRecord.vue'

export const reservationRouter = [
    {
        path: '/reservation/list',
        name: 'ReservationList',
        component: ReservationList
    },
    {
        path: '/reservation/create',
        name: 'ReservationCreate',
        component: ReservationCreate
    },
    {
        path: '/reservation-detail/create',
        name: 'ReservationDetailCreate',
        component: ReservationDetailCreate
    },
    {
        path: '/reservation-detail/detail/:id',
        name: 'ReservationDetailRecord', 
        component: ReservationDetailRecord // 운동기록도 함께 보여진다.
    }

]