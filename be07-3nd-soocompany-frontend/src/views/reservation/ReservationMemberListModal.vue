<template>
    <v-dialog >
        <v-card class="mx-auto">
            <v-card-title class="text-h6 text-center" style="margin-top: 10px;">
                예약 회원 리스트
            </v-card-title>
            <v-card-text style="padding: 20px;">
                <v-row class="d-flex justify-center">
                    <v-table>
                        <tbody>
                          <tr v-for="m in memberList" :key="m.id">
                            
                            <td>{{ m.memberName }}</td>
                          </tr>
                        </tbody>
                      </v-table>
                </v-row>
                <v-row class="d-flex justify-center align-center" style="margin-top: 16px;">
                <RoundedButtonComponent @click="closeModal" text="닫기" class="mx-2"/>
            </v-row>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import RoundedButtonComponent from '@/components/RoundedButtonComponent.vue';
export default {
    props: ['memberList'],
    components:{
        RoundedButtonComponent
    },
    data() {
        return {

        }
    },
    methods: {
        closeModal() {
            this.$emit('update:dialog', false);
        }
    }
}
</script>

