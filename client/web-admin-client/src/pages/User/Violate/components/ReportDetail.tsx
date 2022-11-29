import React, { memo, useState } from 'react';
import { Dialog, Button, Form, Row, Col, Image, ImageViewer } from 'tdesign-react';
import { BrowseIcon } from 'tdesign-icons-react';
import Styles from './ReportDetail.module.less';
import {deleteReport} from "apis/user";
interface IProps {
  show: boolean;
  setShow: Function;
  setShowBan: Function;
  report: {
    content: string;
    id: number;
    images: any;
    reportUser: any;
    reportUserId: number;
    user: any;
    userId: number;
  };
}

function ReportMsg(props: IProps) {
  const { show, setShow, setShowBan, report } = props;
  const [showDel,setShowDel] = useState(false);

  const handleFooterClose = () => {
    setShow(false);
  };
  return (
    <>
      <Dialog
        className={Styles.fixStyle}
        header='举报详细信息'
        visible={show}
        width={500}
        footer={
          <>
            <Button
              size='large'
              theme='danger'
              shape='rectangle'
              onClick={() => {
                setShowBan(true);
              }}
            >
              封号
            </Button>
            <Button
              size='large'
              theme='danger'
              shape='rectangle'
              onClick={() => {
                setShowDel(true);
              }}
            >
              删除
            </Button>
          </>
        }
        onClose={handleFooterClose}
      >
        <Row gutter={[32, 24]}>
          <Col span={5}>被举报人ID：{report.reportUser.id}</Col>
          <Col span={5}>被举报人名字：{report.reportUser.petName}</Col>
        </Row>
        <Row gutter={[32, 24]}>
          <Col span={5}>举报人ID：{report.user.id}</Col>
          <Col span={5}>举报人名字：{report.user.petName}</Col>
        </Row>
        <Row gutter={[32, 24]}>
          <Col span={5}>举报原因：{report.content}</Col>
        </Row>
        <Row gutter={[32, 24]}>
          <Col span={5}>
            举报截图:
            <div className={Styles.imgList}>
              {(() => {
                return report.images.map((item: any, index: number) => {
                  const trigger = ({ open }) => {
                    const mask = (
                      <div
                        style={{
                          background: 'rgba(0,0,0,.6)',
                          color: '#fff',
                          height: '100%',
                          display: 'flex',
                          alignItems: 'center',
                          justifyContent: 'center',
                        }}
                        onClick={open}
                      >
                        <span>
                          <BrowseIcon size='16px' name={'browse'} /> 预览
                        </span>
                      </div>
                    );

                    return (
                      <Image
                        alt={'test'}
                        src={item.url}
                        overlayContent={mask}
                        overlayTrigger='hover'
                        fit='contain'
                        style={{
                          width: 100,
                          height: 100,
                          border: '4px solid var(--td-bg-color-secondarycontainer)',
                          borderRadius: 'var(--td-radius-medium)',
                          backgroundColor: '#fff',
                        }}
                      />
                    );
                  };

                  return (
                    <ImageViewer
                      key={index}
                      zIndex={3000}
                      trigger={trigger}
                      images={report.images.map((item: any) => item.url)}
                      defaultIndex={index}
                    />
                  );
                });
              })()}
            </div>
          </Col>
        </Row>
      </Dialog>
      <Dialog
        className={Styles.fixStyle}
        header='删除举报'
        visible={showDel}
        width={500}
        footer={
          <>
            <Button
              size='large'
              theme='danger'
              shape='rectangle'
              onClick={() => {
                deleteReport(report.id).then(({code}) => {
                  if (code === 200){
                    setShowDel(false)
                    setShow(false)
                  }
                })
              }}
            >
              删除
            </Button>
            <Button
              size='large'
              theme='danger'
              shape='rectangle'
              onClick={() => {
                setShowDel(false)
              }}
            >
              取消
            </Button>
          </>
        }
        // onClose={handleFooterClose}
      >

      </Dialog>
    </>
  );
}

export default memo(ReportMsg);
