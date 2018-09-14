package net.skhu.mentoring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoticePagination {
    private long tid; // 게시판 타입 ID
    private int pg = 1; // 페이지
    private int sz = 10; // 페이지 사이즈
    private int ob; // 정렬 방법
    private int sb; // 검색 방법
    private long requestCount; // 조회 결과 수
    private String st; // 검색 문자열

    public String getQueryString() {
        String url = null;
        try {
            String temp = (st == null) ? "" : URLEncoder.encode(st, "UTF-8");
            url = String.format("tid=%d&pg=%d&sz=%d&ob=%d&sb=%d&st=%s", tid, pg, sz, ob, sb, temp);
        }
        catch (UnsupportedEncodingException e) {

        }
        return url;
    }
}
