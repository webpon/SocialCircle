import React, { memo, useEffect, useState } from 'react';
import { Button, Table } from 'tdesign-react';
import classnames from 'classnames';
import CommonStyle from 'styles/common.module.less';
import ReportDetail from './components/ReportDetail';
import { getReportList } from 'apis/user';
import Ban from './components/Ban';

const User: React.FC = () => {
  // 举报数据
  const [reportData, setReportData] = useState({ list: [], total: 0 });
  // 表格options
  const [loading, setLoading] = useState(false);
  const [current, setCurrent] = useState(1);
  //举报信息详情
  const [showDetail, setShowDetail] = useState(false);
  const [report, setReport] = useState({
    content: '',
    id: 0,
    images: [],
    reportUser: {},
    reportUserId: 0,
    user: {},
    userId: 0,
  });
  // 举报窗口
  const [showBan, setShowBan] = useState(false);
  // 生命周期
  useEffect(() => {
    getReportData(1);
  }, []);
  // 请求函数
  const getReportData = async (page: number) => {
    setLoading(true);
    const res = await getReportList(page);
    setLoading(false);
    setReportData({ list: res.data, total: res.total });
  };

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
      colKey: 'option',
      title: '操作',
      cell({ row }: any) {
        return (
          <>
            <Button
              theme='primary'
              variant='text'
              style={{ paddingLeft: 0 }}
              onClick={() => {
                setReport(row);
                setShowDetail(true);
              }}
            >
              详细信息
            </Button>
            <Button
              theme='warning'
              variant='text'
              onClick={() => {
                // setShowBan(true);
              }}
            >
              封号
            </Button>
          </>
        );
      },
    },
  ];
  return (
    <div className={classnames(CommonStyle.pageWithPadding, CommonStyle.pageWithColor)}>
      <Table
        hover
        rowKey='id'
        verticalAlign='top'
        maxHeight={'73vh'}
        columns={columns}
        loading={loading}
        data={reportData.list}
        pagination={{
          total: reportData.total,
          current: current,
          showPageSize: false,
          showJumper: true,
          onCurrentChange(current) {
            setCurrent(current);
            getReportData(current);
          },
        }}
      />
      <ReportDetail show={showDetail} setShowBan={setShowBan} setShow={setShowDetail} report={report} />
      <Ban visible={showBan} setShowBan={setShowBan} userId={report.id} />
    </div>
  );
};

export default memo(User);
