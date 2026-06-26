import request from './request'

export function getItineraries() { return request({ url: '/itineraries', method: 'get' }) }
export function getItinerary(id) { return request({ url: `/itineraries/${id}`, method: 'get' }) }
export function createItinerary(data) { return request({ url: '/itineraries', method: 'post', data }) }
export function deleteItinerary(id) { return request({ url: `/itineraries/${id}`, method: 'delete' }) }
export function generateItinerary(data) { return request({ url: '/ai/generate-itinerary', method: 'post', data }) }
