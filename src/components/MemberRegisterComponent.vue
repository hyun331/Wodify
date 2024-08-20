<template>
    <div class="background">
        <v-container>
            <v-row justify="center">
                <v-col style="height: 200px;">
                    <v-img :src="require('@/assets/wodify.png')" alt="WODIFY"  contain></v-img>
                </v-col>

            </v-row>

            <v-row class="d-flex  mt-5" justify="center">
                <v-col cols="12" sm="6" md="8">
                    <v-form @submit.prevent="memberRegister" ref="form" lazy-validation>

                        <v-row justify="center" class="half-spacing">
                            <v-col cols="4" class="divField">
                                <label class="v-label">PROFILE IMAGE</label>
                            </v-col>
                            <v-col cols="8">
                                <v-file-input accept="image/*" @change="fileUpdate" prepend-icon=""></v-file-input>
                            </v-col>
                        </v-row>

                        <v-row justify="center" class="half-spacing">
                            <v-col cols="4" class="divField">
                                <v-label>NAME</v-label>
                            </v-col>
                            <v-col cols="8">
                                <v-text-field  v-model="name" :rules="name_phone_rule" required placeholder="필수 입력"></v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4" class="divField">
                                <v-label>EMAIL</v-label></v-col>
                            <v-col cols="8">

                                <v-text-field v-model="email" type="email" required readonly="">
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4" class="divField">
                                <v-label>PHONE</v-label></v-col>
                            <v-col cols="8">

                                <v-text-field v-model="phone" :rules="name_phone_rule" placeholder="필수 입력" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4" class="divField"><v-label>ADDRESS</v-label></v-col>
                            <v-col cols="8">

                                <v-text-field v-model="address" class="test" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4" class="divField">
                                <v-label>DEAD LIFT</v-label></v-col>
                            <v-col cols="8">

                                <v-text-field v-model="deadLift" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4" class="divField"><v-label>SQUAT</v-label></v-col>
                            <v-col cols="8">

                                <v-text-field v-model="squat" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4" class="divField"><v-label>BENCH PRESS</v-label></v-col>
                            <v-col cols="8">

                                <v-text-field v-model="benchPress" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4" class="divField"><v-label>ROLE</v-label></v-col>
                            <v-col cols="8">

                                <v-text-field  v-model="role" required readonly="">
                                </v-text-field>
                            </v-col>
                        </v-row>
                        <br />
                        <br />

                        <v-row justify="center">
                            <!-- <v-btn @click="memberRegister" color="primary" block>등록</v-btn> -->

                            <RoundedButtonComponent text="SIGN UP" buttonType="submit">
                            </RoundedButtonComponent>
                        </v-row>

                    </v-form>
                </v-col>
            </v-row>
        </v-container>
    </div>

    <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
                :dialogText="dialogText" />
</template>

<script>
import axios from 'axios';
import RoundedButtonComponent from './RoundedButtonComponent.vue';
import AlertModalComponent from './AlertModalComponent.vue';

export default {
    props: ['propsRole'],
    components: {
        RoundedButtonComponent,
        AlertModalComponent,
    },
    data() {
        return {
            dialogTitle:'',
            dialogText:'',
            alertModal: false,

            name: '',
            phone: '',
            email: '',
            address: '',
            deadLift: '',
            squat: '',
            benchPress: '',
            role: '',
            memberImage: null,
            name_phone_rule: [
                v => !!v || '',
            ], // Validation rule
        }
    },
    created() {
        this.email = new URL(window.location.href).searchParams.get('email');
        this.role = this.propsRole;
    },
    methods: {
        async memberRegister() {
        const form = this.$refs.form;
        if (form) {
            try {
                const validationResult = await form.validate(); 

                if (validationResult.valid === false) { 
                    this.dialogTitle = "회원가입 실패"
                    this.dialogText = "필수 입력란을 입력해주세요";
                    this.alertModal = true;
                    return;  
                }

                let registerData = new FormData();
                registerData.append("name", this.name);
                registerData.append("email", this.email);
                registerData.append("phone", this.phone);
                registerData.append("address", this.address);
                registerData.append("deadLift", this.deadLift);
                registerData.append("squat", this.squat);
                registerData.append("benchPress", this.benchPress);
                registerData.append("role", this.role);

                if (this.memberImage != null) {
                    registerData.append("memberImage", this.memberImage);
                }

                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member/register`, registerData);
                console.log(response.data.result);
                window.location.href = "/";
            } catch (e) {
                console.error('Error during registration:', e);
            }
        }
    },
        fileUpdate(event) {
            this.memberImage = event.target.files[0];
        }
    }
}
</script>

<style scoped>
.background {
    min-height: 100vh;
    margin: 0;
    background-image: url("@/assets/background.png");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
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


::v-deep .v-text-field input  {    
    background-color: #e7e4e4; 
}

::v-deep .v-field.v-field--appended{    
    background-color: #e7e4e4; 
}



</style>
