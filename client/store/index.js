import Vue from 'vue'
import { MenuItem } from '~/assets/js'

export const state = () => ({
  token: '',
  menu: {
    sidebar: {
      items: [
        new MenuItem('Новости', 'news.svg', '/news'),
        new MenuItem('Обращения', 'speaker.svg', '/appeals'),
        new MenuItem('Техподдержка', 'siren.svg', '/support'),
        new MenuItem('Вход', 'key.svg', '/login'),
      ],
    },
  },
})

export const getters = {
  isLogged(state) {
    return state.token !== ''
  },
  getToken(state) {
    return state.token
  },
  getSidebarMenuItems(state) {
    return state.menu.sidebar.items
  },
}

export const mutations = {
  setToken(state, token) {
    state.token = token
    state.menu.sidebar.items[3] = new MenuItem('Выход', 'key.svg', '/logout')
  },
  deleteToken(state) {
    state.token = ''
    state.menu.sidebar.items[3] = new MenuItem('Вход', 'key.svg', '/login')
  },
}

export const actions = {
  login(context, token) {
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
  },
}
