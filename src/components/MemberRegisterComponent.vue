<template>
    <v-container >
        <v-row class="d-flex justify-content-between mt-5">
            <v-col cols="12" sm="6" md="8">
                <v-card>
                    <v-card-title>회원가입</v-card-title>
                    <v-card-text>
                        <v-form @submit.prevent="memberRegister">
                            <v-text-field label="이름" v-model="name" prepend-icon="mdi-account" required>
                            </v-text-field>

                            <v-text-field label="email" v-model="email" type="email" prepend-icon="mdi-email"
                                required>
                            </v-text-field>

                            <v-text-field label="password" v-model="password" prepend-icon="mdi-lock"
                                type="password" required>
                            </v-text-field>

                            <v-text-field label="도시" v-model="city" prepend-icon="mdi-city">
                            </v-text-field>

                            <v-text-field label="상세주소" v-model="street" prepend-icon="mdi-home">
                            </v-text-field>

                            <v-text-field label="우편변호" v-model="zipcode" prepend-icon="mdi-mailbox">
                            </v-text-field>
                            <v-btn type="submit" color="primary" block>등록</v-btn>



                        </v-form>
                    </v-card-text>
                    
                </v-card>
                
            </v-col>
        </v-row>
    </v-container>

</template>

<script>
export default{


}
</script>