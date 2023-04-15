import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/user/HomeView.vue'
import Login from '../views/user/Login.vue'
import Register from '../views/user/Register.vue'
import Index from '../views/IndexView.vue'
import Logout from '../views/user/Logout.vue'
import Chat from '../views/ChatView.vue'
import UserInfo from '../views/user/UserInfo.vue'
import FavSys from '../views/user/FavSys.vue'
import FavChat from '../views/user/FavChat.vue'
import ShareView from '../views/ShareView.vue'
import ExploreView from '../views/ExploreView.vue'
import AdminView from '../views/admin/AdminView.vue'
import AdminIndexView from '../views/admin/AdminIndexView.vue'
import RoleView from '../views/admin/RoleView.vue'
import UserView from '../views/admin/UserView.vue'
import AiSettingView from '../views/admin/AiSettingView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/index'
  },
  {
    path: '/index',
    name: 'Index',
    component: Index
  },
  {
    path: '/share',
    name: 'Share',
    component: ShareView
  },
  {
    path: '/explore',
    name: 'Explore',
    component: ExploreView
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path:'/logout',
    name:'Logout',
    component:Logout
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/home',
    name: 'Home',
    component: Home,
    children:[
      {path:'',redirect:'info'},
      {path:'info',component:UserInfo},
      {path:'fav_sys',component:FavSys},
      {path:'fav_chat',component:FavChat}
    ]
  },
  {
    path: '/about',
    name: 'about',
    component: () => import(/* webpackChunkName: "about" */ '../views/AboutView.vue')
  },
  {
    path:'/chat',
    name:'Chat',
    component: Chat
  },
  {
    path:'/admin',
    name:'Admin',
    component: AdminView,
    children:[
      {path:'',redirect:'index'},
      {path:'index',component:AdminIndexView},
      {path:'role',component:RoleView},
      {path:'user',component:UserView},
      {path:'ai_setting',component:AiSettingView}
    ]
  }
]

const router = new VueRouter({
  mode:"history",
  routes:routes,
})

// var base = ["Index","Login","Register"];

// router.beforeEach((to,from,next)=>{
//  const token = localStorage.getItem("Token")
//  base.forEach
//  if(base.indexOf(to.name) !== -1 && !token) next({name:'Login'})
//  else next()
// })
//路由守卫，判断是否登录
router.beforeEach((to, from, next) => {
  //主页路径，就直接next()
  if (to.path === '/index') {
    next()
  }
  //如果是登录页，并且没有登录，就直接next()
  else if (to.path === '/login' && !localStorage.getItem("Token")) {
    next()
  }
  //如果是登录页，并且登录了，就提示并跳转到主页
  else if (to.path === '/login' && localStorage.getItem("Token")) {
    alert("您已登录！")
    next({ path: '/index' })
  }
  //如果是注册页，并且没有登录，就直接next()
  else if (to.path === '/register' && !localStorage.getItem("Token")) {
    next()
  }
  //如果是注销页，并且登录了，就直接next()
  else if (to.path === '/logout' && localStorage.getItem("Token")) {
    next()
  }
  //如果是注销页，并且没有登录，就提示并跳转到登录页
  else if (to.path === '/logout' && !localStorage.getItem("Token")) {
    alert("请先登录！")
    next({ path: '/login' })
  }
  //如果是其他页面，就判断是否登录
  else {
    //如果登录了，就next()
    if (localStorage.getItem("Token")) {
      next()
    }
    //如果没有登录，先给出提示，然后跳转到登录页
    else {
      alert("请先登录！")
      next({ path: '/login' })
    }
  }
})

        

export default router
