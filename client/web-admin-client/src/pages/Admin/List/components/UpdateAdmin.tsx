import React, { memo, useRef, } from 'react';
import {
    Dialog, Button, Form,
    Row,
    Col,
    Input,
    Select,
    Avatar,
    MessagePlugin,
} from 'tdesign-react';
import { SubmitContext, FormInstanceFunctions } from 'tdesign-react/es/form/type';
import { useAppDispatch, useAppSelector } from 'modules/store';
import {getAdminList,  selectUserList} from 'modules/user';
import {updateAdminUser} from 'services/user'
import Style from './AddUser.module.less';
const { FormItem } = Form;
const { Option } = Select;
interface IProps {
    isUpdateAdmin?: boolean;
    setUpdateAdmin: Function;
    INITIAL_DATA: {
      id: number,
      permission: number,
      email: string,
      password: string,
      petName: string,
      gender: number,
      headIcon: string

    };
}
function UpdateAdmin(props: IProps) {
    const { isUpdateAdmin, setUpdateAdmin, INITIAL_DATA } = props
    const dispatch = useAppDispatch();
    const { current } = useAppSelector(selectUserList)
    const formRef = useRef<FormInstanceFunctions>();
    const handleFooterClose = () => {
        formRef.current?.reset()
        setUpdateAdmin(false);
    };
    const onSubmit = async (e: SubmitContext) => {
        try {
            const { code, msg } = await updateAdminUser({
              id: INITIAL_DATA.id,
              permission: INITIAL_DATA.permission
            })
            if (code === 200) {
                MessagePlugin.info('修改成功');
                formRef.current?.reset()
                dispatch(
                  getAdminList({ p: 1 }),
                );
                setUpdateAdmin(false)
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
                header="修改权限"
                visible={isUpdateAdmin}
                width={850}
                footer={
                    <>
                        <Button theme="default" onClick={handleFooterClose}>
                            返回
                        </Button>
                    </>
                }
                onClose={handleFooterClose}
            >
                <Form ref={formRef} onSubmit={onSubmit} labelWidth={100} labelAlign='top'>
                    <Row gutter={[32, 24]}>
                        <Col span={6}>
                            <FormItem label='邮箱' name='email' initialData={INITIAL_DATA.email} >
                                <Input type="text" placeholder='请输入邮箱' disabled={true}/>
                            </FormItem>
                        </Col>

                        <Col span={6}>
                          <FormItem label='密码' name='password' initialData="*****" >
                            <Input type="text" placeholder='请输入密码'  disabled={true} />
                          </FormItem>
                        </Col>

                        <Col span={6} className={Style.dateCol} >
                            <FormItem label='用户名' name='petName' initialData={INITIAL_DATA.petName} >
                                <Input type="text" placeholder='请输入用户名'  disabled={true}/>
                            </FormItem>
                        </Col>

                        <Col span={6} className={Style.dateCol} >
                            <FormItem label='性别' name='gender' initialData={INITIAL_DATA.gender} >
                                <Select placeholder='请选择性别' disabled={true}>
                                    <Option key='1' label='男' value={1} />
                                    <Option key='2' label='女' value={2} />
                                </Select>
                            </FormItem>
                        </Col>

                        <Col span={6} className={Style.dateCol}>
                            <FormItem label='权限' name='permission' initialData={INITIAL_DATA.permission} >
                                <Select placeholder='请选择权限'>
                                    <Option key='1' label='普通管理员' value={2} />
                                    <Option key='2' label='高级管理员' value={1} />
                                </Select>
                            </FormItem>
                        </Col>
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

export default memo(UpdateAdmin)
