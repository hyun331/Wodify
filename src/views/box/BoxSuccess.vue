<template>
    <v-app-bar>
        <v-col class="text-center">header자리</v-col>
    </v-app-bar>
    <!-- <p style="position: absolute;width: 1920px;height: 189px;left: 0px;top: 117px;background: #797876;">
    </p> -->
    <br><br><br><br><br><br><br>
    <p class="text-center">등록이 완료되었습니다!</p>
    <br><br><br><br><br><br><br><br>
    <v-col>
        <v-btn style="
        position: absolute;
        width: 150px;
        height: 50px;
        left: 300px;
        top: 500px;

        background: #D9D9D9;
        border-radius: 5px;"
        
        :to="{path: '/box/detail'}"
        >
            내 박스 페이지
        </v-btn>

        <v-btn style="
        position: absolute;
        width: 150px;
        height: 50px;
        left: 600px;
        top: 500px;

        background: #D9D9D9;
        border-radius: 5px;"
        :to="{path: '/'}"
        >
            홈 화면 가기
        </v-btn>

    </v-col>


</template>

<script>
export default {

}
</script>