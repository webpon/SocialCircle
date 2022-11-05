import React, { memo, useRef } from 'react';
import { Dialog, Button, Form, Row, Col, Input, Select, Avatar, MessagePlugin } from 'tdesign-react';
import { SubmitContext, FormInstanceFunctions } from 'tdesign-react/es/form/type';
import { useAppDispatch, useAppSelector } from 'store';
import { getAdminList, getUserList, selectUserList } from 'store/user';
import { addAdminUser } from 'apis/user';
import Style from './AddUser.module.less';
const { FormItem } = Form;
const { Option } = Select;
const { Group } = Avatar;
interface IProps {
  isAddUser?: boolean;
  setAddUser: Function;
}
const INITIAL_DATA = {
  email: '',
  password: '',
  petName: '',
  gender: 1,
  permission: 2,
  headIcon: '',
};
console.log(Style);

function AddUser(props: IProps) {
  const { isAddUser, setAddUser } = props;
  const dispatch = useAppDispatch();
  const { current } = useAppSelector(selectUserList);
  const formRef = useRef<FormInstanceFunctions>();
  const handleFooterClose = () => {
    formRef.current?.reset();
    setAddUser(false);
  };
  const onSubmit = async (e: SubmitContext) => {
    try {
      const { code, msg } = await addAdminUser(formRef.current?.getFieldsValue?.(true));
      if (code === 200) {
        MessagePlugin.info('提交成功');
        formRef.current?.reset();
        dispatch(getAdminList({ p: 1 }));
        setAddUser(false);
      } else {
        MessagePlugin.error(msg || '提交失败');
      }
    } catch (error: any) {
      MessagePlugin.error(error.toString() || '提交失败');
    }
  };
  return (
    <>
      <Dialog
        className={Style.fixStyle}
        header='新增管理'
        visible={isAddUser}
        width={850}
        footer={
          <>
            <Button theme='default' onClick={handleFooterClose}>
              返回
            </Button>
          </>
        }
        onClose={handleFooterClose}
      >
        <Form ref={formRef} onSubmit={onSubmit} labelWidth={100} labelAlign='top'>
          <Row gutter={[32, 24]}>
            <Col span={6}>
              <FormItem label='邮箱' name='email' initialData={INITIAL_DATA.email} rules={[{ required: true }]}>
                <Input type='text' placeholder='请输入邮箱' />
              </FormItem>
            </Col>

            <Col span={6}>
              <FormItem label='密码' name='password' initialData={INITIAL_DATA.password} rules={[{ required: true }]}>
                <Input type='text' placeholder='请输入密码' />
              </FormItem>
            </Col>

            <Col span={6} className={Style.dateCol}>
              <FormItem label='用户名' name='petName' initialData={INITIAL_DATA.petName} rules={[{ required: true }]}>
                <Input type='text' placeholder='请输入用户名' />
              </FormItem>
            </Col>

            <Col span={6} className={Style.dateCol} rules={[{ required: true }]}>
              <FormItem label='性别' name='gender' initialData={INITIAL_DATA.gender} rules={[{ required: true }]}>
                <Select placeholder='请选择性别'>
                  <Option key='1' label='男' value={1} />
                  <Option key='2' label='女' value={2} />
                </Select>
              </FormItem>
            </Col>

            <Col span={6} className={Style.dateCol}>
              <FormItem
                label='权限'
                name='permission'
                initialData={INITIAL_DATA.permission}
                rules={[{ required: true }]}
              >
                <Select placeholder='请选择权限'>
                  <Option key='1' label='普通管理员' value={2} />
                  <Option key='2' label='高级管理员' value={1} />
                </Select>
              </FormItem>
            </Col>
            {/* <Col span={6} className={Style.dateCol} rules={[{ required: true }]}>
                            <FormItem label='用户头像' name='headIcon' initialData={INITIAL_DATA.headIcon}>

                            </FormItem>
                        </Col> */}
          </Row>
          <FormItem className={Style.submitBar}>
            <Button type='submit' theme='primary'>
              提交
            </Button>
            <Button type='reset' style={{ marginLeft: 40 }}>
              重置
            </Button>
          </FormItem>
        </Form>
      </Dialog>
    </>
  );
}

export default memo(AddUser);
