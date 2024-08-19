<template>
    <v-dialog >
        <v-card class="mx-auto" width="500" height="200">
            <v-card-title class="text-h6 text-center" style="margin-top: 10px;">
                정지 요청을 하시겠습니까?
                </v-card-title>
            <v-card-text style="padding: 20px;">
                <v-row class="d-flex justify-center">
                    <span class="text-center">
                        정지는 3회로 제한되며, 정지는 수정이나 철회가 불가능합니다.<br>
                    </span>
                </v-row>
                <v-row class="d-flex justify-center align-center" style="margin-top: 25px;">
                    <RoundedButtonComponent @click="hold" text="Yeees" class="mx-2"/>
                    <RoundedButtonComponent @click="closeModal" text="Nooooo" class="mx-2"/>
                </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
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
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/holding-info/create`);
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

