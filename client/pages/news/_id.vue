<template>
  <div class="news-article default-container">
    <div class="row">
      <div class="col-12 col-md-6 d-flex flex-column sidebar-fixed">
        <div style="margin: 0 auto 48px">
          <Logo color="black" />
        </div>
        <SidebarMenu :items="menuItems" />
      </div>
      <div class="col-12 col-md-6 sidebar"></div>
      <div class="col-12 col-md-6 news-content">
        <div class="heading">
          <h1>Lorem Ipsum</h1>
        </div>
        <div class="main-content">
          <p>
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec at
            ante in mi porttitor facilisis. Suspendisse rhoncus rutrum nisi,
            eget lacinia elit placerat eu. Ut tincidunt magna a metus commodo,
            ac dignissim lorem sagittis. Ut at dolor eu lorem porta volutpat
            quis ac sapien. Vestibulum ante ipsum primis in faucibus orci luctus
            et ultrices posuere cubilia curae; Praesent euismod ipsum eget enim
            egestas, et tristique magna aliquet. Nunc egestas et nunc non
            tristique. Pellentesque at sagittis risus. In facilisis molestie
            ligula sit amet laoreet. Nunc aliquam, purus a elementum volutpat,
            lorem felis accumsan dolor, ut imperdiet augue enim a magna.
          </p>
          <h2>Lorem Ipsum</h2>
          <p>
            Praesent nulla quam, ultricies vitae eros pellentesque, convallis
            commodo dolor. Phasellus auctor nulla vel dignissim aliquet. Nam
            viverra scelerisque enim, non congue felis egestas nec. Fusce
            sagittis malesuada rutrum. Integer maximus arcu neque, vitae
            vehicula sapien porta a. Suspendisse potenti. Cras vitae mi lectus.
            Nam vel tincidunt augue. Mauris pellentesque sapien non gravida
            commodo. In ligula eros, vehicula et nulla facilisis, volutpat
            venenatis urna. Integer rhoncus semper massa, sit amet eleifend urna
            ullamcorper volutpat.
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
  import Vue from 'vue'
  import { mapGetters } from 'vuex'
  import Logo from '~/components/logo/Logo.vue'
  import SidebarMenu from '~/components/menu/sidebar/SidebarMenu.vue'

  export default Vue.extend({
    components: {
      Logo,
      SidebarMenu,
    },
    data() {
      return {
        articleID: 0,
      }
    },
    computed: {
      ...mapGetters({
        menuItems: 'getSidebarMenuItems',
      }),
    },
    mounted() {
      const parsed = parseInt(this.$route.params.id)
      if (isNaN(parsed)) {
        this.$router.push('/news')
      } else {
        this.articleID = parsed
      }
    },
  })
</script>

<style lang="scss">
  .news-content {
    margin-left: 48px;
    .heading {
      display: flex;
      height: 204px;
      h1 {
        font-size: 64px;
        font-weight: 600;
        font-family: 'Jost';
        margin: auto 0 24px;
      }
    }
    .main-content {
      p {
        font-size: 32px;
        font-weight: 300;
        font-family: 'Jost';
      }
      h2 {
        font-size: 48px;
        font-weight: 500;
        font-family: 'Jost';
        margin: 36px 0;
      }
    }
  }
</style>
