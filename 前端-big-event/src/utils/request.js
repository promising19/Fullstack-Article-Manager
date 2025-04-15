//定制请求的实例

//导入axios  npm install axios
import axios from 'axios';
//定义一个变量,记录公共的前缀  ,  baseURL
// const baseURL = 'http://localhost:8080';
const baseURL = '/api';
const instance = axios.create({baseURL})

import { useTokenStore } from '@/stores/token';
import { ElMessage } from 'element-plus';
// 添加请求拦截器
instance.interceptors.request.use(
    (config)=>{
        // 请求前的回调
        // 添加token
        const tokenStore = useTokenStore();
        // 判断有没有token
        if(tokenStore.token){
            config.headers.Authorization = tokenStore.token
        }
        return config;
    },
    (err)=>{
        // 请求错误的回调
        Promise.reject(err)
    }
)

// 导入路由器
// import { useRouter } from 'vue-router';
// const router = useRouter();
import router from '@/router';
//添加响应拦截器
instance.interceptors.response.use(
    result=>{
        return result.data;
    },
    err => {
        // 判断相应状态码，如果相应401，则证明未登录，提示请登录，并跳转到登录页面
        if(err.response.status===401){
            ElMessage.error('请先登录')
            // 然后要跳转到登录页面
            // 使用路由器
            router.push('/login')
        }else{
            ElMessage.error('服务异常')
        }
        return Promise.reject(err);//异步的状态转化成失败的状态
    }
)

export default instance;