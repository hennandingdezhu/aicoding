import axios from 'axios'
const api = axios.create({ baseURL: '/api/tokenscope' })
export const getRecords = (page=1,size=20) => api.get(`/records?page=${page-1}&size=${size}`)
export const createRecord = (data) => api.post('/records', data)
export const deleteRecord = (id) => api.delete(`/records/${id}`)
export const getBudget = () => api.get('/budget')
export const updateBudget = (data) => api.put('/budget', data)
export const getStats = () => api.get('/stats')
export const getDailyTrend = () => api.get('/stats/daily-trend')