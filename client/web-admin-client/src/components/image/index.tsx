import React, { FC, useState, useCallback, memo } from 'react';
import Viewer from 'react-viewer';

export interface ImgProps {
  imgUrl: string;
  style: object;
  images:[]
}
const PictureView: FC<ImgProps> = memo(({ imgUrl,style,images }) => {
  const [visible, setVisible] = useState(false);
  const handleClick = useCallback(() => {
    setVisible(true);
  }, []);
  const handleClose = useCallback(() => {
    setVisible(false);
  }, []);
  let list = []
  images.forEach((item)=>{
    if (item.url !== imgUrl) {
      list.push({
        src: item.url
      })
    }
  })
  return (
    <div style={{"margin": "10px"}}>
      <span onClick={handleClick}>
        <img src={imgUrl} style={style}/>
      </span>
      <Viewer
        visible={visible}
        onClose={handleClose}
        images={[{ src: imgUrl, alt: '' },...list]}
        zIndex={10000}
      />
    </div>
  );
});
PictureView.displayName = 'PictureView';
export default PictureView;

