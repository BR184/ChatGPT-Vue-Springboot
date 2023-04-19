<template>
    <div class="admin-index-container">
        <div class="admin-table-header">
            <div class="admin-table-header-left">
                <h1 style="font-size: 35px">欢迎！</h1>
                <h1 class="admin-name" style="font-size: 35px">{{ this.$getUser().username }}</h1>
            </div>
            <div class="admin-table-header-right">
                <div class="admin-index-timer">
                    <h4>当前时间：</h4>
                    <h2>{{ this.datenow}}</h2>
                </div>
                
            </div>
        </div>
        <el-row :gutter="20" class="admin-index-statistic">
            <el-col :span="6">
                <div>
                    <el-statistic group-separator="," :precision="0" :value="todayNewUser" title="今日新增用户数"></el-statistic>
                </div>
            </el-col>
            <el-col :span="6">
                <div>
                    <el-statistic group-separator="," :precision="0" :value="todayNewAiSetting" title="今日新增设定数"></el-statistic>
                </div>
            </el-col>
            <el-col :span="6">
                <div>
                    <el-statistic group-separator="," :precision="0" :value="todayNewChat" title="今日新增聊天数"></el-statistic>
                </div>
            </el-col>
            <el-col :span="6">
                <div>
                    <el-statistic group-separator="," :precision="0" :value="loginUsers" title="已登陆用户数"></el-statistic>
                </div>
            </el-col>
        </el-row>
        <el-calendar style="margin-top:100px" v-model="value">
        </el-calendar>
    </div>
</template>

<script>
export default {
    data() {
        return {
            value: new Date(),
            todayNewUser:0,
            todayNewAiSetting:0,
            todayNewChat:0,
            loginUsers:0,
            datenow: '00:00:00'
        };
    },
    methods: {
        getCurrentTime() {
            let date = new Date();
            let hour = date.getHours();
            let minute = date.getMinutes();
            let second = date.getSeconds();
            hour = hour < 10 ? "0" + hour : hour;
            minute = minute < 10 ? "0" + minute : minute;
            second = second < 10 ? "0" + second : second;
            return `${hour}:${minute}:${second}`;
        },
        getBasicStatistic() {
            this.$http.get('/admin/statistic').then(res => {
                const data = res.data;
                if (data.code === 200) {
                    this.todayNewUser = data.data.todayNewUser;
                    this.todayNewAiSetting = data.data.todayNewAiSetting;
                    this.todayNewChat = data.data.todayNewChat;
                    this.loginUsers = data.data.loginUsers;
                }
            });
        }
    },
    mounted() {
        var update = setInterval(() => {
            this.datenow = this.getCurrentTime();
            this.getBasicStatistic();
        }, 1000);
        this.$once('hook:beforeDestroy', () => {            
        clearInterval(update);                                    
        })
    },
}
</script>

<style>
.admin-index-container {
    width: 90%;
    user-select: none;
}

.like {
    cursor: pointer;
    font-size: 25px;
    display: inline-block;
}

.admin-index-timer{
    display: flex;
    flex-direction: column;
    height: 80px;
    justify-content: center;
    align-items: center;
}
.admin-index-timer h4{
    margin: 0;
    padding: 0;
}
.admin-index-timer h2{
    margin: 0;
    padding: 0;
}
.admin-index-statistic{
    margin-top: 20px!important;
}
</style>