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
import { saveToken, getToken, removeToken } from "./utils/token.js";
import { saveUser, getUser, removeUser,getAvaPerfix } from "./utils/userinfo";

Vue.config.productionTip = false;
//安装elementui
Vue.use(ElementUI);
//安装axios
Vue.use(VueAxios, axios);

Vue.prototype.$debounce = debounce;

Vue.prototype.$saveToken = saveToken;
Vue.prototype.$getToken = getToken;
Vue.prototype.$removeToken = removeToken;

Vue.prototype.$saveUser = saveUser;
Vue.prototype.$getUser = getUser;
Vue.prototype.$removeUser = removeUser;
Vue.prototype.$getHead = getAvaPerfix()+getUser().head;

router.beforeEach((to, from, next) => {
  if (localStorage.getItem("Token") && !localStorage.getItem("User")) {
        axios.get("/user/info").then((resp) => {
          let data = resp.data;
          if (data.code == 200) {
            localStorage.setItem("User",JSON.stringify(data.data))
          }
        })
      }
  next()
})

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')

