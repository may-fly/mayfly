(window["webpackJsonp"]=window["webpackJsonp"]||[]).push([["chunk-a9f46b80"],{"070a":function(e,t,i){},2945:function(e,t,i){"use strict";var a=i("1462"),s=i("a340"),r=i("d789"),n=function(){function e(t,i){Object(a["a"])(this,e),this.url=t,this.method=i}return Object(s["a"])(e,[{key:"setUrl",value:function(e){return this.url=e,this}},{key:"setMethod",value:function(e){return this.method=e,this}},{key:"getUrl",value:function(){return r["a"].getApiUrl(this.url)}},{key:"request",value:function(e){return r["a"].send(this,e)}}],[{key:"create",value:function(t,i){return new e(t,i)}}]),e}();t["a"]=n},"5f3e":function(e,t,i){"use strict";var a=i("070a"),s=i.n(a);s.a},"6cd5":function(e,t,i){"use strict";i.d(t,"a",(function(){return s})),i.d(t,"b",(function(){return r})),i.d(t,"c",(function(){return n}));var a=i("2945"),s={list:a["a"].create("/devops/machines","get"),info:a["a"].create("/devops/machines/{id}/sysinfo","get"),top:a["a"].create("/devops/machines/{id}/top","get"),monitors:a["a"].create("/devops/machines/{id}/{type}/monitors","get"),save:a["a"].create("/devops/machines","post"),update:a["a"].create("/devops/machines/{id}","put"),del:a["a"].create("/devops/machines/{id}","delete"),files:a["a"].create("/devops/machines/{id}/files","get"),lsFile:a["a"].create("/devops/machines/files/{fileId}/ls","get"),rmFile:a["a"].create("/devops/machines/files/{fileId}/rm","delete"),uploadFile:a["a"].create("/devops/machines/files/upload","post"),fileContent:a["a"].create("/devops/machines/files/{fileId}/cat","get"),updateFileContent:a["a"].create("/devops/machines/files/{id}","put"),addConf:a["a"].create("/devops/machines/{machineId}/files","post"),delConf:a["a"].create("/devops/machines/files/{id}","delete")},r={list:a["a"].create("/devops/redis","get"),info:a["a"].create("/devops/redis/{id}/info","get"),del:a["a"].create("/devops/redis/{id}","delete"),save:a["a"].create("/devops/redis","post"),update:a["a"].create("/devops/redis/{id}","put")},n={scan:a["a"].create("/devops/redis/{cluster}/{id}/scan","get"),value:a["a"].create("/devops/redis/{cluster}/{id}/value","get"),update:a["a"].create("/devops/redis/{cluster}/{id}","put"),del:a["a"].create("/devops/redis/{cluster}/{id}","delete")}},"9cf3":function(e,t,i){"use strict";var a=i("b2a2"),s=i("857c"),r=i("2732"),n=i("9d5c"),o=i("59da");a("search",1,(function(e,t,i){return[function(t){var i=r(this),a=void 0==t?void 0:t[e];return void 0!==a?a.call(t,i):new RegExp(t)[e](String(i))},function(e){var a=i(t,e,this);if(a.done)return a.value;var r=s(e),l=String(this),c=r.lastIndex;n(c,0)||(r.lastIndex=0);var u=o(r,l);return n(r.lastIndex,c)||(r.lastIndex=c),null===u?-1:u.index}]}))},"9d5c":function(e,t){e.exports=Object.is||function(e,t){return e===t?0!==e||1/e===1/t:e!=e&&t!=t}},"9e64":function(e,t,i){"use strict";i.d(t,"a",(function(){return k}));var a=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"dynamic-form"},[i("el-form",{ref:"dynamicForm",attrs:{model:e.form,"label-width":e.formInfo.labelWidth?e.formInfo.labelWidth:"100px",size:e.formInfo.size?e.formInfo.size:"small"}},[e._l(e.formInfo.formRows,(function(t){return i("el-row",{key:t.key},e._l(t,(function(a){return i("el-col",{key:a.key,attrs:{span:a.span?a.span:24/t.length}},[i("el-form-item",{attrs:{prop:a.name,label:a.label,"label-width":a.labelWidth,required:a.required,rules:a.rules}},["input"===a.type?i("el-input",{attrs:{placeholder:a.placeholder,type:a.inputType,clearable:"",autocomplete:"new-password"},on:{change:function(t){a.change&&a.change(e.form)}},model:{value:e.form[a.name],callback:function(t){e.$set(e.form,a.name,"string"===typeof t?t.trim():t)},expression:"form[item.name]"}}):"text"===a.type?i("span",[e._v(e._s(e.form[a.name]))]):"select"===a.type?i("el-select",{staticStyle:{width:"100%"},attrs:{placeholder:a.placeholder,filterable:a.filterable,remote:a.remote,"remote-method":a.remoteMethod,clearable:"",disabled:a.updateDisabled&&null!=e.form.id},on:{focus:function(t){a.focus&&a.focus(e.form)}},model:{value:e.form[a.name],callback:function(t){e.$set(e.form,a.name,"string"===typeof t?t.trim():t)},expression:"form[item.name]"}},e._l(a.options,(function(e){return i("el-option",{key:e.key,attrs:{label:e[a.optionProps&&a.optionProps.label||"label"],value:e[a.optionProps&&a.optionProps.value||"value"]}})})),1):e._e()],1)],1)})),1)})),i("el-row",{attrs:{type:"flex",justify:"center"}},[e._t("btns",[i("el-button",{attrs:{size:"mini"},on:{click:e.reset}},[e._v("重 置")]),i("el-button",{attrs:{type:"primary",size:"mini"},on:{click:e.submit}},[e._v("保 存")])],{submitDisabled:e.submitDisabled,data:e.form,submit:e.submit})],2)],2)],1)},s=[];i("f3dd"),i("dbb3"),i("fe59"),i("b73f"),i("bf84"),i("fe8a"),i("08ba");function r(e,t,i){return t in e?Object.defineProperty(e,t,{value:i,enumerable:!0,configurable:!0,writable:!0}):e[t]=i,e}function n(e,t){var i=Object.keys(e);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);t&&(a=a.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),i.push.apply(i,a)}return i}function o(e){for(var t=1;t<arguments.length;t++){var i=null!=arguments[t]?arguments[t]:{};t%2?n(Object(i),!0).forEach((function(t){r(e,t,i[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(i)):n(Object(i)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(i,t))}))}return e}var l=i("1462"),c=i("a340"),u=i("bb06"),d=i("9691"),f=i("0372"),m=i("e4a1"),p=function(e){Object(u["a"])(i,e);var t=Object(d["a"])(i);function i(){var e;return Object(l["a"])(this,i),e=t.apply(this,arguments),e.form={},e.submitDisabled=!1,e}return Object(c["a"])(i,[{key:"onRoleChange",value:function(){this.formData&&(this.form=o({},this.formData))}},{key:"submit",value:function(){var e=this,t=this.$refs["dynamicForm"];t.validate((function(t){if(!t)return!1;var i=o({},e.form),a=e.form["id"]?e.formInfo["updateApi"]:e.formInfo["createApi"];a?(e.submitDisabled=!0,a.request(e.form).then((function(t){e.$message.success("保存成功"),e.$emit("submitSuccess",i),e.submitDisabled=!1}),(function(t){e.submitDisabled=!1}))):e.$message.error("表单未设置对应的提交权限")}))}},{key:"reset",value:function(){this.$emit("reset"),this.resetFieldsAndData()}},{key:"resetFieldsAndData",value:function(){var e=this.$refs["dynamicForm"];e.resetFields(),this.form={}}},{key:"mounted",value:function(){this.form=o({},this.formData)}}]),i}(m["c"]);Object(f["a"])([Object(m["b"])()],p.prototype,"formInfo",void 0),Object(f["a"])([Object(m["b"])()],p.prototype,"formData",void 0),Object(f["a"])([Object(m["d"])("formData",{deep:!0})],p.prototype,"onRoleChange",null),p=Object(f["a"])([Object(m["a"])({name:"DynamicForm"})],p);var v=p,b=v,h=i("9ca4"),y=Object(h["a"])(b,a,s,!1,null,null,null),_=y.exports,g=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",{staticClass:"form-dialog"},[i("el-dialog",{attrs:{title:e.title,visible:e.visible,width:e.dialogWidth?e.dialogWidth:"500px"}},[i("dynamic-form",{ref:"df",attrs:{"form-info":e.formInfo,"form-data":e.formData},on:{submitSuccess:e.submitSuccess},scopedSlots:e._u([{key:"btns",fn:function(t){return[e._t("btns",[i("el-button",{attrs:{disabled:t.submitDisabled,type:"primary",size:"mini"},on:{click:t.submit}},[e._v("保 存")]),i("el-button",{attrs:{disabled:t.submitDisabled,size:"mini"},on:{click:function(t){return e.close()}}},[e._v("取 消")])])]}}],null,!0)})],1)],1)},C=[],O=function(e){Object(u["a"])(i,e);var t=Object(d["a"])(i);function i(){return Object(l["a"])(this,i),t.apply(this,arguments)}return Object(c["a"])(i,[{key:"close",value:function(){var e=this;this.$emit("update:visible",!1),this.$emit("update:formData",null),this.$emit("close"),setTimeout((function(){var t=e.$refs.df;t.resetFieldsAndData()}),200)}},{key:"submitSuccess",value:function(e){this.$emit("submitSuccess",e),this.close()}}]),i}(m["c"]);Object(f["a"])([Object(m["b"])()],O.prototype,"visible",void 0),Object(f["a"])([Object(m["b"])()],O.prototype,"dialogWidth",void 0),Object(f["a"])([Object(m["b"])()],O.prototype,"title",void 0),Object(f["a"])([Object(m["b"])()],O.prototype,"formInfo",void 0),Object(f["a"])([Object(m["b"])()],O.prototype,"formData",void 0),O=Object(f["a"])([Object(m["a"])({name:"DynamicFormDialog",components:{DynamicForm:_}})],O);var j=O,D=j,w=Object(h["a"])(D,g,C,!1,null,null,null),k=w.exports},b73f:function(e,t,i){var a=i("1c8b"),s=i("efe2"),r=i("da10"),n=i("aa6b").f,o=i("1e2c"),l=s((function(){n(1)})),c=!o||l;a({target:"Object",stat:!0,forced:c,sham:!o},{getOwnPropertyDescriptor:function(e,t){return n(r(e),t)}})},bf84:function(e,t,i){var a=i("1c8b"),s=i("1e2c"),r=i("8d44"),n=i("da10"),o=i("aa6b"),l=i("1bbd");a({target:"Object",stat:!0,sham:!s},{getOwnPropertyDescriptors:function(e){var t,i,a=n(e),s=o.f,c=r(a),u={},d=0;while(c.length>d)i=s(a,t=c[d++]),void 0!==i&&l(u,t,i);return u}})},d2d5:function(e,t,i){"use strict";i.d(t,"a",(function(){return s})),i.d(t,"c",(function(){return r})),i.d(t,"b",(function(){return n}));var a=i("2262"),s={machine:a["a"].code("machine"),del:a["a"].code("machine:delete"),machineFile:a["a"].code("machineFile"),rmFile:a["a"].code("machineFile:rm"),uploadFile:a["a"].code("machineFile:upload"),updateFileContent:a["a"].code("machineFile:updateFileContent"),addConf:a["a"].code("machineFile:addConf")},r={redis:a["a"].code("redis")},n={redisKey:a["a"].code("redis:key"),del:a["a"].code("redis:key:delete")}},d855:function(e,t,i){"use strict";i.r(t),i.d(t,"default",(function(){return I}));var a=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("div",{staticClass:"toolbar"},[i("el-button",{attrs:{type:"primary",icon:"el-icon-plus",size:"mini",plain:""},on:{click:function(t){return e.openFormDialog(!1)}}},[e._v("添加")]),i("el-button",{attrs:{type:"primary",icon:"el-icon-edit",disabled:null==e.currentId,size:"mini",plain:""},on:{click:function(t){return e.openFormDialog(e.currentData)}}},[e._v("编辑")]),i("el-button",{attrs:{type:"danger",icon:"el-icon-delete",disabled:null==e.currentId,size:"mini",plain:""},on:{click:e.deleteNode}},[e._v("删除")]),i("div",{staticStyle:{float:"right"}},[i("el-input",{staticStyle:{width:"140px"},attrs:{placeholder:"host",size:"mini",plain:"",clearable:""},on:{clear:e.search},model:{value:e.params.host,callback:function(t){e.$set(e.params,"host",t)},expression:"params.host"}}),i("el-select",{attrs:{size:"mini",clearable:"",placeholder:"集群选择"},model:{value:e.params.clusterId,callback:function(t){e.$set(e.params,"clusterId",t)},expression:"params.clusterId"}},e._l(e.clusters,(function(e){return i("el-option",{key:e.id,attrs:{value:e.id,label:e.name}})})),1),i("el-button",{attrs:{type:"success",icon:"el-icon-search",size:"mini"},on:{click:e.search}})],1)],1),i("el-table",{staticStyle:{width:"100%"},attrs:{data:e.redisTable,stripe:""},on:{"current-change":e.choose}},[i("el-table-column",{attrs:{label:"选择",width:"50px"},scopedSlots:e._u([{key:"default",fn:function(t){return[i("el-radio",{attrs:{label:t.row.id},model:{value:e.currentId,callback:function(t){e.currentId=t},expression:"currentId"}},[i("i")])]}}])}),i("el-table-column",{attrs:{prop:"host",label:"IP",width:""}}),i("el-table-column",{attrs:{prop:"port",label:"端口",width:""}}),i("el-table-column",{attrs:{prop:"clusterId",label:"集群id"}}),i("el-table-column",{attrs:{prop:"description",label:"描述"}}),i("el-table-column",{attrs:{label:"操作",width:""},scopedSlots:e._u([{key:"default",fn:function(t){return[i("el-button",{directives:[{name:"permission",rawName:"v-permission",value:e.permission.redis.code,expression:"permission.redis.code"}],ref:t.row,attrs:{type:"primary",icom:"el-icon-tickets",size:"mini",plain:""},on:{click:function(i){return e.info(t.row)}}},[e._v("info")]),i("el-button",{directives:[{name:"permission",rawName:"v-permission",value:e.keyPermission.redisKey.code,expression:"keyPermission.redisKey.code"}],ref:t.row,attrs:{type:"success",size:"mini",plain:""},on:{click:function(i){return e.manage(t.row)}}},[e._v("数据管理")])]}}])})],1),i("Info",{attrs:{visible:e.infoDialog.visible,title:e.infoDialog.title,info:e.infoDialog.info},on:{"update:visible":function(t){return e.$set(e.infoDialog,"visible",t)}}}),i("dynamic-form-dialog",{attrs:{"dialog-width":e.formDialog.dialogWidth,visible:e.formDialog.visible,title:e.formDialog.title,"form-info":e.formDialog.formInfo,"form-data":e.formDialog.formData},on:{"update:visible":function(t){return e.$set(e.formDialog,"visible",t)},"update:formData":function(t){return e.$set(e.formDialog,"formData",t)},"update:form-data":function(t){return e.$set(e.formDialog,"formData",t)},submitSuccess:e.submitSuccess}})],1)},s=[],r=(i("b4fb"),i("e35a"),i("9cf3"),i("6a61"),i("cf7f")),n=i("1462"),o=i("a340"),l=i("bb06"),c=i("9691"),u=i("0372"),d=i("e4a1"),f=function(){var e=this,t=e.$createElement,i=e._self._c||t;return i("div",[i("el-dialog",{attrs:{title:e.title,visible:e.visible,"show-close":!0,width:"35%"},on:{close:function(t){return e.close()}}},[i("el-collapse",[i("el-collapse-item",{attrs:{title:"Server(Redis服务器的一般信息)",name:"server"}},[i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("redis_version(版本):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Server.redis_version))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("tcp_port(端口):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Server.tcp_port))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("redis_mode(模式):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Server.redis_mode))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("os(宿主操作系统):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Server.os))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("uptime_in_days(运行天数):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Server.uptime_in_days))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("executable(可执行文件路径):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Server.executable))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("config_file(配置文件路径):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Server.config_file))])])]),i("el-collapse-item",{attrs:{title:"Clients(客户端连接部分)",name:"client"}},[i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("connected_clients(已连接客户端数):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Clients.connected_clients))])])]),i("el-collapse-item",{attrs:{title:"Memory(内存消耗相关信息)",name:"memory"}},[i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("used_memory(分配内存总量):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Memory.used_memory_human))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("used_memory_rss(已分配的内存总量，操作系统角度):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Memory.used_memory_rss_human))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("mem_fragmentation_ratio(used_memory_rss和used_memory 之间的比率):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Memory.mem_fragmentation_ratio))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("used_memory_peak(内存消耗峰值):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Memory.used_memory_peak_human))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("total_system_memory(主机总内存):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.Memory.total_system_memory_human))])])]),i("el-collapse-item",{attrs:{title:"CPU",name:"cpu"}},[i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("used_cpu_sys(由Redis服务器消耗的系统CPU):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.CPU.used_cpu_sys))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("used_cpu_user(由Redis服务器消耗的用户CPU):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.CPU.used_cpu_user))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("used_cpu_sys_children(由后台进程消耗的系统CPU):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.CPU.used_cpu_sys_children))])]),i("div",{staticClass:"row"},[i("span",{staticClass:"title"},[e._v("used_cpu_user_children(由后台进程消耗的用户CPU):")]),i("span",{staticClass:"value"},[e._v(e._s(e.info.CPU.used_cpu_user_children))])])])],1)],1)],1)},m=[],p=function(e){Object(l["a"])(i,e);var t=Object(c["a"])(i);function i(){return Object(n["a"])(this,i),t.apply(this,arguments)}return Object(o["a"])(i,[{key:"close",value:function(){this.$emit("update:visible",!1),this.$emit("close")}}]),i}(d["c"]);Object(u["a"])([Object(d["b"])()],p.prototype,"title",void 0),Object(u["a"])([Object(d["b"])()],p.prototype,"visible",void 0),Object(u["a"])([Object(d["b"])()],p.prototype,"info",void 0),p=Object(u["a"])([Object(d["a"])({name:"Info"})],p);var v=p,b=v,h=(i("5f3e"),i("9ca4")),y=Object(h["a"])(b,f,m,!1,null,null,null),_=y.exports,g=i("d2d5"),C=i("6cd5"),O=i("9e64"),j=function(e){Object(l["a"])(i,e);var t=Object(c["a"])(i);function i(){var e;return Object(n["a"])(this,i),e=t.apply(this,arguments),e.validatePort=function(e,t,i){(t>65535||t<1)&&i(new Error("端口号错误")),i()},e.redisTable=[],e.permission=g["c"],e.keyPermission=g["b"],e.currentId=null,e.currentData=null,e.params={host:null,clusterId:null},e.redisInfo={url:""},e.clusters=[{id:0,name:"单机"}],e.infoDialog={title:"",visible:!1,info:{Server:{},Keyspace:{},Clients:{},CPU:{},Memory:{}}},e.formDialog={visible:!1,title:"",formInfo:{createApi:C["b"].save,updateApi:C["b"].update,formRows:[[{type:"input",label:"主机：",name:"host",placeholder:"请输入节点ip",rules:[{required:!0,message:"请输入节点ip",trigger:["blur","change"]}]}],[{type:"input",label:"端口号：",name:"port",placeholder:"请输入节点端口号",inputType:"number",rules:[{required:!0,message:"请输入节点端口号",trigger:["blur","change"]}]}],[{type:"input",label:"密码：",name:"pwd",placeholder:"请输入节点密码",inputType:"password"}],[{type:"input",label:"描述：",name:"description",placeholder:"请输入节点描述",inputType:"textarea"}]]},formData:{port:6379}},e}return Object(o["a"])(i,[{key:"mounted",value:function(){this.search()}},{key:"choose",value:function(e){e&&(this.currentId=e.id,this.currentData=e)}},{key:"deleteNode",value:function(){var e=Object(r["a"])(regeneratorRuntime.mark((function e(){return regeneratorRuntime.wrap((function(e){while(1)switch(e.prev=e.next){case 0:return e.next=2,C["b"].del.request({id:this.currentId});case 2:this.$message.success("删除成功"),this.search();case 4:case"end":return e.stop()}}),e,this)})));function t(){return e.apply(this,arguments)}return t}()},{key:"manage",value:function(e){this.$router.push("/redis_operation/".concat(e.clusterId,"/").concat(e.id))}},{key:"info",value:function(e){var t=this;C["b"].info.request({id:e.id}).then((function(i){t.infoDialog.info=i,t.infoDialog.title="'".concat(e.host,"' info"),t.infoDialog.visible=!0}))}},{key:"search",value:function(){var e=this;C["b"].list.request(this.params).then((function(t){e.redisTable=t}))}},{key:"openFormDialog",value:function(e){var t;e?(this.formDialog.formData=this.currentData,t="编辑redis节点"):(this.formDialog.formData={port:6379},t="添加redis节点"),this.formDialog.title=t,this.formDialog.visible=!0}},{key:"submitSuccess",value:function(){this.currentId=null,this.currentData=null,this.search()}}]),i}(d["c"]);j=Object(u["a"])([Object(d["a"])({name:"RedisList",components:{Info:_,DynamicFormDialog:O["a"]}})],j);var D=j,w=D,k=Object(h["a"])(w,a,s,!1,null,null,null),I=k.exports},fe8a:function(e,t,i){var a=i("1c8b"),s=i("3553"),r=i("cbab"),n=i("efe2"),o=n((function(){r(1)}));a({target:"Object",stat:!0,forced:o},{keys:function(e){return r(s(e))}})}}]);