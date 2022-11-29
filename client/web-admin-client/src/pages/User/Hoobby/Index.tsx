import React, {useState, memo, useEffect, useRef} from 'react';
import classnames from 'classnames';
import CommonStyle from 'styles/common.module.less';
import {Tree, Space, Row, Col, Button, Dialog, MessagePlugin, Form, Input, FormInstanceFunctions,} from 'tdesign-react';
import {getHobbyApi, addHobbyApi, deleteHobbyApi} from "apis/hobby";
import styles from "../Violate/components/Ban.module.less";
import {isGeneratorFunction} from "util/types";

const {FormItem} = Form;

interface hobbyType {
  label: string;
  children?: [
    {
      label: string;
      id: number;
      personNum: number;
    }
  ]
}

export default memo(() => {
  const [list, setList] = useState([]);
  const [del, setDel] = useState({id: 0, title: "", show: false});
  const [addShow, setAddShow] = useState(false);
  const [title,setTitle] = useState(null);
  const [form] = Form.useForm();
  const formRef = useRef<FormInstanceFunctions>();

  const onSubmit = (e) => {
    if (e.validateResult === true) {
      const formValue = formRef.current?.getFieldsValue?.(true) || {};
      // @ts-ignore
      addHobbyApi(formValue).then(({code,data})=>{
            if (code === 200){
              let arr:hobbyType[] = [];
              list.forEach((item:hobbyType)=>{
                  if (item.label === data.title){
                    item.children?.push(data);
                  }
                  arr.push(item)
              })
              // @ts-ignore
              setList(arr);
              MessagePlugin.success('添加成功');
              setAddShow(false);
              return
            }
            MessagePlugin.error('添加失败');
      })
    }
  };


  function getList() {
    getHobbyApi().then((data) => {
      setList([])
      for (let i in data) {
        data[i].forEach(d => {
          d.label = d.hobbyName
          delete d.hobbyName
          delete d.title
        })
        const hobby: hobbyType = {
          label: i,
          children: data[i]
        }
        // @ts-ignore
        setList([hobby, ...list])
      }
    })

  }

  useEffect(getList, []);

  function delBun() {
    deleteHobbyApi(del.id).then(({msg, code}) => {
      if (code === 200) {
        getList();
        setDel({id: 0, title: "", show: false})
        MessagePlugin.success(msg);
      }
      MessagePlugin.error(msg);
    })
  }

  // @ts-ignore
  // @ts-ignore
  return (
    <div className={classnames(CommonStyle.pageWithPadding, CommonStyle.pageWithColor)}>
      <Button onClick={() => setAddShow(true)}>添加兴趣</Button>
      <Tree data={list}
            line
            hover
            expandOnClickNode
            label={(node) => {
              const data = node.data;
              if (data.children) {
                return (
                  <Row justify="space-between">
                    <Col span={2}>{node.label}</Col>
                    <Col span={1}>
                      <Button
                        theme="primary"
                        variant="text"
                        onClick={() => {
                          setTitle(node.label)
                          setAddShow(true)
                        }}
                      >
                        添加
                      </Button>
                    </Col>
                  </Row>
                );
              }

              // @ts-ignore
              return (
                <Row justify="space-between">
                  <Col span={1}>{data.id}</Col>
                  <Col span={2}>{node.label}</Col>
                  <Col span={1}>{data.personNum}</Col>
                  <Col span={1}>
                    <Button
                      theme="danger"
                      variant="text"
                      onClick={() => setDel({show: true, id: data.id, title: node.label})}
                    >
                      删除
                    </Button>
                  </Col>
                </Row>
              )
            }}
      />
      <Dialog
        className={styles.wrapper}
        zIndex={2501}
        header='删除兴趣'
        visible={del.show}
        onConfirm={delBun}
        onClose={() => setDel({id: 0, title: "", show: false})}
      >
        <p>
          是否删除"{del.title}"这兴趣
        </p>
      </Dialog>
      <Dialog
        className={styles.wrapper}
        zIndex={2501}
        header='添加兴趣'
        visible={addShow}
        confirmBtn={null}
        // cancelBtn={null}
        onCloseBtnClick={()=>setAddShow(false)}
      >
        <Form form={form}
              ref={formRef}
              onSubmit={onSubmit}
              colon
              labelWidth={100}
        >
          <FormItem label="兴趣标题" name="title">
            <Input defaultValue={title}/>
          </FormItem>
          <FormItem label="兴趣名字" name="hobbyName">
            <Input/>
          </FormItem>
          <FormItem style={{marginLeft: 310}}>
            <Space>
              <Button type="submit" theme="primary">
                提交
              </Button>
              <Button type="reset" theme="default">
                重置
              </Button>
            </Space>
          </FormItem>
        </Form>
      </Dialog>
    </div>
  );
});
