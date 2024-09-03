<template>
    <v-dialog max-width="750px">
        <v-card>
            <v-card-title class="freesentation text-center" style="font-size: 40px;">회원 정보 수정</v-card-title>
            <v-card-text>
                <v-form @submit.prevent="changeMemberInfo" ref="form" lazy-validation>


                    <v-row justify="center" class="half-spacing">
                        <v-col cols="4" class="divField">
                            <v-label>NAME</v-label>
                        </v-col>
                        <v-col cols="8">
                            <v-text-field v-model="changeName" :rules="name_phone_rule" required
                                placeholder="필수 입력"></v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4" class="divField">
                            <v-label>EMAIL</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field v-model="changeEmail" type="email" required readonly="">
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4" class="divField">
                            <v-label>PHONE</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field v-model="changePhone" :rules="name_phone_rule" placeholder="필수 입력" required>
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4" class="divField"><v-label>ADDRESS</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field v-model="changeAddress" class="test" required>
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4" class="divField">
                            <v-label>DEAD LIFT</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field v-model="changeDeadLift" required>
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4" class="divField"><v-label>SQUAT</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field v-model="changeSquat" required>
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <v-row justify="center">
                        <v-col cols="4" class="divField"><v-label>BENCH PRESS</v-label></v-col>
                        <v-col cols="8">

                            <v-text-field v-model="changeBenchPress" required>
                            </v-text-field>
                        </v-col>
                    </v-row>

                    <br />
                    <br />




                    <v-row justify="center">
                        <v-col cols="2">
                            <v-btn type="submit"> 수정</v-btn>
                            <!-- <RoundedButtonComponent
                            text="수정"
                            :buttonType='submit'
                            > </RoundedButtonComponent> -->
                        </v-col>
                        <v-col cols="2">
                            <v-btn @click="closeModal"> 닫기</v-btn>
                            <!-- <RoundedButtonComponent
                            text="닫기"
                            :buttonType="'button'" 
                            @click="closeModal"
                            > </RoundedButtonComponent>                 -->


                        </v-col>
                    </v-row>



                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios';
// import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
export default {
    components: {
        // RoundedButtonComponent
    },
    props: {
        name: String,
        email: String,
        phone: String,
        address: String,
        benchPress: String,
        deadLift: String,
        squat: String,

    },
    data() {
        return {
            changeName: this.name,
            changeEmail: this.email,
            changePhone: this.phone,
            changeAddress: this.address,
            changeBenchPress: this.benchPress,
            changeDeadLift: this.deadLift,
            changeSquat: this.squat,
            name_phone_rule: [
                v => !!v || '',
            ],

        }
    },




    // //modal에 값 적용
    watch: {
        name(value) {
            this.changeName = value;
        },
        email(value) {
            this.changeEmail = value;
        },
        phone(value) {
            this.changePhone = value;
        },
        address(value) {
            this.changeAddress = value;
        },
        benchPress(value) {
            this.changeBenchPress = value;
        },
        squat(value) {
            this.changeSquat = value;
        },
        deadLift(value) {
            this.changeDeadLift = value;
        },

    },



    methods: {
        async changeMemberInfo() {
            const changeData = {
                name: this.changeName,
                phone: this.changePhone,
                address: this.changeAddress,
                benchPress: this.changeBenchPress,
                squat: this.changeSquat,
                deadLift: this.changeDeadLift,
            };
            const form = this.$refs.form;
            if (form) {
                try {
                    const validationResult = await form.validate();
                    if (validationResult.valid === false) {
                        alert("필수 입력란을 입력해주세요");
                        return;
                    }
                }catch(e){
                    console.log('error during update user');
                }
            }

            try {

                const response = await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/member/update`, changeData);
                console.log(response.data.result);
                this.closeModal();
                window.location.href = "/member/detail";


            } catch (e) {
                console.log(e);



            }

        },
        closeModal() {

            this.$emit('update:dialog', false)
        }
    },


}

</script>

<style scoped>
.errorSpan {
    font-size: 11px;
    color: red;
    border: 1px;
}


.divField {
    display: flex;
    justify-content: flex-end;
    height: 70px;
}

.v-label {
    font-size: 20px;
    font-family: "Rubik Mono One", monospace;
    font-weight: 400;
    font-style: normal;
}

div.v-col-8 {
    height: 70px;

}
</style>