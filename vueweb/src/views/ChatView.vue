<template>
    <div class="chat_container">
        <el-row :gutter="0" class="chat_row">
            <el-col :span="4" class="left_menu">
                <el-menu style="height: 100%;" default-active="0" class="el-menu-vertical-demo" 
                background-color="transparent" text-color="#909399" active-text-color="#e74645">
                    <!-- v-for遍历 el-menu-item -->
                    <el-menu-item v-for="(item, i) in chatList" :key="item.chatID" :index="i.toString()"
                        @click="getChatInfoAndContent(item.chatID)" class="chat-menu">
                        <i class="el-icon-menu"></i>
                        <span slot="title">{{ item.title }}</span>
                        <i class="el-icon-delete delete-button" @click="deleteChat(item.chatID)"></i>
                    </el-menu-item>
                    <!-- 新增聊天 -->
                    <el-menu-item>
                        <div style="border-top:1px dashed #909399;margin:0 10px 0 10px" @click="addNewChat()">
                            <i class="el-icon-plus"></i>
                            <span slot="title">新增聊天</span>
                        </div>
                    </el-menu-item>
                </el-menu>
            </el-col>
            <el-col style="height: 105%;" :span="20">
                <el-row class="chat_area">
                    <el-scrollbar class="chats_container" ref="scrollbar">
                        <div v-for="(item, i) in chatContent"
                            :class="item.chatSide == 'gpt' ? 'chat_box_assistant' : 'chat_box_user'">
                            <img v-if="item.chatSide == 'gpt'" src="../assets/GPTlogo.png" class="user_head" />
                            <div class="chat_box_container">
                                {{ item.chatContent }}
                            </div>
                            <img v-if="item.chatSide == 'user'" :src="head_url" class="user_head" />
                        </div>
                        <div style="height:50px"></div>
                    </el-scrollbar>
                </el-row>
                <el-row style="height: 30%">
                    <el-col :span="8" class="value_area">
                        <el-scrollbar ref="myScrollbar" class="value_inner">
                            <span id="info">temperature 文本随机性(0-2)</span>
                            <el-slider v-model="chat_from.temperature" id="sliderT" input-size="mini"
                                :show-input-controls="false" show-input :max="2" :step="0.01" />
                            <span id="info">top_p 文本多样性(0-1)</span>
                            <el-slider v-model="chat_from.top_p" id="sliderT" input-size="mini" :show-input-controls="false"
                                show-input :max="1" :step="0.01" />
                            <span id="info">presence_penalty 内容重复度(0-1)</span>
                            <el-slider v-model="chat_from.presence_penalty" id="sliderT" input-size="mini"
                                :show-input-controls="false" show-input :max="1" :step="0.01" />
                            <span id="info">frequency_penalty 字词重复度(0-1)</span>
                            <el-slider v-model="chat_from.frequency_penalty" id="sliderT" input-size="mini"
                                :show-input-controls="false" show-input :max="1" :step="0.01" />
                            <span id="info">AI默认设定(1000字以内)</span>
                            <el-autocomplete v-model="chat_from.system" class="value_choose" popper-class="my-autocomplete"
                                :fetch-suggestions="querySearch" placeholder="请输入内容" @select="handleSelect">
                                <!-- 增加设定 -->
                                <i class="el-icon-plus el-input__icon add-sys" slot="suffix" @click="openAddNewSys">
                                </i>
                                <template slot-scope="{item}">
                                    <div class="sys-title-container">
                                        <span class="intro">{{ item.intro }}</span>
                                        <!-- 编辑设定 -->
                                        <i class="el-icon-edit el-input__icon edit-sys" slot="suffix"
                                            @click="openEditSys(item.id)">
                                        </i>
                                    </div>
                                    <div class="sys-value-container">
                                        <div class="sys-value-left-container">
                                            <div class="el-icon-search-zoom">
                                                <i class="el-icon-search"></i>
                                            </div>
                                            <div class="name">{{ item.value }}</div>
                                        </div>
                                        <!-- 删除设定 -->
                                        <i class="el-icon-delete el-input__icon delete-sys" slot="suffix"
                                            @click="deleteSys(item.id)">
                                        </i>
                                    </div>
                                </template>
                            </el-autocomplete>
                        </el-scrollbar>
                    </el-col>
                    <el-col :span="15" style="height: 100%" class="mid_container">
                        <el-col style="height: 10%;" class="token_value">
                            <el-link style="min-width:100px" @click="open1">
                                Token占用:
                                <i class="el-icon-question"></i>
                            </el-link>
                            <div style="width: 75%;" @click="open1">
                                <el-progress :percentage="percentage" :text-inside="true" :stroke-width="24"
                                    :color="customColors"></el-progress>
                            </div>
                        </el-col>
                        <el-col style="height:90%">
                            <!-- 使用tempMsg接收el-input的消息 -->
                            <el-input :disabled="inputDisabled" class="chat_input" style="height: 100%;width:90%"
                                type="textarea" resize="none" placeholder="请输入内容" v-model="tempMsg"
                                @keydown.enter="sendMsg()">
                            </el-input>
                        </el-col>
                    </el-col>
                    <el-col :span="1" style="height:100%;">
                        <el-button @click="sendMsg()">
                            发送
                        </el-button>
                    </el-col>
                </el-row>
            </el-col>
        </el-row>
        <!-- 新增系统设定的Dialog -->
        <el-dialog title="新增系统设定" :visible.sync="addSysDialogVisible" width="30%" append-to-body>
            <el-form :model="new_sys_from" label-width="80px" :rules="add_sys_rule" ref="new_sys_from">
                <el-form-item label="系统介绍" prop="intro">
                    <el-input type="input" v-model="new_sys_from.intro" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="系统内容" prop="value">
                    <el-input class="new-sys-textarea" resize='none' type="textarea" v-model="new_sys_from.value"
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="共享设定:">
                    <el-radio-group v-model="new_sys_from.shared">
                        <el-radio :label="0">私人</el-radio>
                        <el-radio :label="1">分享</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="addSysDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addSys(new_sys_from)">确 定</el-button>
            </span>
        </el-dialog>
        <!-- 编辑系统设定的Dialog -->
        <el-dialog title="编辑系统设定" :visible.sync="editSysDialogVisible" width="30%" append-to-body>
            <el-form :model="edit_sys_from" label-width="80px" :rules="edit_sys_rule" ref="edit_sys_from">
                <el-form-item label="系统介绍" prop="intro">
                    <el-input type="input" v-model="edit_sys_from.intro" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="系统内容" prop="value">
                    <el-input class="new-sys-textarea" resize='none' type="textarea" v-model="edit_sys_from.value"
                        autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="共享设定:">
                    <el-radio-group v-model="edit_sys_from.shared">
                        <el-radio :label="0">私人</el-radio>
                        <el-radio :label="1">分享</el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="editSysDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="editSys(edit_sys_from)">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<style>
/*.chat_container * {
    box-sizing: border-box;
    border: 1px solid red;
    background-position: center;
}*/
.name{
    max-width: 100%;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space:nowrap;
    transition: all .5s;
}
.add-sys {
    margin-right: 5px;
    cursor: pointer;
    transition: all 0.3s;
}

.add-sys:hover {
    color: #e74645;
}

.edit-sys {
    cursor: pointer;
    transition: all 0.3s;
}

.edit-sys:hover {
    color: #e74645;
}

.delete-sys {
    cursor: pointer;
    transition: all 0.3s;
}

.delete-sys:hover {
    color: #e74645;
}

.delete-button {
    color: #606266!important;
    position: relative;
    top: 35%;
    float: right;
    opacity: 0!important;
    transition: all 0.3s;
}

.delete-button:hover {
    color: #e74645!important;
}

.chat-menu:hover .delete-button {
    opacity: 1!important;
}

.chat-menu {
    user-select: none;
}

.chat_box_assistant {
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: start;
    background-color: transparent;
    max-width: 88%;
    border-radius: 5px;
    margin-top: 40px;
    padding: 5px;
}

.chat_box_user {
    display: flex;
    flex-direction: row;
    justify-content: flex-end;
    align-items: start;
    background-color: transparent;
    max-width: 88%;
    border-radius: 5px;
    margin-top: 40px;
    padding: 5px;
}

.chat_box_container {
    margin-left: 20px;
    margin-right: 20px;
    width: fit-content;
    white-space: normal;
    word-break: break-all;
    max-width: 60%;
    background-color: white;
    padding: 10px;
    border-radius: 10px;
    text-align: left;
}

.user_head {
    border-radius: 25%;
    height: 40px;
    width: 40px;
}

.chats_container {
    width: 100%;
    height: 100%;
    padding: 0 !important;
}

.chat_container {
    position: fixed;
    width: 100%;
    height: 100%;
    overflow: hidden !important;
    ;
}

.chat_row {
    height: 100%;
}

.el-input__inner:focus,
.el-textarea__inner:focus {
    border: 1px #e74645 solid !important;

}
.chat_input,.el-textarea{
    width: 100% !important;
    height: 100% !important;
}
.chat_input textarea{
    border: 1px #e74645 solid !important;
    border-bottom: 0 !important;
    border-radius: 0 !important;
}
.chat_input textarea {
    height: 100% !important;
    width: 100% !important;
    font-family: "Microsoft YaHei";
    font-size: 18px !important;
    font-weight: 500 !important;
    border: 1px #e6e6e6 solid !important;
    border-bottom: 0 !important;
    border-radius: 0 !important;
}

.new-sys-textarea .el-textarea__inner:focus {
    border: 1px #e74645 solid !important;
}

.new-sys-textarea .el-textarea__inner{
    height: 200px !important;
    width: 100% !important;
    font-family: "Microsoft YaHei";
    font-size: 16px !important;
    font-weight: normal;
    border: 1px #e6e6e6 solid !important;
}

.el-progress-bar__innerText {
    color: white !important;
}

.left_menu {
    height: 100%;
    box-shadow: 0 0 5px #cac6c6;
}

.chat_area {
    border: 0;
    border-bottom: 4px;
    border-color: #e74645;
    border-style: solid;
    height: 60%;
    background-color: #f5f5f5;
}

.mid_container {
    border: 1px solid #e6e6e6;
}

.token_value {
    border-left: 1.8px solid #e6e6e6;
    width: 100%;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    align-items: center;
    justify-content: center;
}

.chats_container .el-scrollbar__wrap {
    width: 105%;
    margin-left: 50px;
    overflow-x: hidden !important;
    overflow-y: scroll !important;
}
.value_inner .el-scrollbar__wrap {
    width: 105%;
    overflow-x: hidden !important;
    overflow-y: scroll !important;
}

.value_inner .el-scrollbar__view {
    padding-left: 8%;
    width: 90%;
    height: 100%;
}

.el-slider__input {
    width: 20% !important;
    margin-right: 10px;
}

.el-slider__runway {
    margin-right: 120px !important;
}

.el-slider__bar {
    background-color: #fb7756 !important;
}

.el-slider__button {
    border-color: #e74645 !important;
}

.el-scrollbar__thumb {
    background-color: #e74645 !important;
    border-radius: 10px !important;
}

.inline-input {
    width: 88%;
    float: left;
}

.value_area {
    height: 100%;
}

.value_inner {
    height: 100%;
    width: 100%;
}

.value_choose {
    width: 88%;
    padding-top: 4%;
    padding-right: 10%;
    padding-bottom: 10%;
    float: left;
}

.chat_input {
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    background-position: center;
}
.sys-title-container {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
}
.sys-value-container {
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: space-between;
    align-items: center;
}
.sys-value-left-container{
    width: 90%;
    display: flex;
    flex-direction: row;
    flex-wrap: nowrap;
    justify-content: start;
    align-items: start;
}

.el-icon-search-zoom{
    height: 100%;
    display: flex;
    flex-direction: column;
    flex-grow: 0;
    height: 100%!important;
    width: 20px !important;
    margin-right: 5px;
    margin-top: 10px;
    transition: all 1s;
}
.el-icon-search{
    width: 20px;
    height: 20px;
    transition: all 1s;
}
.el-icon-search-zoom:hover .el-icon-search{
    color: #e74645;
}
.el-icon-search-zoom:hover + .name{
    height: fit-content;
    line-height: 25px;
    white-space:normal;
}
.el-icon-search-zoom:hover{
    padding-bottom: 100%;
}
#info {
    text-overflow: clip;
    white-space: nowrap;
    display: inline-block;
}

#sliderT {
    width: 90%;
    background-color: transparent;
}

span {
    font-weight: 700;
}
</style>
<script>
export default {
    data() {
        return {
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
            addSysDialogVisible: false,
            editSysDialogVisible: false,
            inputDisabled: false,
            temp_id: "0",
            head_url: "",
            chatList: [],
            chatContent: [],
            chatInfo: [],
            saves: [],
            sys: [],
            state: '',
            tempMsg: '',
            percentage: 0,
            customColor: '#409eff',
            customColors: [
                { color: '#2f54eb', percentage: 10 },
                { color: '#1890ff', percentage: 20 },
                { color: '#13c2c2', percentage: 30 },
                { color: '#52c41a', percentage: 40 },
                { color: '#a0d911', percentage: 50 },
                { color: '#fadb14', percentage: 60 },
                { color: '#faad14', percentage: 70 },
                { color: '#fa8c16', percentage: 80 },
                { color: '#fa541c', percentage: 90 },
                { color: '#f5222d', percentage: 100 }
            ],
            chat_from: {
                id: 0,
                system: '',
                message: '',
                temperature: 2.00,
                topP: 0.00,
                presencePenalty: 0.00,
                frequencyPenalty: 0.00,
            },
            new_sys_from: {
                intro: '',
                value: '',
                shared: 0,
            },
            edit_sys_from: {
                id: 0,
                intro: '',
                value: '',
                shared: 0,
            },
        }
    },
    methods: {
        //打开新增设定对话框
        openAddNewSys() {
            this.new_sys_from.value = this.chat_from.system
            this.addSysDialogVisible = true;
        },
        //打开编辑设定对话框
        openEditSys(id) {
            if(id==0){
                this.$message({
                    message: "系统默认设定不可编辑！",
                    type: "error"
                });
                return
            }
            this.axios.get('/system/get?id=' + id)
                .then(response => {
                    let data = response.data;
                    if(data.code == 200) {
                        console.log(data)
                        this.edit_sys_from.intro = data.data.intro
                        this.edit_sys_from.value = data.data.value
                        this.edit_sys_from.shared = data.data.shared
                        this.temp_id = id.toString()
                        this.editSysDialogVisible = true;
                    } else {
                        this.$message({
                            message: "获取设定失败！",
                            type: "error"
                        });
                    } 
                })
                .catch(error => {
                    console.log(error);
                });  
        },
        //新增设定
        addSys(form) {
            this.$refs.new_sys_from.validate(valid => {
                if (valid) {
                    //axios使用post方法发送设定
                    this.axios.post('/system', form)
                        .then(response => {
                            let data = response.data;
                            //判断是否保存成功
                            if (data.code == 200) {
                                this.$message({
                                    message: "添加成功！",
                                    type: "success"
                                });
                                this.addSysDialogVisible = false;
                                //刷新设定列表
                                this.getSys()
                            } else {
                                this.addSysDialogVisible = false;
                                this.$message({
                                    message: "添加失败！",
                                    type: "error"
                                });
                            }
                        })
                        .catch(error => {
                            console.log(error);
                        });
                } else {
                    this.$message({
                        message: "请检查输入内容！",
                        type: "error"
                    });
                }
            });
        },
        //编辑设定
        editSys(form) {
            form.id=this.temp_id
            this.$refs.edit_sys_from.validate(valid => {
                if (valid) {
                    //axios使用put方法发送设定
                    this.axios.put('/system', form)
                        .then(response => {
                            let data = response.data;
                            //判断是否保存成功
                            if (data.code == 200) {
                                this.$message({
                                    message: "编辑成功！",
                                    type: "success"
                                });
                                this.temp_id = "0"
                                this.editSysDialogVisible = false;
                            } else {
                                this.temp_id = "0"
                                this.editSysDialogVisible = false;
                                this.$message({
                                    message: "编辑失败！",
                                    type: "error"
                                });
                            }
                        })
                        .catch(error => {
                            console.log(error);
                        });
                } else {
                    this.$message({
                        message: "请检查输入内容！",
                        type: "error"
                    });
                }
            });
        },
        //删除设定
        deleteSys(id) {
            this.$confirm('此操作将永久删除该设定, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //axios使用delete方法删除设定
                this.axios.delete('/system?id=' + id)
                    .then(response => {
                        let data = response.data;
                        //判断是否删除成功
                        if (data.code == 200) {
                            this.$message({
                                message: "删除成功！",
                                type: "success"
                            });
                        } else {
                            this.$message({
                                message: "删除失败！",
                                type: "error"
                            });
                        }
                    })
                    .catch(error => {
                        console.log(error);
                    });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        //新增聊天
        addNewChat() {
            //输入聊天标题
            this.$prompt('请输入聊天标题', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                inputPattern: /^[\u4e00-\u9fa5_a-zA-Z0-9]+$/,
                inputErrorMessage: '聊天标题只能是中文、英文、数字和下划线'
            }).then(({ value }) => {
                //axios使用get方法保存对话的参数
                this.axios.get('/chat/new?title=' + value)
                    .then(response => {
                        let data = response.data;
                        //判断是否保存成功
                        if (data.code == 200) {
                            this.$message({
                                message: "添加成功！",
                                type: "success"
                            });
                            //刷新聊天列表
                            this.created();
                        } else {
                            this.$message({
                                message: "添加失败！",
                                type: "error"
                            });
                        }
                    },)
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '取消新增'
                });
            });
        },
        //删除聊天
        deleteChat(chatID) {
            //再次确认是否删除
            this.$confirm('此操作将永久删除该聊天, 是否继续?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //axios使用delete方法删除对话
                this.axios.delete('/chat?id=' + chatID)
                    .then(response => {
                        let data = response.data;
                        //判断是否删除成功
                        if (data.code == 200) {
                            this.$message({
                                message: "删除成功！",
                                type: "success"
                            });
                            //刷新聊天列表
                            this.created();
                        } else {
                            this.$message({
                                message: "删除失败！",
                                type: "error"
                            });
                        }
                    },).catch(() => {
                        this.$message({
                            type: 'error',
                            message: '删除失败！请检查网络连接！'
                        });
                    });
            }).catch(() => {
                this.$message({
                    type: 'info',
                    message: '已取消删除'
                });
            });
        },
        //获取聊天信息和内容
        getChatInfoAndContent(chatID) {
            //axios使用get方法获取保存对话的参数
            this.axios.get('/chat/info/?id=' + chatID)
                .then(response => {
                    //获取到的数据赋值给chatInfo
                    this.chatInfo = response.data.data;
                    //将获取到的数据赋值给chat_from,以在页面显示
                    this.chat_from.id = this.chatInfo.chatId;
                    this.chat_from.temperature = this.chatInfo.temperature;
                    this.chat_from.topP = this.chatInfo.top_p;
                    this.chat_from.presencePenalty = this.chatInfo.presence_penalty;
                    this.chat_from.frequencyPenalty = this.chatInfo.frequency_penalty;
                    this.chat_from.system = this.chatInfo.system;
                    this.percentage = parseFloat((this.chatInfo.usageTotalTokens / 4096 * 100 * 2).toFixed(2)) > 100 ? 100 : parseFloat((this.chatInfo.usageTotalTokens / 4096 * 100 * 2).toFixed(2));
                })
                .catch(error => {
                    console.log(error);
                });
            //axios使用get方法获取保存的对话
            this.axios.get('/chat/?id=' + chatID)
                .then(response => {
                    this.chatContent = response.data.data;
                })
                .catch(error => {
                    console.log(error);
                });

        },
        //加载页面时初始化数据
        created() {
            this.head_url=this.$getHead()+this.$getUser().head;
            //axios使用get方法获取保存的对话
            this.axios.get('/chat/list')
                .then(response => {
                    this.chatList = response.data.data;
                    //如果有保存的对话则获取第一个对话的信息
                    if (this.chatList != null) {
                        this.getChatInfoAndContent(this.chatList[0].chatID);
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        },
        //发送消息
        sendMsg() {
            //如果token占用达到100%则不允许发送消息
            if (this.percentage == 100) {
                this.$message({
                    message: "Token占用已满，无法发送消息！",
                    type: "error"
                });
                return;
            }
            //禁用输入框
            this.inputDisabled = true;
            this.chat_from.message = this.tempMsg;
            this.tempMsg = '';
            const newChat = {
                chatSide: 'user',
                chatContent: this.chat_from.message,
                enable: 0
            }
            this.chatContent.push(newChat)
            this.$nextTick(() => {
                this.$refs.scrollbar.wrap.scrollTop = this.$refs.scrollbar.wrap.scrollHeight;
            });
            //axios使用post方法发送对话,并且设置超时时间为120秒
            this.axios.post('/chat', this.chat_from, { timeout: 1000 * 120 })
                .then(response => {
                    this.chat_from.message = '';
                    let data = response.data;
                    if (data.code == 200) {
                        this.getChatInfoAndContent(this.chatInfo.chatId);
                        //等待200毫秒后自动滚动到底部
                        setTimeout(() => {
                            this.$nextTick(() => {
                                this.inputDisabled = false;
                                this.$refs.scrollbar.wrap.scrollTop = this.$refs.scrollbar.wrap.scrollHeight;
                            });
                        }, 200);
                    } else {
                        this.inputDisabled = false;
                        this.$message({
                            message: "消息发送失败!错误代码：" + data.code,
                            type: "error"
                        });
                    }
                })
                .catch(error => {
                    this.inputDisabled = false;
                    this.$message({
                        message: "消息发送失败!错误代码：" + error.code,
                        type: "error"
                    });
                });
        },
        //提示框
        open1() {
            const h = this.$createElement;

            this.$notify({
                title: '什么是Token占用？',
                message: h('i', { style: 'color: teal' }, 'Token是OpenAI官方限制的对话上下文内容上限，当你聊天内容占满所有的Token时，我们会自动清理之前的对话，此时GPT会遗忘之前的内容！'),
                duration: 8000
            });
        },
        querySearch(queryString, cb) {
            this.axios.get('/system')
                .then(response => {
                    let data = response.data;
                    //判断是否获取成功
                    if (data.code == 200) {
                        var saves = data.data;
                        saves.unshift({ "intro": "默认设定", "value": "You are a AI helper","id":0})
                        var results = queryString ? saves.filter(this.createFilter(queryString)) : saves;
                        // 调用 callback 返回建议列表的数据
                        cb(results);
                    } else {
                        this.$message({
                            message: "获取设定失败！",
                            type: "error"
                        });
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        },
        createFilter(queryString) {
            return (saves) => {
                return (saves.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        handleSelect(item) {
        },
        handleIconClick(ev) {
        }
    },
    mounted() {
        this.created();
    },
}
</script>