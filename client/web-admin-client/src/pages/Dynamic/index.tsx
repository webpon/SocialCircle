import { getDynamicApi, deleteDynamicApi } from 'apis/dynamic';
import React, { memo, useEffect, useRef, useState } from 'react';
import { BrowserRouterProps } from 'react-router-dom';
import style from './Index.module.less';
import { Pagination, Button, Dialog, List, Layout, MessagePlugin } from 'tdesign-react';
import Item from "./component/Item";

const Dynamic: React.FC<BrowserRouterProps> = () => {

  const [list, setList] = useState<Array<API.dynamicType>>([]);
  const [p, setP] = useState(1);
  const [total, setTotal] = useState(1);
  const [delInfo, setDelInfo] = useState({ visible: false, id: 0, petName: "" });
  const listRef = useRef();

  function getList() {
    getDynamicApi({ p }).then(({ code, data, total }) => {
      if (code === 200) {
        setList(data)
        setTotal(total);
      }
    })

  }
  useEffect(getList, [p]);

  function delDynamic() {
    deleteDynamicApi(delInfo.id).then(({ code, msg }) => {
      if (code === 200) {
        MessagePlugin.success(msg);
        setDelInfo({ visible: false, id: 0, petName: '' })
        getList();
        return
      }
      MessagePlugin.error(msg);
    })
  }


  return (
    <div
      className={style.list}
    >
      <List
        ref={listRef}
        // onScroll={({scrollTop, scrollBottom}) => {
        //
        // }}
        split
        asyncLoading={'加载中'}
        onLoadMore={(e) => {

        }}
      >
        {list.map((item, i) => <Item {...item} key={i} del={setDelInfo} />)}
      </List>
      <Pagination
        total={total}
        totalContent
        showPageSize={false}
        onChange={({ current }) => {
          setP(current)

          // @ts-ignore
          listRef.current.scrollTop = 0;

        }}
      />
      <Dialog
        header="删除动态"
        visible={delInfo.visible}
        onClose={() => setDelInfo({ visible: false, id: 0, petName: '' })}
        onConfirm={delDynamic}
      >
        <p>是否删除{delInfo.petName}的动态？动态id为：{delInfo.id}</p>
      </Dialog>
    </div>
  );
};

export default memo(Dynamic);
