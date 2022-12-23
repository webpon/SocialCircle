import {http} from "@/utils/http/axios";
import Topic from "@/type/Topic.type";

const baseUrl = "/topic"
export function getConcern(p:number) {
  return http.request<API.BasicResponseModel<Topic>>({
    url: `${baseUrl}/concern`,
    method:"get",
    params: {p}
  })
}
