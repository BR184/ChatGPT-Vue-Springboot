<template>
    <div class="index-main-container">

        <div class="block" style="position: relative;">
            <el-carousel height="500px">
                <el-carousel-item v-for="item, index in title" :key="item">
                    <div :class="index % 2 == 0 ? 'head-1' : 'head-2'">
                        <img :src="item.src" class="index-head-img">
                        <h1>{{ item.value }}</h1>
                    </div>
                    <div class="index-head-bottom-line1"></div>
                    <div class="index-head-bottom-line2"></div>
                    <div class="index-head-bottom-line3"></div>
                    <div class="index-head-bottom-line4"></div>
                    <div class="index-head-bottom-line5"></div>
                </el-carousel-item>
            </el-carousel>
            <el-button type="danger" class="central_button" @click="chat">立即尝试</el-button>
        </div>
        <div class="index-mid-container">
            <div class="index-info-page">
                <h1 class="index-info-page-title">聊天</h1>
                <div class="index-info-page-value">
                    <span class="index-info-page-text">
                        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                        对于喜欢自定义聊天参数的用户来说，我们的“聊天”功能是一个非常好的选择。
                        您可以自由地设置对话主题、语气、风格等参数，让GPT3.5以您喜欢的方式与您进行交流。
                        这将给您带来一个全新的人工智能聊天体验，让您更加深入地了解GPT3.5的能力和潜力。
                    </span>
                </div>
            </div>
            <div class="index-info-page">
                <h1 class="index-info-page-title">探索</h1>
                <div class="index-info-page-value">
                    <span class="index-info-page-text">
                        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                        对于希望更好地了解GPT3.5能力的用户来说，“探索”功能是一个必不可少的工具。
                        在这里，您可以浏览其他用户发布的GPT3.5默认设定，并通过与GPT3.5的交互体验来深入了解它的功能和特点。
                        这将为您打开一个全新的世界，让您更好地理解和掌握人工智能技术。
                    </span>
                </div>
            </div>
            <div class="index-info-page">
                <h1 class="index-info-page-title">分享</h1>
                <div class="index-info-page-value">
                    <span class="index-info-page-text">
                        &nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
                        对于喜欢分享和讨论自己聊天记录的用户来说，“分享”功能将是一个非常有趣的选择。
                        您可以将自己与GPT3.5的有趣聊天记录发布到网站上，并与其他用户分享和讨论。
                        这将让您更好地了解人工智能技术的应用，同时也可以与其他志同道合的用户交流和互动，让您的聊天体验更加丰富和有趣。
                    </span>
                </div>
            </div>
        </div>
        1212112
    </div>
</template>

<script>
export default {
    data() {
        return {
            title: [{
                src: require('../../static/TITLE1.png'),
                value: '什么都可以问，提出问题，问题描述可长可短。提问越精确，答案就合适。',
            },
            {
                src: require('../../static/TITLE2.png'),
                value: '自定义AI对话参数，让AI更懂你，更懂你的问题，更懂你的需求。'
            },
            {
                src: require('../../static/TITLE3.png'),
                value: '分享自己的AI参数，让更多人体验你的AI对话效果，让你的AI更有价值。'
            },
            {
                src: require('../../static/TITLE4.png'),
                value: '探索别人分享的AI参数，体验不同的AI对话效果，让你的问题得到更好的解决。'
            },
            ]
        }
    },
    methods: {
        chat() {
            this.$router.push({ path: '/chat' })
        },
    }

}
</script>

<style>
.index-info-page-text{
    display: block;
    font-weight: 400;
    line-height: 40px;
    padding: 20px;
    height: 88% !important;
    font-size: 20px;
    text-align: left;
    overflow: hidden;
    text-overflow: ellipsis;
}
.index-info-page-title {
    font-size: 40px;
    margin: 10px 0 10px 0;
    height: 16%;
}

.index-info-page-value {
    font-size: 20px;
    margin: 0;
    height: 80%;
    padding-bottom: auto;
    background-color: #ebf1f4;
}

.index-mid-container {
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: start;
    width: 100%;
    height: 700px;
}

.index-info-page {
    border-top: 5px solid #f16d6d;
    margin: 100px;
    width: 22%;
    height: 500px;
    box-shadow: 0 0 10px #ccc;
}

.head-1 {
    padding-top: 70px;
    background-color: #e74645 !important;
    display: flex;
    flex-direction: row;
    justify-content: space-around;
    align-items: start;
    height: 100%;
    width: 100%;
}

.head-1 h1 {
    color: #fff;
    margin: 10px;
    font-size: 400%;
    text-align: left;
}

.head-2 {
    padding-top: 70px;
    background-color: #2f384c !important;
    display: flex;
    flex-direction: row-reverse;
    justify-content: space-around;
    align-items: start;
    height: 100%;
    width: 100%;
}

.head-2 h1 {
    color: #fff;
    margin: 10px;
    font-size: 400%;
    text-align: right;
}

.index-head-title {
    margin: 0;
    color: #fff;
    font-size: 60px;
    text-align: right;
}

.central_button {
    border: 1.5px solid #facd60 !important;
    position: absolute;
    top: 80%;
    left: 50%;
    height: 45px;
    width: 100px;
    transform: translate(-50%, -50%);
    z-index: 9999;
    transition: all 0.5;
    text-align: center;
    box-shadow: none;
}
.central_button:hover {
    box-shadow: 0 0 10px #fff;
    transform: translate(-40%, -50%);
    scale: 1.2;
}

.el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 500px;
    margin: 0;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
}

.index-head-img {
    height: 50%;
    width: auto;
    margin: 20px;
    border-radius: 20px;
}

.index-head-bottom-line1 {
    width: 100% !important;
    height: 10px !important;
    background-color: #fff !important;
    position: absolute;
    bottom: 112px;
}

.index-head-bottom-line2 {
    width: 100% !important;
    height: 8px !important;
    background-color: #fff !important;
    position: absolute;
    bottom: 92px;
}

.index-head-bottom-line3 {
    width: 100% !important;
    height: 6px !important;
    background-color: #fff !important;
    position: absolute;
    bottom: 72px;
}

.index-head-bottom-line4 {
    width: 100% !important;
    height: 4px !important;
    background-color: #fff !important;
    position: absolute;
    bottom: 52px;
}

.index-head-bottom-line5 {
    width: 100% !important;
    height: 2px !important;
    background-color: #fff !important;
    position: absolute;
    bottom: 32px;
}</style>