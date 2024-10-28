<template>
    <div class="chooseRegisterBackground">
        <v-container>
            
            <v-row>
                <v-col cols="6">
                    <v-img :src="require('@/assets/wodify.png')" alt="WODIFY" sizes="70%" contain></v-img>
                </v-col>
                <v-col cols="6" hidden>

                </v-col>
            </v-row>

            <!-- <div class="rubikMonoOne">
                SIGN UP
            </div> -->
            <v-row justify="center" class="d-flex justify-content-between mt-5" style="width: 80%; margin: 0 auto;">
                <v-col cols="6">
                    <v-card  class="choose-register-hover-image"  @click="userLogin">
                        <v-img :src=defaultImage alt="일반회원가입 이미지"  contain></v-img>
                        <v-card-title justify="center"  class="text-center  font-choose" >
                            일반 회원가입
                        </v-card-title>
                    </v-card>
                </v-col>
                <v-col cols="6">
                    <v-card  class="choose-register-hover-image" @click="coachLogin"> 
                        <v-img :src=defaultImage alt="코치회원가입 이미지"  contain></v-img>
                        <v-card-title justify="center" class="text-center font-choose">
                            코치 회원가입
                        </v-card-title>
                    </v-card>
                </v-col>
            </v-row>
            


        </v-container>
    </div>


</template>

<script>

export default {
    data(){
        return{
            defaultImage: require('@/assets/home.png'),
           

        }
    },

    methods:{
        userLogin(){
            this.$router.push(`/member/user-register?email=${this.$route.query.email}`);
        },
        coachLogin(){
            this.$router.push(`/member/coach-register?email=${this.$route.query.email}`);
        }
    }

}
</script>


<style>
.chooseRegisterBackground {
    min-height: 100vh;
    margin: 0;
    background-image: url("@/assets/background.png");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
}

.choose-register-hover-image:hover img{
    content: url('@/assets/home_color2.png');
}

div.font-choose{
    font-family: "Do Hyeon";
    font-weight: 500;
    font-size: 30px;
    
}

@import url('https://fonts.googleapis.com/css2?family=Do+Hyeon&display=swap');
.do-hyeon-regular {
    font-family: "Do Hyeon", sans-serif;
    font-weight: 400;
    font-style: normal;
  }
  
</style>