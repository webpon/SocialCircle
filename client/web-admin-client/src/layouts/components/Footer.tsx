import React from 'react';
import { Layout, Row } from 'tdesign-react';
import { useAppSelector } from 'store';
import { selectGlobal } from 'store/global';

const { Footer: TFooter } = Layout;

const Footer = () => {
  const globalState = useAppSelector(selectGlobal);
  if (!globalState.showFooter) {
    return null;
  }

  return (
    <TFooter>
      <Row justify='center'>卷就完了~~~</Row>
    </TFooter>
  );
};

export default React.memo(Footer);
