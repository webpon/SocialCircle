import React, {memo, useEffect, useRef, useState} from "react";
import style from "../index.module.less";
import {Avatar, Col, Image, ImageViewer, Layout, List, Row, Skeleton, Space} from "tdesign-react";
import {dynamicType} from "../index";
import {BrowseIcon, DeleteIcon} from 'tdesign-icons-react';
import {getUserInfoById} from "apis/user";

const {Header, Content} = Layout;

const {ListItem} = List;
interface type extends dynamicType{
  del: Function
}
function Item(props: type) {
  const {dynamic, images,del, topic} = props
  const [time, setTime] = useState("");
  const [user, setUser] = useState({});

  useEffect(() => {
    const date = new Date(dynamic.publishTime);
    let time = date.toLocaleDateString().replace(/\//g, "-") + " " + date.toTimeString().substr(0, 8)
    setTime(time);
    getUserInfoById({id: dynamic.userId}).then(({data}) => {
      setUser(data);
    })
  }, [])

  const trigger = (open: any, url: string) => {
    const mask = (
      <div
        className={style.trigger}
        onClick={open}
      >
        <span><BrowseIcon size="16px" name={'browse'}/> 预览</span>
      </div>
    );

    return (
      <Image
        alt={'test'}
        src={url}
        overlayContent={mask}
        overlayTrigger="hover"
        fit="contain"
        style={{
          width: 100,
          height: 100,
          border: '4px solid var(--td-bg-color-secondarycontainer)',
          borderRadius: 'var(--td-radius-medium)',
          backgroundColor: '#fff'
        }}
      />
    )
  }
  return (
    <ListItem>
      <Layout>
        <Header className={style.header}>
          <Row style={{width: "200px"}}>
            <Col span={3}>
              <Avatar
                image={user.headIcon}
                hideOnLoadFailed={false}
                style={{marginRight: '40px'}}
                size="40px"
              />
            </Col>
            <Col span={6}>
              <h4 style={{lineHeight: "0px"}}>{user.petName}</h4>
            </Col>
          </Row>
        </Header>

        <Content style={{background: "#fff"}}>
          <p>{dynamic.content}</p>
          <Space breakLine size={16}>
            {
              images
                .sort((value, value2) => value.sort - value2.sort)
                .map(({url}) => <ImageViewer trigger={({open}) => trigger(open, url)} images={[url]}/>
                )
            }
          </Space>
          <div>
            {
              (function () {
                if (topic !== undefined) {
                  return <span>#{topic.title}</span>
                }
              })()
            }
          </div>
          <div>
            <span>
              {time}
            </span>
            <span style={{float: "right"}}>
              <DeleteIcon onClick={()=>{
                del({visible: true ,id: dynamic.id,petName: user.petName})
              }}/>
            </span>
          </div>
        </Content>
      </Layout>
    </ListItem>
  );
}

export default memo(Item);
