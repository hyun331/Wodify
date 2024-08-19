<template>
    <div class="container">
        <v-container>
            <div>
                <h1 class="title">BOXES</h1>
                <br><br>
                <v-row>
                    <v-col>
                        <v-form @submit.prevent="searchBoxes">
                            <v-row>
                                <v-col cols="auto">
                                    <v-select v-model="searchType" :items="searchOptions" item-title="text"
                                        item-value="value" label="Search By">
                                    </v-select>
                                </v-col>
                                <v-col>
                                    <v-text-field v-model="searchValue" label="Search">
                                    </v-text-field>
                                </v-col>
                                <v-col cols="auto">
                                    <v-btn type="submit" class="search-button">🔍</v-btn>
                                </v-col>
                            </v-row>
                        </v-form>
                    </v-col>
                </v-row>
                <v-row justify="center">
                    <v-col>
                        <v-card>
                            <v-card-text>
                                <v-table style="table-layout: fixed; width: 100%;">
                                    <thead>
                                        <tr>
                                            <th style="font-weight: bold; width: 10%;">LOGO</th>
                                            <th style="font-weight: bold; width: 25%;">NAME</th>
                                            <th style="font-weight: bold; width: 25%;">ADDRESS</th>
                                            <th style="font-weight: bold; width: 20%;">TIME</th>
                                            <th style="font-weight: bold; width: 20%;">FEE</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr v-for="(box, index) in boxes" :key="`box-${index}`"
                                            @click="goToDetail(box.id)">
                                            <td>
                                                <v-img :src="box.logoPath"
                                                    style="height: 50px; width: 50px; border-radius: 50%;"></v-img>
                                            </td>
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
                <v-pagination v-model="currentPage" :length="totalPages" :total-visible="5" class="mt-4"
                    @input="fetchBoxes" />
            </div>
        </v-container>
    </div>
</template>

<script>
import axios from 'axios';

export default {
    data() {
        return {
            currentPage: 1,
            itemsPerPage: 1,
            boxes: [],
            totalPages: 1,
            searchType: 'name',
            searchValue: '',
            searchOptions: [
                { text: 'NAME', value: 'name' },
                { text: 'ADDRESS', value: 'address' }
            ],
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
                        ...(this.searchType === 'name' ? { searchName: this.searchValue } : {}),
                        ...(this.searchType === 'address' ? { searchAddress: this.searchValue } : {})
                    }
                });

                this.boxes = response.data.result.content || [];
                this.totalPages = response.data.result.totalPages || 1;
            } catch (error) {
                console.error('Box 목록을 가져오는 중 오류 발생:', error);
            }
        },
        searchBoxes() {
            this.currentPage = 1;
            this.fetchBoxes();
        },
        goToDetail(id) {
            this.$router.push(`/box/detail/${id}`);
        },
    },
    watch: {
        currentPage() {
            this.fetchBoxes();
        },
    },
};
</script>


<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Rubik+Mono+One&display=swap');

.title {
    font-family: 'Rubik Mono One', sans-serif;
}

.container {
    background-color: #D9D9D9;
    min-height: 100vh;
}

.search-button {
    margin-top: 12px; /* 버튼의 위쪽 여백을 16px로 설정하여 아래로 이동 */
    border-radius: 100px; /* 버튼 모서리를 둥글게 설정 (값 조정 가능) */
    padding: 8px 16px; /* 버튼의 패딩 조정 */
}
</style>