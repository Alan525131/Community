package org.lufengxue.constant;


/**
 * 1xx	信息，服务器收到请求，需要请求者继续执行操作
 * 2xx	成功，操作被成功接收并处理
 * 3xx	重定向，需要进一步的操作以完成请求
 * 4xx	客户端错误，请求包含语法错误或无法完成请求
 * 5xx	服务器错误，服务器在处理请求的过程中发生了错误
 */
public class HttpStatus {

    /* ---------------- 2XX: 一般来说是成功的 ---------------- */

    /**
     * HTTP Status-Code 200: 成功.
     */
    public static final int OK = 200;

    /**
     * HTTP Status-Code 201: 请求已创建, 资源无法及时建立.
     */
    public static final int CREATED = 201;

    /**
     * HTTP Status-Code 202: 服务器已接受请求，但尚未处理.
     */
    public static final int ACCEPTED = 202;

    /**
     * HTTP Status-Code 203: 服务器已成功处理了请求，但返回的实体头部元信息是非权威信息.
     */
    public static final int NOT_AUTHORITATIVE = 203;

    /**
     * HTTP Status-Code 204: 服务器成功处理了请求，但不需要返回任何实体内容.
     */
    public static final int NO_CONTENT = 204;

    /**
     * HTTP Status-Code 205: 服务器成功处理了请求，没有返回任何内容并重置表单内容.
     */
    public static final int RESET = 205;

    /**
     * HTTP Status-Code 206: 服务器已经成功处理了部分请求.
     */
    public static final int PARTIAL = 206;



    /* ---------------- 3XX: 重定向 ---------------- */

    /**
     * HTTP Status-Code 300: 多项选择.
     */
    public static final int MULT_CHOICE = 300;

    /**
     * HTTP Status-Code 301: 永久移动.
     */
    public static final int MOVED_PERM = 301;

    /**
     * HTTP Status-Code 302: 临时重定向.
     */
    public static final int MOVED_TEMP = 302;

    /**
     * HTTP Status-Code 303: 查看其他.
     */
    public static final int SEE_OTHER = 303;

    /**
     * HTTP Status-Code 304: 未修改.
     */
    public static final int NOT_MODIFIED = 304;

    /**
     * HTTP Status-Code 305: 使用代理服务器.
     */
    public static final int USE_PROXY = 305;

    /**
     * HTTP 1.1 Status-Code 307: 临时重定向.
     * 见：RFC-7231
     */
    public static final int TEMP_REDIRECT = 307;

    /**
     * HTTP 1.1 Status-Code 308: 永久重定向
     * 见：RFC-7231
     */
    public static final int PERMANENT_REDIRECT = 308;



    /* ---------------- 4XX: 客户端错误 ---------------- */

    /**
     * HTTP Status-Code 400: 错误的请求.
     */
    public static final int BAD_REQUEST = 400;

    /**
     * HTTP Status-Code 401: 当前请求需要用户验证.
     */
    public static final int UNAUTHORIZED = 401;

    /**
     * HTTP Status-Code 402: 需要付款.
     */
    public static final int PAYMENT_REQUIRED = 402;

    /**
     * HTTP Status-Code 403: 禁止的.
     */
    public static final int FORBIDDEN = 403;

    /**
     * HTTP Status-Code 404: 未找到.
     */
    public static final int NOT_FOUND = 404;

    /**
     * HTTP Status-Code 405: 不允许的方法.
     */
    public static final int BAD_METHOD = 405;

    /**
     * HTTP Status-Code 406: 不能接受的.
     */
    public static final int NOT_ACCEPTABLE = 406;

    /**
     * HTTP Status-Code 407: 需要代理身份验证.
     */
    public static final int PROXY_AUTH = 407;

    /**
     * HTTP Status-Code 408: 请求超时.
     */
    public static final int CLIENT_TIMEOUT = 408;

    /**
     * HTTP Status-Code 409: 冲突.
     */
    public static final int CONFLICT = 409;

    /**
     * HTTP Status-Code 410: 失效了.
     */
    public static final int GONE = 410;

    /**
     * HTTP Status-Code 411: 需要消息体长度.
     */
    public static final int LENGTH_REQUIRED = 411;

    /**
     * HTTP Status-Code 412: 请求头前提条件失败.
     */
    public static final int PRECON_FAILED = 412;

    /**
     * HTTP Status-Code 413: 请求的实体太大.
     */
    public static final int ENTITY_TOO_LARGE = 413;

    /**
     * HTTP Status-Code 414: 请求 URI 太长.
     */
    public static final int REQ_TOO_LONG = 414;

    /**
     * HTTP Status-Code 415: 不支持的媒体类型.
     */
    public static final int UNSUPPORTED_TYPE = 415;



    /* ---------------- 5XX: 服务端错误 ---------------- */

    /**
     * HTTP Status-Code 500: 内部服务器错误.
     */
    public static final int INTERNAL_ERROR = 500;

    /**
     * HTTP Status-Code 501: 未实现.
     */
    public static final int NOT_IMPLEMENTED = 501;

    /**
     * HTTP Status-Code 502: 错误的网关.
     */
    public static final int BAD_GATEWAY = 502;

    /**
     * HTTP Status-Code 503: 暂停服务.
     */
    public static final int UNAVAILABLE = 503;

    /**
     * HTTP Status-Code 504: 网关超时.
     */
    public static final int GATEWAY_TIMEOUT = 504;

    /**
     * HTTP Status-Code 505: 不支持 HTTP 版本.
     */
    public static final int VERSION = 505;

    /**
     * 是否为重定向状态码
     *
     * @param responseCode 被检查的状态码
     * @return 是否为重定向状态码
     * @since 5.6.3
     */
    public static boolean isRedirected(int responseCode) {
        return responseCode == MOVED_PERM
                || responseCode == MOVED_TEMP
                || responseCode == SEE_OTHER
                // issue#1504@Github，307和308是RFC 7538中http 1.1定义的规范
                || responseCode == TEMP_REDIRECT
                || responseCode == PERMANENT_REDIRECT;

    }
}
