<template>
    <v-dialog>
        <v-card>
            <v-card-text>
                <p><strong>Date:</strong> {{ wod.date }}</p>
                <p><strong>Time Cap:</strong> {{ wod.timeCap }}</p>
                <p><strong>Rounds:</strong> {{ wod.rounds }}</p>
                <p><strong>Info:</strong> {{ wod.info }}</p>
                <v-btn @click="closeModal">close</v-btn>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
// import axios from 'axios';
export default {
    props: ['wod'],
    // props: ['reservationDate'],
    // props: {
    //     reservationDate: {
    //         type: Date,
    //         required: true
    //     }
    // },
    // data(){
    //     return{
    //         wod: {},
    //     }
    // },
    // async create(){
    //     try{
    //         const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find/${this.reservationDate}`);
    //         this.wod = response.data.result;
    //         console.log(this.wod);
    //     }catch(e){
    //         console.log(e)
    //     }
    // }
    methods: {
        closeModal() {
            this.$emit('update:dialog');
        }
    }
}
</script>