<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
        ${name},你好${message}

        <hr size="5" color="green">

        <#assign jokerXue="薛之谦">
        ${jokerXue}
        <hr size="5" color="green">

        <#assign userInfo={'name':'薛之谦','address':'上海'}>
        姓名：${userInfo.name}
        地址：${userInfo.address}

        <hr size="5" color="green">
        <#include "head.ftl">
        <#if success="true">
            恭喜你登船了
            <#else >
            登船失败
        </#if>
</body>
</html>