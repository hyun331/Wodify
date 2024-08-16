<template>
    <v-dialog max-width="500px">
        <v-card>
            <v-card-title class="text-h5 text-center">DELETE?</v-card-title>
            <v-card-text class="text-center">
                <v-form @submit.prevent="deleteRecord">
                    <v-btn type="submit">YES</v-btn>
                    <v-btn @click="closeModal">NO</v-btn>
                </v-form>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
import axios from 'axios'

export default {
    props: {
        recordId: {
            type: Number,
            required: true
        }
    },
    methods: {
        async deleteRecord() {
            try {
                await axios.patch(`${process.env.VUE_APP_API_BASE_URL}/record/delete/${this.recordId}`);
                window.location.reload(); // 스무스하게 바꾸고 싶은데.. 일단 보류
            } catch (e) {
                console.log(e);
            }
        },
        closeModal() {
            this.$emit('update:dialog');
        }
    }
}
</script>