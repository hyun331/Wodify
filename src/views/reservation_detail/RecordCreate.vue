<template>
<div class="page-container">
   <v-container>
        <v-row justify="center">
            <v-col cols="12" sm="2" md="4">
                <v-card>
                    <v-card-text>
                        <v-form @submit.prevent="">
                            <v-radio-group inline>
                            <v-radio label="Radio One" value="one"></v-radio>
                            <v-radio label="Radio Two" value="two"></v-radio>
                            </v-radio-group>
                            <v-text-field label="카테고리명" v-model="category" required></v-text-field>
                            <v-text-field label="가격" v-model="price" required></v-text-field>
                            <v-btn type="submit" color="primary" block>등록</v-btn>
                        </v-form>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>
    </v-container>
</div>
</template>

<script>
export default{
    data(){
        return{

        }
    }
}
</script>