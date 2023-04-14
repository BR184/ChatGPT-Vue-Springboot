<template>
  <div class="home" style="width: 100%;height:100%">
    <el-row :gutter="0" class="menu">
      <el-col :span="12" :offset="6" class="menu-container">
        <el-col :span="8" class="left-menu">
          <h1 class="title">个人中心</h1>
          <el-menu default-active="/home/info" class="el-menu-vertical-demo" active-text-color="#e74645"
          background-color="#fff" :router="true">
            <el-menu-item index="/home/info">
              <i class="el-icon-menu"></i>
              <span slot="title">个人信息</span>
            </el-menu-item>
            <el-menu-item index="/home/fav_chat">
              <i class="el-icon-menu"></i>
              <span slot="title">聊天收藏</span>
            </el-menu-item>
            <el-menu-item index="/home/fav_sys">
              <i class="el-icon-setting"></i>
              <span slot="title">设定收藏</span>
            </el-menu-item>
          </el-menu>
        </el-col>
        <el-col :span="16" class="right-class">
          <router-view style="height:100%" />
        </el-col>
      </el-col>
    </el-row>
  </div>
</template>

<script>
// @ is an alias to /src

export default {
  name: 'HomeView',
}
</script>

<style>
.home{
  position: fixed;
  width: 100%;
  height: 100%;
  background-color: #ebf1f4;
}
.menu {
  width: 100%;
  height: 100%;
}
.menu-container{
  background-color: white;
  margin-top: 60px;
  height: 80%;
  border: 3px solid #e74645;
  border-radius: 30px;
  box-shadow: 0 0 25px #cacaca;
}
.left-menu {
  height: 100%;
  border-right: 2px solid #eaeaea;
}
.right-class{
  height: 100%;
}
.menu-container .left-menu .title {
  text-align: center;
  font-size: 30px;
  font-weight: bold;
  margin: 40px 30px 20px 30px;
  padding-bottom:40px;
  border-bottom: 1px solid #d4d4d4;
}

/*.home * {
  box-sizing: border-box;
  border: 1px solid red;
  background-position: center;
}*/
</style>
