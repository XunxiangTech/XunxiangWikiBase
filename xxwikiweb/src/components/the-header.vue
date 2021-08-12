<template>
  <a-layout-header class="header">
    <div class="logo"></div>
    <a-menu
        theme="dark"
        mode="horizontal"
        :style="{ lineHeight: '64px'}"
    >
      <a-menu-item key="/">
        <router-link to="/">首页</router-link>
      </a-menu-item>
      <a-menu-item key="/about">
        <router-link to="/about">关于我们</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/user" :style="user.id ? {} : {display:'none'}">
        <router-link to="/admin/user">用户管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/wiki" :style="user.id ? {} : {display:'none'}">
        <router-link to="/admin/wiki">百科书管理</router-link>
      </a-menu-item>
      <a-menu-item key="/admin/category" :style="user.id ? {} : {display:'none'}">
        <router-link to="/admin/category">分类管理</router-link>
      </a-menu-item>
      <a-menu-item class="login-menu" :style="user.id ? {} : {display:'none'}">
        <span>您好：{{user.name}}</span>
      </a-menu-item>
      <a class="login-menu" v-show="!user.id" @click="showLoginModal" :style="{right: '10px'}">
        <span>登录</span>
      </a>
      <a-popconfirm
          title="确认退出登录?"
          ok-text="是"
          cancel-text="否"
          @confirm="logout()"
      >
        <a class="login-menu" v-show="user.id" :style="{right: '10px'}">
          <span>退出登录</span>
        </a>
      </a-popconfirm>

      <a class="login-menu" v-show="!user.id" @click="showRegisterModal" :style="{right: '45px'}">
        <span>注册</span>
      </a>
    </a-menu>

    <a-modal
        title="登录"
        v-model:visible="loginModalVisible"
        :confirm-loading="loginModalLoading"
        @ok="login"
    >
      <a-form :model="loginUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="登录名">
          <a-input v-model:value="loginUser.username"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="loginUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>

    <a-modal
        title="注册"
        v-model:visible="registerModalVisible"
        :confirm-loading="registerModalLoading"
        @ok="register"
    >
      <a-form :model="registerUser" :label-col="{ span: 6 }" :wrapper-col="{ span: 18 }">
        <a-form-item label="用户名">
          <a-input v-model:value="registerUser.username"/>
        </a-form-item>
        <a-form-item label="昵称">
          <a-input v-model:value="registerUser.name"/>
        </a-form-item>
        <a-form-item label="邮箱">
          <a-input v-model:value="registerUser.email"/>
        </a-form-item>
        <a-form-item label="密码">
          <a-input v-model:value="registerUser.password" type="password"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </a-layout-header>
</template>

<script lang="ts">
import {defineComponent, ref, computed} from 'vue';
import axios from 'axios';
import {message} from 'ant-design-vue';
import store from "@/store";

declare let hexMd5: any;
declare let KEY: any;

export default defineComponent({
  name: 'the-header',
  setup() {
    // 登录后保存
    const user = computed(() => store.state.user);

    // 用来注册
    const registerUser = ref({
      username: "test888",
      name: "测试用户888",
      email: "test888@test.com",
      password: "test123"
    });
    const registerModalVisible = ref(false);
    const registerModalLoading = ref(false);
    const showRegisterModal = () => {
      registerModalVisible.value = true;
    };

    // 注册
    const register = () => {
      console.log("开始注册");
      registerModalLoading.value = true;
      registerUser.value.password = hexMd5(registerUser.value.password);
      axios.post('/user/register', registerUser.value).then((response) => {
        registerModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          registerModalVisible.value = false;
          message.success("注册成功！");

          //store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // 用来登录
    const loginUser = ref({
      username: "test",
      password: "test123"
    });
    const loginModalVisible = ref(false);
    const loginModalLoading = ref(false);
    const showLoginModal = () => {
      loginModalVisible.value = true;
    };

    // 登录
    const login = () => {
      console.log("开始登录");
      loginModalLoading.value = true;
      loginUser.value.password = hexMd5(loginUser.value.password);
      axios.post('/user/login', loginUser.value).then((response) => {
        loginModalLoading.value = false;
        const data = response.data;
        if (data.success) {
          loginModalVisible.value = false;
          message.success("登录成功！");

          store.commit("setUser", data.content);
        } else {
          message.error(data.message);
        }
      });
    };

    // 退出登录
    const logout = () => {
      console.log("退出登录开始");
      axios.get('user/logout').then((response) => {
        const data = response.data;
        if (data.success) {
          message.success("退出登录成功！");
          store.commit("setUser", {});
        } else {
          message.error(data.message);
        }
      });
    };

    return {
      registerModalVisible,
      registerModalLoading,
      showRegisterModal,
      registerUser,
      register,

      loginModalVisible,
      loginModalLoading,
      showLoginModal,
      loginUser,
      login,

      user,
      logout
    }
  }
});
</script>

<style>
.logo {
  width: 120px;
  height: 31px;
  /*background: rgba(255, 255, 255, 0.2);*/
  /*margin: 16px 28px 16px 0;*/
  float: left;
  color: white;
  font-size: 18px;
}

.login-menu {
  color: white;
  background-color: transparent !important;
  position: absolute;
}
</style>