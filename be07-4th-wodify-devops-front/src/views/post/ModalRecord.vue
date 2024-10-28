<template>
    <v-dialog v-model="dialog" max-width="1000px">
        <v-card>
            <v-card-text>
                <div class="flex-container">
                    <div class="date-picker-box">
                        <v-date-picker v-model="selectedDate" @update:model-value="onDateSelected" />
                    </div>

                    <div class="wod-box">
                        <div v-if="!wod" class="no-wod">WOD가 없습니다.</div>
                        <div v-else>
                            <div class="wod-item-box">
                                <strong>Time Cap:</strong> {{ wod.timeCap }}
                            </div>
                            <div class="wod-item-box">
                                <strong>Rounds:</strong> {{ wod.rounds }}
                            </div>
                            <div class="wod-item-box">
                                <strong>Info:</strong> {{ wod.info }}
                            </div>
                            <div v-for="(detail, index) in wod.wodDetResDtoList" :key="index" class="wod-detail-box">
                                <div>{{ detail.name }}: {{ detail.contents }}</div> <!-- 한 줄로 name과 contents 표시 -->
                            </div>
                        </div>
                    </div>

                    <div class="record-display">
                        <div v-if="!record" class="no-record">기록이 없습니다.</div>
                        <div v-else>
                            <div class="record-item-box">
                                <div class="record-item"><strong>SNF:</strong> {{ snfStatus }}</div>
                            </div>
                            <div class="record-item-box">
                                <div class="record-item"><strong>Time:</strong> {{ formattedTime }}</div>
                            </div>
                            <div class="comments-box">
                                <strong>Comments:</strong>
                                <div class="comments-content">{{ record.comments }}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </v-card-text>
            <v-card-actions>
                <v-btn class="custom-btn" style="margin-right: 8px;"  @click="insertRecord">Insert</v-btn>
                <v-btn class="custom-btn" style="margin-right: 8px;"  @click="closeModal">Close</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, computed, defineEmits } from 'vue';
import axios from 'axios';
import { format } from 'date-fns';

const dialog = ref(true);
const selectedDate = ref(null);
const record = ref(null);
const wod = ref(null);
const emit = defineEmits(['close', 'insert']);

const closeModal = () => {
    dialog.value = false;
    emit('close');
};

const insertRecord = () => {
    if (record.value || wod.value) {
        emit('insert', { record: record.value, wod: wod.value });
        dialog.value = false;
    }
};

const onDateSelected = async (date) => {
    if (date) {
        const formattedDate = format(new Date(date), 'yyyy-MM-dd');

        try {
            record.value = null;
            wod.value = null;

            const wodResponse = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/wod/find/${formattedDate}`);
            if (wodResponse.status === 200) {
                wod.value = wodResponse.data.result;
            }

            const recordResponse = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/post/record/${formattedDate}`);
            if (recordResponse.status === 200) {
                record.value = recordResponse.data.result;
            }
        } catch (error) {
            console.error('Error fetching data:', error.response ? error.response.data : error.message);
        }
    }
};

const snfStatus = computed(() => {
    return record.value ? (record.value.snf === 'Y' ? '성공' : '실패') : '';
});

const formattedTime = computed(() => {
    return record.value && record.value.time ? record.value.time.slice(0, 5) : '';
});
</script>

<style scoped>
.flex-container {
    display: flex;
    gap: 20px;
    width: 100%;
}

.date-picker-box {
    flex: 0 0 200px; /* date-picker의 너비 고정 */
}

.wod-box, .record-display {
    flex: 1;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 8px;
    background-color: #f9f9f9;
    text-align: center;
    display: flex;
    flex-direction: column;
    justify-content: center;
    overflow-y: auto;
}

.no-wod, .no-record {
    color: #888;
    font-size: 14px;
    margin-top: auto;
    margin-bottom: auto;
}

.wod-item-box, .record-item-box {
    margin-bottom: 10px;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    background-color: #fff;
    text-align: left;
}

.wod-detail-box {
    padding: 8px;
    margin-bottom: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    background-color: #fff;
    text-align: left;
}

.comments-box {
    max-height: 150px;
    overflow-y: auto;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
    background-color: #fff;
    text-align: left;
}

.comments-content {
    white-space: pre-wrap;
}

.custom-btn {
    background-color: black !important;
    color: white !important;
    font-weight: 600; /* 글씨 두께를 조금 두껍게 설정 */
  }
</style>
