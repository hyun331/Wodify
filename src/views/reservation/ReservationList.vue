<template>
  <div class="page-container">
    <div class="box right-align">
      <br>
      <span class="boxLocation">
        CROSSFIT RICH NANGOK
      </span>
      <br>
    </div>

    <v-container>
      <div>
        <h1 class="title" style="margin-top: 10px;">RESERVATION</h1>
      </div>
      <v-row>
        <v-col>
          <v-card>
            <v-card-text>
            
              <v-table>
                <thead>
                  <tr>
                    <th style="font-weight: bold;">DATE</th>
                    <th style="font-weight: bold;">TIME</th>
                    <th style="font-weight: bold;">PEOPLE</th>
                    <th style="font-weight: bold;">WOD</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="r in reservationList" :key="r.id" @click="viewDetail()" style="cursor: pointer;">
                    <td>{{ r.date }}</td>
                    <td>{{ r.time }}</td>
                    <td>{{ r.maxPeople }}</td>
                    <ReservationMemberListModal
                    v-model="memberListModal" @update:dialog="memberListModal = $event"
                    :memberList="r.reservationDetails" />
                    <td><v-btn @click="wod">view</v-btn></td>
                  </tr>
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
import ReservationMemberListModal from './ReservationMemberListModal.vue';
export default {
  components :{
    ReservationMemberListModal
  },
  data() {
    return {
      reservationList: [],
      tableHeaders: [
        { title: "ID", key: 'id', align: 'center' },
        { title: 'date', key: 'date', align: 'center' },
        { title: 'time', key: 'time', align: 'center' },
        { title: 'maxPeople', key: 'maxPeople', align: 'center' },
        { title: 'wod', key: 'wod', align: 'center' },
      ],
      memberListModal :false
    };
  },
  async created() {

    try {
      console.log(`${process.env.VUE_APP_API_BASE_URL}`);
      const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/reservation/box/list/1`);
      console.log(response)
      this.reservationList = response.data.result.content;
    } catch (error) {
      console.log(error);
    }
  },
  methods:{
    viewDetail(){
      this.showHoldingModal();
    },
    showHoldingModal() {
      this.memberListModal=true;
    },
    wod() {
      alert("wod");
    }
  }
};
</script>

<style>
.bold {
  font-weight: bold;
}
</style>
