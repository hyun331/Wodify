
const CLIENT_ID="2d129c6af1317e9dc12a8669b1957416";
const REDIRECT_URL = "https://www.wodify.site/member/auth/kakao/callback";

export const KAKAO_AUTH_URL = `https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=${CLIENT_ID}&redirect_uri=${REDIRECT_URL}`;