title ���ݿ����dbsvr-1
cd dbsvr
call mvn spring-boot:run -Dspring-boot.run.profiles=svr1
pause

rem java -jar app.jar --spring.profiles.active=test --server.port=8060