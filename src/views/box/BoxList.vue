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
                                        <tr v-for="b in boxList" :key="b.id">
                                            <td>{{ b.name }}</td>
                                            <td>{{ b.address }}</td>
                                            <td>{{ b.operating_hours }}</td>
                                            <td>{{ b.fee }}</td>
                                        </tr>
                                    </tbody>
                                </v-table>
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

    }
};
</script>

<style>
.container {
    background-color: #D9D9D9;
    min-height: 100vh;
}
</style>
