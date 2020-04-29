<template>
  <div class="login">
    <div class="login-form">
      <div class="login-header">
        <img src="../../assets/images/logo.png" width="150" height="120" alt />
        <!-- <p>{{ $Config.name.siteName }}</p> -->
      </div>
      <el-input
        placeholder="请输入用户名"
        suffix-icon="fa fa-user"
        v-model="loginForm.username"
        style="margin-bottom: 18px"
      ></el-input>

      <el-input
        placeholder="请输入密码"
        suffix-icon="fa fa-keyboard-o"
        v-model="loginForm.password"
        type="password"
        style="margin-bottom: 18px"
        autocomplete="new-password"
        @keyup.native.enter="login"
      ></el-input>

      <el-row>
        <el-col :span="12">
          <img @click="getCaptcha" width="130px" height="40px" :src="captchaImage" style="cursor: pointer"/>
        </el-col>
        <el-col :span="12">
          <el-input
            placeholder="请输入验证码"
            suffix-icon="fa fa-user"
            v-model="loginForm.captcha"
            style="margin-bottom: 18px"
          ></el-input>
        </el-col>
      </el-row>

      <el-button
        type="primary"
        :loading="loginLoading"
        style="width: 100%;margin-bottom: 18px"
        @click.native="login"
      >登录</el-button>
      <div>
        <el-checkbox v-model="remember">记住密码</el-checkbox>
        <!-- <a href="javascript:;" style="float: right;color: #3C8DBC;font-size: 14px">Register</a> -->
      </div>
    </div>
  </div>
</template>

<script>
import openApi from '../../common/openApi.js'
import sockets from '~/common/sockets'
// import Vue from 'vue'
export default {
  data() {
    return {
      captchaImage: '',
      loginForm: {
        username: '',
        password: '',
        captcha: '',
        uuid: ''
      },
      remember: false,
      loginLoading: false
    }
  },
  methods: {
    async getCaptcha() {
      let res = await openApi.captcha()
      this.captchaImage = res.base64
      this.loginForm.uuid = res.uuid
    },
    async login() {
      this.loginLoading = true
      try {
        let res = await openApi.login(this.loginForm)
        if (this.remember) {
          localStorage.setItem('remember', JSON.stringify(this.loginForm))
        } else {
          localStorage.removeItem('remember')
        }
        setTimeout(() => {
          //保存用户token以及菜单按钮权限
          this.$Permission.savePermission(res)
          this.$notify({
            title: '登录成功',
            message: '很高兴你使用Mayfly Admin！别忘了给个Star哦。',
            type: 'success'
          })
          this.loginLoading = false
          // 有重定向则重定向，否则到首页
          let redirect = this.$route.query.redirect
          if (redirect) {
            this.$router.push(redirect)
          } else {
            this.$router.push({
              path: '/'
            })
          }
        }, 500)
      } catch (err) {
        this.loginLoading = false
      }
    },
    getRemember() {
      return localStorage.getItem('remember')
    }
  },
  mounted() {
    this.getCaptcha()
    let remember = JSON.parse(this.getRemember())
    if (remember) {
      this.remember = true
      this.loginForm.username = remember.username
      this.loginForm.password = remember.password
    } else {
      this.remember = false
    }
  }
}
</script>

<style lang="less">
@import 'Login.less';
</style>
