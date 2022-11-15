import axios from 'axios';
import proxy from '../configs/host';
const env = import.meta.env.MODE || 'development';
const API_HOST = proxy[env].API;

const SUCCESS_CODE = 200;
const TIMEOUT = 5000;

export const instance = axios.create({
  baseURL: API_HOST,
  timeout: TIMEOUT,
  withCredentials: true,
});

instance.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) {
    config.headers = {
      ...config.headers,
      authorization: token
    }
  }
  return config
}
)

instance.interceptors.response.use(
  // eslint-disable-next-line consistent-return
  (response) => {
    if (response.status === 200) {
      const { data } = response;
      if (data.code === SUCCESS_CODE) {
        return data;
      } else if (data.code === 401) {
        window.location.replace('/#/login')
        localStorage.removeItem('token')
      }
      return Promise.reject(data);
    }
    return Promise.reject(response?.data);
  },
  (e) => Promise.reject(e),
);

export default instance;
