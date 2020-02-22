import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import StuQuery from '../components/Student'
import CourseQuery from '../components/Course'
import TeacherQuery from '../components/Teacher'
import ElectSys from '../components/ElectSys'
import AllCourse from '../components/AllCourse'
import MyCourse from '../components/MyCourse'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'home',
    component: Home
  },
  {
    path: '/student', 
    name: 'student', 
    component: StuQuery
  }, 
  {
    path: '/course', 
    name: 'course', 
    component: CourseQuery
  }, 
  {
    path: '/teacher', 
    name: 'teacher', 
    component: TeacherQuery
  }, 
  {
    path: '/elect', 
    name: 'elect', 
    component: ElectSys,
    children:[
      {
        path: 'all',
        name: 'allcourses',
        component: AllCourse
      }, 
      {
        path: 'mine',
        name: 'mine',
        component: MyCourse        
      }]
  }, 
  {
    path: '/analysis', 
    name: 'analysis', 
    component: ()=> import('@/components/Analysis.vue')
  }, 
]

const router = new VueRouter({
  mode: 'history',
  routes
})

export default router
