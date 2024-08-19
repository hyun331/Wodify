<template>
    <v-dialog max-width="500px">
        <v-card>
            <v-card-title class="text-h5 text-center">회원 등록</v-card-title>
            <v-card-text>
                <v-form @submit.prevent="registrationCreate">
                    <v-text-field label="email" v-model="email" type="email" prepend-icon="mdi-email" required>
                    
                    </v-text-field>

                    <v-text-field label="등록일" v-model="registrationDate" type="date"
                        required>
                        
                    </v-text-field>

                    <v-text-field label="등록 기간" v-model="registrationMonth" 
                        required>
                    </v-text-field>

                    <v-text-field v-model="newEndDate"  readonly=""
                        required>
                        {{ getEndDate }}
                    </v-text-field>
                    <v-row>
                        <v-col>
                            <v-btn color="primary" type="submit" block>등록/연장</v-btn>
                        </v-col>
                        <v-col>                    
                            <v-btn color="secondary" @click="closeModal" block>X</v-btn>


                        </v-col>
                    </v-row>

                    

                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
export default {
    props: {
        modalUserEmail: String,   // Receives user email
        modalNextStartDate: String // Receives the end date
    },
    data() {
        return {
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

                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/registration/create`, registrationData);
                console.log(response.data.result);
                this.closeModal();
                window.location.href="/member/list/user";
                // this.$router.push("/member/list/user");
                
            


            }catch(e){
                console.log(e);
        
                alert(e.response?.data?.error_message||"입력값을 확인해주세요");
            }
            

        },
        closeModal() {

            this.registrationDate = "";
            this.registrationMonth = "";
            this.$emit('update:dialog', false)
        }
    },


}

</script>