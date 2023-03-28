<template>
    <div class="chat_container">
        <el-row :gutter="0" class="chat_row">
            <el-col :span="4" class="left_menu">
                <el-menu style="height: 100%;" default-active="1" class="el-menu-vertical-demo" @open="handleOpen"
                    @close="handleClose" background-color="transparent" text-color="#909399" active-text-color="#e74645">
                    <el-menu-item index="1">
                        <i class="el-icon-menu"></i>
                        <span slot="title">导航一</span>
                    </el-menu-item>
                    <el-menu-item index="2">
                        <i class="el-icon-menu"></i>
                        <span slot="title">导航二</span>
                    </el-menu-item>
                </el-menu>
            </el-col>
            <el-col style="height: 100%;" :span="20">
                <el-row class="chat_area">
                    <el-col :span="24">

                    </el-col>
                </el-row>
                <el-row style="height: 32.5%">
                    <el-col :span="8" class="value_area">
                        <el-scrollbar class="value_inner">
                            <span id="info">temperature 文本随机性(0-2)</span>
                            <el-slider v-model="chat_from.temperature" id="sliderT" input-size="mini"
                                :show-input-controls="false" show-input :max="2" :step="0.01"
                                 />
                            <span id="info">top_p 文本多样性(0-1)</span>
                            <el-slider v-model="chat_from.top_p" id="sliderT" input-size="mini" :show-input-controls="false"
                                show-input :max="1" :step="0.01"  />
                            <span id="info">presence_penalty 内容重复度(0-1)</span>
                            <el-slider v-model="chat_from.presence_penalty" id="sliderT" input-size="mini"
                                :show-input-controls="false" show-input :max="1" :step="0.01"
                                 />
                            <span id="info">frequency_penalty 字词重复度(0-1)</span>
                            <el-slider v-model="chat_from.frequency_penalty" id="sliderT" input-size="mini"
                                :show-input-controls="false" show-input :max="1" :step="0.01"
                                 />
                            <span id="info">AI默认设定(1000字以内)</span>
                            <el-autocomplete class="value_choose" popper-class="my-autocomplete" v-model="state"
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
                    <el-col :span="15" style="height:100%;">
                        <el-input class="chat_input" style="height: 100%;" type="textarea" resize="none" placeholder="请输入内容"
                            v-model="chat_from.chat_content">
                        </el-input>
                    </el-col>
                    <el-col :span="1" style="height:100%;">1</el-col>
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

.chat_container {
    position: fixed;
    width: 100%;
    overflow: auto;
    height: 100vh;
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
    border-top: 0 !important;
    border-bottom: 0 !important;
    border-radius: 0 !important;
}

.el-textarea__inner,
.el-textarea {
    height: 100% !important;
    font-family: "Microsoft YaHei";
    font-size: 24px !important;
    font-weight: bold;
    border: 1px #e6e6e6 solid !important;
    border-top: 0 !important;
    border-bottom: 0 !important;
    border-radius: 0 !important;
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

.el-scrollbar__wrap {
    width: 105%;
    overflow-x: hidden !important;
    overflow-y: scroll !important;
    ;
}

.el-scrollbar__view {
    padding-left: 10%;
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

.value_choose{
    width: 88%;
    padding-top: 5%;
    padding-right: 10%;
    float: left;
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
            saves: [],
            state: '',
            chat_from: {
                chat_side: '',
                chat_content: '',
                temperature: 1.00,
                top_p: 0.00,
                presence_penalty: 0.00,
                frequency_penalty: 0.00,
                system_persona: ''
            }
        }
    },
    methods: {
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
    }
}
</script>