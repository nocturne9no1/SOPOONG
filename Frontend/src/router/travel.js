import TravelJournalPage from '@/views/travel/TravelJournalPage'
import TravelEdit from '@/views/travel/TravelEdit'


export default [
  {
    // path: `travel/${journalid}`
    path: '/travel',
    name: 'TravelJournalPage',
    component: TravelJournalPage,
  },
  {
    path: '/travel/edit',
    name: 'TravelEdit',
    component: TravelEdit,
  }
]