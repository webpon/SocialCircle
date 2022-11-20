import React, {useState, useRef} from 'react';
import {useNavigate} from 'react-router-dom';
import {Form, MessagePlugin, Input, Checkbox, Button, FormInstanceFunctions, SubmitContext} from 'tdesign-react';
import {LockOnIcon, UserIcon, BrowseOffIcon, BrowseIcon, RefreshIcon} from 'tdesign-icons-react';
import classnames from 'classnames';
import {useAppDispatch} from 'store';
import {login, getUserInfo} from 'store/user';
import useCountdown from '../../hooks/useCountDown';

import Style from './index.module.less';
import {login as loginApi} from "apis/user";

const {FormItem} = Form;

export type ELoginType = 'password' | 'phone' | 'qrcode';

export default function Login() {
  const [loginType, changeLoginType] = useState<ELoginType>('password');
  const [showPsw, toggleShowPsw] = useState(false);
  const {countdown, setupCountdown} = useCountdown(60);
  const formRef = useRef<FormInstanceFunctions>();
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const onSubmit = async (e: SubmitContext) => {
    if (e.validateResult === true) {
      try {
        const formValue = formRef.current?.getFieldsValue?.(true) || {};
        loginApi(formValue).then(async ({code, data}) => {
          if (code === 200) {
            localStorage.setItem("token", data)
            await dispatch(getUserInfo());
            MessagePlugin.success('登录成功');
            navigate('/');
          } else {
            MessagePlugin.error('登录失败');
          }
        })
      } catch (e: any) {
        MessagePlugin.error(e.message);
      }
    }
  };

  const emailCheck = (val: string) => {
    const reg = /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
    if (reg.test(val)) return {result: true, type: 'success'};
    return {result: false, message: '请输入正确的邮箱', type: 'error'};
  };

  const switchType = (val: ELoginType) => {
    formRef.current?.reset?.();
    changeLoginType(val);
  };

  return (
    <div>
      <Form
        ref={formRef}
        className={classnames(Style.itemContainer, `login-${loginType}`)}
        labelWidth={0}
        onSubmit={onSubmit}
      >
        {loginType === 'password' && (
          <>
            <FormItem name='email' rules={[{required: true, message: '账号必填', type: 'error'}]}>
              <Input clearable size='large' placeholder='请输入邮箱：' prefixIcon={<UserIcon/>}/>
            </FormItem>
            <FormItem name='password' rules={[{required: true, message: '密码必填', type: 'error'}]}>
              <Input
                size='large'
                type={showPsw ? 'text' : 'password'}
                placeholder='请输入登录密码：'
                prefixIcon={<LockOnIcon/>}
                suffixIcon={
                  showPsw ? (
                    <BrowseIcon onClick={() => toggleShowPsw((current) => !current)}/>
                  ) : (
                    <BrowseOffIcon onClick={() => toggleShowPsw((current) => !current)}/>
                  )
                }
              />
            </FormItem>
            <div className={classnames(Style.checkContainer, Style.rememberPwd)}>
              <Checkbox>记住账号</Checkbox>
              <span className={Style.checkContainerTip}>忘记账号？</span>
            </div>
          </>
        )}

        {/* // 邮箱登陆 */}
        {loginType === 'phone' && (
          <>
            <FormItem
              name='email'
              rules={[{required: true, message: '邮箱必填', type: 'error'}, {validator: emailCheck}]}
            >
              <Input maxlength={20} size='large' placeholder='请输入您的邮箱' prefixIcon={<UserIcon/>}/>
            </FormItem>
            <FormItem name='verifyCode' rules={[{required: true, message: '验证码必填', type: 'error'}]}>
              <Input size='large' placeholder='请输入验证码'/>
              <Button
                variant='outline'
                className={Style.verificationBtn}
                disabled={countdown > 0}
                onClick={setupCountdown}
              >
                {countdown === 0 ? '发送验证码' : `${countdown}秒后可重发`}
              </Button>
            </FormItem>
          </>
        )}
        {loginType !== 'qrcode' && (
          <FormItem className={Style.btnContainer}>
            <Button block size='large' type='submit'>
              登录
            </Button>
          </FormItem>
        )}
        <div className={Style.switchContainer}>
          {loginType !== 'password' && (
            <span className='tip' onClick={() => switchType('password')}>
              使用账号密码登录
            </span>
          )}
          {loginType !== 'phone' && (
            <span className='tip' onClick={() => switchType('phone')}>
              使用邮箱验证码登录
            </span>
          )}
        </div>
      </Form>
    </div>
  );
}
