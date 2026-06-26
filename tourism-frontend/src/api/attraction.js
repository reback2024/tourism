import request from './request'

/** 获取景点列表 */
export function getAttractions(params) {
  return request({ url: '/attractions', method: 'get', params })
}

/** 获取景点详情 */
export function getAttractionDetail(id) {
  return request({ url: `/attractions/${id}`, method: 'get' })
}

/** 获取热门景点 */
export function getHotAttractions() {
  return request({ url: '/attractions/hot', method: 'get' })
}

/** 获取城市列表 */
export function getCities() {
  return request({ url: '/attractions/cities', method: 'get' })
}
