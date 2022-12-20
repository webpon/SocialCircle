import {http} from "@/utils/http/axios";

export function getEmoji() {
  return http.request<any>({
    url:"https://social-circle-file.oss-cn-hangzhou.aliyuncs.com/emoji.json",
    method:"get"
  })
}
