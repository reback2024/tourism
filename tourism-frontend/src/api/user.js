import request from './request'

/** 获取当前用户信息 */
export function getUserInfo() {
  return request({ url: '/user/info', method: 'get' })
}

/** 更新用户信息 */
export function updateUser(data) {
  return request({ url: '/user/update', method: 'put', data })
}
