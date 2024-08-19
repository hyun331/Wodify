<template>
    <div class="background">
    <v-container>
        <v-row class="d-flex justify-content-between mt-5">
            <v-col>
                <v-form @submit.prevent="searchMember">
                    <v-row>
                        <v-col>
                            <v-text-field
                                v-model="searchName" label="Search"
                            >

                            </v-text-field>
                        </v-col>

                        <v-col cols="auto" class="noto-sans">
                            <v-btn type="submit" icon style="margin-top: 16px; margin-right: 20px;">
                                <v-icon>mdi-magnify</v-icon>
                              </v-btn>
                            <!-- <v-btn type="submit">
                                회원 검색
                            </v-btn> -->
                            
                        </v-col>

                        
                    
                    </v-row>
                </v-form>
            </v-col> 
            <v-col class="noto-sans">
                <v-btn @click="showRegistrationModal('', '')" >회원등록</v-btn>
            </v-col>



        </v-row>
        <v-row>
            <v-col>
                <v-card>
                    <v-card-title class="text-h6 text-center" style="font-family: ONE-Mobile-POP;">
                    </v-card-title>
                    <v-card-text >
                        <v-table >
                            <thead>
                                <tr class="noto-sans">
                                    <th style="text-align:center;">NAME</th>
                                    <th style="text-align:center;">EMAIL</th>
                                    <th style="text-align:center;">등록정보</th>
                                    <th style="text-align:center;">연장</th>
                                    <th style="text-align:center;">등록일</th>
                                    <th style="text-align:center;">종료일</th>
                                    <th style="text-align:center;" v-if="role === 'CEO'">탈퇴</th>
                                </tr>
                            </thead>
                            <tbody  >
                                <tr v-for="u in userList " :key="u.id" style="text-align:center;">
            
                                    <td><v-btn :to="{ path: `/user/detail/${u.id}` }" class="hover-text" style=" box-shadow: none;">{{u.name}}</v-btn></td>
                                    <td>{{u.email}}</td>
                                    <td>{{u.state}}</td>
                                    <td><v-btn @click="showRegistrationModal(u.email, u.endDate)" class="noto-sans">연장</v-btn></td>
                                    <td>{{u.registrationDate}}</td>
                                    <td>{{u.endDate}}</td>
                                    <td v-if="role === 'CEO'"><v-btn @click="deleteUser(u.email)" class="noto-sans deleteUserBtn"> 탈퇴</v-btn></td>
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
        :registrationError="false"
        @update:dialog="registrationModal = $event"
        >
        </RegistrationCreateModal>

        <UserDeleteFromBoxModal
        v-model="userDeleteFromBoxModal"
        :email="deleteUserEmail"
        @update:dialog="userDeleteFromBoxModal = $event"
        >


        </UserDeleteFromBoxModal>

        <AlertModalComponent v-model="alertModal" @update:dialog="alertModal = $event" :dialogTitle="dialogTitle"
        :dialogText="dialogText" />

    </v-container>
</div>
</template>

<script>
import axios from 'axios';
import RegistrationCreateModal from '@/views/registrationInfo/RegistrationCreateModal.vue';
import UserDeleteFromBoxModal from '@/views/member/UserDeleteFromBoxModal.vue';
import AlertModalComponent from './AlertModalComponent.vue';
export default{
    props:['isCEO'],
    components:{
        RegistrationCreateModal,
        UserDeleteFromBoxModal,
        AlertModalComponent,
    },
    data(){
        return{


            dialogTitle:'',
            dialogText:'',
            alertModal: false,

            userList:[],
            pageSize: 5,
            currentPage:0,
            isLastPage: false,
            isLoading: false,
            registrationModal: false,
            userDeleteFromBoxModal : false,
            registrationError : false, 

            modalUserEmail : "",
            modalNextStartDate: "",

            deleteUserEmail : "",
            searchName:"",
            role : localStorage.getItem('role'),

            
        }
    }, 
    created(){
        this.loadUser();
        window.addEventListener('scroll', this.scrollPagination);
    },
    beforeUnmount(){
        window.removeEventListener('scroll', this.scrollPagination);
    },

    methods:{
        searchMember(){
            this.userList = [];
            this.currentPage = 0;
            this.isLastPage = false;

            this.loadUser();
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
                
                if(this.searchName != ""){
                    params.searchName = this.searchName;
                }

                const response = await axios.get(`${process.env.VUE_APP_API_BASE_URL}/member/list/user`, {params});
                const additionalData = response.data.result.content;
                console.log(additionalData);
            
                this.isLastPage = response.data.isLastPage;
                this.userList =[...this.userList, ...additionalData];
                this.currentPage++;
                this.isLoading = false;
                console.log(response.data);
            }catch(e){
                this.dialogTitle = '박스에 가입해주세요';
                this.dialogText = '박스에 가입하지 않은 코치는 회원 목록을 이용하지 못합니다'
                this.alertModal = true;
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

            this.modalUserEmail = userEmail;
            this.modalNextStartDate = userEndDate;
            this.registrationModal=true;
        },
        deleteUser(email){
            this.deleteUserEmail = email;
            this.userDeleteFromBoxModal = true;

        },

    }
    

}

</script>

<style scoped>


.hover-text:hover {
    background-color: rgb(238, 232, 116);
}

.deleteUserBtn:hover {
    background-color: rgb(254, 0, 0);
    color: aliceblue;
}



</style>