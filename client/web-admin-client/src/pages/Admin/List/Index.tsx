import React, { useState, memo, useEffect } from 'react';
import { Table, Tag, Row, Col, Button, MessagePlugin, Avatar, Dialog } from 'tdesign-react';
import { UserIcon } from 'tdesign-icons-react';
import classnames from 'classnames';
import CommonStyle from 'styles/common.module.less';
import style from './Index.module.less';
import AddUser from './components/AddUser';
import { getAdminList, deleteAdminUser } from 'apis/admin';

const GenderMap: {
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

const PermissionMap: {
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
  /*-------------- 数据初始化 --------------*/
  // 管理员数据
  const [adminData, setAdminData] = useState({ list: [], total: 0 });
  // 表格options
  const [loading, setLoading] = useState(false);
  const [current, setCurrent] = useState(1);
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
      cell({ row }: any) {
        return <div>{new Date(row.loginTime).toLocaleString()}</div>;
      },
    },
    {
      align: 'left',
      width: 200,
      ellipsis: true,
      colKey: 'permission',
      title: '权限',
      cell({ row }: any) {
        return PermissionMap[row.permission];
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
      cell({ row }: any) {
        return <Avatar image={row.headIcon} icon={<UserIcon />} style={{ marginRight: '40px' }} />;
      },
    },
    {
      align: 'left',
      width: 100,
      ellipsis: true,
      colKey: 'gender',
      title: '性别',
      cell({ row }: any) {
        return GenderMap[row.gender];
      },
    },
    {
      align: 'left',
      fixed: 'right',
      width: 180,
      colKey: 'op',
      title: '操作',
      cell: function ({ row }: any) {
        return (
          <>
            <Button theme='primary' variant='text'>
              管理
            </Button>
            <Button
              theme='primary'
              variant='text'
              onClick={() => {
                setDeleteInfo({
                  visible: true,
                  id: row.id,
                });
              }}
            >
              删除
            </Button>
          </>
        );
      },
    },
  ];

  // 生命周期
  useEffect(() => {
    getAdminData(1);
  }, [current]);
  // 请求函数
  const getAdminData = async (page: number) => {
    setLoading(true);
    const res = await getAdminList({
      p: page,
    });
    setLoading(false);
    setAdminData({ list: res.data, total: res.total });
  };

  /*-------------- 新增管理员 --------------*/
  const [isAddUser, setIsAddUser] = useState(false);

  /*-------------- 删除管理员 --------------*/
  const [deleteInfo, setDeleteInfo] = useState({ visible: false, id: 0 });
  const [delLoading, setDelLoading] = useState(false);
  async function deleteAdmin(id: number) {
    setDelLoading(true);
    try {
      const { code } = await deleteAdminUser({ id });
      if (code === 200) {
        setDeleteInfo({
          visible: false,
          id: 0,
        });
        getAdminData(1);
      }
    } catch (error: any) {
      MessagePlugin.error(error.msg);
    } finally {
      setDelLoading(false);
    }
  }

  return (
    <div className={classnames(CommonStyle.pageWithPadding, CommonStyle.pageWithColor)}>
      <Row justify='space-between' className={style.toolBar}>
        <Col>
          <Button onClick={() => setIsAddUser(true)}>新增管理</Button>
        </Col>
      </Row>
      <Table
        hover
        columns={columns}
        loading={loading}
        data={adminData.list}
        rowKey='accountId'
        verticalAlign='top'
        maxHeight={'73vh'}
        pagination={{
          pageSize: 10,
          current,
          total: adminData.total,
          showPageSize: false,
          showJumper: true,
          onCurrentChange(current) {
            setCurrent(current);
            getAdminData(current);
          },
        }}
      />
      <AddUser setAddUser={setIsAddUser} isAddUser={isAddUser} />

      <Dialog
        header='确认删除？'
        visible={deleteInfo.visible}
        confirmBtn={
          <Button theme='danger' loading={delLoading}>
            删除
          </Button>
        }
        onClose={() => {
          setDeleteInfo({
            visible: false,
            id: 0,
          });
        }}
        onConfirm={() => deleteAdmin(deleteInfo.id)}
      >
        <p>删除后将无法恢复！</p>
      </Dialog>
    </div>
  );
});
