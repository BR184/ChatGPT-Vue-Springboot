import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//引入elementui
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//引入axios
import axios from './utils/request.js';
import VueAxios from 'vue-axios';
import { debounce } from "./utils/debounce.js";
import { saveToken,getToken,removeToken,hasToken } from "./utils/token.js";

Vue.config.productionTip = false;
//安装elementui
Vue.use(ElementUI);
//安装axios
Vue.use(VueAxios,axios);
Vue.prototype.$debounce = debounce;
Vue.prototype.$saveToken = saveToken;
Vue.prototype.$getToken = getToken;
Vue.prototype.$removeToken = removeToken;
Vue.prototype.$hasToken = hasToken;
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
