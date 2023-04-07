<template>
    <div class="info-container">
        <img :src="this.$getHead()+this.$getUser().head" class="user-avatar">
        <el-upload class="upload-demo" action="https://jsonplaceholder.typicode.com/posts/" :show-file-list="false"
            :before-upload="beforeAvatarUpload" :http-request="uploadFile">
            <el-button type="danger" size="small" style="margin-top:10px">修改头像</el-button>
        </el-upload>
        <!-- 分割线 -->
        <el-divider></el-divider>
        <!-- 显示用户信息 -->
        <div class="user-info-container">
            <el-table :data="tableData" border class="user-info">
                <el-table-column prop="info" label="信息" width="130">
                </el-table-column>
                <el-table-column prop="value" label="内容" width="">
                </el-table-column>
                <el-table-column fixed="right" label="操作" width="100">
                    <template slot-scope="scope">
                        <el-button v-if="canEdit(scope.row.info)" type="text" size="small"
                            @click="edit(scope.row.info)">编辑</el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <!-- 修改用户名的Dialog -->
        <el-dialog title="修改用户名" :visible.sync="usernameDialogVisible" width="30%" append-to-body>
            <el-form :model="usernameFrom" label-width="80px" :rules="user_rule">
                <el-form-item label="新用户名" prop="username">
                    <el-input v-model="usernameFrom.username" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="usernameFrom.password" type="password" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="usernameDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitUsername(usernameFrom)">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 修改密码的Dialog -->
        <el-dialog title="修改密码" :visible.sync="passwordDialogVisible" width="30%" append-to-body>
            <el-form :model="passwordFrom" label-width="80px" :rules="pass_rule">
                <el-form-item label="旧密码" prop="password">
                    <el-input v-model="passwordFrom.password" type="password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                    <el-input v-model="passwordFrom.newPassword" type="password" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                    <el-input v-model="passwordFrom.confirmPassword" type="password" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="passwordDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitPassword(passwordFrom)">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 修改邮箱的Dialog -->
        <el-dialog title="修改邮箱" :visible.sync="emailDialogVisible" width="30%" append-to-body>
            <el-form :model="emailFrom" label-width="80px" :rules="email_rule">
                <el-form-item label="新邮箱" prop="email">
                    <el-input v-model="emailFrom.email" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input v-model="emailFrom.password" type="password" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="emailDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitEmail(emailFrom)">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script>
export default {
    data() {
        var validatePass = (rule, value, callback) => {
            if (value === '') {
                callback(new Error('请再次输入密码'));
            } else if (value !== this.passwordFrom.newPassword) {
                callback(new Error('两次输入密码不一致!'));
            } else {
                callback();
            }
        };
        return {
            // 用户名修改的Dialog校验
            user_rule: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
                ]
            },
            // 密码修改的Dialog校验
            pass_rule: {
                password: [
                    { required: true, message: '请输入旧密码', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
                ],
                newPassword: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: '请再次输入密码', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' },
                    { validator: validatePass, trigger: 'blur' }
                ]
            },
            // 邮箱修改的Dialog校验
            email_rule: {
                email: [
                    { required: true, message: '请输入邮箱', trigger: 'blur' },
                    { pattern:/^([a-zA-Z0-9]+[-_\.]?)+@[a-zA-Z0-9]+\.[a-z]+$/, message: '请输入正确的邮箱地址', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
                ]
            },
            usernameDialogVisible: false,
            passwordDialogVisible: false,
            emailDialogVisible: false,

            usernameFrom: {
                username: '',
                password: ''
            },
            passwordFrom: {
                password: '',
                newPassword: '',
                confirmPassword: ''
            },
            emailFrom: {
                email: '',
                password: ''
            },
            imageUrl: '',
            testId: [],
            tableData: [
                {
                    info: "用户名",
                    value: this.$getUser().username
                }, {
                    info: "密码",
                    value: "**********"
                }, {
                    info: "邮箱",
                    value: this.$getUser().email
                }, {
                    info: "余额",
                    value: this.$getUser().coin
                }, {
                    info: "注册时间",
                    value: this.$getUser().regTime
                }, {
                    info: "上次登录时间",
                    value: this.$getUser().lastLogin
                }, {
                    info: "账号状态",
                    // 0正常 1封禁
                    value: this.$getUser().deleted == 0 ? "正常" : "封禁"
                },]
        }
    },
    methods: {
        canEdit(name) {
            if (name == "用户名" || name == "密码" || name == "邮箱") {
                return true;
            }
        },
        edit(name) {
            //修改用户名
            if (name == "用户名") {
                this.usernameDialogVisible = true;
            }
            //修改密码
            else if (name == "密码") {
                this.passwordDialogVisible = true;
            }
            //修改邮箱
            else if (name == "邮箱") {
                this.emailDialogVisible = true;
            }
        },
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isPNG = file.type === 'image/png';
            const isLt2M = file.size / 1024 / 1024 < 2;
            if (!isJPG && !isPNG) {
                this.$message.error("仅支持jpg，png格式的图片！");
            }
            if (!isLt2M) {
                this.$message.error("上传图片大小不能超过 2MB!");
            }
            return (isPNG || isJPG) && isLt2M;
        },
        uploadFile(item) {
            let fileObj = item.file;
            const form = new FormData(); // FormData 对象
            form.append("file", fileObj);
            this.axios.post("upload/avatar", form)
                .then((resp) => {
                    let data = resp.data;
                    if (data.code == "200") {
                        this.$message({
                            message: "上传成功",
                            type: "success",
                        });
                        //更新头像
                        const new_user = this.$getUser()
                        new_user.head = data.data
                        this.$removeUser;
                        localStorage.setItem("User", JSON.stringify(new_user))
                        //刷新页面
                        this.$router.go(0)
                    }
                })
                .catch((data) => {
                    this.$message.error("上传失败，请稍后重试");
                });
        },
        submitUsername(form) {
            this.axios.put("user", form)
                .then((resp) => {
                    let data = resp.data;
                    if (data.code == "200") {
                        this.$message({
                            message: "修改成功",
                            type: "success",
                        });
                        //更新用户名
                        const new_user = this.$getUser()
                        new_user.username = form.username
                        this.$removeUser;
                        localStorage.setItem("User", JSON.stringify(new_user))
                        //刷新页面
                        this.$router.go(0)
                    }else{
                        this.$message.error(data.msg);
                    }
                })
                .catch((data) => {
                    this.$message.error("修改失败，请稍后重试");
                });
        },
        submitPassword(form) {
            this.axios.put("user", form)
                .then((resp) => {
                    let data = resp.data;
                    if (data.code == "200") {
                        this.$message({
                            message: "修改成功，请重新登录！",
                            type: "success",
                        });
                        //准备注销
                        this.$removeUser();
                        this.$removeToken();
                        //跳转到登录页面
                        this.$router.push("/login");
                    }else{
                        this.$message.error(data.msg);
                    }
                })
                .catch((data) => {
                    this.$message.error("修改失败，请稍后重试");
                });
        },
        submitEmail(form) {
            this.axios.put("user", form)
                .then((resp) => {
                    let data = resp.data;
                    if (data.code == "200") {
                        this.$message({
                            message: "修改成功",
                            type: "success",
                        });
                        //更新邮箱
                        const new_user = this.$getUser()
                        new_user.email = form.email
                        this.$removeUser;
                        localStorage.setItem("User", JSON.stringify(new_user))
                        //刷新页面
                        this.$router.go(0)
                    }else{
                        this.$message.error(data.msg);
                    }
                })
                .catch((data) => {
                    this.$message.error("修改失败，请稍后重试");
                });
        },
    },

}
</script>
<style>
.info-container {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: center;
    color: red;
}

.cell>.el-button>span {
    color: #1ac0c6 !important;
}

.user-avatar {
    margin-top: 20px;
    width: 200px;
    height: 200px;
    border-radius: 50%;
    margin-bottom: 10px;
}

.user-info {
    user-select: none;
}

.user-info-container {
    width: 90%;
    display: block;
    overflow: auto;
}</style>