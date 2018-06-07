cd regcenter
call mvn clean
cd ..
start 注册中心regcenter.bat

cd dbsvr
call mvn clean
cd ..
start 数据库服务dbsvr-1.bat
start 数据库服务dbsvr-2.bat

cd web
call mvn clean
cd ..
start 网站服务web.bat