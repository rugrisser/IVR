import Vue from 'vue'
import Vue2Storage from 'vue2-storage'

Vue.use(Vue2Storage, {
  prefix: 'lycsovet_',
  driver: 'local',
  ttl: 86400000,
})
