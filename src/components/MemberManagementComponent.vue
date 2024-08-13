<template>
    <div class="background">
    <v-container>
        <v-row class="d-flex justify-content-between mt-5">
            <!-- <v-col>
                <v-form @submit.prevent="searchProducts">

                    <v-row>
                        <v-col cols="auto">
                            <v-select
                            v-model="searchType"
                            :items="searchOptions"
                            item-title="text"
                            item-value="value"
                            >
                            </v-select>
                        </v-col>
                        <v-col>
                            <v-text-field
                                v-model="searchValue" label="Search"
                            >

                            </v-text-field>
                        </v-col>

                        <v-col cols="auto">
                            <v-btn type="submit">
                                검색
                            </v-btn>
                            
                        </v-col>
                    
                    </v-row>
                </v-form>
            </v-col> -->
            <!-- <v-col cols="auto" v-if="!isAdmin">
                <v-btn color="secondary" class="mr-2">장바구니</v-btn>
                <v-btn color="success">주문하기</v-btn>
            </v-col> -->
<!-- 
            <v-col cols="auto" v-if="isAdmin">
                <v-btn href="/product/create" color="success">상품등록</v-btn>    
            </v-col> -->
            <h1>회원관리 {{isCEO}}</h1>
        </v-row>
        <v-row>
            <v-col>
                <v-card>
                    <v-card-title class="text-h6 text-center" style="font-family: ONE-Mobile-POP;">
                        <!-- {{pageTitle}} -->
                    </v-card-title>
                    <v-card-text>
                        <v-table>
                            <thead>
                                <tr>
                                    <!-- <th>ID</th> -->
                                    <th>NAME</th>
                                    <th>EMAIL</th>
                                    <th>등록정보</th>
                                    <th>연장</th>
                                    <th>등록일</th>
                                    <th>종료일</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="u in userList " :key="u.id">
                                    <!-- <td><v-img :src="p.imagePath" style="height: 100px; width:auto;"></v-img></td> -->
                                    <!-- <td>{{u.id}}</td> -->
                                    <td>{{u.name}}</td>
                                    <td>{{u.email}}</td>
                                    <td>{{u.state}}</td>
                                    <td><v-btn @click="showRegistrationModal(u.email, u.endDate)">연장</v-btn></td>
                                    <td>{{u.registrationDate}}</td>
                                    <td>{{u.endDate}}</td>
                                </tr>
                            </tbody>

                        </v-table>
                    </v-card-text>
                </v-card>
            </v-col>
        </v-row>

        <RegistrationCreateModal 
        v-model="registrationModal" 
        :modalUserEmail="modalUserEmail"
        :modalNextStartDate="modalNextStartDate"
        @update:dialog="registrationModal = $event"
        >

        </RegistrationCreateModal>

    </v-container>
</div>
</template>

<script>
import axios from 'axios';
import RegistrationCreateModal from '@/views/registrationInfo/RegistrationCreateModal.vue';
export default{
    //isAmin이라는 변수를 넘겨받을 수 있다.
    props:['isCEO'],
    components:{
        RegistrationCreateModal,
    },
    data(){
        return{

            // //검색
            // searchType:"optional",
            // searchOptions:[
            //     {text:"선택", value:"optional"},
            //     {text:"상품명", value:"name"},
            //     {text:"카테고리", value:"category"}
            // ],
            // searchValue: "",

            userList:[],
            pageSize: 5,
            currentPage:0,
            isLastPage: false,
            isLoading: false,
            registrationModal: false,
            modalUserEmail : "",
            modalNextStartDate: "",

            
        }
    }, 
    created(){
        // if(this.isCEO){
        //     this.loadCoach();

        // }
        this.loadUser();
        window.addEventListener('scroll', this.scrollPagination);
    },
    beforeUnmount(){
        window.removeEventListener('scroll', this.scrollPagination);
    },

    methods:{
        searchProducts(){
            this.productList = [];
            this.currentPage = 0;
            this.isLastPage = false;
            // this.isLoading = false;

            this.loadProduct();
        },
        deleteProduct(productId){
            alert(productId);
        },
        async loadUser(){
            try{
                if(this.isLoading || this.isLastPage){
                    return;
                }

                this.isLoading=true;

                let params ={
                    size: this.pageSize,
                    page: this.currentPage,
                };

                //params : {size:5, page=0, category:"fruits"}형태 또는{size:5, page=0, name:"apple"}
                // if(this.searchType === 'name'){
                //     params.searchName = this.searchValue;
                // }else if(this.searchType === 'category'){
                //     params.category = this.searchValue;
                // }
                //localhost:8080/product/list?category=fruits&size=5&pag0
                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/list/user`, {params});
                const additionalData = response.data.result.content;
                console.log(additionalData);
            
                this.isLastPage = response.data.isLastPage;
                // if(additionalData.length==0){
                //     this.isLastPage = true;
                //     return;
                // }
                //두 배열을 합치는 문법
                this.userList =[...this.userList, ...additionalData];
                this.currentPage++;
                this.isLoading = false;
                console.log(response.data);
            }catch(e){
                console.log(e);
            }
            
        },
        scrollPagination(){
            const isBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - 100;
            if(isBottom && !this.isLastPage && !this.isLoading){
                this.loadUser();
            }
        },
        showRegistrationModal(userEmail, userEndDate){
            this.registrationModal=true;
            this.modalUserEmail = userEmail;
            this.modalNextStartDate = userEndDate;
        }

    }
    

}

</script>