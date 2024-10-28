import ReservationList from '@/views/reservation/ReservationList.vue'
import ReservationCreate from '@/views/reservation/ReservationCreate.vue'
import ReservationDetailList from '@/views/reservation_detail/ReservationDetailList.vue'
import ReservationDetailCreate from '@/views/reservation_detail/ReservationDetailCreate.vue'
import ReservationDetailRecord from '@/views/reservation_detail/ReservationDetailRecord.vue'
import RecordList from '@/views/reservation_detail/RecordList.vue'

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
        path: '/reservation-detail/list',
        name: 'ReservationDetailList',
        component: ReservationDetailList
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
    },

    {
        path: '/record/list',
        name: 'RecordList', 
        component: RecordList // 운동기록도 함께 보여진다.
    },

]