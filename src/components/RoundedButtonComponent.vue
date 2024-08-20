<template>
    <button class="rounded-button" :type="buttonType">
        <i class="fas fa-map-marker-alt"></i>
        <span class="button-text">{{ text }}</span>
    </button>
</template>
<script>
export default {
    props: ['text', 'buttonType']
}
</script>
<style scoped>
.rounded-button {
    background-color: #000000; /* 버튼 배경색 */
    border: none;
    padding: 15px 32px;
    text-align: center;
    text-decoration: none;
    display: inline-block;
    border-radius: 50px; /* 둥근 정도 조절 */
    cursor: pointer;
}
.button-text {
    /* 텍스트 스타일 */
    color: white; /* 텍스트 색상 */
    font-size: 16px;
    /* 필요한 다른 스타일 추가 */
  }
.rounded-button:hover {
    border: 2px solid white; /* 버튼에 흰색 테두리 추가 */
    background-color: rgb(48, 48, 48); /* 버튼 배경색을 조금 어두운 색으로 변경 */
    outline: none; /* 기본 포커스 아웃라인 제거 */
}
</style>