set v=8.4
set imageName=registry.cn-hangzhou.aliyuncs.com/lwj-jwl/social-circle-manager-server:
docker build  -t %imageName%%v% .
docker push %imageName%%v%
