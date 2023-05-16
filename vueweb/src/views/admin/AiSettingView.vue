<template>
    <div class="admin-table-container">
        <div class="admin-table-header">
            <div class="admin-table-header-left">
                <h1 style="font-size: 35px">设定管理</h1>
                <el-button size="small" class="role-add-btn" type="success" @click="handleAdd">新增设定</el-button>
            </div>
            <div class="admin-table-header-right">
                <el-pagination background layout="prev, pager, next" @current-change="handleCurrentChange"
                    :current-page.sync="this.page" :page-count="this.total_page">
                </el-pagination>
            </div>
        </div>
        <el-table height="250" :data="tableData.filter(data => !search || data.id.toLowerCase().includes(search.toLowerCase())
            || data.userId.toLowerCase().includes(search.toLowerCase()) || data.intro.toLowerCase().includes(search.toLowerCase())
            || data.value.toLowerCase().includes(search.toLowerCase()) || data.createTime.toString().toLowerCase().includes(search.toLowerCase())
        )" style="width: 100%">
            <el-table-column label="ID" prop="id" width="100">
            </el-table-column>
            <el-table-column label="发布者ID" prop="userId" width="100">
            </el-table-column>
            <el-table-column label="简介" show-overflow-tooltip prop="intro">
            </el-table-column>
            <el-table-column label="内容" show-overflow-tooltip prop="value">
                <template slot-scope="scope">
                    <div class="cell el-tooltip" style="display:-webkit-box;text-overflow: ellipsis;overflow: hidden;-webkit-line-clamp: 3;-webkit-box-orient: vertical;white-space: pre-line;">
                        {{ scope.row.value }}
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="状态" prop="shared" :formatter="stateTranslate" width="50">
            </el-table-column>
            <el-table-column label="收藏数" prop="fav" width="70">
            </el-table-column>
            <el-table-column label="创建时间" prop="createTime" width="92">
            </el-table-column>
            <el-table-column label="更新时间" prop="updateTime" width="92">
            </el-table-column>
            <el-table-column fixed="right" align="right" width="160">
                <template slot="header" slot-scope="scope">
                    <el-input v-model="search" size="mini" placeholder="输入关键字搜索" />
                </template>
                <template slot-scope="scope">
                    <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
                    <el-button v-if="![1, 2].includes(scope.row.id)" size="mini" type="danger"
                        @click="handleDelete(scope.row)">删除</el-button>
                    <el-button v-else size="mini" type="info">默认</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!-- 编辑设定的Dialog -->
        <el-dialog title="编辑系统设定" :visible.sync="editAiSettingDialogFormVisible" width="30%" append-to-body>
            <el-form :model="edit_aiSetting_from" label-width="80px" :rules="edit_sys_rule" ref="edit_aiSetting_from">
                <el-form-item label="ID" prop="id">
                    <el-tag type="info">{{ edit_aiSetting_from.id }}</el-tag>
                </el-form-item>
                <el-form-item label="发布者ID" prop="id">
                    <el-tag type="info">{{ edit_aiSetting_from.userId }}</el-tag>
                </el-form-item>
                <el-form-item label="系统介绍" prop="intro">
                    <el-input type="input" v-model="edit_aiSetting_from.intro" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="系统内容" prop="value">
                    <el-input class="new-sys-textarea" resize='none' type="textarea" v-model="edit_aiSetting_from.value"
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="共享设定:">
                    <el-radio-group v-model="edit_aiSetting_from.shared">
                        <el-radio :label="0">私人</el-radio>
                        <el-radio :label="1">分享</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editAiSettingDialogFormVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitHandleEdit(edit_aiSetting_from)">确 定</el-button>
            </span>
        </el-dialog>
         <!-- 新增设定的Dialog -->
         <el-dialog title="新增系统设定" :visible.sync="addAiSettingDialogVisible" width="30%" append-to-body>
            <el-form :model="new_aiSetting_from" label-width="80px" :rules="add_sys_rule" ref="new_aiSetting_from">
                <el-form-item label="系统介绍" prop="intro">
                    <el-input type="input" v-model="new_aiSetting_from.intro" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="系统内容" prop="value">
                    <el-input class="new-sys-textarea" resize='none' type="textarea" v-model="new_aiSetting_from.value"
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="共享设定:">
                    <el-radio-group v-model="new_aiSetting_from.shared">
                        <el-radio :label="0">私人</el-radio>
                        <el-radio :label="1">分享</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addAiSettingDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitHandleAdd(new_aiSetting_from)">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
export default {
    data() {
        return {
            editAiSettingDialogFormVisible: false,
            addAiSettingDialogVisible: false,
            page: 1,
            total_page: 0,
            tableData: [],
            edit_aiSetting_from: {
                id: 0,
                userId: 0,
                intro: '',
                value: '',
                shared: 0,
            },
            new_aiSetting_from: {
                intro: '',
                value: '',
                shared: 0,
            },
            search: '',
            //新增系统设定的Dialog校验规则
            add_sys_rule: {
                intro: [
                    { required: true, message: '请输入简介', trigger: 'blur' },
                    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
                ],
                value: [
                    { required: true, message: '请输入设定内容', trigger: 'blur' },
                    { min: 2, max: 1000, message: '长度在 2 到 1000 个字符', trigger: 'blur' }
                ],
            },
            //编辑系统设定的Dialog校验规则、
            edit_sys_rule: {
                intro: [
                    { required: true, message: '请输入简介', trigger: 'blur' },
                    { min: 2, max: 30, message: '长度在 2 到 30 个字符', trigger: 'blur' }
                ],
                value: [
                    { required: true, message: '请输入设定内容', trigger: 'blur' },
                    { min: 2, max: 1000, message: '长度在 2 到 1000 个字符', trigger: 'blur' }
                ],
            },
        }
    },
    methods: {
        // 初始化
        init() {
            this.getTableData(this.page);
        },
        // 状态翻译
        stateTranslate(row) {
            switch (row.shared) {
                case 0:
                    return '私人'
                case 1:
                    return '共享'
                default:
                    return '其他'
            }
        },
        // 获取设定列表
        getTableData(page) {
            this.axios.get('/system/manage?page=' + page+'&mode=1').then(res => {
                const data = res.data;
                if (data.code == 200) {
                    this.tableData = data.data.list;
                    this.page = data.data.page;
                    this.total_page = data.data.totalPage;
                } else {
                    this.$message.error("获取设定列表失败！" + data.message);
                }
            });
        },
        //新增设定
        handleAdd() {
            this.addAiSettingDialogVisible = true;
        },
        // 提交新增设定
        submitHandleAdd(form) {
            this.$refs.new_aiSetting_from.validate((valid) => {
                if (valid) {
                    this.axios.post('/system', form).then(res => {
                        const data = res.data;
                        if (data.code == 200) {
                            this.$message.success("新增设定成功！");
                            this.addAiSettingDialogVisible = false;
                            this.getTableData(this.page);
                        } else {
                            this.$message.error("新增设定失败！" + data.message);
                        }
                    });
                } else {
                    return false;
                }
            });
        },
        // 编辑设定
        handleEdit(info) {
            this.edit_aiSetting_from.id = info.id;
            this.edit_aiSetting_from.userId = info.userId;
            this.edit_aiSetting_from.intro = info.intro;
            this.edit_aiSetting_from.value = info.value;
            this.edit_aiSetting_from.shared = info.shared;
            this.editAiSettingDialogFormVisible = true;
        },
        // 提交编辑设定
        submitHandleEdit(form) {
            console.log(form)
            this.$refs.edit_aiSetting_from.validate((valid) => {
                if (valid) {
                    this.axios.put('/system/manage', form).then(res => {
                        const data = res.data;
                        console.log(data)
                        if (data.code == 200) {
                            this.$message.success("编辑设定成功！");
                            this.editAiSettingDialogFormVisible = false;
                            this.getTableData(this.page);
                        } else {
                            this.$message.error("编辑设定失败！" + data.message);
                        }
                    });
                } else {
                    this.$message.error("编辑设定失败！请检查输入内容是否正确。");
                    return false;
                }
            });
        },
        // 删除设定
        handleDelete(info) {
            this.$confirm('此操作将永久删除该设定, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.axios.delete('/system/manage?id=' + info.id).then(res => {
                    const data = res.data;
                    if (data.code == 200) {
                        this.$message.success("删除设定成功！");
                        this.getTableData(this.page);
                    } else {
                        this.$message.error("删除设定失败！" + data.message);
                    }
                });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        //分页
        handleCurrentChange(val) {
            this.page = val;
            this.getTableData(this.page);
        },
    },
    mounted() {
        this.init();
    }
}
</script>

<style>
.role-add-btn {
    margin-top: 4px !important;
    margin-left: 30px !important;
}

.admin-table-header {
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
    width: 100%;
    height: 80px;
    margin-bottom: 15px;
}

.admin-table-header-left {
    display: flex;
    width: 50%;
    flex-direction: row;
    justify-content: start;
    align-items: center;
    border-bottom: 2px solid #e34847 !important;
}

.admin-table-header-right {
    display: flex;
    width: 50%;
    flex-direction: row;
    justify-content: end;
    align-items: center;
}

.admin-table-container {
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: start;
    width: 90%;
    height: 90%;
    user-select: none !important;
}

tbody {
    user-select: text !important;
    caret-color: transparent !important;
}

.per-choose-container {
    display: flex;
    flex-direction: column;
    justify-content: start;
    align-items: start;
    flex-wrap: wrap;
    user-select: none !important;
    caret-color: transparent !important;
    margin-top: 5px;
}

.el-select {
    width: 100% !important;
}

.el-select-dropdown__item.selected {
    color: #e74645 !important;
}</style>