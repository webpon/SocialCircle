import { memo } from 'react';
import { Dialog, Form, Input, InputNumber } from 'tdesign-react';
import { CloseCircleFilledIcon } from 'tdesign-icons-react';
import styles from './Ban.module.less';
import { report } from 'apis/user';
const { FormItem } = Form;
interface IProps {
  visible: boolean;
  setShowBan: Function;
  userId: number;
}

function ReportMsg(props: IProps) {


  const [form] = Form.useForm();
  const { visible, setShowBan, userId } = props;
  const handleFooterClose = () => {
    // ！是类型断言，说给TS听，说一定纯在reset方法
    form.reset!();
    setShowBan(false);
  };

  function reportBan() {


  }
  return (
    <>
      <Dialog
        className={styles.wrapper}
        zIndex={2501}
        header='封禁用户！'
        visible={visible}
        onConfirm={reportBan}
        onClose={handleFooterClose}
      >
        <CloseCircleFilledIcon style={{ color: 'red', fontSize: '58px' }} />
        <Form colon form={form} onSubmit={reportBan} onReset={reportBan} style={{ padding: '25px 0 30px' }}>
          <FormItem label='封号原因' name='reason'>
            <Input />
          </FormItem>
          <FormItem label='封号天数' name='date'>
            <InputNumber />
          </FormItem>
        </Form>
      </Dialog>
    </>
  );
}
export default memo(ReportMsg);
