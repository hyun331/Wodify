<template>
    <div class="background">
        <v-container>
            <v-row class="d-flex justify-content-between mt-5">
                <v-col cols="6">
                    <v-img :src="require('@/assets/wodify.png')" alt="WODIFY"  contain></v-img>
                </v-col>
                <v-col cols="6">

                </v-col>
            </v-row>
            <v-row justify="center" class="d-flex justify-content-between mt-5" style="width: 80%; margin: 0 auto;">
                <v-col cols="6">
                    <v-card >
                        <v-img :src="require('@/assets/wodify.png')" alt="일반회원가입 이미지" height="400" contain></v-img>
                        <br>
                        <v-btn style="width: 90%;">일반회원가입</v-btn>
                    </v-card>
                </v-col>
                <v-col cols="6">
                    <v-card>
                        <v-img :src="require('@/assets/wodify.png')" alt="코치회원가입 이미지" height="400" contain></v-img>
                        <br>
                        <v-btn style="width: 90%;">코치회원가입</v-btn>
                    </v-card>
                </v-col>
            </v-row>
            


        </v-container>
    </div>


</template>

<script>

export default {

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