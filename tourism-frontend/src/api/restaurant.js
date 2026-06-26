import request from './request'

export function getRestaurants(params) {
  return request({ url: '/restaurants', method: 'get', params })
}

export function getRestaurantDetail(id) {
  return request({ url: `/restaurants/${id}`, method: 'get' })
}
