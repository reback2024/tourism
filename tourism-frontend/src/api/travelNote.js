import request from './request'

export function getTravelNotes(params) { return request({ url: '/travel-notes', method: 'get', params }) }
export function getTravelNote(id) { return request({ url: `/travel-notes/${id}`, method: 'get' }) }
export function createTravelNote(data) { return request({ url: '/travel-notes', method: 'post', data }) }
export function updateTravelNote(id, data) { return request({ url: `/travel-notes/${id}`, method: 'put', data }) }
export function deleteTravelNote(id) { return request({ url: `/travel-notes/${id}`, method: 'delete' }) }
