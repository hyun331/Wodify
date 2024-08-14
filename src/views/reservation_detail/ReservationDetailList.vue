<template>
    <div class="page-container">
    

    <v-container>
      <div>
        <h1 class="title">MY</h1>
        <h1 class="title">RESERVATION</h1>
        </div>
      <v-row>
        <v-col>
          <v-card>
            <v-card-text>
              <v-table >
                <thead>
                    <tr>
                      <th style="font-weight: bold;">DATE</th>
                      <th style="font-weight: bold;">TIME</th>
                      <th style="font-weight: bold;">COACH</th>
                      <th style="font-weight: bold;">RECORD</th></tr>
                </thead>
                <tbody>
                  <tr v-for="r in reservationList" :key="r.id" @click="goToDetail(r.id)" style="cursor: pointer;">
                    <td>{{r.date}}</td>
                    <td>{{r.time}}</td>
                    <td>{{r.coachName}}</td>
                    <td v-if="r.recordId">{{r.recordSnF}}</td><td v-else><v-btn>create</v-btn></td></tr>
                </tbody>
            </v-table>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import axios from 'axios';
export default {
    
  data() {
    return {
      reservationList: []
    };
  },
  async created() {
        try {
          console.log(`${process.env.VUE_APP_API_BASE_URL}`);
            const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservation-detail/mylist`);
            console.log(response)
            this.reservationList = response.data.result.content;
        } catch (error) {
            console.log(error);
        }
  },
  methods:{
    goToDetail(id) {
      this.$router.push(`/reservation-detail/detail/${id}`);
    }
  }
};
</script>

<style>

</style>
