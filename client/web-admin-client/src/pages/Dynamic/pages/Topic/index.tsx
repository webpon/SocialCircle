import React, {useState, memo, useEffect} from 'react';
import {Input, Table, Tag, Row, Col, Button, MessagePlugin, Avatar, Dialog} from 'tdesign-react';
import classnames from 'classnames';
import CommonStyle from 'styles/common.module.less';
import {addTopicApi, deleteTopicApi, getTopicApi} from "apis/topic";


export default memo(() => {
  const [topicList, setTopicList] = useState([]);
  const [total,setTotal] = useState(0);
  const [del, setDel] = useState({id: 0, show: false});
  const [add, setAdd] = useState({title: '', show: false, describe: ''});
  const [topic,setTopic] = useState({title: '', show: false, describe: '', id: 0});
  let p = 1;

  function getTopic() {
    getTopicApi({p}).then(({data, total}) => {
      setTopicList(data)
      setTotal(total)
    })
  }

  useEffect(getTopic, [])

  function handler() {
    setDel({show: false, id: 0})
    setAdd({show: false, title: "", describe:""})
    setTopic({show: false, title: "", describe:"", id:0, concernNum:0})
  }


  function delTopic() {
    deleteTopicApi(del.id).then(getTopic)
    handler()
  }

  function addTopic() {
    const {title, describe} = add
    addTopicApi({title, describe})
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
      colKey: 'describe',
      title: '描述',
    },
    {
      align: 'left',
      width: 100,
      ellipsis: true,
      colKey: 'concernNum',
      title: '关注数',
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
              setTopic({show: true, id: row.id, describe: row.describe, title: row.title, concernNum: row.concernNum})
            }}>
              详细信息
          </Button>
          <Button theme='danger' variant='text'
                  onClick={() => setDel({show: true, id: row.id})}>
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
          <Button onClick={()=>setAdd({title: "", show: true, describe:""})}>新增话题</Button>
        </Col>
      </Row>
      <Table
        columns={columns}
        data={topicList}
        rowKey='id'
        verticalAlign='top'
        hover
        maxHeight={'73vh'}
        pagination={{
          total: total,
          showPageSize: false,
          onCurrentChange(current, pageInfo) {
            p = pageInfo.current
            getTopic()
          },
        }}
      />
      <Dialog
        header='确认删除当前话题？'
        visible={del.show}
        onClose={handler}
        onConfirm={delTopic}
      >
        <p>删除后该话题的所有信息将被清空,且无法恢复！</p>
      </Dialog>
      <Dialog
        header='详细信息'
        visible={topic.show}
        onClose={handler}
        onConfirm={delTopic}
      >
        <p>id:{topic.id}</p>
        <p>标题:{topic.title}</p>
        <p>关注数:{topic.concernNum}</p>
        <p>
          <span>描述:</span>
          <br/>
          {topic.describe}
        </p>
        <br/>
      </Dialog>
      <Dialog
        header='添加分类'
        visible={add.show}
        onClose={handler}
        onConfirm={addTopic}
      >
        <Input
          placeholder="请输入话题标题"
          value={add.title}
          clearable
          onChange={(title) => {
            const {describe} = add
            setAdd({show: true,title, describe})
          }}
        />
        <br/>
        <Input
          placeholder="请输入话题描述"
          value={add.describe}
          clearable
          onChange={(describe) => {
            const {title} = add
            setAdd({show: true,title, describe})
          }}
        />
      </Dialog>
    </div>
  );
});
