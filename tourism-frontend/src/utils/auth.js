const TOKEN_KEY = 'tourism_token'
const USER_KEY = 'tourism_user'

/** 获取Token */
export function getToken() {
  return localStorage.getItem(TOKEN_KEY)
}

/** 存储Token */
export function setToken(token) {
  localStorage.setItem(TOKEN_KEY, token)
}

/** 删除Token */
export function removeToken() {
  localStorage.removeItem(TOKEN_KEY)
  localStorage.removeItem(USER_KEY)
}

/** 获取存储的用户信息 */
export function getStoredUser() {
  const userStr = localStorage.getItem(USER_KEY)
  if (userStr) {
    try {
      return JSON.parse(userStr)
    } catch {
      return null
    }
  }
  return null
}

/** 存储用户信息 */
export function setStoredUser(user) {
  localStorage.setItem(USER_KEY, JSON.stringify(user))
}
