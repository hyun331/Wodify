<template>
    <v-container>
        <v-row class="d-flex justify-content-between mt-5" >
            <v-col>
                <v-card>
                    <v-card-title class="text-h6 text-center" style="font-family: ONE-Mobile-POP;">
                        명예의 전당
                    </v-card-title>
                    <v-card-text>
                        <v-table>
                            <thead>
                                <tr>
                                    <th>RANK</th>
                                    <th>NAME</th>
                                    <th>EMAIL</th>
            
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="u in userList " :key="u.id">
                                    

                                    <td>
                                        <v-img v-if="u.rank != 4 && u.rank!=5" :src="require(`@/assets/${u.rank}_hallOfFameImg.png`)" alt="member Image" style="width: 50px; height: 50px;" contain ></v-img>
                                        <v-img v-else :src="require(`@/assets/blackHallOfFameImg.png`)" alt="member Image" style=" width: 50px; height: 50px;" contain ></v-img>
                                    </td>
                                    <td>{{u.name}}</td>
                                    <td>{{u.email}}</td>

                                </tr>
                            </tbody>

                        </v-table>
                    </v-card-text>
                </v-card>
            </v-col>
            </v-row>

    </v-container>

</template>

<script>
import axios from 'axios';
export default {
    data() {
        return{
            userList : [], 
        }
        

    },
    async created(){
        try{
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/hallOfFame`);
            this.userList = response.data.result;
            console.log(response.data.result);
        }catch(e){
            console.log(e);
        }


    },
    methods: {

    }
}
</script>