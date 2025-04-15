// 定义store
/**
 * 第一个参数：名字，唯一性
 * 第二个参数：函数，在函数内部可以定义状态的左右内容
 * 
 * 返回值：函数
 */
import { defineStore } from "pinia";
import { ref } from "vue"; // ✅ 这里必须引入 ref

export const useTokenStore = defineStore('token', () => {
    // 1、响应式变量
    const token = ref('');

    // 2、定义修改 token 的函数
    const setToken = (newToken) => {
        token.value = newToken;
    };

    // 3、定义移除 token 的函数
    const removeToken = () => {
        token.value = '';
    };

    return { 
        token, setToken, removeToken 
    };
},{
    persist:true //持久化存储
});
