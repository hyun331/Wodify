<template>
    <v-dialog v-model="dialog" max-width="400px">
        <v-card>
            <v-card-title class="headline">{{ dialogTitle }}</v-card-title>
            <v-card-text>{{ dialogText }}</v-card-text>
            <v-card-actions>
                <v-btn class="hover-btn" text @click="closeModal">OK</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>
<script>
export default {
    props: ['dialogTitle', 'dialogText'],
    methods: {
        closeModal() {
            this.$emit('update:dialog', false);
        }
    }
}
</script>
<style scoped>
.hover-btn {
    background-color: white;
    color: black;
    transition: background-color 0.3s ease, color 0.3s ease;
  }
  
  .hover-btn:hover {
    background-color: black;
    color: white;
  }
</style>