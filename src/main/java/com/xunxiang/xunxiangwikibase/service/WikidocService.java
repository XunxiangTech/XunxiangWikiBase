package com.xunxiang.xunxiangwikibase.service;


import com.xunxiang.xunxiangwikibase.domain.Wikidoc;
import com.xunxiang.xunxiangwikibase.req.WikidocQueryReq;
import com.xunxiang.xunxiangwikibase.req.WikidocSaveReq;
import com.xunxiang.xunxiangwikibase.resp.PageResp;
import com.xunxiang.xunxiangwikibase.resp.WikidocResp;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface WikidocService {

    /**
     * Select the wikidoc based on id
     * @param id wikidoc id
     * @return the wikidoc of this id
     */
    Wikidoc select(Long id);

    /**
     * Retrieve Correct wikidocs
     * @param wikiDocQueryReq requests param for retrieving
     * @return PageResp to include total count and pagination doc list
     */
    PageResp<WikidocResp> list(WikidocQueryReq wikiDocQueryReq);

    /**
     * Create or Update wikidocs
     * @param wikidocSaveReq request param for editing
     */
    void save(WikidocSaveReq wikidocSaveReq) throws ParseException;

    /**
     * Delete wikidocs
     * @param id wikidoc id
     */
    void delete(Long id);

    /**
     * Delete list of wikidocs
     * @param idstr
     */
    void delete(List<String> idstr);

}
