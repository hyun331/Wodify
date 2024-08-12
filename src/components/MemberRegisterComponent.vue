<template>
    <div class="background1">
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
                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">NAME</v-label></v-col>
                            <v-col cols="10">
                                
                                <v-text-field v-model="name" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">EMAIL</v-label></v-col>
                            <v-col cols="10">
                                
                                <v-text-field  v-model="email" type="email" required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">PHONE</v-label></v-col>
                            <v-col cols="10">
                                
                                <v-text-field v-model="phone"  required>
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">ADDRESS</v-label></v-col>
                            <v-col cols="10">
                                
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

   

                        <v-btn type="submit" color="primary" block>등록</v-btn>



                    </v-form>

                </v-col>
            </v-row>

        </v-container>
    </div>
</template>

<script>
export default {
    data(){
        return{
            name:"",
            phone:"",
            email:"",
            address:"",
            deadLift:"",
            squat:"",
            benchPress:"",
        }
    },
    methods: {
        memberRegister() {

        }
    }


}
</script>

<style>
.background1 {
    min-height: 100vh;
    margin: 0;
    background-image: url("@/assets/background.png");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
}
</style>