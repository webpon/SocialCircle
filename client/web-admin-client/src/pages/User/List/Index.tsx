import React, { useState, memo, useEffect } from 'react';
import { Table, Tag, Row, Col, Button, Input, Avatar } from 'tdesign-react';
import { UserIcon } from 'tdesign-icons-react';
import classnames from 'classnames';
import { useAppDispatch, useAppSelector } from 'store';
import { getUserList, selectUserList } from 'store/user';
import { selectListBase, getList, clearPageState } from 'store/list/base';
import CommonStyle from 'styles/common.module.less';
import style from './Index.module.less';
import {SearchIcon} from "tdesign-icons-react/lib";

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


export default memo(() => {
    const dispatch = useAppDispatch();
    const pageState = useAppSelector(selectUserList);
    const [selectedRowKeys, setSelectedRowKeys] = useState<(string | number)[]>([1, 2]);
    const [q,setQ] = useState(null);
    const { loading, contractList, current, pageSize, total } = pageState;
    // const { loading, adminList, current, pageSize, total } = pageState;
    useEffect(() => {
        dispatch(
            getUserList({ p: 1, q: q}),
        );
        return () => {
            console.log('clear');
            dispatch(clearPageState());
        };
    }, [q]);

    function onSelectChange(value: (string | number)[]) {
        setSelectedRowKeys(value);
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
            cell() {
                return (
                    <>
                        {/*<Button theme='primary' variant='text'>*/}
                        {/*    管理*/}
                        {/*</Button>*/}
                        <Button theme='primary' variant='text'>
                            封号
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
                    <Input suffixIcon={<SearchIcon />} placeholder='请输入你需要搜索的型号' onEnter={(v,e)=>{
                      if (v === ""){
                        setQ(null)
                        return
                      }
                      setQ(v)
                    }}/>
                </Col>
            </Row>
            <Table
                columns={columns}
                loading={loading}
                data={contractList}
                rowKey='id'
                selectedRowKeys={selectedRowKeys}
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
                            getUserList({
                                p: pageInfo.current,
                              q
                            }),
                        );
                    },
                }}
            />
        </div>
    );
});
