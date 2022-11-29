set v=1.1
set image=registry.cn-hangzhou.aliyuncs.com/lwj-jwl/social-circle-manager-page:%v%
docker build -t %image% .
docker push %image%
