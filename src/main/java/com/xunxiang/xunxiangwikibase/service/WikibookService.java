package com.xunxiang.xunxiangwikibase.service;


import com.xunxiang.xunxiangwikibase.req.WikibookQueryReq;
import com.xunxiang.xunxiangwikibase.req.WikibookSaveReq;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikibookResp;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;

public interface WikibookService {

    /**
     * Retrieve Correct wikibooks
     * @param wikiBookQueryReq requests param for retrieving
     * @return PageResp to include total count and pagination book list
     */
    PageResp<WikibookResp> list(WikibookQueryReq wikiBookQueryReq);

    /**
     * Create or Update wikibooks
     * @param wikibookSaveReq request param for editing
     */
    void save(WikibookSaveReq wikibookSaveReq) throws ParseException;

}
