// import axios from 'axios'
// import router from '@/router'

const state = {
  alarmList: [],
}

const getters = {
  isAlarm(state) {
    var a = true
    for (let each of state.alarmList) {
      if ( each.alarmCheck === 1) {
        a = false
      }
    }
    return a
  }
}
const mutations = {
  SET_ALARMLIST(state, alarmData) {
    state.alarmList = alarmData
  },

  DEL_ALARM(state, idx) {
    state.alarmList.splice(idx, 1)
  }
}

const actions = {
  // 알람 데이터 받아옴
  getAlarm(context, alarmData) {
    console.log(alarmData)
    context.commit('SET_ALARMLIST', alarmData)
  },
  delAlarm(context, idx) {
    context.commit('DEL_ALARM', idx)
  }
}

export default {
  state, getters, mutations, actions
}