package com.xunxiang.xunxiangwikibase.mapper;

import com.xunxiang.xunxiangwikibase.domain.Wikidoc;
import com.xunxiang.xunxiangwikibase.domain.WikidocExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface WikidocMapper {
    long countByExample(WikidocExample example);

    int deleteByExample(WikidocExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Wikidoc record);

    int insertSelective(Wikidoc record);

    List<Wikidoc> selectByExample(WikidocExample example);

    Wikidoc selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Wikidoc record, @Param("example") WikidocExample example);

    int updateByExample(@Param("record") Wikidoc record, @Param("example") WikidocExample example);

    int updateByPrimaryKeySelective(Wikidoc record);

    int updateByPrimaryKey(Wikidoc record);
}