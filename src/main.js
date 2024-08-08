import { createApp } from 'vue'
import App from './App.vue' 
import router from '@/router/index.js'
import vuetify from './plugins/vuetify';
import axios from 'axios';


const app = createApp(App);

axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`;

        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
)

axios.interceptors.response.use(
    response => response,
    async error => {
        if (error.response && error.response.status === 401) {
            const refreshToken = localStorage.getItem('refreshToken');
            try {
                localStorage.removeItem('token');
                const response = await axios.post(`${process.env.VUE_APP_API_BASE_URL}/member/refresh-token`, { refreshToken });
                console.log(response.data.result.token);
                localStorage.setItem('token', response.data.result.token);
                window.location.reload();
            }catch(e){
                localStorage.clear();
                window.location.href = "/login";
            }
            
        }
        return Promise.reject(error);

    }
)



app.use(router);
app.use(vuetify);
app.mount('#app');