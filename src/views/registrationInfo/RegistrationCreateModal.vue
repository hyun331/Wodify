<template>
    <v-dialog max-width="500px">
        <v-card>
            <v-card-title class="freesentation text-center" style="font-size: 40px;">회원 등록</v-card-title>
            <v-card-text>
                <v-form @submit.prevent="registrationCreate">
                    <span v-if="emailError" class="errorSpan">이메일을 입력하세요</span>
                    <v-text-field label="email" v-model="email" type="email" prepend-icon="" 
                    required>
                    </v-text-field>
                    
           

                    <v-text-field label="등록일" v-model="registrationDate" type="date"
                        required>
                        
                    </v-text-field>

                    <span v-if="registError" class="errorSpan">유효한 등록기간을 입력하세요</span>
                    <v-text-field label="등록 기간" v-model="registrationMonth" 
                        required>
                    </v-text-field>

                    <v-text-field v-model="newEndDate"  readonly=""
                        required>
                        {{ getEndDate }}
                    </v-text-field>
                    <v-row>
                        
                        <v-col>
                            <RoundedButtonComponent
                            text="등록/연장"
                            :buttonType='submit'
                            > </RoundedButtonComponent>
                        </v-col>
                        <v-col>    
                            <RoundedButtonComponent
                            text="닫기"
                            :buttonType="'button'" 
                            @click="closeModal"
                            > </RoundedButtonComponent>                


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
    components:{
        RoundedButtonComponent
    },
    props: {
        modalUserEmail: String,  
        modalNextStartDate: String ,
        registrationError: Boolean
    },
    data() {
        return {
            emailError: this.registrationError,
            registError: this.registrationError,
            email: this.modalUserEmail||"",
            registrationDate: this.modalNextStartDate||"",
            registrationMonth: "",
            newEndDate:"",
        }
    },

    //modal에 값 적용
    watch: {
        modalUserEmail(value) {
            this.email = value;
        },
        modalNextStartDate(value) {
            this.registrationDate = value;
        }
    },
    
    computed:{
        getEndDate(){
            if (this.registrationDate && this.registrationMonth > 0) {
                const startDate = new Date(this.registrationDate);
                const endDate = new Date(startDate.setMonth(startDate.getMonth() + parseInt(this.registrationMonth)));
                return endDate.toISOString().substr(0, 10); // Format as YYYY-MM-DD
            }else{
                return "종료일 - 개월 수를 입력해주세요";
            }
        }

    },
    methods: {
        async registrationCreate() {
            const registrationData = {
                email: this.email,
                registrationDate : this.registrationDate,
                registrationMonth: this.registrationMonth
            };

            try{
                if(this.registrationMonth == ""){
                    this.registError = true;
                    return;
                }

                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/registration/create`, registrationData);
                console.log(response.data.result);
                this.closeModal();
                window.location.href="/member/list/user";
                

            }catch(e){
                console.log(e);
                if(e.response.data.status_code === 404){
                    this.emailError = true;
                }
                else{
                    this.registError = true;
                }

        
            }

        },
        closeModal() {
            this.emailError = false;
            this.registError = false;

            this.registrationDate = "";
            this.registrationMonth = "";
            this.$emit('update:dialog', false)
        }
    },


}

</script>

<style scoped>
.errorSpan{
    font-size: 11px;
    color: red;
    border: 1px;
}
</style>