/**
 *  LIZENZBEDINGUNGEN - Seanox Software Solutions ist ein Open-Source-Projekt,
 *  im Folgenden Seanox Software Solutions oder kurz Seanox genannt.
 *  Diese Software unterliegt der Version 2 der GNU General Public License.
 *
 *  Seanox Test Utilities
 *  Copyright (C) 2017 Seanox Software Solutions
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of version 2 of the GNU General Public License as published
 *  by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
 *  more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package com.seanox.test.utils;

/**
 *  Pattern for regular expressions.
 */
public class Pattern {

    /** Pattern for an access log entry with status 200 */
    public static final String ACCESS_LOG_STATUS_200 = ACCESS_LOG_STATUS("200");
    
    /** Pattern for an access log entry with status 201 */
    public static final String ACCESS_LOG_STATUS_201 = ACCESS_LOG_STATUS("201");

    /** Pattern for an access log entry with status 302 */
    public static final String ACCESS_LOG_STATUS_302 = ACCESS_LOG_STATUS("302");

    /** Pattern for an access log entry with status 304 */
    public static final String ACCESS_LOG_STATUS_304 = ACCESS_LOG_STATUS("304");

    /** Pattern for an access log entry with status 400 */
    public static final String ACCESS_LOG_STATUS_400 = ACCESS_LOG_STATUS("400");

    /** Pattern for an access log entry with status 401 */
    public static final String ACCESS_LOG_STATUS_401 = ACCESS_LOG_STATUS("401");

    /** Pattern for an access log entry with status 403 */
    public static final String ACCESS_LOG_STATUS_403 = ACCESS_LOG_STATUS("403");

    /** Pattern for an access log entry with status 404 */
    public static final String ACCESS_LOG_STATUS_404 = ACCESS_LOG_STATUS("404");

    /** Pattern for an access log entry with status 405 */
    public static final String ACCESS_LOG_STATUS_405 = ACCESS_LOG_STATUS("405");

    /** Pattern for an access log entry with status 501 */
    public static final String ACCESS_LOG_STATUS_501 = ACCESS_LOG_STATUS("501");

    /** Pattern for an access log entry with status 502 */
    public static final String ACCESS_LOG_STATUS_502 = ACCESS_LOG_STATUS("502");

    /** Pattern for an access log entry with status 503 */
    public static final String ACCESS_LOG_STATUS_503 = ACCESS_LOG_STATUS("503");

    /** Pattern for an access log entry with status 504 */
    public static final String ACCESS_LOG_STATUS_504 = ACCESS_LOG_STATUS("504");
    
    /** 
     *  Pattern for a custom access log entry
     *  @param  code
     *  @return custom pattern
     */
    public static String ACCESS_LOG_STATUS(String code) {
        
        code = code == null ? "-" : "\\Q" + code + "\\E";
        return "^\\d+(\\.\\d+){3} - (-|\"[^\"]+\") \\[[^]]+\\] (-|\"[^\"]+\") " + code + " \\d+ - -$";
    }
    
    /** 
     *  Pattern for a custom access log entry
     *  @param  code
     *  @param  request
     *  @return custom pattern
     */
    public static String ACCESS_LOG_STATUS(String code, String request) {
        return Pattern.ACCESS_LOG_STATUS(code, request, null);
    }
    
    /** 
     *  Pattern for a custom access log entry
     *  @param  code
     *  @param  request
     *  @param  length
     *  @return custom pattern
     */
    public static String ACCESS_LOG_STATUS(String code, String request, int length) {
        
        code = code == null ? "-" : "\\Q" + code + "\\E";
        if (request != null)
            request = request.replaceAll("(?s)^\\s*([^\r\n]+).*$", "$1").trim();
        request = request == null ? "-" : "\\Q\"" + request + "\"\\E"; 
        return "^\\d+(\\.\\d+){3} - (-|\"[^\"]+\") \\[[^]]+\\] " + request + " " + code + " " + length + " - -$";
    }
    
    /** 
     *  Pattern for a custom access log entry
     *  @param  code
     *  @param  request
     *  @param  length
     *  @param  user
     *  @return custom pattern
     */
    public static String ACCESS_LOG_STATUS(String code, String request, String user) {
        
        code = code == null ? "-" : "\\Q" + code + "\\E";
        if (request != null)
            request = request.replaceAll("^(?s)\\s*([^\r\n]+).*$", "$1").trim();
        request = request == null ? "-" : "\\Q\"" + request + "\"\\E"; 
        user = user == null ? "-" : "\\Q\"" + user + "\"\\E";
        return "^\\d+(\\.\\d+){3} - " + user + " \\[[^]]+\\] " + request + " " + code + " \\d+ - -$";
    }
    
    /** 
     *  Pattern for a HTTP response.<br>
     *  Grouping:
     *  <dir>
     *    0: match<br>
     *    1: header<br>
     *    2: body<br>
     *  </ul> 
     */
    public static final String HTTP_RESPONSE = "(?s)^(.*?)(?:(?:\r\n){2})(.*)$";

    public static final String HTTP_RESPONSE_ALLOW_DIFFUSE = "(?si)^.*\\sAllow:.*$";
    
    public static final String HTTP_RESPONSE_ALLOW(String... methods) {
        
        if (methods == null)
            methods = new String[0];
        String methodFilter = "";
        for (String method : methods) {
            if (method == null)
                continue;
            method = method.trim();
            if (method.isEmpty())
                continue;
            if (!methodFilter.isEmpty())
                methodFilter += ", ";
            methodFilter += method;
        }
        return "(?si)^.*\r\nAllow: \\Q" + methodFilter + "\\E\r\n.*$";
    }
    
    public static final String HTTP_RESPONSE_CONTENT_LENGTH = "(?si)^.*\r\nContent-Length: \\d+(\r\n.*)*$";
    
    public static final String HTTP_RESPONSE_CONTENT_LENGTH_DIFFUSE = "(?si)^.*\\sContent-Length:.*$";
    
    public static String HTTP_RESPONSE_CONTENT_LENGTH(long length) {
        return "(?si)^.*\r\nContent-Length: " + length + "(\r\n.*)*$";
    }

    public static final String HTTP_RESPONSE_CONTENT_RANGE_DIFFUSE = "(?si)^.*\\sContent-Range:.*$";
    
    public static String HTTP_RESPONSE_CONTENT_RANGE(long start, long end , long size) {
        return "(?si)^.*\r\nContent-Range: bytes " + start + "-" + end + "/" + size + "(\r\n.*)*$";
    }
    
    public static final String HTTP_RESPONSE_CONTENT_TYPE = "(?si)^.*\r\nContent-Type: [a-z/]+(\r\n.*)*$";
    
    public static final String HTTP_RESPONSE_CONTENT_TYPE_DIFFUSE = "(?si)^.*\\sContent-Type:.*$";
    
    public static final String HTTP_RESPONSE_CONTENT_TYPE_IMAGE_JPEG = "(?si)^.*\r\nContent-Type: image/jpeg\r\n.*$";

    public static final String HTTP_RESPONSE_CONTENT_TYPE_OCTET_STREAM = "(?si)^.*\r\nContent-Type: application/octet-stream\r\n.*$";
    
    public static final String HTTP_RESPONSE_CONTENT_TYPE_TEXT_HTML = "(?si)^.*\r\nContent-Type: text/html\r\n.*$";

    public static final String HTTP_RESPONSE_CONTENT_TYPE_VND_MS_EXCEL = "(?si)^.*\r\nContent-Type: application/vnd.ms-excel\r\n.*$";

    public static final String HTTP_RESPONSE_DATE = "(?si)^.*\r\nDate: [a-z]+, \\d+ [a-z]+ \\d+ \\d+:\\d+:\\d+ [a-z]+(\r\n.*)*$";

    public static final String HTTP_RESPONSE_LAST_MODIFIED = "(?si)^.*\r\nLast-Modified: [a-z]+, \\d+ [a-z]+ \\d+ \\d+:\\d+:\\d+ [a-z]+(\r\n.*)*$";

    public static final String HTTP_RESPONSE_LAST_MODIFIED_DIFFUSE = "(?si)^.*\\sLast-Modified:.*$";

    public static final String HTTP_RESPONSE_LOCATION_DIFFUSE = "(?si)^.*\\sLocation:.*$";
    
    public static final String HTTP_RESPONSE_LOCATION(String url) {
        
        if (url == null)
            throw new IllegalArgumentException();
        return "(?s)^.*\r\nLocation: \\Q" + url.trim() + "\\E\r\n.*$";
    }

    public static final String HTTP_RESPONSE_SERVER = "(?si)^.*\r\nServer: Seanox-Devwex\\b.*$";

    public static final String HTTP_RESPONSE_SERVER_DIFFUSE = "(?si)^.*\\sServer:.*$";

    /** Pattern for a http response header with status 200 */
    public static final String HTTP_RESPONSE_STATUS_200 = HTTP_RESPONSE_STATUS("200");

    /** Pattern for a http response header with status 201 */
    public static final String HTTP_RESPONSE_STATUS_201 = HTTP_RESPONSE_STATUS("201");

    /** Pattern for a http response header with status 302 */
    public static final String HTTP_RESPONSE_STATUS_302 = HTTP_RESPONSE_STATUS("302");

    /** Pattern for a http response header with status 304 */
    public static final String HTTP_RESPONSE_STATUS_304 = HTTP_RESPONSE_STATUS("304");

    /** Pattern for a http response header with status 400 */
    public static final String HTTP_RESPONSE_STATUS_400 = HTTP_RESPONSE_STATUS("400");
    
    /** Pattern for a http response header with status 401 */
    public static final String HTTP_RESPONSE_STATUS_401 = HTTP_RESPONSE_STATUS("401");
    
    /** Pattern for a http response header with status 403 */
    public static final String HTTP_RESPONSE_STATUS_403 = HTTP_RESPONSE_STATUS("403");

    /** Pattern for a http response header with status 404 */
    public static final String HTTP_RESPONSE_STATUS_404 = HTTP_RESPONSE_STATUS("404");
    
    /** Pattern for a http response header with status 405 */
    public static final String HTTP_RESPONSE_STATUS_405 = HTTP_RESPONSE_STATUS("405");
    
    /** Pattern for a http response header with status 406 */
    public static final String HTTP_RESPONSE_STATUS_406 = HTTP_RESPONSE_STATUS("406");
    
    /** Pattern for a http response header with status 413 */
    public static final String HTTP_RESPONSE_STATUS_413 = HTTP_RESPONSE_STATUS("413");
    
    /** Pattern for a http response header with status 501 */
    public static final String HTTP_RESPONSE_STATUS_501 = HTTP_RESPONSE_STATUS("501");

    /** Pattern for a http response header with status 502 */
    public static final String HTTP_RESPONSE_STATUS_502 = HTTP_RESPONSE_STATUS("502");

    /** Pattern for a http response header with status 504 */
    public static final String HTTP_RESPONSE_STATUS_504 = HTTP_RESPONSE_STATUS("504");
    
    /** 
     *  Pattern for a custom http response header
     *  @param  code
     */
    public static final String HTTP_RESPONSE_STATUS(String code) {

        if (code == null || code.trim().isEmpty())
            throw new IllegalArgumentException();
        return "(?s)^HTTP/1\\.0 " + code + "\\s+\\w+.*$";
    } 
    
    public static final String HTTP_RESPONSE_WWW_AUTHENTICATE = "(?si)^.*\r\nWWW-Authenticate: (Basic|Digest)\\s.*$";

    public static final String HTTP_RESPONSE_WWW_AUTHENTICATE_DIFFUSE = "(?si)^.*\\sWWW-Authenticate:.*$";

    public static String HTTP_RESPONSE_WWW_AUTHENTICATE(String method) {
        return "(?si)^.*\r\nWWW-Authenticate: \\Q" + method + "\\E\\s.*$";
    }

    public static final String HTTP_RESPONSE_WWW_AUTHENTICATE_BASIC = HTTP_RESPONSE_WWW_AUTHENTICATE("Basic");

    public static final String HTTP_RESPONSE_WWW_AUTHENTICATE_BASIC(String realm) {
        return "(?si)^.*\r\nWWW-Authenticate: Basic realm=\"\\Q" + realm + "\\E\"\r\n.*$";
    }
    
    public static final String HTTP_RESPONSE_WWW_AUTHENTICATE_DIGEST = HTTP_RESPONSE_WWW_AUTHENTICATE("Digest");

    public static String HTTP_RESPONSE_WWW_AUTHENTICATE_DIGEST(String realm) {
        return "(?si)^.*\r\nWWW-Authenticate: Digest realm=\"\\Q" + realm + "\\E\",.*$";
    }
    
    /** cross platform line break */
    public static final String LINE_BREAK = "(?:(?:\\r\\n)|(?:\\n\\r)|[\\r\\n])";
    
    /** spaces without line break */
    public static final String LINE_SPACE = "[^\\S\\r\\n]";
    
    /** 
     *  Pattern for netweork connection.<br>
     *  Format: {@code host:port}<br>
     *  Grouping:
     *  <dir>
     *    0: match<br>
     *    1: host<br>
     *    2: port<br>
     *  </ul> 
     */
    public static final String NETWORK_CONNECTION = "^(?i:([a-z_\\-\\d\\.:]+):(\\d{1,5}))$";
}