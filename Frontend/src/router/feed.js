import NewsFeed from '../views/feed/NewsFeed'
import FollowingPeopleFeed from '../views/feed/FollowingPeopleFeed'
import ScrapFeed from '../views/feed/ScrapFeed.vue'

export default [
  {
    path: '/feed',
    name: 'NewsFeed',
    component: NewsFeed,
  },
  {
    path: '/feed/follow',
    name: 'FollowingPeopleFeed',
    component: FollowingPeopleFeed
  },
  {
    path: 'feed/scrap',
    name: 'ScrapFeed',
    component: ScrapFeed,
  }

]