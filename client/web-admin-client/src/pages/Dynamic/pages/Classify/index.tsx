import React, {useState, memo, useEffect} from 'react';
import {Input, Table, Tag, Row, Col, Button, MessagePlugin, Avatar, Dialog} from 'tdesign-react';
import classnames from 'classnames';
import CommonStyle from 'styles/common.module.less';
import {addClassifyApi, deleteClassifyApi, getClassifyApi} from "services/classify";


export default memo(() => {
  const [classifyList, setClassifyList] = useState([]);
  const [del, setDel] = useState({id: 0, show: false});
  const [add, setAdd] = useState({title: '', show: false});

  function getClassify() {
    getClassifyApi().then(({data}) => {
      setClassifyList(data)
    })
  }

  useEffect(getClassify, [])

  function handler() {
    setDel({show: false, id: 0})
    setAdd({show: false, title: ""})
  }


  function delClassify() {
    deleteClassifyApi(del.id).then(getClassify);
    handler()
  }

  function addClassify() {
    addClassifyApi(add.title).then(getClassify)
    handler()
  }

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
      colKey: 'title',
      title: '分类名称',
    },
    {
      align: 'left',
      width: 100,
      ellipsis: true,
      colKey: 'dynamicCountNumber',
      title: '动态数量',
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
            <Button theme='danger' variant='text' onClick={() => {
              setDel({show: true, id: row.id})
            }}>
              删除
            </Button>
          </>
        );
      },
    },

  ]


  return (
    <div className={classnames(CommonStyle.pageWithPadding, CommonStyle.pageWithColor)}>
      <Row justify='space-between'>
        <Col>
          <Button onClick={()=>setAdd({title: "", show: true})}>新增分类</Button>
        </Col>
      </Row>
      <Table
        columns={columns}
        data={classifyList}
        rowKey='id'
        verticalAlign='top'
        hover
        maxHeight={'73vh'}
        pagination={{
          total: classifyList.length,
          showPageSize: false,
          showJumper: false,
          showPreviousAndNextBtn: false,
          showPageNumber: false
        }}
      />
      <Dialog
        header='确认删除当前分类？'
        visible={del.show}
        onClose={handler}
        onConfirm={delClassify}
      >
        <p>删除后该分类的所有信息将被清空,且无法恢复！</p>
      </Dialog>
      <Dialog
        header='添加分类'
        visible={add.show}
        onClose={handler}
        onConfirm={addClassify}
      >
        <Input
          placeholder="请输入内容"
          value={add.title}
          clearable
          onChange={(title) => {
            setAdd({show: true,title})
          }}
        />
      </Dialog>
    </div>
  );
});
