<template>
    <div class="background">
        <v-container>

            <v-row class="d-flex justify-content-between mt-5" justify="center">
                <v-col cols="12" sm="6" md="8">
                  
                        <v-row>
                            <v-col v-if="isMemberBaseUrl">
                                <v-img :src="require('@/assets/memberBaseImg.png')" alt="member Image" style="border-radius: 50%; width: 100px; height: 100px;" contain ></v-img>
                            </v-col>
                            <v-col  v-else>
                                <v-img :src="memberInfo.imagePath" style="border-radius: 100%; width: 100px; height: 100px;"></v-img>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">NAME</v-label></v-col>
                            <v-col cols="10">
                                
                                <v-text-field> {{ memberInfo.name }}
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">EMAIL</v-label></v-col>
                            <v-col cols="10">
                                
                                <v-text-field > {{ memberInfo.email }}
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">PHONE</v-label></v-col>
                            <v-col cols="10">
                                
                                <v-text-field> {{ memberInfo.phone }}
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="2"><v-label class="rubikMonoOne" style="font-size: 25px;">ADDRESS</v-label></v-col>
                            <v-col cols="10">
                                
                                <v-text-field> {{memberInfo.address}}
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">DEAD LIFT</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field >{{memberInfo.deadLift}}
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">SQUAT</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field> {{memberInfo.squat}}
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">BENCH PRESS</v-label></v-col>
                            <v-col cols="8">
                                
                                <v-text-field> {{memberInfo.benchPress}}
                                </v-text-field>
                            </v-col>
                        </v-row>

                        <v-row justify="center">
                            <v-col cols="4"><v-label class="rubikMonoOne" style="font-size: 25px;">BOX</v-label></v-col>
                            <v-col cols="5">
                                
                                {{memberInfo.boxName}}
                                
                            </v-col>
                            <v-col cols="3">
                                <v-btn v-if="!isOnHold">정지</v-btn>
                                <v-btn v-else>정지 해제</v-btn>
                            </v-col>
                        </v-row>

   





                </v-col>
            </v-row>
    </v-container>
    </div>
</template>

<script>
import axios from 'axios';
export default{
    data(){
        return{
            memberInfo:{},
            isMemberBaseUrl:false,
            isOnHold:false,

        }
    },
    async created(){
        try{
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/detail`);
            console.log(response.data.result);
            
            this.memberInfo = response.data.result;
            if(this.memberInfo.imagePath === '/assets/memberBaseImg.png'){
                this.isMemberBaseUrl = true;
            }
        }catch(e){
            console.log(e);
        }

    }
}
</script>