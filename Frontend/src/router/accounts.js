import ChangePassword from '../components/accounts/ChangePassword.vue'
import DeleteAccount from '../components/accounts/DeleteAccount.vue'
import EmailAuthentication from '../views/accounts/EmailAuthentication.vue'
import FindAccount from '../components/accounts/FindAccount.vue'
import ProfileSettings from '../views/accounts/ProfileSettings.vue'
import ResetPassword from '../components/accounts/ResetPassword.vue'
import SignIn from '../views/accounts/SignIn.vue' 
import SignUp from '../views/accounts/SignUp.vue'

export default [
  {
    path: '/settings',
    name: 'Settings',
    component: ProfileSettings,
    children: [
      {
        // /user/:id/profile 과 일치 할 때
        // UserProfile은 User의 <router-view> 내에 렌더링 됩니다.
        path: 'changepassword',
        name: 'ChangePassword',
        component: ChangePassword,
      },
      {
        path: 'deleteaccount',
        name: 'DeleteAccount',
        component: DeleteAccount,
      }
    ]
  },
  {
    path: '/auth',
    component: EmailAuthentication,
  },
  {
    path: '/signin',
    name: 'SignIn',
    component: SignIn,
    children: [
      {
        path: 'identify',
        name: 'FindAccount',
        component: FindAccount,
      },
      {
        path: 'identify/password/reset',
        name: 'ResetPassword',
        component: ResetPassword,
      }
    ]
  },
  {
    path: '/signup',
    name: 'SignUp',
    component: SignUp,
  },
]