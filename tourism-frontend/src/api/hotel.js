import request from './request'

export function getHotels(params) {
  return request({ url: '/hotels', method: 'get', params })
}

export function getHotelDetail(id) {
  return request({ url: `/hotels/${id}`, method: 'get' })
}
