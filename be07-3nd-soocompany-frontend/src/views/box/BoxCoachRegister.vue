<template>
    <v-dialog max-width="650px">
        <v-card>
            <v-card-title class="text-h3 text-center freesentation">박스 가입</v-card-title>
            <br>
            <v-card-text>
                <v-row justify="center">
                    <h4 class="noto-sans" style="font-weight: 500;">박스 코드를 입력해주세요</h4>
                </v-row>
                
                
                <v-form @submit.prevent="coachRegistrationBox">
                    <v-row justify="center">
                        <v-col cols="7">
                            <v-text-field :label="boxCodeLabel" v-model="code" type="password" required>
                            </v-text-field>
                        </v-col>
                        <v-col cols="auto">
                            <RoundedButtonComponent text="가입" :buttonType='submit' class="noto-sans" style="font-weight: 500;"> </RoundedButtonComponent>
                        </v-col>

                        <v-col cols="2">
                            <RoundedButtonComponent text="X" :buttonType="'button'" @click="closeModal" class="noto-sans" style="font-weight: 500;"> </RoundedButtonComponent>
                        </v-col>
                    </v-row>
                    

                    
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