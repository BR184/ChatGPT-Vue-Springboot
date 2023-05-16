<template>
  <el-container id="app">
    <el-header v-if="nav_show" id="header">
      <el-row class="nav">
        <!-- 左侧 -->
        <el-col :span="4">
          <!-- LOGO -->
          <img src="../src/assets/GPT SHARE LOGO.png" width="200" alt="LOGO" />
        </el-col>
        <el-col :span="6">
          <!-- 菜单 -->
          <el-menu class="meun" :default-active="this.$route.path" mode="horizontal" background-color=#fff
            active-text-color="#e74645" style="height: 100%;" :router="true">
            <el-menu-item index="/index">首页</el-menu-item>
            <el-menu-item index="/chat">聊天</el-menu-item>
            <el-menu-item index="/explore">探索</el-menu-item>
            <el-menu-item index="3">分享</el-menu-item>
          </el-menu>
        </el-col>
        <!-- 中间 -->
        <el-col :span="5">
          <!-- 搜索栏 -->
          <div class="center-nav">
            <el-input placeholder="请输入内容"></el-input>
          </div>
        </el-col>
        <!-- 右侧 -->
        <el-col :span="9">
          <!-- 头像和菜单 -->
          <div v-if="this.show_info()" class="head">
            <el-dropdown trigger="hover">
              <el-avatar :src="this.$getHead()+this.$getUser().head" @click.native="home"></el-avatar>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item @click.native="home">个人中心</el-dropdown-item>
                <el-dropdown-item @click.native="logout">登出</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
            <p style="margin-left:15px">{{this.$getUser().username}}</p>
            <el-button style="margin-left:15px" type="danger" size="small" v-if="adm_show" @click="admin">管理</el-button>
          </div>
          <el-button type="danger" size="small" v-else @click="login">登录</el-button>
        </el-col>
      </el-row>
    </el-header>
    <!-- 其余内容 -->
    <div>
      <router-view/>
    </div>
  </el-container>
</template>
<script>

export default {
  data: () => ({
    token: null,
    nav_show: true,
    adm_show: false,
    footer_show: true
  }),
  methods: {
    home() {
      this.$router.push({ path: '/home' })
    },
    login() {
      this.$router.push({ path: '/login' })
    },
    logout() {
      this.axios.get('/user/logout').then((resp) => {
        const data = resp.data
        if (data.code == 200) {
          this.$message({
            message: "注销成功！", 
            type: "success"
          }); 
          this.$removeToken();
          this.$removeUser();
          this.$router.push({ path: '/login' })
        }else{
          this.$message.error("注销失败！请先登陆！错误代码"+data.code); 
          this.$removeToken();
          this.$removeUser();
          this.$router.push({ path: '/login' })
        }
      })
    },
    admin() {
      this.$router.push({ path: '/admin' })
    },
    show_info(){
      if(this.$getToken()!=null){
        //检查是否有基础权限
        this.axios.get('/admin/is_admin').then((resp) => {
          const data = resp.data
          if (data.code == 200) {
            this.adm_show = true;
          }else{
            this.adm_show = false;
          }
        })
        return true;
      }
      return false;
    }
  },
}

</script>
<style lang="less">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  height: 100%;
}

.el-footer{
  text-align: center;
  color: #fff;
  background-color: #e34847;
  height: 200px!important;
  width: 100%!important;
  font-size: small;
  margin-top: 169px;
  padding: 100px 0 0 0!important;
}
.nav {
  user-select: none;
  position: fixed;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px #e6e6e6 solid;
  height: 60px;
  min-width: 900px;
  background-color: transparent;
}

.meun {
  background-color: transparent;
}
.head{
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}

#header {
  padding: 0;
  height: 60px;
  caret-color: transparent;
}

.user_info {
  cursor: pointer;
}
</style>
