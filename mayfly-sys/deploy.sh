#! /bin/sh
#接收外部参数
docker_registry=$1
docker_namespace=$2
project_name=$3
tag=$4
imageName=$docker_registry/$docker_namespace/$project_name:$tag

echo "镜像名: $imageName"
echo "项目名: $project_name"

 #查询容器是否存在，存在则删除
containerId=$(docker ps -a | grep -w "${project_name}":"${tag}" | awk '{print $1}')
if [ "$containerId" != "" ] ;
then
  #停掉容器
  docker stop "$containerId"
  #删除容器
  docker rm "$containerId"
  echo "成功删除容器"
fi
#查询镜像是否存在，存在则删除
imageId=$(docker images | grep -w "$project_name" | awk '{print $3}')
if [ "$imageId" != "" ] ;
then
  #删除镜像
  docker rmi -f "$imageId"
  echo "成功删除镜像"
fi

# 登录docker私服
docker login -u user -p pwd "$docker_registry"
# 下载镜像
docker pull "$imageName"
# 启动容器
docker run -itd --name "$project_name" --network mayfly-net -p80:8080 "$imageName"
echo "容器启动成功"
