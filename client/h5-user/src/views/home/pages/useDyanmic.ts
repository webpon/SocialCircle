import {watchEffect} from "vue";

export default function (dynamics, p, getDy, gotoGet) {

  const deleteDyn = (id) => {
    dynamics.value = dynamics.value.filter((item) => item.dynamic.id !== id);
  }

  if (p != null) {
    watchEffect(() => {
      getDy(p.value)
    });
  }
  const handleScroll = (e) => {
    const {scrollTop, clientHeight, scrollHeight} = e.target
    if (scrollTop + clientHeight >= scrollHeight - 100 && gotoGet.value) {
      gotoGet.value = false
      p.value++
    }
  }
  return {handleScroll, deleteDyn}
}
