import {http} from "@/utils/http/axios";

export function concern(concern: number) {
  return http.request({
    url:"/concern",
    method: "post",
    data:{concern}
  },
    {
      isTransformResponse: false,
    }
  );
}
