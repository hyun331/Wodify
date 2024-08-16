<template>
    <v-dialog>
        <v-card>
            <v-card-text>
                <!-- <p><strong>Date:</strong> {{ wod.date }}</p>
                <p><strong>Time Cap:</strong> {{ wod.timeCap }}</p>
                <p><strong>Rounds:</strong> {{ wod.rounds }}</p>
                <p><strong>Info:</strong> {{ wod.info }}</p> -->
                <p>ha</p>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
// import axios from 'axios';
export default{

    data(){
        return{

        }
    }
    // props: {
    //     reservationDate: {
    //         type: Date,
    //         required: true
    //     }
    // },
    // data:{
    //     wod: {},
    // },
    // async created(){
    //     try{
    //         console.log(this.reservation.date);
    //         const response = await axios.get(`http://localhost:8090/wod/find/${this.reservationDate}`);
    //         this.wod = response.data.result;
    //     }catch(e){
    //         console.log(e)
    //     }
    // }
}
</script>