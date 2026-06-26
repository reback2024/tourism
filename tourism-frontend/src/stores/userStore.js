import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { getToken, setToken, removeToken, getStoredUser, setStoredUser } from '@/utils/auth'
import { login as loginApi, register as registerApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(getToken())
  const user = ref(getStoredUser())

  // 是否已登录
  const isLoggedIn = computed(() => !!token.value)
  // 用户角色
  const role = computed(() => user.value?.role || '')
  // 是否为管理员
  const isAdmin = computed(() => role.value === 'ADMIN')
  // 用户昵称
  const nickname = computed(() => user.value?.nickname || user.value?.username || '')

  /** 登录 */
  async function login(username, password) {
    const res = await loginApi({ username, password })
    token.value = res.data.token
    user.value = res.data.user
    setToken(res.data.token)
    setStoredUser(res.data.user)
    return res
  }

  /** 注册 */
  async function register(data) {
    return await registerApi(data)
  }

  /** 登出 */
  function logout() {
    token.value = null
    user.value = null
    removeToken()
  }

  /** 设置用户信息 */
  function setUser(newUser) {
    user.value = newUser
    setStoredUser(newUser)
  }

  return {
    token, user, isLoggedIn, role, isAdmin, nickname,
    login, register, logout, setUser
  }
})
