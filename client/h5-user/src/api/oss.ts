import {http} from "@/utils/http/axios";
import OSSType from "@/type/OSS.type";

/**
 * @description: 签名
 */
export function signature() {
  return http.request<API.BasicResponseModel<OSSType>>(
    {
      url: '/oss',
      method: 'GET'
    },
    {
      isTransformResponse: true
    }
  );
}

export function uploader(host: string, form: FormData) {
  return http.request<any>(
    {
      url: host,
      method: 'POST',
      data: form
    }
  );
}

export function uploaderFile(file: any) {
  return new Promise(async (res, rej) => {
    const data = await signature();
    const key = data.dir + ('xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'
      .replace(/[xy]/g, c => {
        return (c === 'x' ? (Math.random() * 16 | 0) : ('r&0x3' | '0x8')).toString(16)
      })) + `_${file.name}`
    let form = new FormData(); // FormData 对象
    form.append("policy", data.policy)
    form.append("signature", data.signature)
    form.append("OSSAccessKeyId", data.accessId)
    form.append("key", key);
    form.append("dir", data.dir)
    form.append("host", data.host)
    form.append("file", file); // 文件对象
    uploader(data?.host, form).catch(()=>{

    })
    res(data.host + "/" + key);
  })
}
