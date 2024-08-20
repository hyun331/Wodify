<template>
    <div class="background">
        <!-- <img src="@/assets/wodify.png" class="header-image" /> -->
        <div class="slider">
            <div class="logo-overlay">
                <img src="@/assets/wodify.png" alt="Logo" class="logo-image" />
            </div>
            <div class="slider-wrapper">
                <div class="slider-container" :style="sliderStyle">
                    <img v-for="(image, index) in images" :key="index" :src="image" alt="Banner Image"
                        class="slider-image" />
                </div>
            </div>
            <button class="left-arrow" @click="prevImage">◀</button>
            <button class="right-arrow" @click="nextImage">▶</button>
        </div>

        <div class="container-title">
            <h2>RECOMMAD BOXES</h2>


        </div>
        <div class="card-container">


            <div class="card" v-for="course in courses" :key="course.id">
                <img :src="course.image" alt="Course Image">
                <h3>{{ course.title }}</h3>
                <p>{{ course.description }}</p>
            </div>
        </div>
        <FooterComponent />
    </div>
</template>

<script>
import FooterComponent from '@/components/FooterComponent.vue';
export default {
    data() {
        return {
            currentImageIndex: 0,
            images: [
                require('@/assets/home1.jpg'),
                require('@/assets/home2.jpg'),
                require('@/assets/home3.jpg'),
                require('@/assets/home1.jpg'),
                require('@/assets/home2.jpg'),
                require('@/assets/home3.jpg'),
            ],
            interval: null,
            textOverlays: [
                "Welcome to our website!",
                "Check out our new features!",
                "Join us today!",
            ],
            courses: [
                {
                    id: 1,
                    image: require('@/assets/home.png'),
                    title: 'you can do it',
                    description: '요가의 기초이론, 호흡의 역할과 활용, 핸즈온, 변형·감화된 아사나를 배우고 조금 더 깊이있고 다양한 아사나를 배우는 과정'
                },
                {
                    id: 1,
                    image: require('@/assets/home.png'),
                    title: 'you can do it',
                    description: '요가의 기초이론, 호흡의 역할과 활용, 핸즈온, 변형·감화된 아사나를 배우고 조금 더 깊이있고 다양한 아사나를 배우는 과정'
                },
                {
                    id: 1,
                    image: require('@/assets/home.png'),
                    title: 'you can do it',
                    description: '요가의 기초이론, 호흡의 역할과 활용, 핸즈온, 변형·감화된 아사나를 배우고 조금 더 깊이있고 다양한 아사나를 배우는 과정'
                },
                {
                    id: 1,
                    image: require('@/assets/home.png'),
                    title: 'you can do it',
                    description: '요가의 기초이론, 호흡의 역할과 활용, 핸즈온, 변형·감화된 아사나를 배우고 조금 더 깊이있고 다양한 아사나를 배우는 과정'
                },
            ]
        };
    },
    components: {
        FooterComponent
    },
    computed: {
        sliderStyle() {
            return {
                transform: `translateX(-${this.currentImageIndex * 100}%)`,
                transition: 'transform 0.5s ease-in-out',
                width: `${this.images.length * 100}%`,
            };
        },

    },
    methods: {
        nextImage() {
            this.currentImageIndex = (this.currentImageIndex + 1) % this.images.length;
        },
        prevImage() {
            this.currentImageIndex = (this.currentImageIndex - 1 + this.images.length) % this.images.length;
        },
        startAutoSlide() {
            this.interval = setInterval(this.nextImage, 4000); // Change image every 4 seconds
        },
        stopAutoSlide() {
            clearInterval(this.interval);
        },
    },
    mounted() {
        this.startAutoSlide();
    },
    beforeUnmount() {
        this.stopAutoSlide();
    },
};
</script>

<style scoped>
.background {
    min-height: 100vh;
    background-color: white;
}

.slider {
    position: relative;
    width: 100vw; /* 전체 너비를 페이지 전체로 확장 */
    height: 900px;
    overflow: hidden;
    margin: 0; /* 여백 제거 */
    border: none; /* 테두리 제거 */
    border-radius: 0; /* 모서리 둥글림 제거 */
    /*     
    position: relative;
    width: 85vw;
    height: 900px;
    overflow: hidden;
    margin: 0 auto;
    border: 1px solid #ddd;
    border-radius: 4px; */
}

.logo-overlay {
    position: absolute;
    top: 20px; /* 상단에서 20px 아래 */
    left: 30%;
    transform: translateX(-50%);
    z-index: 15;
}

.logo-image {
    width: 700px; /* 로고 크기 */
    height: auto;
    /* 원하는 경우 추가 스타일 적용 */
}

.slider-wrapper {
    display: flex;
    width: 100%;
    height: 100%;
}

.slider-container {
    display: flex;
    width: 100%;
    height: 100%;
}

.slider-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
    /* Adjusts image to cover container without distortion */
    flex-shrink: 0;
    /* Ensures images don't shrink */
    filter: brightness(70%);
    /* Reduce brightness to 70% */
    opacity: 0.8;
}

.left-arrow,
.right-arrow {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    color: white;
    border: none;
    padding: 10px;
    cursor: pointer;
    z-index: 10;
}

.left-arrow {
    left: 10px;
    color: #5b5b5b4b;
}

.right-arrow {
    right: 10px;
    color: #5b5b5b4b;
}

.left-arrow:hover,
.right-arrow:hover {
    background-color: rgba(190, 190, 190, 0.077);
}


.card-container {
    margin-left: 150px;
    margin-right: 150px;
    display: flex;
    justify-content: space-between;

}

.card {
    width: 20%;
    /* 카드 너비 조절 */
    text-align: center;
    padding: 20px;
    border: 1px solid #ccc;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
    transition: all 0.3s ease;
}

.card img {
    max-width: 100%;
}

.container-title {
    text-align: center;
    margin: 40px;
}

.card:hover {
    transform: scale(1.05);
    /* 크기 5% 증가 */
    box-shadow: 0px 5px 10px rgba(0, 0, 0, 0.2);
    /* 그림자 추가 */
}
</style>