import axios from 'axios'
import router from '@/router'

const state = {
  allFeedsList: null,
  followingPeopleFeedsList: null,
  scrapFeedsList: null,
  myTravelJournal: null,
  personalTravelJournal: null,
  presentTravel: null,
  presentTravelIdx: null,
  travelDetailList: null,
  nowTravel: null,
}
const getters = {
  // 뉴스피드 전체 여행일지
  getAllFeedsList: state => state.allFeedsList,
  // 팔로잉하는 사람들 전체 여행일지
  getFollowingPeopleFeedsList: state => state.followingPeopleFeedsList,
  // 스크랩 전체 여행일지
  getScrapFeedsList: state => state.scrapFeedsList,
  // 나의 전체 여행일지
  getMyTravelJournal: state => state.myTravelJournal,
  // 개인의 전체 여행일지
  getPersonalTravelJournal: state => state.personalTravelJournal,
  // 현재 보고있는 여행일지 총정보
  getTravel: state => state.presentTravel,
  // 현재 여행일지의 모든 위치일지
  getTravelDetail: state => state.travelDetailList,
  // 현재 보고 있는 여행일지 인덱스
  getPresentTravelIdx: state => state.presentTravelIdx,
  getNowTravel: state => state.nowTravel
}

const mutations = {
  SET_ALL_FEEDS_LIST(state, feedsList) {
    state.allFeedsList = feedsList
  },
  SET_FOLLOWING_PEOPLE_FEEDS_LIST(state, feedsList) {
    state.followingPeopleFeedsList = feedsList
  },
  SET_SCRAP_FEEDS_LIST(state, feedsList) {
    state.scrapFeedsList = feedsList
  },
  SET_MY_TRAVEL_JOURNAL(state, journals) {
    state.myTravelJournal = journals
  },
  SET_PERSONAL_TRAVEL_JOURNAL(state, journals) {
    state.personalTravelJournal = journals
  },
  SET_TRAVEL_DETAIL(state, travelDetail) {
    state.presentTravel = travelDetail[0]
    state.presentTravelIdx = travelDetail[1]
    state.travelDetailList = travelDetail[2]
  },
  SET_NOW_TRAVEL(state, nowTravel) {
    state.nowTravel = nowTravel
  }
}
const actions = {
  // 전체 여행일지 피드 가져오기
  // 유저아이디는 팔로우, 좋아요 컨트롤 위함.
  allFeedsList(context, userId){
    axios.get('feed/all', { params: { page: 0, size: 30, userId: userId} })
      .then(res => {
        context.commit('SET_ALL_FEEDS_LIST', res.data.data.success)
      })
  },

  guestAllFeedsList(context) {
    axios.get('feed/all', { params: { page: 0, size: 30} })
      .then(res=> {
        console.log(res)
        context.commit('SET_ALL_FEEDS_LIST', res.data.data.success)
      })
  },

  // 팔로우한 사람들 피드 리스트 가져오기
  followingPeopleFeedsList(context, id) {
    axios.get('feed/follow', { params: {page: 0, size: 30, userId: id }, headers: { 'X-AUTH-TOKEN' : context.rootGetters.getToken } })
    .then(res => {
      console.log('팔로잉한 사람들 피드 리스트 불러오기 성공', res.data)
      context.commit('SET_FOLLOWING_PEOPLE_FEEDS_LIST', res.data.data.success)
      })
  },
  // 스크랩 게시글 가져오기
  scrapFeedsList(context, id) {
    axios.get('scrap', { params : { page: 0, size: 30, id: id }, headers: { 'X-AUTH-TOKEN' : context.rootGetters.getToken }})
      .then(res => {
        context.commit('SET_SCRAP_FEEDS_LIST', res.data.data.success)
      })
  },
  // 내 여행일지
  travelJournalList(context, userId) {
    axios.get('travel/travelList', { params: {userId : userId}, headers: { 'X-AUTH-TOKEN' : context.rootState.accounts.authToken, 'Access-Control-Allow-Origin': '*' } })
      .then(res => {
        context.commit("SET_MY_TRAVEL_JOURNAL", res.data.data)
      })
  },

  // 개인 여행일지
  personalTravelJournalList(context, userId) {
    axios.get('travel/travelList', { params: {userId : userId}, headers: { 'X-AUTH-TOKEN' : context.rootState.accounts.authToken, 'Access-Control-Allow-Origin': '*' } })
      .then(res => {
        context.commit("SET_PERSONAL_TRAVEL_JOURNAL", res.data.data)
    })
  },
  
  travelDetail(context, travel) {
    axios.get('travel/travelDetail', { params: {travelIdx : travel.travelIdx}, headers: { 'X-AUTH-TOKEN' : context.rootState.accounts.authToken } })
      .then(res => {
        // 현재 여행일지 인덱스 기억하기.
        console.log(travel)
        context.commit('SET_TRAVEL_DETAIL', [travel, travel.travelIdx, res.data.data.placeList])
        router.push('/travel')
      })
  },
  
  deleteTravel(context, travelIdx) {
    axios.delete('travel/delete', { params: { travelIdx : travelIdx }, headers: { 'X-AUTH-TOKEN' : context.rootState.accounts.authToken } })
      .then(res => {
        console.log("삭제성공", res.data)
        if (context.state.myTravelJournal) {
          for (let idx in context.state.myTravelJournal) {
            if ( context.state.myTravelJournal[idx].travelIdx === travelIdx) {
              context.state.myTravelJournal.splice(idx, 1)
            }
          }
        }
        router.go();
      })
  },

  deleteTravelDetail(context, placeIdx) {
    axios.delete('place/delete', { params: { placeIdx : placeIdx }, headers: { 'X-AUTH-TOKEN' : context.rootState.accounts.authToken } })
      .then(res => {
        console.log("삭제오나료",res.data)
        if (context.state.travelDetailList) {
          for (let idx in context.state.travelDetailList) {
            if ( context.state.travelDetailList[idx].placeIdx === placeIdx ) {
              context.state.travelDetailList.splice(idx, 1)
              console.log("확인", context.state.travelDetailList)
              if (context.state.travelDetailList.length === 0) {
                // 상세 일지가 없으니 현재 일지도 지워버리기
                // context.dispatch('deleteTravel', context.state.presentTravelIdx)
                // 여행일지로 내보낼까?
                // router.push('/main');
              } 
            }
          }
        }
        // router.go();
      })
  },

  nowTravel(context, nowTravel) {
    context.commit("SET_NOW_TRAVEL", nowTravel)
  }
}


export default {
  state, getters, mutations, actions
}