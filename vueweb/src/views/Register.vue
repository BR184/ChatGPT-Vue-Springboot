<template>
    <div id="poster">
        <el-form class="register_container" :model="ruleForm" status-icon :rules="rules" ref="ruleForm" label-width="0">
            <h1 class="register_title">
                注册你的账户
            </h1>
            <el-form-item label="" prop="username">
                <el-input type="text" v-model="ruleForm.username" autocomplete="off" placeholder="用户名"
                    prefix-icon="el-icon-user"></el-input>
            </el-form-item>
            <el-form-item label="" prop="password">
                <el-input type="password" v-model="ruleForm.password" autocomplete="off" placeholder="密码(8-16位，至少包含一个数字和字母)"
                    show-password prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
            <el-form-item label="" prop="checkPass">
                <el-input type="password" v-model="ruleForm.checkPass" autocomplete="off" placeholder="重复密码" show-password
                    prefix-icon="el-icon-lock"></el-input>
            </el-form-item>
            <el-form-item label="" prop="email">
                <el-input type="text" v-model="ruleForm.email" autocomplete="off" placeholder="邮箱"
                    prefix-icon="el-icon-message"></el-input>
            </el-form-item>
            <el-form-item prop='agree'>
                <el-checkbox v-model='agree'><span>我已阅读并同意</span><el-link target="_blank">《用户协议》</el-link></el-checkbox>
            </el-form-item>
            <el-form-item style="margin:20px 0px 10px 0px">
                <el-button type="success" @click="submitForm('ruleForm')" style="width:100%">注册账号</el-button>
            </el-form-item>
            <el-form-item>
                <el-button type="info" @click="toLogin" style="width:48.5%">返回登陆</el-button>
                <el-button @click="resetForm('ruleForm')" style="width:48.5%">重置</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
export default {
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请输入密码'));
            } else {
                if (this.ruleForm.checkPass !== '') {
                    this.$refs.ruleForm.validateField('checkPass');
                }
                callback();
            }
        };
        var validatePass2 = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.ruleForm.password) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        function validateEmail(rule, value, callback) {
            const regEmail = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(\.[a-zA-Z0-9_-])+/;
            if (regEmail.test(value)) {
                return callback();
            }
            callback(new Error("请输入合法的邮箱"));
        };
        function validateUser(rule, value, callback) {
            const usernameRegex = /^.{2,16}$/;
            if (usernameRegex.test(value)) {
                return callback();
            }
            callback(new Error("用户名长度为2-16个字符！"));
        };


        return {
            agree: false,
            ruleForm: {
                username: '',
                password: '',
                checkPass: '',
                email: '',
            },
            rules: {
                username: [
                    {
                        validator: this.$debounce((rule, value, callback) => {
                            validateUser(rule, value, callback)
                        }, 500), trigger: 'change'
                    }
                ],
                password: [
                    { validator: validatePass, trigger: 'change' }
                ],
                checkPass: [
                    { validator: validatePass2, trigger: 'change' }
                ],
                email: [
                    {
                        validator: this.$debounce((rule, value, callback) => {
                            validateEmail(rule, value, callback)
                        }, 500), trigger: 'blur'
                    }
                ],
            }
        };
    },
    methods: {
        submitForm(ruleForm) {
            if (this.agree) {
                this.axios.post("http://localhost:8081/user/register", this.ruleForm).then((resp) => {
                    let data = resp.data;
                    if (data.success) {
                        this.ruleForm = {};
                        this.$message({
                            message: "注册成功！",
                            type: "success"
                        });
                    }

                })
            } else {
                this.$message({
                    message: '请先阅读并同意《用户协议》以继续',
                    type: 'warning'
                });
            }
        },
        toLogin() {
            this.$router.push({ path: '/login' })
        }
    },
}
</script>

<style>
#poster {
    background-position: center;
    height: 100%;
    width: 100%;
    background-size: cover;
    position: fixed;
}

body {
    margin: 0px;
    padding: 0px;
}

.register_container {
    border-radius: 20px;
    background-clip: padding-box;
    margin: 90px auto;
    width: 350px;
    padding: 35px 35px 15px 35px;
    background-color: #fff;
    border: 1px solid #eaeaea;
    box-shadow: 0 0 25px #cac6c6;
}

.register_title {
    margin: 0px auto 40px auto;
    text-align: center;
    color: #505458;
}
</style>