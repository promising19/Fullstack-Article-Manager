<script setup>
    import { User, Lock } from '@element-plus/icons-vue'
    // import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
    import { ref } from 'vue'
    import { ElMessage } from 'element-plus'
    //控制注册与登录表单的显示， 默认显示注册
    const isRegister = ref(false)
    // 绑定数据
    // 定义数据模型
    // 与后台参数名字尽量保持一致
    // 然后绑定对应的表单
    const registerData = ref({
        username: '',
        password: '',
        rePassword: ''
    })

    // 校验密码的函数
    // 然后绑定表单
    const checkRePassword = (rule,value,callback)=>{
        if(value===''){ //三个等号是强等,表示连字符类型也要相等
            callback(new Error('请再次确认密码'))
        }else if(value !== registerData.value.password){
            callback(new Error('确保两次输入的密码一样'))
        }else{
            callback()
        }
    }

    // 定义表单校验规则
    const rules = {
        username: [
            { required: true, message: '请输入用户名', trigger: 'blur' },
            { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
        ],
        password: [
            { required: true, message: '请输入密码', trigger: 'blur' },
            { min: 5, max: 16, message: '长度为5~16位非空字符', trigger: 'blur' }
        ],
        rePassword: [
            // 自定义校验函数
            {validator:checkRePassword,trigger:'blur'}
        ]
    }

    // 调用后台接口，完成注册
    import {userRegisterService,userLoginService} from '@/api/user.js'
    const register = async()=>{
        // registerData是一个响应式数据，如果要获取值，需要.value
        let result = await userRegisterService(registerData.value);
        if(result.code === 0){
            // 成功
            ElMessage.success(result.msg ? result.msg : '注册成功');
        }else{
            ElMessage.error('注册失败');
        }
    }

    // 登录函数
    // 点击登录按钮之后就会触发这个方法
    import {useTokenStore} from '@/stores/token.js'
    import {useRouter} from 'vue-router'
    const router = useRouter()
    const tokenStore = useTokenStore();
    const login = async()=>{
        // 调用接口完成登录
        // 既然要调用接口，就要提供完成调用接口这一行为的函数 在api.user.js中
        let result = await userLoginService(registerData.value)
        if(result.code === 0){
            // 成功
            ElMessage.success(result.msg ? result.msg : '登录成功');
            // 把得到的token存储到pinia中
            tokenStore.setToken(result.data)
            // 跳转到首页
            // 借助路由完成跳转
            router.push('/')
        }else{
            ElMessage.error('登录失败');
        }
    }

    // 清空显示数据函数
    const clearRegisterData = ()=>{
        registerData.value = {
            username:'',
            password:'',
            rePassword:''
        }
    }
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
        <el-col :span="6" :offset="3" class="form">
            <!-- 注册表单 -->
            <el-form ref="form" size="large" autocomplete="off" v-if="isRegister" :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>注册</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                        v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item prop="rePassword">
                    <el-input :prefix-icon="Lock" type="password" placeholder="请输入再次密码"
                        v-model="registerData.rePassword"></el-input>
                </el-form-item>
                <!-- 注册按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="register">
                        注册
                    </el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = false;clearRegisterData()" >
                        ← 返回
                    </el-link>
                </el-form-item>
            </el-form>

            <!-- 登录表单 -->
             <!-- 复用注册表单的数据 -->
              <!-- 绑定好数据之后，进行表单校验 -->
               <!-- 然后给登录按钮绑定事件 -->
                <!-- 提供单击事件函数 -->
            <el-form ref="form" size="large" autocomplete="off" v-else :model="registerData" :rules="rules">
                <el-form-item>
                    <h1>登录</h1>
                </el-form-item>
                <el-form-item prop="username">
                    <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码" v-model="registerData.password"></el-input>
                </el-form-item>
                <el-form-item class="flex">
                    <div class="flex">
                        <el-checkbox>记住我</el-checkbox>
                        <el-link type="primary" :underline="false">忘记密码？</el-link>
                    </div>
                </el-form-item>
                <!-- 登录按钮 -->
                <el-form-item>
                    <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
                </el-form-item>
                <el-form-item class="flex">
                    <el-link type="info" :underline="false" @click="isRegister = true;clearRegisterData()">
                        注册 →
                    </el-link>
                </el-form-item>
            </el-form>
        </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
    /* 样式 */
    .login-page {
        height: 100vh;
        background-color: #fff;

        .bg {
            background: url('@/assets/logo2.png') no-repeat 60% center / 240px auto,
                url('@/assets/login_bg.jpg') no-repeat center / cover;
            border-radius: 0 20px 20px 0;
        }

        .form {
            display: flex;
            flex-direction: column;
            justify-content: center;
            user-select: none;

            .title {
                margin: 0 auto;
            }

            .button {
                width: 100%;
            }

            .flex {
                width: 100%;
                display: flex;
                justify-content: space-between;
            }
        }
    }
</style>