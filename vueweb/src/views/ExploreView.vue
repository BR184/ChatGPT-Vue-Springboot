<template>
    <div class="shared-container">
        <div class="container-top">
            <h1 class="title-explore">探索并尝试各种AI设定</h1>
            <div class="container-top-right">
                <div>
                    <el-radio-group v-model="mode" @change="handleModeChange">
                      <el-radio-button :label="1">最新发布</el-radio-button>
                      <el-radio-button :label="2">最旧发布</el-radio-button>
                      <el-radio-button :label="3">最多使用</el-radio-button>
                      <el-radio-button :label="5">最近更新</el-radio-button>
                    </el-radio-group>
                  </div>
                <el-pagination background layout="prev, pager, next" @current-change="handleCurrentChange"
                :current-page.sync="this.current_page" :page-count="this.total_page">
                </el-pagination>
            </div>
        </div>
        <div class="main-divider"></div>
        <div class="central-container">
            <div v-for="sys in sys_list" :key="sys.id" class="export-value-container">
                <div class="central-header">
                    <h1 class="">主题：{{ sys.intro }}</h1>
                    <el-button class="save-btn" v-if="user.id != sys.userId" type="danger"
                        @click="saveSys(sys.id)">使用</el-button>
                    <el-button v-else type="danger" @click="deleteSys(sys.id)">删除</el-button>
                </div>
                <span class="sys-name">使用数:{{ sys.fav }}
                    &nbsp发布者:{{ sys.username }}
                    &nbsp发布时间:{{ sys.createTime.substring(0, 10) }}
                    &nbsp更新时间:<span v-if="sys.updateTime">{{ sys.updateTime.substring(0, 10) }}</span><span v-else>无</span>
                </span>
                <!-- 分割线 -->
                <el-divider class="sys-divider"></el-divider>
                <h3 class="sys-value">{{ sys.value }}</h3>
            </div>
        </div>
        <div class="pagination-container">
            <el-pagination background layout="prev, pager, next, jumper" @current-change="handleCurrentChange"
                :current-page.sync="this.current_page" :page-count="this.total_page">
            </el-pagination>
        </div>
        <!-- 底部 -->
        <el-footer id="footer">
            <div class="footer">
                <p>© 2023 GPT SHARE 聊天分享交流网站</p>
                <p>Copyright © 1998 - 2023 KLBR184. All Rights Reserved.</p>
                <p>KLBR184 版权所有｜营业执照｜鲁ICP备1XXXXXXX号-X｜客服电话：4006666666｜违法与不良信息举报邮箱：XXXXXXXXXX@klbr184.com</p>
            </div>
        </el-footer>
    </div>
</template>

<script>
export default {
    data() {
        return {
            sys_list: [],
            current_page: 1,
            total_page: 1,
            user: [],
            mode:1
        }
    },
    methods: {
        created() {
            this.user = JSON.parse(localStorage.getItem('User'))
            this.axios.get('/system/shared?page=1&mode=1').then((resp) => {
                const data = resp.data
                if (data.code == 200) {
                    this.sys_list = data.data.list;
                    this.current_page = data.data.page;
                    this.total_page = data.data.totalPage;
                } else {
                    this.$message.error("获取列表失败！错误代码" + data.code);
                }
            })
                .catch((error) => {
                    this.$message.error("获取列表失败！错误:" + error);
                    //一段时间后重试
                    setTimeout(() => {
                        this.created();
                    }, 5000);
                })
        },
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
                            this.created();
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
        saveSys(id) {
            this.$confirm('是否保存该设定?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                //axios使用delete方法删除设定
                this.axios.get('/system/save?id=' + id)
                    .then(response => {
                        let data = response.data;
                        //判断是否删除成功
                        if (data.code == 200) {
                            this.$message({
                                message: "保存成功！",
                                type: "success"
                            });
                            this.created();
                        } else {
                            this.$message({
                                message: "保存失败！",
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
                    message: '已取消保存'
                });
            });
        },
        handleModeChange(){
            console.log(this.mode)
            this.handleCurrentChange(1);
        },
        handleCurrentChange(val) {
            if (val==null) {
                return;
            }
            this.current_page = val;
            console.log(this.mode)
            this.axios.get('/system/shared?page=' + this.current_page + '&' + 'mode=' + this.mode).then((resp) => {
                const data = resp.data
                if (data.code == 200) {
                    console.log(data)
                    this.sys_list = data.data.list;
                    this.current_page = data.data.page;
                    this.total_page = data.data.totalPage;
                } else {
                    this.$message.error("获取列表失败！错误代码" + data.code);
                }
            })
                .catch((error) => {
                    this.$message.error("获取列表失败！错误:" + error);
                })
        }
    },
    //页面加载完成后执行
    mounted() {
        this.created();
    },
    beforeCreate() {
        document.querySelector('html').setAttribute('style', 'overflow-x:hidden!important')
    },
    beforeDestroy() {
        document.querySelector('html').setAttribute('style', 'overflow-x:auto')
    }
}
</script>

<style>
/* .shared-container * {
    box-sizing: border-box;
    border: 1px solid red;
    background-position: center;
} */
.container-top-right .el-radio-button:first-child .el-radio-button__inner{
    border-radius: 0 !important;
    box-shadow: transparent!important;
}
.container-top-right .el-radio-button:last-child .el-radio-button__inner{
    border-radius: 0 !important;
    box-shadow: transparent!important;
}
.container-top-right .el-radio-button__orig-radio:checked+.el-radio-button__inner{
    background-color: #e74645!important;
    border: none !important;
    box-shadow: -1px 0 0 0 #e74645 !important;
    box-shadow: transparent!important;
}
.el-radio-button.is-active .el-radio-button__inner{
    background-color: transparent!important;
    border: none !important;
    border-radius: 0 !important;
    box-shadow: transparent!important;
}
.el-radio-button.is-active span{
    background-color: #e74645!important;
    border: none !important;
    border-radius: 0 !important;
    box-shadow: 0!important;
}
.container-top-right .el-radio-button span:hover{
    color: #e74645;
}
.container-top-right .el-radio-button.is-active span:hover{
    color: #d5d5d5;
}
.container-top-right{
    width: fit-content;
    height: 100px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: end;
}
.sort-select{
    margin-left: 20px; 
}
.shared-container {
    user-select: none;
}

.shared-container {
    width: 100%;
}

.title-explore {
    font-size: 60px;
    font-weight: 800;
    border: 0;
    font-family: "Microsoft YaHei";
    color: #333333;
    margin-bottom: 20px!important;
}

.main-divider {
    margin-top: 50px;
    margin-bottom: 15px;
    margin-left: 5%;
    margin-right: 5%;
    width: 90% !important;
    height: 1px;
    background-color: #999999;
}

.save-btn {
    background-color: #facd60 !important;
    border-color: #facd60 !important;
    transition: all 0.3;
}

.save-btn:hover {
    background-color: #f7d88a !important;
    border-color: #f7d88a !important;
}

.central-header {
    display: flex;
    width: 100%;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.sys-name {
    font-size: 10px;
    font-weight: 800;
    font-family: "Microsoft YaHei";
    color: #999999;
    margin-bottom: 10px;
}

.sys-divider {
    margin-top: 0 !important;
    margin-bottom: 30px !important;
    padding: 0;
}

.shared-container {
    height: 100%;
    width: 100%;
    background-color: #f5f5f5;
}

.export-value-container {
    margin-top: 30px;
    padding: 2%;
    background-color: white;
    border-radius: 30px;
    min-width: min-content;
    width: 45%;
    height: fit-content !important;
    display: flex !important;
    flex-direction: column;
    justify-items: start;
    align-items: flex-start;
    text-align: start;
    text-overflow: ellipsis;
    border: 0px solid #ffee00;
    box-shadow: 0 0 10px #ebebeb;
    transition: all 0.5s;
}

.export-value-container:hover {
    scale: 1.1;
    z-index: 9999;
    border: 6px solid #e74645;
    box-shadow: 0 0 20px #999999;
}

.scollbar-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-content: center;
    width: 100%;
    height: 95%;
    background-color: #f5f5f5;
}

.central-container {
    width: 90%;
    padding-left: 5%;
    padding-right: 5%;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: space-between;
    align-items: start;
}

.pagination-container {
    padding-top: 100px;
    margin-top: 100px;
    scale: 1.65;
    width: 100%;
    height: 200px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: center;
    align-items: start;
}

.container-top {
    margin-bottom: 0px;
    width: 90%;
    padding-left: 5%;
    padding-right: 5%;
    height: 100px;
    display: flex;
    flex-wrap: wrap;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;
}

.number:not(.active) {
    background-color: #ffffff !important;
}

.number,
.active {
    background-color: #e74645 !important;
}

.number:not(.active):hover {
    color: #e74645 !important;
}

.number:hover {
    color: #606266 !important;
}
</style>