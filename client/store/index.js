import Vue from 'vue'

export const state = () => ({
  token: '',
})

export const getters = {
  isLogged(state) {
    return state.token !== ''
  },
  getToken(state) {
    return state.token
  },
}

export const mutations = {
  setToken(state, token) {
    state.token = token
  },
  deleteToken(state) {
    state.token = ''
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
