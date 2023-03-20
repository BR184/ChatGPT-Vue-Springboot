<template>
  <el-container id="app">
    <el-header v-if="nav_show" id="header">
      <el-row class="nav">
        <!-- 左侧 -->
        <el-col :span="3">
          <!-- LOGO -->
          <img src="xxlogoxx" alt="LOGO" />
        </el-col>
        <el-col :span="7">
          <!-- 菜单 -->
          <el-menu class="meun" :default-active="activeIndex" mode="horizontal" background-color="transparent"
            active-text-color="#e74645" style="height: 100%;" :router="true">
            <el-menu-item index="/index">首页</el-menu-item>
            <el-submenu index="2">
              <template index="/explore" slot="title">探索</template>
              <el-menu-item index="2-1" rout>选项1</el-menu-item>
              <el-menu-item index="2-2">选项2</el-menu-item>
              <el-menu-item index="2-3">选项3</el-menu-item>
            </el-submenu>
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
          <el-dropdown v-if="this.$getToken()" trigger="hover">
            <el-avatar src="avatar.png"></el-avatar>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item>个人中心</el-dropdown-item>
              <el-dropdown-item>设置</el-dropdown-item>
              <el-dropdown-item @click.native="logout">登出</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <el-button type="danger" size="small" v-else @click="login">登录</el-button>
        </el-col>
      </el-row>
    </el-header>
    <!-- 其余内容 -->
    <div>
      <router-view />
    </div>
  </el-container>
</template>
<script>

export default {
  data: () => ({
    activeIndex: "/index",
    token: null,
    nav_show: true
  }),
  methods: {
    login() {
      this.$router.push({ path: '/login' })
    },
    logout() {
      console.log()
      this.axios.get('/user/logout').then((resp) => {
        const data = resp.data
        if (data.code == 200) {
          this.$message({
            message: "注销成功！", 
            type: "success"
          }); 
          this.$removeToken();
          this.$router.push({ path: '/login' })
        }else{
          this.$message.error("注销失败！错误代码"+data.code); 
        }
      })
    },
  }
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

.nav {
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

#header {
  padding: 0;
  height: 60px;
  caret-color: transparent;
}

.user_info {
  cursor: pointer;
}
</style>
