import React, {memo, useEffect, useState} from 'react';
import {BrowserRouterProps} from 'react-router-dom';
import {useAppDispatch, useAppSelector} from "../../modules/store";
import {getReportList, getUserList, selectReportList} from "../../modules/user";
import {clearPageState} from "../../modules/list/base";
import {Avatar, Button, Col, Input, Row, Table} from "tdesign-react";
import classnames from "classnames";
import CommonStyle from "../../styles/common.module.less";
import style from "./List/List.module.less";
import {SearchIcon} from "tdesign-icons-react/lib";
import {GenderMap} from "./List/List";
import ReportMsg from "./components/ReportMsg";


const User: React.FC<BrowserRouterProps> = () => {
  const dispatch = useAppDispatch();
  const pageState = useAppSelector(selectReportList);
  const {loading, list, current, pageSize, total} = pageState;
  const [selectedRowKeys, setSelectedRowKeys] = useState<(string | number)[]>([1, 2]);
  const [showMsg, setShowMsg] = useState(false);
  const [report, setReport] = useState({
    content: "",
    id: 0,
    images: [],
    reportUser: {},
    reportUserId: 0,
    user: {},
    userId: 0
  });

  useEffect(() => {
    dispatch(
      getReportList(1)
    );
    return () => {
      dispatch(clearPageState());
    };
  }, []);

  const columns: any = [
    {
      align: 'left',
      width: 100,
      ellipsis: true,
      colKey: 'id',
      title: 'ID',
    },
    {
      align: 'left',
      width: 100,
      ellipsis: true,
      colKey: 'reportUserId',
      title: '被举报id',
    },
    {
      align: 'left',
      width: 200,
      ellipsis: true,
      colKey: 'content',
      title: '举报原因',
    },
    {
      align: 'left',
      fixed: 'right',
      width: 180,
      colKey: 'op',
      title: '操作',
      cell({row}) {
        return (
          <>
            <Button theme='primary' variant='text' onClick={() => {
              setReport(row)
              setShowMsg(true)
            }}>
              详细信息
            </Button>
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
      </Row>
      <Table
        columns={columns}
        loading={false}
        data={list}
        rowKey='id'
        selectedRowKeys={selectedRowKeys}
        verticalAlign='top'
        hover
        maxHeight={'73vh'}
        pagination={{
          pageSize,
          total,
          current,
          showJumper: true,
          showPageSize: false,
          onCurrentChange(current, pageInfo) {
            dispatch(
              getUserList({
                p: pageInfo.current
              }),
            );
          },
        }}
      />
      <ReportMsg show={showMsg} setShow={setShowMsg} report={report}/>
    </div>
  );
};

export default memo(User);
