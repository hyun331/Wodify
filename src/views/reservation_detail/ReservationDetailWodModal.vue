<template>
    <v-dialog max-width="600px" max-height="auto">
        <v-card>
            <v-card-text>
                <p><strong>Date:</strong> {{ wod.date }}</p>
                <p><strong>Time Cap:</strong> {{ wod.timeCap }}</p>
                <p><strong>Rounds:</strong> {{ wod.rounds }}</p>
                <p><strong>Info:</strong> {{ wod.info }}</p>
                <v-btn class="close-btn hover-btn " @click="closeModal">close</v-btn>
            </v-card-text>
        </v-card>
    </v-dialog>
</template>

<script>
export default {
    props: ['wod'],
    methods: {
        closeModal() {
            this.$emit('update:dialog');
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
.close-btn {
    display: block;
    margin-left: auto;
    margin-top: 20px;
}
</style>