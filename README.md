 <p align="center">
   <img src="https://img.shields.io/badge/Spring%20Cloud-Greenwich.SR2-blue.svg" alt="Coverage Status">
   <img src="https://img.shields.io/badge/Spring%20Boot-2.1.6.RELEASE-blue.svg" alt="Downloads">
 </p>  
 
# spring-boot-oauth-jwt
本项目基于Spring Security OAuth2 2.3.4.RELEASE实现了OAuth2以及JWT两个认证方式，启动服务时要做简单的基础工作
redis、mysql，在mysql中导入创建表，具体sql请查看field的sql.md。这里的common是用来配置redis，以及公共工具类的，必须有。
## JWT
jwt项目有两个服务，分别是token发放的服务和资源服务
启动jwt项目
启动jwt-resource
没有token直接访问jwt-resource接口是无法访问的。
通过向JWT申请token访问即可访问
通Postman的访问方式在file文件夹下用户认证中心的文件，将该文件到入到psotman修改下端口即可。返回示例如下
```java
{
    "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjE1Njg2OTkxNjUsInVzZXJfbmFtZSI6ImFkbWluIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJjMzZiNGVjMi0zYmI5LTRkZDgtYjcwMS1mMjg5NjZmZTU5YzIiLCJjbGllbnRfaWQiOiJjbGllbnQiLCJzY29wZSI6WyJhbGwiXX0.h9Psk4K8eJOCNbO6nYaL_36Li6Jfvc5mvhij7e3FT7M",
    "token_type": "bearer",
    "refresh_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJhZG1pbiIsInNjb3BlIjpbImFsbCJdLCJhdGkiOiJjMzZiNGVjMi0zYmI5LTRkZDgtYjcwMS1mMjg5NjZmZTU5YzIiLCJleHAiOjE1NzEyODM5NjUsImF1dGhvcml0aWVzIjpbIlJPTEVfQURNSU4iLCJST0xFX1VTRVIiXSwianRpIjoiZGFmNmYzZmEtYTY4Zi00MzNiLWE4MDktOTNjNDZmZmQ2NTk2IiwiY2xpZW50X2lkIjoiY2xpZW50In0.s1sYy9_yoA_CDbLniIVzypqhF2rUQlHiEXlEZM4etg0",
    "expires_in": 7199,
    "scope": "all",
    "jti": "c36b4ec2-3bb9-4dd8-b701-f28966fe59c2"
}
```
访问资源服务器时添加请求头
key是Authorization
value是Bearer “access_token”的值
即可访问
## OAuth2
同JWT一样
详细说明[博客地址](https://blog.csdn.net/MyPersonalSong/article/details/100771177)

