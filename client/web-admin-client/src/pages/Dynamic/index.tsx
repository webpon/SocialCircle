import {getDynamicApi} from 'apis/dunamic';
import React, {memo, useEffect, useRef, useState} from 'react';
import {BrowserRouterProps} from 'react-router-dom';
import style from './Index.module.less';
import  { Pagination,Button, Dialog, List, Layout} from 'tdesign-react';
import Item from "./component/Item";

export interface dynamicType {
  "dynamic": {
    "id": number,
    "title": string,
    "content": string,
    "likeNum": number,
    "commentNum": number,
    "shareNum": number,
    "userId": number,
    "topicId": number,
    "classifyId": number,
    "publishTime": string
  },
  "images": [
    {
      "id": number,
      "reportId": number,
      "dynamicId": number,
      "url": string,
      "sort": number
    }
  ],
  "topic": {
    "id": number,
    "title": string,
    "describe": string,
    "concernNum": number
  }
}

const Dynamic: React.FC<BrowserRouterProps> = () => {

  const [list, setList] = useState<Array<dynamicType>>([]);
  const [p, setP] = useState(1);
  const [total, setTotal] = useState(1);
  const [delInfo,setDelInfo] = useState({visible:false, id:0, petName:""});
  const listRef = useRef();

  useEffect(() => {
    getDynamicApi({p}).then(({code, data, total}) => {
      if (code === 200) {
        setList(data)
        setTotal(total);
      }
    })
  }, [p])


  return (
    <div
      className={style.list}
    >
      <List
        ref={listRef}
        // onScroll={({scrollTop, scrollBottom}) => {
        //   console.log(scrollTop)
        // }}
        split
        asyncLoading={'加载中'}
        onLoadMore={(e) => {
          console.log(e)
        }}
      >
        {list.map((item, i) => <Item {...item} key={i} del={setDelInfo}/>)}
      </List>
      <Pagination
        total={total}
        totalContent
        showPageSize={false}
        onChange={({current})=>{
          setP(current)
          console.log(listRef.current.scrollTop)
          // @ts-ignore
          listRef.current.scrollTop = 0;
          console.log(listRef.current)
        }}
      />
      <Dialog
        header="Basic Modal"
        visible={delInfo.visible}
        onClose={()=>setDelInfo({visible: false,id: 0,petName: ''})}
        // onConfirm={onConfirm}
      >
        <p>This is a dialog</p>
      </Dialog>
    </div>
  );
};

export default memo(Dynamic);
