
</template>
<script>
// import axios from 'axios';
import { KAKAO_AUTH_URL } from '@/router/Oauth';
export default{
    methods:{
        kakaoLogin(){
            window.location.href=KAKAO_AUTH_URL;
        }

    }

}
</script>