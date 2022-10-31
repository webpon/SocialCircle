import React, { useState, memo, useEffect } from 'react';
import { Table, Tag, Row, Col, Button, Input, Avatar, Dialog } from 'tdesign-react';
import { UserIcon } from 'tdesign-icons-react';
import classnames from 'classnames';
import { useAppDispatch, useAppSelector } from 'modules/store';
import {getAdminList, getUserList, selectUserList} from 'modules/user';
import { clearPageState } from 'modules/list/base';
import CommonStyle from 'styles/common.module.less';
import style from './List.module.less';
import {
  deleteAdminUser as deleteAdminUserApi
} from 'services/user'

import AddUser from './components/AddUser';

export const GenderMap: {
    [key: number]: React.ReactElement;
} = {
    1: (
        <Tag theme='warning' variant='light'>
            男
        </Tag>
    ),
    2: (
        <Tag theme='warning' variant='light'>
            女
        </Tag>
    ),
};

export const PermissionMap: {
    [key: number]: React.ReactElement;
} = {
    1: (
        <Tag theme='primary' variant='light'>
            高级管理员
        </Tag>
    ),
    2: (
        <Tag theme='warning' variant='light'>
            普通管理员
        </Tag>
    ),
};


export default memo(() => {
    const dispatch = useAppDispatch();
    const pageState = useAppSelector(selectUserList);
    const [isAddUser, setIsAddUser] = useState(false)
    const [selectedRowKeys, setSelectedRowKeys] = useState<(string | number)[]>([1, 2]);
    const [alertProps, setAlertProps] = useState({ visible: false, title: "" });
    const { loading, contractList, current, pageSize, total } = pageState;
    useEffect(() => {
        dispatch(
          getAdminList({ p: 1 }),
        );
        return () => {
            console.log('clear');
            dispatch(clearPageState());
        };
    }, []);

    function onSelectChange(value: (string | number)[]) {
        setSelectedRowKeys(value);
    }
    function setAddUser(isShow: Boolean) {
        // @ts-ignore
      setIsAddUser(isShow)
    }
    function deleteAdmin(id:number) {
      deleteAdminUserApi({id}).then(({code})=>{
        if (code === 200){
          console.log("删除成功")
        }
      });
    }
    const columns: any = [
        {
            align: 'left',
            width: 100,
            ellipsis: true,
            colKey: 'id',
            title: '用户ID',
        },
        {
            align: 'left',
            width: 250,
            ellipsis: true,
            colKey: 'email',
            title: 'Email',
        },
        {
            align: 'left',
            width: 200,
            ellipsis: true,
            colKey: 'accountId',
            title: '账号',
        },
        {
            align: 'left',
            width: 200,
            ellipsis: true,
            colKey: 'loginTime',
            title: '最近一次登录时间',
            cell({ row }) {
                return <div>{new Date(row.loginTime).toLocaleString()}</div>
            },
        },
        {
            align: 'left',
            width: 200,
            ellipsis: true,
            colKey: 'permission',
            title: '权限',
            cell({ row }) {
                return PermissionMap[row.permission]
            }
        },
        {
            align: 'left',
            width: 200,
            ellipsis: true,
            colKey: 'petName',
            title: '账户名称',
        },
        {
            align: 'left',
            width: 100,
            ellipsis: true,
            colKey: 'headIcon',
            title: '头像',
            cell({ row }) {
                return <Avatar image={row.headIcon} icon={<UserIcon />} style={{ marginRight: '40px' }} />
            }
        },
        {
            align: 'left',
            width: 100,
            ellipsis: true,
            colKey: 'gender',
            title: '性别',
            cell({ row }) {
                return GenderMap[row.gender];
            },
        },
        {
            align: 'left',
            fixed: 'right',
            width: 180,
            colKey: 'op',
            title: '操作',
          cell: function (row: any) {
            return (
              <>
                <Button onClick={() => {
                  deleteAdmin(row.id)
                }} theme='primary' variant='text'>
                  管理
                </Button>
                <Button theme='primary' variant='text'
                        onClick={() => {
                          setAlertProps({
                            visible: true,
                            title: `删除`,
                            content: `是否删除id为:${row.id}的管理`,
                            confirmBtn: '删除',
                          });
                        }}
                >
                  删除
                </Button>
              </>
            );
          },
        },
    ]
    
    return (
        <div className={classnames(CommonStyle.pageWithPadding, CommonStyle.pageWithColor)}>
            <Row justify='space-between' className={style.toolBar}>
                <Col>
                    <Button onClick={() => setAddUser(true)}>新增管理</Button>
                </Col>
            </Row>
            <Table
                columns={columns}
                loading={loading}
                data={contractList}
                rowKey='accountId'
                verticalAlign='top'
                hover
                onSelectChange={onSelectChange}
                maxHeight={'73vh'}
                pagination={{
                    pageSize,
                    total,
                    current,
                    showJumper: true,
                    onCurrentChange(current, pageInfo) {
                        dispatch(
                            getAdminList({
                                p: pageInfo.current,
                            }),
                        );
                    },
                }}
            />
            <AddUser setAddUser={setAddUser} isAddUser={isAddUser} />

          <Dialog
            {...alertProps}
            cancelBtn="取消"
            onClose={() => {
              setAlertProps({ visible: false });
            }}
            onConfirm={() => {
              setAlertProps({ visible: false });
            }}
          />
        </div>
    );
});
