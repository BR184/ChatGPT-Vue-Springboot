<template>
    <div class="admin-table-container">
        <div class="admin-table-header">
            <div class="admin-table-header-left">
                <h1 style="font-size: 35px">用户管理</h1>
                <el-button size="small" class="role-add-btn" type="success" @click="toRegister">注册用户</el-button>
            </div>
            <div class="admin-table-header-right">
                <el-pagination background layout="prev, pager, next" @current-change="handleCurrentChange"
                    :current-page.sync="this.page" :page-count="this.total_page">
                </el-pagination>
            </div>
        </div>
        <el-table height="250" :data="tableData.filter(data => !search || data.username.toLowerCase().includes(search.toLowerCase())
            || data.email.toLowerCase().includes(search.toLowerCase()) || data.coin.toString().includes(search.toLowerCase())
            || data.regTime.toLowerCase().includes(search.toLowerCase()) || data.id.toString().toLowerCase().includes(search.toLowerCase())
        )" style="width: 100%">
            <el-table-column label="ID" prop="id" width="98">
            </el-table-column>
            <el-table-column label="用户名称" prop="username">
            </el-table-column>
            <el-table-column label="邮箱" prop="email">
            </el-table-column>
            <el-table-column label="余额" prop="coin" width="60">
            </el-table-column>
            <el-table-column label="注册时间" prop="regTime">
            </el-table-column>
            <el-table-column label="上次登陆时间" prop="lastLogin">
            </el-table-column>
            <el-table-column label="头像" prop="head" width="70">
                <template slot-scope="scope">
                    <el-avatar shape="square" :size="40" :src="scope.row.head"></el-avatar>
                </template>
            </el-table-column>
            <el-table-column label="状态" prop="state" :formatter="stateTranslate" width="50">
            </el-table-column>
            <el-table-column fixed="right" align="right" width="145">
                <template slot="header" slot-scope="scope">
                    <el-input v-model="search" size="mini" placeholder="输入关键字搜索" />
                </template>
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 编辑用户的Dialog -->
        <el-dialog title="编辑用户" :visible.sync="editUserDialogFormVisible" width="30%" append-to-body>
            <el-form :model="editUserFrom" label-width="80px">
                <!-- 显示用户ID和用户名和头像，并为用户名和头像添加重置按钮 -->
                <el-form-item label="ID" prop="id">
                    <el-tag type="info">{{editUserFrom.id}}</el-tag>
                </el-form-item>
                <el-form-item label="用户名" prop="username">
                    <el-tag type="info">{{editUserFrom.username}}</el-tag>
                    <el-button style="float: right" type="text" size="small" @click="resetUsername(editUserFrom.id)">重置</el-button>
                </el-form-item>
                <el-form-item label="头像" prop="avatar">
                    <el-avatar shape="square" :size="40" :src="editUserFrom.avatar"></el-avatar>
                    <el-button style="float: right" type="text" size="small" @click="resetAvatar(editUserFrom.id)">重置</el-button>
                </el-form-item>
                <el-form-item label="余额">
                    <el-input v-model="editUserFrom.coin"></el-input>
                </el-form-item>
                <el-form-item  label="用户角色">
                    <div class="per-choose-container">
                        <!-- 新增权限下拉框 -->
                        <el-select v-model="editUserFrom.roles" multiple placeholder="请选择">
                            <el-option v-for="item in this.roles" :key="item.id"
                                :label="item.roleName" :value="item.id"></el-option>
                        </el-select>
                    </div>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="editUserFrom.state" placeholder="请选择状态">
                        <el-option label="正常" :value="0"></el-option>
                        <el-option label="封禁" :value="1"></el-option>
                    </el-select>
                </el-form-item>
                <!-- 如果封禁用户，输入禁用理由 -->
                <el-form-item v-if="editUserFrom.state == 1" label="禁用理由">
                    <el-input v-model="editUserFrom.banMsg"></el-input>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editUserDialogFormVisible = false">取 消</el-button>
                <el-button type="danger" @click="submitEditUserFrom(editUserFrom)">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            editUserDialogFormVisible: false,
            tableData: [],
            roles:[],
            editUserFrom:{
                id: 0,
                username: '',
                avatar: '',
                coin: 0,
                state: 0,
                roles:[],
                banMsg: '',
            },
            page: 1,
            total_page: 1,
            search: '',
        }
    },
    methods: {
        // 初始化
        init() {
            this.getTableData(this.page);
        },
        // 状态翻译
        stateTranslate(row){
            switch(row.state){
	          case 0:
	            return '正常'
	          case 1:
	            return '封禁'
	          default:
	            return '其他'
	        }
        },
        //获取角色列表
        getTableData(page) {
            this.axios.get('/user/all?page=' + page).then(res => {
                const data = res.data;
                if (data.code == 200) {
                    this.tableData = data.data.data;
                    this.page = data.data.page;
                    this.total_page = data.data.totalpage;
                } else {
                    this.$message.error("获取列表失败，5秒后自动重试！" + data.message);
                    setTimeout(() => {
                        this.getTableData(1);
                    }, 5000);
                }
            });
        },
        //编辑
        handleEdit(row) {
            this.editUserDialogFormVisible = true;
            this.editUserFrom.id = row.id;
            this.editUserFrom.username = row.username;
            this.editUserFrom.coin = row.coin;
            this.editUserFrom.state = row.state;
            this.editUserFrom.banMsg = row.banMsg;
            this.editUserFrom.avatar = row.head;
        },
        //提交编辑
        submitEditUserFrom(from){
            this.axios.put('/user/manage', from).then(res => {
                const data = res.data;
                if (data.code == 200) {
                    this.$message.success("编辑成功！");
                    this.editUserDialogFormVisible = false;
                    this.getTableData(this.page);
                } else {
                    this.$message.error("编辑失败！" + data.message);
                }
            });
        },
        //重置用户名
        resetUsername(id){
            //确定重置用户名
            this.$confirm('此操作将重置用户名，是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.axios.get('/user/reset_username?id='+id).then(res => {
                    const data = res.data;
                    if (data.code == 200) {
                        this.$message.success("重置成功！");
                        this.editUserFrom.username = data.data;
                        this.getTableData(page);
                    } else {
                        this.$message.error("重置失败！" + data.message);
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消重置'
                });          
            });
        },
        //重置头像
        resetAvatar(id){
            //确定重置头像
            this.$confirm('此操作将重置头像，是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.axios.get('/user/reset_avatar?id='+id).then(res => {
                    const data = res.data;
                    if (data.code == 200) {
                        this.$message.success("重置成功！");
                        this.editUserFrom.avatar = data.data;
                        this.getTableData(page);
                    } else {
                        this.$message.error("重置失败！" + data.message);
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消重置'
                });          
            });
        },
        //分页
        handleCurrentChange(val){
            this.page = val;
            this.getTableData(this.page);
        },
        //注册按钮跳转
        toRegister(){
            this.$router.push('/register');
        },
    },
    mounted() {
        this.init();
    }
}
</script>

<style>
.el-button--text{
    color: #fb7756 !important;
}
.el-button--text:hover{
    color: #e74645 !important;
}
.el-button--default:hover{
    color: #1ac0c6 !important;
}
</style>