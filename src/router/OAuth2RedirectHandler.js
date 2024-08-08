import axios from "axios";


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
                alert(response.data.result.token);
                //홈화면
                window.location.href="/";
            }catch(e){
                alert(e.response.data.error_message);
                window.location.href="/member/choose/register"
            }
            

        }
    }
}

