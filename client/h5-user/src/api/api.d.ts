declare namespace API {
    interface BasicResponseModel<T = any> {
        code: number;
        msg: string | null;
        data: T | null;
    }
}
