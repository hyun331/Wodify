import axios from "axios";
import { jwtDecode } from "jwt-decode";

export default{

    name: 'OAuth2RedirectHandler',
    data(){
        return{

        }
    },
    created(){
        const code = new URL(window.location.href).searchParams.get('code');
        if(code){
            this.getAuthToken(code);
        }
    },
    methods:{
        async getAuthToken(code){
            try{
                const response = await axios.get(`http://localhost:8090/member/auth/kakao/callback?code=${code}`);
                //홈화면
                const token = response.data.result.token;
                const refreshToken = response.data.result.refreshToken;
                const role = jwtDecode(token).role;
                const memberId = jwtDecode(token).sub; // JWT의 subject로 설정된 memberId 추출


                localStorage.setItem('token', token);
                localStorage.setItem('refreshToken', refreshToken);
                localStorage.setItem('role', role);
                localStorage.setItem('memberId', memberId); // memberId를 저장

                window.location.href="/";
            }catch(e){
                // alert(e.response.data.result);
                window.location.href=`/member/choose/register?email=${e.response.data.result}`;
            }
            

        }
    }
}

