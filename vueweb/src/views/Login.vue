<template>
    <body id="poster">
        <el-form class="login_container" laber-position="left" label-width="0px">
            <h1 class="login_title">
                登陆你的账户
            </h1>
            <el-form-item>
                <el-input type="text" v-model="loginForm.username" placeholder="账号" autocomplete="off" prefix-icon="el-icon-user
                "></el-input>
            </el-form-item>
            <el-form-item style="margin-bottom: 2px;">
                <el-input type="password" v-model="loginForm.password" placeholder="密码" autocomplete="off" show-password prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
            <div class="login_text" style="width: 100%;">
                <el-link target="_blank">忘记密码</el-link>
            </div>
            <el-form-item style="margin-bottom:10px">
                <el-button type="info" @click="toRegister" style="width:100%">注册账号</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="success" @click="onSubmit" style="width:100%">立即登陆</el-button>
            </el-form-item>
        </el-form>
    </body>
</template>

<script>
    export default {
        data() {
            return {
                loginForm: {
                    username: '',
                    password: '' 
                }
            }
        },
        methods: { 
            onSubmit() {
                // console.log('submit!',this.loginForm);
            },
            toRegister(){
                this.$router.push({path:'/register'})
            }
        }
    }
</script>

<style>
    #poster{
        background-position: center;
        height: 100%;
        width: 100%;
        background-size: cover;
        position: fixed;
    }
    body{
        margin: 0px;
        padding: 0px;
    }
    .login_container{
        border-radius: 20px;
        background-clip: padding-box;
        margin: 90px auto;
        width: 350px;
        padding: 35px 35px 15px 35px;
        background-color: #fff;
        border: 1px solid #eaeaea;
        box-shadow:0 0 25px #cac6c6;
    }
    .login_title{
        margin: 0px auto 40px auto;
        text-align: center;
        color: #505458;
    }
    .login_text{
        display:flex;
        flex-direction: row;
        justify-content:end;
        margin-bottom: 30px;
    }
</style>