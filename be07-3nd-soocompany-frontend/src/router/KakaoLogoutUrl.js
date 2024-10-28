
const CLIENT_ID="2d129c6af1317e9dc12a8669b1957416";
const LOGOUT_REDIRECT_URL = "http://localhost:8091";


export const KAKAO_LOGOUT_URL = `https://kauth.kakao.com/oauth/logout?client_id=${CLIENT_ID}&logout_redirect_uri=${LOGOUT_REDIRECT_URL}`;