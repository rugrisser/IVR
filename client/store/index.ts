import Vue from 'vue'
import { MenuItem, IStoreState } from '~/assets/ts'

export const state = () => ({
  token: '',
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
  isLogged(state: IStoreState) {
    return state.token != ""
  },
  getToken(state: IStoreState) {
    return state.token
  },
  getSidebarMenuItems(state: IStoreState) {
    return state.menu.sidebar.items
  }
}

export const mutations = {
  setToken(state: IStoreState, token: String) {
    state.token = token
    state.menu.sidebar.items[3] = new MenuItem('Выход', 'key.svg', '/logout')
  },
  deleteToken(state: IStoreState) {
    state.token = ""
    state.menu.sidebar.items[3] = new MenuItem('Вход', 'key.svg', '/login')
  }
}

export const actions = {
  login(context, token: String) {
    Vue.$storage.set('token', token, { ttl: 86400000 })
    context.commit('setToken', token)
  },
  logout(context) {
    Vue.$storage.remove('token')
    context.commit('deleteToken')
  },
  updateToken(context) {
    const token = Vue.$storage.get('token', null)
    if (token == null) {
      context.commit('deleteToken')
    } else {
      context.commit('setToken', token)
    }
  }
}