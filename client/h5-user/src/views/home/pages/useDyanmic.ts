import {watchEffect} from "vue";
import {showToast} from "vant";

export default function (dynamics, p, getDy, loadingUp) {

  const deleteDyn = (id) => {
    dynamics.value = dynamics.value.filter((item) => item.dynamic.id !== id);
  }

  const onLoad = () => {
    if (p != null) {
      p.value++;
      getDy(p.value);
    }else {
      getDy(1)
    }
  };
  const onRefresh = () => {
    showToast('刷新成功');
    loadingUp.value = false;
    p.value = 0;
    dynamics.value = [];
    onLoad();
  };

  return { deleteDyn, onLoad, onRefresh }
}
