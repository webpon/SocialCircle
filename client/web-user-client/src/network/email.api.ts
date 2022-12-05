import http from "@/network/index";
import DataType from "@/network/data.type";

interface emailCodeType {
    code: string;
    email: string;
    codeKey: string;
}

// 发送验证码给用户
export function sendLogInEmailCode(params:emailCodeType) {
    return http.get<DataType<void>>("/code/logIn",{params})
}