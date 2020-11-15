<template>
  <div class="sidebar-menu">
    <NuxtLink
      v-for="(item, index) in items"
      :key="index"
      class="menu-item d-flex"
      :to="item.link"
      tag="div"
    >
      <img class="default-icon" :src="defaultIconLink(item.icon)" />
      <img class="hovered-icon" :src="hoveredIconLink(item.icon)" />
      <span>{{ item.text }}</span>
    </NuxtLink>
  </div>
</template>

<script>
  import { mapGetters } from 'vuex'
  export default {
    name: 'SidebarMenu',
    props: {
      items: {
        type: Array,
        required: true,
      },
    },
    data() {
      return {
        deafultIconLinkAlias: '/img/ui/menu/icon/black/',
        hoveredIconLinkAlias: '/img/ui/menu/icon/primary_dark/',
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
        logged: 'isLogged',
        token: 'getToken',
      }),
    },
    mounted() {
      if (this.logged) {
        for (const index in this.items) {
          if (this.items[index].link === '/login') {
            console.log('YAHOOO')
          }
        }
      }
    },
    methods: {
      defaultIconLink(icon) {
        return this.deafultIconLinkAlias + icon
      },
      hoveredIconLink(icon) {
        return this.hoveredIconLinkAlias + icon
      },
    },
  }
</script>
<style lang="scss">
  .sidebar-menu {
    display: flex;
    flex-direction: column;
    .menu-item {
      cursor: pointer;
      padding: 18px 36px;
      margin: 6px 0;
      border-radius: 100px;
      transition: 500ms ease-out;
      &:hover {
        background-color: primary-color-opacity(10);
        span {
          color: $primary-color-dark;
        }
        .default-icon {
          display: none;
        }
        .hovered-icon {
          display: block;
        }
      }
      img {
        height: 32px;
        margin: auto 0;
      }
      span {
        margin: auto auto auto 32px;
        font-size: 32px;
        font-family: 'Jost';
        letter-spacing: 2px;
        transition: 200ms ease-out;
      }
      .default-icon {
        display: block;
      }
      .hovered-icon {
        display: none;
      }
    }
    .router-active,
    .router-exact-active {
      background-color: primary-color-opacity(15);
      border-radius: 100vh;
      span {
        color: $primary-color-dark;
      }
      .default-icon {
        display: none;
      }
      .hovered-icon {
        display: block;
      }
      &:hover {
        background-color: primary-color-opacity(15);
      }
    }
  }
</style>
