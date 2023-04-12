<template>
    <div class="admin-table-container">
        <h1>权限管理</h1>
        <el-table :data="tableData.filter(data => !search || data.roleName.toLowerCase().includes(search.toLowerCase())
            || data.roleKey.toLowerCase().includes(search.toLowerCase()) || data.createTime.toLowerCase().includes(search.toLowerCase())
            || data.state.toLowerCase().includes(search.toLowerCase()) || data.id.toString().toLowerCase().includes(search.toLowerCase())
        )" style="width: 100%">
            <el-table-column label="ID" prop="id">
            </el-table-column>
            <el-table-column label="用户名称" prop="roleName">
            </el-table-column>
            <el-table-column label="用户键" prop="roleKey">
            </el-table-column>
            <el-table-column label="创建时间" prop="createTime">
            </el-table-column>
            <el-table-column label="更新时间" prop="updateTime">
            </el-table-column>
            <el-table-column label="状态" prop="state">
            </el-table-column>
            <el-table-column align="right" width="160">
                <template slot="header" slot-scope="scope">
                    <el-input v-model="search" size="mini" placeholder="输入关键字搜索" />
                </template>
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 编辑角色的Dialog -->
        <el-dialog title="编辑角色" :visible.sync="editRoleDialogFormVisible" append-to-body>
            <el-form :model="editRoleForm" label-width="80px">
                <el-form-item label="角色名称">
                    <el-input v-model="editRoleForm.roleName"></el-input>
                </el-form-item>
                <el-form-item label="角色键">
                    <el-input v-model="editRoleForm.roleKey"></el-input>
                </el-form-item>
                <!-- 显示所有权限 -->
                <el-form-item  label="角色权限">
                    <div class="per-choose-container">
                        <!-- 新增权限下拉框 -->
                        <el-select v-model="editRoleForm.rolePermissions" multiple placeholder="请选择">
                            <el-option v-for="item in this.permissions" :key="item.id"
                                :label="item.permissionName" :value="item.id"></el-option>
                        </el-select>
                    </div>
                </el-form-item>
                <el-form-item label="角色状态">
                    <el-select v-model="editRoleForm.state" placeholder="请选择">
                        <el-option label="正常" value="0"></el-option>
                        <el-option label="禁用" value="1"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editRoleDialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitEditRole(editRoleForm)">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            have_table_data: false,
            editRoleDialogFormVisible: false,
            page : 1,
            tableData: [],
            //拥有的权限列表
            permissions: [],
            editRoleForm: {
                id:0,
                roleName: '',
                roleKey: '',
                rolePermissions: [],
                state: ''
            },
            search: ''
        }
    },
    methods: {
        // 初始化
        init() {
            this.getTableData(this.page);
        },
        // 获取角色列表
        getTableData(page) {
            this.axios.get('/role?page=' + page).then(res => {
                this.have_table_data = false;
                const data = res.data;
                if (data.code == 200) {
                    if (data.data.data == null) {
                        this.have_table_data = false;
                    } else {
                        this.tableData = data.data.data;
                        this.have_table_data = true;
                    }
                } else {
                    this.$message.error("获取列表失败，5秒后自动重试！" + data.message);
                    setTimeout(() => {
                        this.getTableData(1);
                    }, 5000);
                }
            });
        },
        // 编辑角色
        handleEdit(info) {
            this.editRoleForm.roleName = info.roleName;
            this.editRoleForm.roleKey = info.roleKey;
            this.editRoleForm.state = info.state;
            this.editRoleForm.id = info.id;
            //获取角色拥有权限列表
            this.axios.get('/permission?roleId=' + info.id).then(res => {
                const data = res.data;
                if (data.code == 200) {
                    const tempRolePers = data.data;
                    this.editRoleForm.rolePermissions = [];
                    //遍历tempRolePers
                    for (const tempRolePer of tempRolePers) {
                        this.editRoleForm.rolePermissions.push(tempRolePer.id);
                    }
                    //获取所有权限列表
                    this.axios.get('/permission/all').then(res => {
                        const data = res.data;
                        if (data.code == 200) {
                            this.permissions = data.data;
                            this.editRoleDialogFormVisible = true;
                        } else {
                            this.$message.error("获取全部权限列表失败！" + data.message);
                            this.editRoleForm.rolePermissions = [];
                        }
                    });
                } else {
                    this.$message.error("获取角色权限失败！" + data.message);
                    this.editRoleForm.rolePermissions = [];
                }
            });
        },
        // 删除角色
        handleDelete(info) {
            console.log(index, row);
        },
        // 提交编辑角色
        submitEditRole(from){
            if(from.id==0){
                this.$message.error("角色ID不能为空！");
                return;
            }
            console.log(from);
            this.axios.put('/role',from).then(res => {
                const data = res.data;
                if (data.code == 200) {
                    this.$message.success("编辑角色成功！");
                    this.editRoleDialogFormVisible = false;
                    this.getTableData(this.page);
                } else {
                    this.$message.error("编辑角色失败！" + data.message);
                }
            });
        }
    },
    mounted() {
        this.init();
    }
}
</script>

<style>
.admin-table-container {
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: start;
    width: 90%;
    height: 90%;
    user-select: none !important;
}
tbody{
    user-select: text !important;
    caret-color:transparent !important;
}
.per-choose-container{
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: start;
    flex-wrap: wrap;
    user-select: none !important;
    caret-color:transparent !important;
    margin-top: 5px;
}
.el-select{
    width: 100%!important;
}
.el-select-dropdown__item.selected{
    color: #e74645!important;
}
</style>