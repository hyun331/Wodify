<template>
    <v-dialog >
        <v-card class="mx-auto" width="500">
            <v-card-title class="text-h6 text-center" style="margin-top: 10px;">
                와드
            </v-card-title>
            <v-card-text style="padding: 20px;">
                <v-row>
                    <v-col>DATE</v-col>
                    <v-col>{{wod.date}}</v-col>
                </v-row>

                <v-row>
                    <v-col>TIME CAP</v-col>
                    <v-col>{{wod.timeCap}}</v-col>
                </v-row>

                <v-row>
                    <v-col>ROUND</v-col>
                    <v-col>{{wod.rounds}}</v-col>
                </v-row>
                <hr>

                <v-row v-for="w in wodDetResDtoList" :key="w.id">
                    <v-col>{{w.name}}</v-col>
                    <v-col>{{w.contents}}</v-col>
                </v-row>



                <v-row class="d-flex justify-content-end align-center" style="margin-top: 25px;">
                    <RoundedButtonComponent @click="closeModal" text="X" class="mx-2"/>
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
            wod : {},
            wodDetResDtoList :[],

        }
    },
    async created(){
        const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/search/${this.wodId}`);
        this.wod = response.data.result;
        this.wodDetResDtoList = response.data.result.wodDetResDtoList;
        
        console.log(response.data.result.id);
    },
    methods: {
        closeModal() {
            this.$emit('update:dialog', false);
        },
    }
}
</script>
