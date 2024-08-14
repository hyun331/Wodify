<template>
    <v-container>
        <h1>Update Post</h1>
        <div v-if="post">
            <v-form @submit.prevent="updatePost">
                <v-text-field label="Title" v-model="post.title" required></v-text-field>
                <v-textarea label="Contents" v-model="post.contents" required></v-textarea>
                <v-select label="Type" v-model="post.type" :items="['NOTICE', 'POST']" required></v-select>

                <v-row>
                    <v-col v-for="file in post.files" :key="file.id" cols="12" sm="6" md="4">
                        <v-img :src="file.url"></v-img>
                        <v-btn color="error" @click="removeImage(file)">Remove</v-btn>
                    </v-col>
                </v-row>

                <input type="file" multiple @change="handleFileUpload" />
                <v-row>
                    <v-col v-for="(file, index) in newFiles" :key="index" cols="12" sm="6" md="4">
                        <v-img :src="file.preview" aspect-ratio="1.75"></v-img>
                        <v-btn color="error" @click="removeNewFile(index)">Remove</v-btn>
                    </v-col>
                </v-row>

                <v-row>
                    <v-btn color="primary" type="submit">Update</v-btn>
                    <v-btn color="secondary" @click="goBackToDetail">Cancel</v-btn>
                </v-row>
            </v-form>
        </div>
        <div v-else>
            <p>Loading post details...</p>
        </div>
    </v-container>
</template>

<script>
import axios from 'axios';

export default {
    props: ['id'],
    data() {
        return {
            post: null,
            removedFileIds: [],
            newFiles: [],
        };
    },
    mounted() {
        this.fetchPostDetail();
    },
    methods: {
        async fetchPostDetail() {
            try {
                const response = await axios.get(`http://localhost:8090/post/detail/${this.id}`);
                this.post = response.data.result;
            } catch (error) {
                console.error('Error fetching post detail:', error);
                alert('Failed to load post details.');
            }
        },
        removeImage(file) {
            this.removedFileIds.push(file.id);
            this.post.files = this.post.files.filter(f => f.id !== file.id);
        },
        handleFileUpload(event) {
            const files = event.target.files;
            for (let i = 0; i < files.length; i++) {
                const reader = new FileReader();
                reader.onload = (e) => {
                    this.newFiles.push({
                        file: files[i],
                        preview: e.target.result
                    });
                };
                reader.readAsDataURL(files[i]);
            }
        },
        removeNewFile(index) {
            this.newFiles.splice(index, 1);
        },
        async updatePost() {
            const formData = new FormData();
            formData.append('title', this.post.title);
            formData.append('contents', this.post.contents);
            formData.append('type', this.post.type);
            this.removedFileIds.forEach(id => formData.append('removedFileIds', id));
            this.newFiles.forEach(file => {formData.append('newFiles', file.file);});

            try {
                const response = await axios.patch(`http://localhost:8090/post/update/${this.id}`, formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                });

                if (response.status === 200) {
                    alert('Post updated successfully.');
                    this.goBackToDetail();
                } else {
                    alert('Failed to update post.');
                }
            } catch (error) {
                console.error('Error updating post:', error);
                alert('An error occurred while updating the post.');
            }
        },
        goBackToDetail() {
            this.$router.push(`/post/detail/${this.id}`);
        },
    },
};
</script>