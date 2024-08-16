import PostCreate from "@/views/post/PostCreate.vue";
import PostDetail from "@/views/post/PostDetail.vue";
import PostList from "@/views/post/PostList.vue";
import PostUpdate from "@/views/post/PostUpdate.vue";
import PostTest from "@/views/post/PostTest.vue";
export const postRouter = [
	{
		path: "/post/create",
		name: "PostCreate",
		component: PostCreate,
	},
	{
		path: "/post/list",
		name: "PostList",
		component: PostList,
	},
	{
		path: "/post/detail/:id",
		name: "PostDetail",
		component: PostDetail,
		props: true,
	},
    {
        path: '/post/update/:id',
        name: 'PostUpdate',
        component: PostUpdate,
        props: true,
    },
	{
		path: '/post/test',
		name: 'PostTest',
		component: PostTest,
	}
];
