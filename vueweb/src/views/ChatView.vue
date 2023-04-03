<template>
    <div class="chat_container">
        <el-row :gutter="0" class="chat_row">
            <el-col :span="4" class="left_menu">
                <el-menu style="height: 100%;" default-active="0" class="el-menu-vertical-demo" @open="handleOpen"
                    @close="handleClose" background-color="transparent" text-color="#909399" active-text-color="#e74645">
                    <!-- v-for遍历 el-menu-item -->
                    <el-menu-item v-for="(item, i) in chatList" :key="item.chatID" :index="i.toString()"
                        @click="getChatInfoAndContent(item.chatID)">
                        <i class="el-icon-menu"></i>
                        <span slot="title">{{ item.title }}</span>
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
                            <img v-if="item.chatSide == 'user'" src="../assets/GPTlogo.png" class="user_head" />
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
                                <i class="el-icon-edit el-input__icon" slot="suffix" @click="handleIconClick">
                                </i>
                                <template slot-scope="{ item }">
                                    <div class="name">{{ item.value }}</div>
                                    <span class="intro">{{ item.intro }}</span>
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
                            <el-input class="chat_input" style="height: 100%;width:90%" type="textarea" resize="none"
                                placeholder="请输入内容" v-model="tempMsg" @keydown.ctrl.enter="sendMsg()">
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
    </div>
</template>
<style>
/*.chat_container * {
    box-sizing: border-box;
    border: 1px solid red;
    background-position: center;
}*/

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
    align-items: center;
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

el-input>.el-input__inner:focus,
.el-textarea__inner:focus {
    border: 1px #e74645 solid !important;
    border-bottom: 0 !important;
    border-radius: 0 !important;
}

.el-textarea__inner,
.el-textarea {
    height: 100% !important;
    width: 100% !important;
    font-family: "Microsoft YaHei";
    font-size: 24px !important;
    font-weight: bold;
    border: 1px #e6e6e6 solid !important;
    border-bottom: 0 !important;
    border-radius: 0 !important;
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

.el-scrollbar__wrap {
    width: 105%;
    overflow-x: hidden !important;
    overflow-y: scroll !important;
}

.el-scrollbar__view {
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
    padding-top: 10px;
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
            chatList: [],
            chatContent: [],
            chatInfo: [],
            saves: [],
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
            }
        }
    },
    methods: {
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
                    this.percentage = parseFloat((this.chatInfo.usageTotalTokens / 4096 * 100 * 2).toFixed(2));
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
            //axios使用get方法获取保存的对话
            this.axios.get('/chat/list')
                .then(response => {
                    this.chatList = response.data.data;
                    //如果有保存的对话则获取第一个对话的信息
                    if (this.chatList != null) {
                        //axios使用get方法获取保存对话的参数
                        this.axios.get('/chat/info/?id=' + this.chatList[0].chatID)
                            .then(response => {
                                this.chatInfo = response.data.data;
                                this.chat_from.id = this.chatInfo.chatId;
                                this.chat_from.temperature = this.chatInfo.temperature;
                                this.chat_from.top_p = this.chatInfo.top_p;
                                this.chat_from.presence_penalty = this.chatInfo.presence_penalty;
                                this.chat_from.frequency_penalty = this.chatInfo.frequency_penalty;
                                this.chat_from.system = this.chatInfo.system;
                                this.percentage = parseFloat((this.chatInfo.usageTotalTokens / 4096 * 100).toFixed(2));
                            })
                            .catch(error => {
                                console.log(error);
                            });
                        //axios使用get方法获取保存的对话
                        this.axios.get('/chat/?id=' + this.chatList[0].chatID)
                            .then(response => {
                                this.chatContent = response.data.data;
                            })
                            .catch(error => {
                                console.log(error);
                            });
                    }
                })
                .catch(error => {
                    console.log(error);
                });
        },
        sendMsg() {
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
            //axios使用post方法发送对话,并且设置超时时间为30秒
            this.axios.post('/chat', this.chat_from, { timeout: 1000 * 30 })
                .then(response => {
                    this.chat_from.message = '';
                    let data = response.data;
                    if (data.code == 200) {
                        this.getChatInfoAndContent(this.chatInfo.chatId);
                        //等待1秒后自动滚动到底部
                        setTimeout(() => {
                            this.$nextTick(() => {
                                this.$refs.scrollbar.wrap.scrollTop = this.$refs.scrollbar.wrap.scrollHeight;
                            });
                        }, 1000);
                    }
                })
                .catch(error => {
                    console.log(error);
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
        handleOpen(key, keyPath) {
            console.log(key, keyPath);
        },
        handleClose(key, keyPath) {
            console.log(key, keyPath);
        },
        querySearch(queryString, cb) {
            var saves = this.saves;
            var results = queryString ? saves.filter(this.createFilter(queryString)) : saves;
            // 调用 callback 返回建议列表的数据
            cb(results);
        },
        createFilter(queryString) {
            return (saves) => {
                return (saves.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0);
            };
        },
        loadAll() {
            return [
                { "value": "You are an AI helper", "intro": "默认设定1" },
                { "value": "You are an AI helper", "intro": "默认设定2" },
                { "value": "You are an AI helper", "intro": "默认设定3" },
                { "value": "You are an AI helper", "intro": "默认设定4" },
            ]
        },
        handleSelect(item) {
            console.log(item);
        },
        handleIconClick(ev) {
            console.log(ev);
        }
    },
    mounted() {
        this.saves = this.loadAll();
        this.created();
    },
}
</script>