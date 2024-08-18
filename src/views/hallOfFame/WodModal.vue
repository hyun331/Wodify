<template>
    <v-dialog >
        <v-card class="mx-auto" width="500" height="200">
            <v-card-title class="text-h6 text-center" style="margin-top: 10px;">
                어제의 와드
            </v-card-title>
            <v-card-text style="padding: 20px;">
                <v-row class="d-flex justify-center align-center" style="margin-top: 25px;">
                    <RoundedButtonComponent @click="closeModal" text="Nooooo" class="mx-2"/>
                </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';

export default {
    components:{
        RoundedButtonComponent
    },
    props:['wodId'],
    data() {
        return {


        }
    },
    async created(){
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/search/${this.wodId}`);
        console.log(response.data.result.id);
        alert(response.data.result.id);
    },
    methods: {
        closeModal() {
            this.$emit('update:dialog', false);
        },
    }
}
</script>

