package com.boredream.bdcodehelper.net;

import java.util.HashMap;
import java.util.Map;

public class ErrorConstants {
    public static Map<Integer, String> errors;

    static {
        errors = new HashMap<>();
        errors.put(9001, "Application Id为空，请初始化.");
        errors.put(9002, "解析返回数据出错");
        errors.put(9003, "上传文件出错");
        errors.put(9004, "文件上传失败");
        errors.put(9005, "批量操作只支持最多50条");
        errors.put(9006, "objectId为空");
        errors.put(9007, "文件大小超过10M");
        errors.put(9008, "上传文件不存在");
        errors.put(9009, "没有缓存数据");
        errors.put(9010, "网络超时");
        errors.put(9011, "BmobUser类不支持批量操作");
        errors.put(9012, "上下文为空");
        errors.put(9013, "BmobObject（数据表名称）格式不正确");
        errors.put(9014, "第三方账号授权失败");
        errors.put(9015, "其他错误均返回此code");
        errors.put(9016, "无网络连接，请检查您的手机网络.");
        errors.put(9017, "与第三方登录有关的错误，具体请看对应的错误描述");
        errors.put(9018, "参数不能为空");
        errors.put(9019, "格式不正确：手机号码、邮箱地址、验证码");

        errors.put(401, "用户登陆信息失效");
        errors.put(500, "服务器忙，请稍后再试");

        errors.put(101, "用户名或密码不正确");
        errors.put(102, "格式不正确");
        errors.put(103, "查询单个对象或更新对象时必须提供objectId 或 非法的 class 名称，class 名称是大小写敏感的，并且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线.");
        errors.put(104, "关联的class名称不存在");
        errors.put(105, "字段名是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线 或 字段名是");
        errors.put(106, "不是一个正确的指针类型");
        errors.put(107, "数据格式不正确");
        errors.put(108, "用户名和密码是必需的");
        errors.put(109, "登录信息是必需的，如邮箱和密码时缺少其中一个提示此信息");
        errors.put(111, "传入的字段值与字段类型不匹配");
        errors.put(112, "请求的值必须是数组");
        errors.put(113, "请求数组中每个元素应该是一个像这样子的json对象");
        errors.put(114, "请求数组大于50");
        errors.put(117, "纬度范围在[-90, 90] 或 经度范围在[-180, 180]");
        errors.put(120, "要使用此功能，请在");
        errors.put(131, "不正确的deviceToken");
        errors.put(132, "不正确的installationId");
        errors.put(133, "不正确的deviceType");
        errors.put(134, "deviceToken已经存在");
        errors.put(135, "installationId已经存在");
        errors.put(136, "只读属性不能修改 或 android设备不需要设置deviceToken");
        errors.put(138, "表是只读的");
        errors.put(139, "角色名称是大小写敏感的，并且必须以英文字母开头，有效的字符仅限在英文字母、数字、空格、横线以及下划线。");
        errors.put(141, "缺失推送需要的data参数");
        errors.put(142, "时间格式应该如下： 2013-12-04 00:51:13");
        errors.put(143, "必须是一个数字");
        errors.put(144, "不能是之前的时间");
        errors.put(145, "文件大小错误");
        errors.put(146, "文件名错误");
        errors.put(147, "文件分页上传偏移量错误");
        errors.put(148, "文件上下文错误");
        errors.put(149, "空文件");
        errors.put(150, "文件上传错误");
        errors.put(151, "文件删除错误");
        errors.put(160, "图片错误");
        errors.put(161, "图片模式错误");
        errors.put(162, "图片宽度错误");
        errors.put(163, "图片高度错误");
        errors.put(164, "图片长边错误");
        errors.put(165, "图片短边错误");
        errors.put(201, "缺失数据");
        errors.put(202, "用户名已经存在");
        errors.put(203, "邮箱已经存在");
        errors.put(204, "必须提供一个邮箱地址");
        errors.put(205, "没有找到此邮件的用户");
        errors.put(206, "登录用户才能修改自己的信息。RestAPI的Http Header中没有提供sessionToken的正确值，不能修改或删除用户");
        errors.put(207, "验证码错误");
        errors.put(208, "authData不正确");
        errors.put(209, "该手机号码已经存在");
        errors.put(210, "密码不正确");
        errors.put(211, "找不到用户");
        errors.put(301, "验证错误详细提示，如邮箱格式不正确");
        errors.put(302, "Bmob后台设置了应用设置值， 如'不允许SDK创建表 '");
        errors.put(310, "云端逻辑运行错误的详细信息");
        errors.put(311, "云端逻辑名称是大小写敏感的，且必须以英文字母开头，有效的字符仅限在英文字母、数字以及下划线。");
        errors.put(401, "唯一键不能存在重复的值");
        errors.put(402, "查询的wher语句长度大于具体多少个字节");
        errors.put(601, "不正确的BQL查询语句");
        errors.put(1002, "该应用能创建的表数已达到限制");
        errors.put(1003, "该表的行数已达到限制");
        errors.put(1004, "该表的列数已达到限制");
        errors.put(1005, "每月api请求数量已达到限制");
        errors.put(1006, "该应用能创建定时任务数已达到限制");
        errors.put(1007, "该应用能创建云端逻辑数已达到限制");
        errors.put(1500, "你上传的文件大小已超出限制");

        errors.put(10010, "该手机号发送短信达到限制(对于一个应用来说，一天给同一手机号发送短信不能超过10条，一小时给同一手机号发送短信不能超过5条，一分钟给同一手机号发送短信不能超过1条)");
        errors.put(10011, "该账户无可用的发送短信条数");
        errors.put(10012, "身份信息必须审核通过才能使用该功能");
        errors.put(10013, "非法短信内容");
    }
}
