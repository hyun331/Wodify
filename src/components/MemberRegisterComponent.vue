<template>
    <div class="background">
        <v-container>
            <v-row>
                <v-col cols="6">
                    <v-img :src="require('@/assets/wodify.png')" alt="WODIFY" sizes="70%" contain></v-img>
                </v-col>
                <v-col cols="6">
                    <!-- <div class="rubikMonoOne" style="font-size: 50px;"><h1> USER REGISTER</h1></div> -->
                </v-col>
            </v-row>



            <v-row class="d-flex justify-content-between mt-5" justify="center">
                <v-col cols="12" sm="6" md="8">
                  

                    <v-form @submit.prevent="memberRegister">

                        <v-row>
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">PROFILE IMAGE</v-label></v-col>

                            
                            <v-col cols="8">
                                <v-file-input
                                label="PROFILE"
                                accept="image/*"
                                @change="fileUpdate"
                                ></v-file-input>
                            </v-col>
                        </v-row>
                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">NAME</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field v-model="name" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">EMAIL</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field  v-model="email" type="email" required disabled="">
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">PHONE</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field v-model="phone"  required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">ADDRESS</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field v-model="address"  required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">DEAD LIFT</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field v-model="deadLift"  required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">SQUAT</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field v-model="squat"  required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">BENCH PRESS</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field v-model="benchPress"  required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">ROLE</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field v-model="role"  required readonly="">
                                </v-text-field>
                            </v-col>
                        </v-row>

   

                        <v-btn type="submit" color="primary" block>SIGN UP</v-btn>



                    </v-form>

                </v-col>
            </v-row>

        </v-container>
    </div>
</template>

<script>
import axios from 'axios';
export default {
    props:['propsRole'],
    data(){
        return{
            name:"",
            phone:"",
            email:"",
            address:"",
            deadLift:"",
            squat:"",
            benchPress:"",
            role:"",
            memberImage: null,
        }
    },
    created(){
        this.email = new URL(window.location.href).searchParams.get('email');
        this.role = this.propsRole;
    },
    methods: {
        async memberRegister() {
            try{
                
                let registerData = new FormData();
                registerData.append("name", this.name);
                registerData.append("email", this.email);
                registerData.append("phone", this.phone);
                registerData.append("address", this.address);
                registerData.append("deadLift", this.deadLift);
                registerData.append("squat", this.squat);
                registerData.append("benchPress", this.benchPress);
                registerData.append("role", this.role);
                if(this.memberImage != null){
                    registerData.append("memberImage", this.memberImage);
                }
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member/register`, registerData);
                console.log(response.data.result);
                window.location.href = "/";
            }catch(e){
                console.log(e);
            }
        },
        fileUpdate(event){
            this.memberImage = event.target.files[0];
        }
    }


}
</script>

<style>
.background{
    min-height: 100vh;
    margin: 0;
    background-image: url("@/assets/background.png");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
}
</style>