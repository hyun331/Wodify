<template>
    <v-dialog>
        <v-card class="mx-auto" max-width="500"> 
            <v-card-text style="padding: 20px;">
                <v-row class="d-flex justify-center">
                    <span class="text-center">정지 요청을 해제하시겠습니까?</span>
                </v-row>
                <v-row class="d-flex justify-center align-center" style="margin-top: 16px;">
                    <RoundedButtonComponent @click="hold" text="Yeees" class="mx-2"/>
                    <RoundedButtonComponent @click="closeModal" text="Nooooo" class="mx-2"/>
                </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script >
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
import axios from 'axios';
export default {
    components:{
        RoundedButtonComponent
    },
    data() {
        return {

        }
    },
    methods: {
        closeModal() {
            this.$emit('update:dialog', false);
        },
        async hold() {
            try {
                const response = await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/unholding`);
                console.log(response);
                this.closeModal();
                window.location.reload();
            } catch (error) {
                console.log(error);
            }
        }
    }
}
</script>

