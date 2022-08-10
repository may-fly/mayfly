import{A as e}from"./Api.1649234737806.js";import{E as s}from"./Enum.1649234737806.js";const c={list:e.create("/sys/resources","get"),detail:e.create("/sys/resources/{id}","get"),save:e.create("/sys/resources","post"),update:e.create("/sys/resources/{id}","put"),del:e.create("/sys/resources/{id}","delete"),changeStatus:e.create("/sys/resources/{id}/{status}","put")},a={list:e.create("/sys/roles","get"),save:e.create("/sys/roles","post"),update:e.create("/sys/roles/{id}","put"),del:e.create("/sys/roles/{id}","delete"),roleResourceIds:e.create("/sys/roles/{id}/resourceIds","get"),roleResources:e.create("/sys/roles/{id}/resources","get"),saveResources:e.create("/sys/roles/{id}/resources","post")},o={list:e.create("/sys/accounts","get"),save:e.create("/sys/accounts","post"),update:e.create("/sys/accounts/{id}","put"),del:e.create("/sys/accounts/{id}","delete"),changeStatus:e.create("/sys/accounts/{id}/{status}","put"),roleIds:e.create("/sys/accounts/{id}/roleIds","get"),roles:e.create("/sys/accounts/{id}/roles","get"),resources:e.create("/sys/accounts/{id}/resources","get"),saveRoles:e.create("/sys/accounts/{id}/roles","post")},u={list:e.create("/sys/logs","get")};var d={ResourceTypeEnum:new s().add("MENU","\u83DC\u5355",1).add("PERMISSION","\u6743\u9650",2),accountStatus:new s().add("ENABLE","\u6B63\u5E38",1).add("DISABLE","\u7981\u7528",0),logType:new s().add("SYS_LOG","\u7CFB\u7EDF",4).add("ERR_LOG","\u5F02\u5E38",5)};export{a,o as b,d as e,u as l,c as r};