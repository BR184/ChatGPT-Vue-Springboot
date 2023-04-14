<template>
    <div class="admin-page">
        <div class="admin-container">
            <el-col :span="4" class="menu-left-container">
                <el-menu default-active="index" class="menu-left"
                    background-color="#e74645" text-color="#fff" active-text-color="#facd60" :router="true">
                    <h1 class="menu-left-title">管理系统</h1>
                    <div class="admin-title-line"></div>
                    <el-menu-item index="index">
                        <i class="el-icon-s-home"></i>
                        <span slot="title">首页</span>
                    </el-menu-item>
                    <el-menu-item index="role">
                        <i class="el-icon-document-copy"></i>
                        <span slot="title">角色管理</span>
                    </el-menu-item>
                    <el-menu-item index="user">
                        <i class="el-icon-user"></i>
                        <span slot="title">用户管理</span>
                    </el-menu-item>
                    <el-menu-item index="ai_setting">
                        <i class="el-icon-set-up"></i>
                        <span slot="title">设定管理</span>
                    </el-menu-item>
                    <el-submenu index="2">
                        <template slot="title">
                            <i class="el-icon-location"></i>
                            <span>导航一</span>
                        </template>
                        <el-menu-item-group>
                            <el-menu-item index="2-1">选项1</el-menu-item>
                            <el-menu-item index="2-2">选项2</el-menu-item>
                        </el-menu-item-group>
                    </el-submenu>
                </el-menu>
            </el-col>
            <el-col :span="20" class="menu-right-container">
                <router-view></router-view>
            </el-col>
        </div>
    </div>
</template>

<script>
export default {

}
</script>

<style>
hr{
    margin: 0;
}
.admin-title-line{
    width: 100% !important;
    height: 2px;
    background-color: #fff;
}
.admin-page {
    position: fixed;
    width: 100%;
    height: 100%;
    background-color: #f5f5f5;
    display: flex;
    justify-content: center;
    align-items: center;
}

.admin-container {
    height: 100%;
    width: 80%;
    background-color: #fff;
}

.menu-left-title {
    color: #fff;
    font-size: 30px;
    margin-top: 50px;
    margin-bottom: 50px;
}

.menu-left-container {
    height: 100%;
    background-color: #e74645;
    padding: 0;
}

.menu-left {
    height: 100%;
    user-select: none;
}

.menu-right-container {
    height: 100%;
    background-color: #fff;
    padding: 0;
    display: flex;
    justify-content: center;
    align-items: start;
}

.admin-container .el-menu-item {
    background-color: transparent !important;
}

.admin-container .el-menu-item.is-active {
    background-color: #b63a39 !important;
    font-weight: bolder;
}
</style>