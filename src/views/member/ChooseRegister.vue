<template>
    <div class="background">
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
                    <v-card  @mouseover="changeImage('user')" @mouseleave="resetImage('user')">
                        <v-img :src=userImage alt="일반회원가입 이미지"  contain></v-img>
                        <v-card-title justify="center" class="text-cneter text-h5">
                            일반회원가입
                        </v-card-title>
                    </v-card>
                </v-col>
                <v-col cols="6">
                    <v-card  @mouseover="changeImage('coach')"  @mouseleave="resetImage('coach')"> 
                        <v-img :src=coachImage alt="코치회원가입 이미지"  contain></v-img>
                        <v-card-title justify="center" class="text-cneter text-h5">
                            코치회원가입
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
            userImage : require('@/assets/home.png'),
            coachImage : require('@/assets/home.png'),
            hoverImage: require('@/assets/home_color2.png'),

        }
    },
    methods:{
        changeImage(role){
            if(role === 'user'){
                this.userImage = this.hoverImage;
            }else{
                this.coachImage = this.hoverImage;
            }
        },
        resetImage(role){
            if(role === 'user'){
                this.userImage = this.defaultImage;
            }else{
                this.coachImage = this.defaultImage;
            }
        }
    }

}
</script>


<style>
.background {
    height: 100vh;
    overflow: hidden;
    margin: 0;
    background-image: url("@/assets/background.png");
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center;
}
</style>