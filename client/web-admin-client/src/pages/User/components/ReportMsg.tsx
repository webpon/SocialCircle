import React, {memo, useRef, useState,} from 'react';
import {
  Dialog, Button, Form,
  Row,
  Col,
  Input
} from 'tdesign-react';
import {SubmitContext, FormInstanceFunctions} from 'tdesign-react/es/form/type';
import {useAppDispatch, useAppSelector} from 'modules/store';
import {selectUserList} from 'modules/user';
import {reportApi, updateAdminUser} from 'services/user'
import Style from './ReportMsg.module.less';
import PictureView from "../../../components/image";
import {Picker,Cell, PickerItem} from "tdesign-mobile-react";
interface IProps {
  show: boolean
  setShow: Function;
  report: {
    content: string,
    id: number,
    images: any,
    reportUser: object,
    reportUserId: number,
    user: object,
    userId: number
  }
}

function ReportMsg(props: IProps) {
  const {show, setShow, report} = props
  const [reportInfo, setReportInfo] = useState({ visible: false, id: 0 });
  const [reason,setReason] = useState("");

  const handleFooterClose = () => {
    setShow(false)
  };
  function handleClose() {
    setReportInfo({
      visible: false,
      id: 0,
    });
  }
  const [state, setState] = useState({
    date: { visible: false, values: [], labelText: '' },
  });

  const formatOptions = (labels: any[]) => labels.map((label, index) => ({ label, value: index }));

  const currentYear = new Date().getFullYear();  const yearOptions = formatOptions(new Array(10).fill(0).map((_, index) => `${currentYear - index}年`));
  const monthOptions = formatOptions(new Array(12).fill(0).map((_, index) => `${index + 1}月`));
  const dayOptions = formatOptions(new Array(31).fill(0).map((_, index) => `${index + 1}日`));
  const optionsListMap = {
    date: [yearOptions, monthOptions, dayOptions]
  };

  const togglePicker = (name:string, visible:boolean) => {
    setState({
      ...state,
      [name]: { ...state[name], visible },
    });
  };

  // 设置时间
  const getSelectedLabelText = (optionsList: any[], values: string[]) => {
    return optionsList
      .reduce((acc, options, index) => {
        const value = values[index];
        const option = options.find((item) => item.value === value);
        if (option) acc.push(option.label);
        return acc;
      }, [])
      .join('-');
  };

  const handleSelect = (name:string, values: string[]) => {
    // @ts-ignore
    const labelText = getSelectedLabelText(optionsListMap[name], values);
    setState({
      ...state,
      [name]: { ...state[name], values, labelText, visible: false },
    });
  };
  function reportBut() {
    var d = new Date();
    var hour= d.getHours();//得到小时数
    var minute= d.getMinutes();//得到分钟数
    var second= d.getSeconds();//得到秒数
    reportApi({
      userId: report.reportUserId,
      reason,
      endTime:state.date.labelText + ` ${hour}:${minute}:${second}`
    })
  }

  const NotePanel = (value, title) => <div className={`note-panel ${value ? '' : 'empty'}`}>{value || title}</div>;


  return (
    <>
      <Dialog
        className={Style.fixStyle}
        header="举报详细信息"
        visible={show}
        width={500}
        footer={
          <>
            <Button  size="large" theme="danger" shape="rectangle" block onClick={()=>{
              setReportInfo({
                visible: true,
                id: report.id,
              });
            }}>
              封号
            </Button>
          </>
        }
        onClose={handleFooterClose}
      >
          <Row gutter={[32, 24]}>
            <Col span={5}>
                被举报人ID：{report.reportUser.id}
            </Col>
            <Col span={5}>
                被举报人名字：{report.reportUser.petName}
            </Col>
          </Row>
          <Row gutter={[32, 24]}>
            <Col span={5}>
                举报人ID：{report.user.id}
            </Col>
            <Col span={5}>
              举报人名字：{report.user.petName}
            </Col>
          </Row>
          <Row gutter={[32, 24]}>
            <Col span={5}>
                举报原因：{report.content}
            </Col>
          </Row>
          <Row gutter={[32, 24]}>
            <Col span={5}>
              举报截图:
              <div className={Style.imgList}>
                {
                  (()=>{
                    return  report.images.map((item)=>{
                      return <PictureView imgUrl={item.url} style={{"margin": "10px","width":80, "height":80}} key={item.id} images={report.images}/>
                    })
                  })()
                }
              </div>
            </Col>
          </Row>
      </Dialog>
      <Dialog
        header='是否封禁当前用户？'
        visible={reportInfo.visible}
        onConfirm={reportBut}
        onClose={handleClose}
      >
        <Cell
          arrow
          title="日期"
          note={NotePanel(state.date.labelText, '选择日期')}
          onClick={() => togglePicker('date', true)}
        />
        <Input
          label={'封号原因'}
          placeholder="请输入文字"
          required={true}
          value={reason}
          onChange={(reason) => {
            // @ts-ignore
            setReason( reason);
          }}
        />
      <Picker
        visible={state.date.visible}
        defaultValue={state.date.values}
        onConfirm={(values) => handleSelect('date', values)}
        onCancel={() => togglePicker('date', false)}
      >
        <PickerItem options={yearOptions} />
        <PickerItem options={monthOptions} />
        <PickerItem options={dayOptions} />
      </Picker>
      </Dialog>

    </>
  );
}

export default memo(ReportMsg)
