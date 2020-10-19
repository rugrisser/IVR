import { MenuItem, IStoreState } from '~/assets/ts'

export const state = () => ({
  menu: {
    sidebar: {
      items: [
        new MenuItem('Новости', 'news.svg', '/news'),
        new MenuItem('Обращения', 'speaker.svg', '/appeals'),
        new MenuItem('Техподдержка', 'siren.svg', '/support'),
        new MenuItem('Вход', 'key.svg', '/login'),
      ]
    }
  }
} as IStoreState)

export const getters = {
  getSidebarMenuItems(state: IStoreState) {
    return state.menu.sidebar.items
  }
}