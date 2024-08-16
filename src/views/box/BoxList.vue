<template>
    <div class="container">
        <v-container>
            <div>
                <h1 class="title">BOXES</h1>
                <br><br>
                <v-row justify="center">
                    <v-col>
                        <v-card>
                            <v-card-text>
                                <v-table>
                                    <thead>
                                        <tr>
                                            <th style="font-weight: bold;">NAME</th>
                                            <th style="font-weight: bold;">ADDRESS</th>
                                            <th style="font-weight: bold;">TIME</th>
                                            <th style="font-weight: bold;">FEE</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="(box, index) in boxes" :key="`box-${index}`">
                                            <td>{{ box.name }}</td>
                                            <td>{{ box.address }}</td>
                                            <td>{{ box.operatingHours }}</td>
                                            <td>{{ box.fee }}</td>
                                        </tr>
                                    </tbody>
                                </v-table>
                            </v-card-text>
                        </v-card>
                    </v-col>
                </v-row>
            </div>

            <v-pagination v-model="currentPage" :length="totalPages" :total-visible="5" class="mt-4"
                @input="fetchBoxes" />
        </v-container>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            currentPage: 1, 
            itemsPerPage: 10, 
            boxes: [], 
            totalPages: 1, 
        };
    },
    mounted() {
        this.fetchBoxes(); 
    },
    methods: {
        async fetchBoxes() {
            try {
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/box/list`, {
                    params: {
                        page: this.currentPage - 1, 
                        size: this.itemsPerPage, 
                    },
                });

                this.boxes = response.data.result.content;
                this.totalPages = response.data.result.totalPages;
            } catch (error) {
                alert("BOX LIST 불러오기가 실패 하였습니다.");
            }
        },
    },
};
</script>



<style>
@import url('https://fonts.googleapis.com/css2?family=Rubik+Mono+One&display=swap');

.title {
    font-family: 'Rubik Mono One', sans-serif;
}

.container {
    background-color: #D9D9D9;
    min-height: 100vh;
}
</style>
