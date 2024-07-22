package org.rce4j.net;

import jdk.jfr.Experimental;

/**
 * An enum that contains conventionally used HTTP Status Codes and their paired descriptions, which are defined by IANA in: <a href="http://www.iana.org/assignments/http-status-codes/">Hypertext Transfer Protocol (HTTP) Status Code Registry</a>
 *
 * @see <a href="http://www.iana.org/assignments/http-status-codes/">Hypertext Transfer Protocol (HTTP) Status Code Registry</a> [IANA]
 * @see <a href="https://httpwg.org/specs/rfc9110.html#overview.of.status.codes">RFC 9110 - HTTP Semantics</a> [IETF]
 * @see <a href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status">HTTP response status codes - HTTP | MDN</a> [MDN]
 */
public enum HttpCode {
    // Information responses
    CONTINUE_100(100, "Continue"),
    SWITCHING_PROTOCOLS_101(101, "Switching Protocols"),
    PROCESSING_102(102, "Processing"),
    EARLY_HINTS_103(103, "Early Hints"),
    
    // Successful responses
    OK_200(200, "OK"),
    CREATED_201(201, "Created"),
    ACCEPTED_202(202, "Accepted"),
    NON_AUTHORITATIVE_INFORMATION_203(203, "Non-Authoritative Information"),
    NO_CONTENT_204(204, "No Content"),
    RESET_CONTENT_205(205, "Reset Content"),
    PARTIAL_CONTENT_206(206, "Partial Content"),
    MULTI_STATUS_207(207, "Multi-Status"),
    ALREADY_REPORTED_208(208, "Already Reported"),
    IM_USED_226(226, "IM Used"),
    
    // Redirection messages
    MULTIPLE_CHOICES_300(300, "Multiple choices"),
    MOVED_PERMANENTLY_301(301, "Moved Permanently"),
    FOUND_302(302, "Found"),
    SEE_OTHER_303(303, "See Other"),
    NOT_MODIFIED_304(304, "Not Modified"),
    @Deprecated(since = "Security concerns regarding in-band configuration of a proxy")
    USE_PROXY_305(305, "Use Proxy"),
    @Deprecated(since = "Used in a previous version of the HTTP/1.1 specification")
    UNUSED_306(306, "unused"),
    TEMPORARY_REDIRECT_307(307, "Temporary Redirect"),
    PERMANENT_REDIRECT_308(308, "Permanent Redirect"),
    
    // Client error responses
    BAD_REQUEST_400(400, "Bad Request"),
    UNAUTHORIZED_401(401, "Unauthorized"),
    @Experimental
    PAYMENT_REQUIRED_402(402, "Payment Required"),
    FORBIDDEN_403(403, "Forbidden"),
    NOT_FOUND_404(404, "Not Found"),
    METHOD_NOT_ALLOWED_405(405, "Method Not Allowed"),
    NOT_ACCEPTABLE_406(406, "Not Acceptable"),
    PROXY_AUTHENTICATION_REQUIRED(407, "Proxy Authentication Required"),
    REQUEST_TIMEOUT_408(408, "Request Timeout"),
    CONFLICT_409(409, "Conflict"),
    GONE_410(410, "Gone"),
    LENGTH_REQUIRED_411(411, "Length Required"),
    PRECONDITION_FAILED_412(412, "Precondition Failed"),
    PAYLOAD_TOO_LARGE_413(413, "Payload Too Large"),
    URI_TOO_LONG_414(414, "URI Too Long"),
    UNSUPPORTED_MEDIA_TYPE_415(415, "Unsupported Media Type"),
    RANGE_NOT_SATISFIABLE_416(416, "Range Not Satisfiable"),
    EXPECTATION_FAILED_417(417, "Expectation Failed"),
    IM_A_TEAPOT_418(418, "I'm a teapot"),
    MISDIRECTED_REQUEST_421(421, "Misdirected Request"),
    UNPROCESSABLE_CONTENT_422(422, "Unprocessable Content"),
    LOCKED_423(423, "Locked"),
    FAILED_DEPENDENCY_424(424, "Failed Dependency"),
    TOO_EARLY_425(425, "Too Early"),
    UPGRADE_REQUIRED_426(426, "Upgrade Required"),
    PRECONDITION_REQUIRED_428(428, "Precondition Required"),
    TOO_MANY_REQUESTS_429(429, "Too Many Requests"),
    REQUEST_HEADER_FIELDS_TOO_LARGE_431(431, "Request Header Fields Too Large"),
    UNAVAILABLE_FOR_LEGAL_REASONS_451(451, "Unavailable For Legal Reasons"),
    
    // Server error responses
    INTERNAL_SERVER_ERROR_500(500, "Internal Server Error"),
    NOT_IMPLEMENTED_501(501, "Not Implemented"),
    BAD_GATEWAY_502(502, "Bad Gateway"),
    SERVICE_UNAVAILABLE_503(503, "Service Unavailable"),
    GATEWAY_TIMEOUT_504(504, "Gateway Timeout"),
    HTTP_VERSION_NOT_SUPPORTED_505(505, "HTTP Version Not Supported"),
    VARIANT_ALSO_NEGOTIATES_506(506, "Variant Also Negotiates"),
    INSUFFICIENT_STORAGE_507(507, "Insufficient Storage"),
    LOOP_DETECTED_508(508, "Loop Detected"),
    NOT_EXTENDED_510(510, "Not Extended"),
    NETWORK_AUTHENTICATION_REQUIRED_511(511, "Network Authentication Required");
    
    HttpCode(int code, String message) {
        this.CODE = code;
        this.MESSAGE = message;
    }
    
    public static HttpCode valueOf(int code) {
        return switch (code) {
            case 100 -> CONTINUE_100;
            case 101 -> SWITCHING_PROTOCOLS_101;
            case 102 -> PROCESSING_102;
            case 103 -> EARLY_HINTS_103;
            
            case 200 -> OK_200;
            case 201 -> CREATED_201;
            case 202 -> ACCEPTED_202;
            case 203 -> NON_AUTHORITATIVE_INFORMATION_203;
            case 204 -> NO_CONTENT_204;
            case 205 -> RESET_CONTENT_205;
            case 206 -> PARTIAL_CONTENT_206;
            case 207 -> MULTI_STATUS_207;
            case 208 -> ALREADY_REPORTED_208;
            case 226 -> IM_USED_226;
            
            case 300 -> MULTIPLE_CHOICES_300;
            case 301 -> MOVED_PERMANENTLY_301;
            case 302 -> FOUND_302;
            case 303 -> SEE_OTHER_303;
            case 304 -> NOT_MODIFIED_304;
            case 305 -> USE_PROXY_305;
            case 306 -> UNUSED_306;
            case 307 -> TEMPORARY_REDIRECT_307;
            case 308 -> PERMANENT_REDIRECT_308;
            
            case 400 -> BAD_REQUEST_400;
            case 401 -> UNAUTHORIZED_401;
            case 402 -> PAYMENT_REQUIRED_402;
            case 403 -> FORBIDDEN_403;
            case 404 -> NOT_FOUND_404;
            case 405 -> METHOD_NOT_ALLOWED_405;
            case 406 -> NOT_ACCEPTABLE_406;
            case 407 -> PROXY_AUTHENTICATION_REQUIRED;
            case 408 -> REQUEST_TIMEOUT_408;
            case 409 -> CONFLICT_409;
            case 410 -> GONE_410;
            case 411 -> LENGTH_REQUIRED_411;
            case 412 -> PRECONDITION_FAILED_412;
            case 413 -> PAYLOAD_TOO_LARGE_413;
            case 414 -> URI_TOO_LONG_414;
            case 415 -> UNSUPPORTED_MEDIA_TYPE_415;
            case 416 -> RANGE_NOT_SATISFIABLE_416;
            case 417 -> EXPECTATION_FAILED_417;
            case 418 -> IM_A_TEAPOT_418;
            case 421 -> MISDIRECTED_REQUEST_421;
            case 422 -> UNPROCESSABLE_CONTENT_422;
            case 423 -> LOCKED_423;
            case 424 -> FAILED_DEPENDENCY_424;
            case 425 -> TOO_EARLY_425;
            case 426 -> UPGRADE_REQUIRED_426;
            case 428 -> PRECONDITION_REQUIRED_428;
            case 429 -> TOO_MANY_REQUESTS_429;
            case 431 -> REQUEST_HEADER_FIELDS_TOO_LARGE_431;
            case 451 -> UNAVAILABLE_FOR_LEGAL_REASONS_451;
            
            case 500 -> INTERNAL_SERVER_ERROR_500;
            case 501 -> NOT_IMPLEMENTED_501;
            case 502 -> BAD_GATEWAY_502;
            case 503 -> SERVICE_UNAVAILABLE_503;
            case 504 -> GATEWAY_TIMEOUT_504;
            case 505 -> HTTP_VERSION_NOT_SUPPORTED_505;
            case 506 -> VARIANT_ALSO_NEGOTIATES_506;
            case 507 -> INSUFFICIENT_STORAGE_507;
            case 508 -> LOOP_DETECTED_508;
            case 510 -> NOT_EXTENDED_510;
            case 511 -> NETWORK_AUTHENTICATION_REQUIRED_511;
            
            default -> null;
        };
    }
    
    public final int CODE;
    public final String MESSAGE;
    
    @Override
    public String toString() {
        return CODE + " " + MESSAGE;
    }
}
