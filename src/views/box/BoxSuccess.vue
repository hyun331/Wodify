<template>
    <div class="container">
        <v-container>
            <div>
                <h1 class="title">BOX</h1>
                <h1 class="title">SUCCESS</h1>
                <br><br>
                <v-row justify="center">
                    <v-col>
                        <v-card>
                            <v-card-title class="text-h5 text-center">
                                등록이 완료되었습니다!
                            </v-card-title>
                            <br><br><br><br><br><br><br><br><br><br><br><br><br>
                            <v-card-text>
                                <v-row justify="center">
                                    <v-col cols="6" sm="4">
                                        <v-btn color="black" @click="myBox" block>MY BOX</v-btn>
                                    </v-col>
                                    <v-col cols="6" sm="4">
                                        <v-btn color="black" @click="home" block>HOME</v-btn>
                                    </v-col>
                                </v-row>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </v-row>
            </div>
        </v-container>
    </div>
</template>

<script>
export default {
    data() {
        return {
        };
    },
    methods: {
        myBox() {
            window.location.href = '/';
        },
        home() {
            window.location.href = '/';
        }
    }
};
</script>

<style>
.container {
    background-color: #D9D9D9;
    min-height: 100vh;
}
</style>
