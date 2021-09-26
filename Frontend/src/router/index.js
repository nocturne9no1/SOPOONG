import Vue from 'vue'
import VueRouter from 'vue-router'
import PageNotFound from '../views/PageNotFound.vue'
import CreateJourney from '../views/createJourney/CreateJourney.vue'
import Intro from '../views/Intro.vue'

// 없앨 예정
// import Pintest from '../views/Pintest.vue'
// import completeForm from '../views/completeForm.vue'
// import test from '../components/dummyFiles(DONOTDELETE)/test'

// modules
import accounts from './accounts.js'
import main from './main.js'
import feed from './feed.js'
import travel from './travel.js'
// import main from '../views/main/MainPage.vue'
import personal from './personal.js'

Vue.use(VueRouter)

const routes = [
  ...accounts,
  ...main,
  ...feed,
  ...travel,
  ...personal,

  {
    path: '/',
    redirect: '/intro',
    // component: test,
  },

  // 404
  {
    path: '*',
    redirect: '/404',
  },
  {
    path: '/404',
    name: 'PageNotFound',
    component: PageNotFound
  },
  {
    path: '/create',
    name: 'CreateJourney',
    component: CreateJourney
  },

  // 개발용 임시 router. 추후 삭제 예정
  // {
  //   path: '/main',
  //   name: 'main',
  //   component: main,
  // },
  {
    path: '/intro',
    name: 'intro',
    component: Intro,
  },
  // {
  //   path: '/pintest',
  //   name: 'pintest',
  //   component: Pintest,
  // },
  // {
  //   path: '/last',
  //   name: 'completeForm',
  //   component: completeForm,
  // }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// navation duplicate 에러 방지용
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
	return originalPush.call(this, location).catch(err => {
		if (err.name !== 'NavigationDuplicated') throw err;
	});
};

export default router
