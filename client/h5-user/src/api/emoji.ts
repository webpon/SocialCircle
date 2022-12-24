import {http} from "@/utils/http/axios";

export function getEmoji() {
  return http.request<any>({
    url:"https://social-circle-file.oss-cn-hangzhou.aliyuncs.com/emoji.json",
    method:"get"
  })
}

export function getGIF() {
  return http.request<any>({
    url:"https://social-circle-file.oss-cn-hangzhou.aliyuncs.com/gif.json",
    method:"get"
  })
}
