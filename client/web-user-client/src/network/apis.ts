import http from '@/network/index'
// 测试api
export function getTest() {
    return http.get('http://httpbin.org/get')
}
export function PostTest() {
    return http.get('http://httpbin.org/get')
}