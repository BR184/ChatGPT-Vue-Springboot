<template>
    <div class="div">
        <div class="block" style="position: relative;">
            <el-carousel height="500px">
                <el-carousel-item v-for="item in 4" :key="item">
                    <h3 class="small">{{ item }}</h3>
                </el-carousel-item>
            </el-carousel>
            <el-button type="danger" class="central_button" @click="chat">立即尝试</el-button>
        </div>
    </div>
</template>

<script>
export default {
    methods: {
        chat() {
            this.$router.push({ path: '/chat' })
        },
    }

}
</script>

<style>
.central_button {
    position: absolute;
    top: 80%;
    left: 50%;
    height: 45px;
    width: 100px;
    transform: translate(-50%, -50%);
    z-index: 9999;
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
</style>