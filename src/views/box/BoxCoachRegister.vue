<template>
    <v-dialog max-width="500px">
        <v-card>
            <v-card-title class="text-h5 text-center">박스 가입</v-card-title>
            <v-card-text>
                <h4>박스 코드를 입력해주세요</h4>
                
                <v-form @submit.prevent="coachRegistrationBox">
                    <v-text-field :label="boxCodeLabel" v-model="code" type="password" required>
                    </v-text-field>

                    <RoundedButtonComponent text="가입" :buttonType='submit'> </RoundedButtonComponent>
                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
export default {
    components: {
        RoundedButtonComponent
    },

    data() {
        return {
           code: '', 
           boxCodeLabel: 'BOX CODE',
        }
    },

    methods: {
        async coachRegistrationBox() {
            const registrationData = {
                code: this.code
            };

            try {

                const response = await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/member/coach/box/update`, registrationData);
                console.log(response.data.result);
                this.closeModal();
                window.location.href = "/box/mybox";


            } catch (e) {
                console.log(e);
                this.boxCodeLabel = "올바른 박스 코드를 입력해주세요";
                

            }

        },
        closeModal() {
    
            this.boxCodeLabel = 'BOX CODE';
            this.$emit('update:dialog', false)
        }
    },


}

</script>

<style scoped>

</style>