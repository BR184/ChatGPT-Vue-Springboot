<template>
    <div>
        <h1>欢迎进入后台管理系统->{{ this.$getUser().username }}</h1>
        <el-calendar v-model="value">
        </el-calendar>
    </div>
</template>

<script>
    export default {
        
    }
</script>

<style>

</style>