import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/HomeView.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import Index from '../views/IndexView.vue'
import Logout from '../views/Logout.vue'
import Chat from '../views/ChatView.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Index',
    component: Index
  },
  {
    path: '/index',
    name: 'Index',
    component: Index
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
    component: Home
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
  }
]

const router = new VueRouter({
  mode:"history",
  routes:routes,
})
//var base = ["Index","Login","Register"];

//router.beforeEach((to,from,next)=>{
//  const token = localStorage.getItem("Token")
//  base.forEach
//  if(base.indexOf(to.name) !== -1 && !token) next({name:'Login'})
//  else next()
//})

export default router
