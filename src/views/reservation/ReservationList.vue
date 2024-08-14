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
              <v-data-table :headers="tableHeaders" :items="reservationList" class="elevation-1" show-expand hide-default-footer>
                <template v-slot:[`item.orderStatus`]="{ item }">
                    <span :class="{ 'text-red': item.orderStatus === 'CANCELD' }">{{ item.orderStatus }}</span>
                </template>
              
                <template v-slot:expanded-row="{item}">
                    <v-row>
                        <v-col>
                          
                            <v-list-item v-for="detailItem in item.reservationDetails" :key="detailItem.id">
                                <v-list-item-content>
                                    <v-list-item-title>
                                        {{ detailItem.memberName }}
                                    </v-list-item-title>
                                </v-list-item-content>
                            </v-list-item>
                        </v-col>
                    </v-row>
                </template>
            </v-data-table>
            <br>
            <br>
            <br>
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
                    <tr v-for="r in reservationList" :key="r.id">
                    <td>{{r.date}}</td>
                    <td>{{r.time}}</td>
                    <td>{{r.maxPeople}}</td>
                    <td><v-btn>view</v-btn></td>
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
export default {
    
  data() {
    return {
      reservationList: [],
      tableHeaders:[
                {title:"ID", key:'id', align:'center'},
                {title:'date',key:'date', align:'center'},
                {title:'time',key:'time', align:'center'},
                {title:'maxPeople',key:'maxPeople', align:'center'}
            ],
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
    }
};
</script>

<style>
.bold {
  font-weight: bold;
}
</style>
