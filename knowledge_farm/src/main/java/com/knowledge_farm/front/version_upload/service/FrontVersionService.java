package com.knowledge_farm.front.version_upload.service;

import com.knowledge_farm.entity.Question;
import com.knowledge_farm.entity.QuestionTitle;
import com.knowledge_farm.entity.QuestionType;
import com.knowledge_farm.entity.Version;
import com.knowledge_farm.front.version_upload.dao.VersionDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.persistence.criteria.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName FrontVersionUpload
 * @Description
 * @Author 张帅华
 * @Date 2020-05-30 21:25
 */
@Service
@Transactional(readOnly = true)
public class FrontVersionService {
    @Resource
    private VersionDao versionDao;
    @Value("${file.apkFileLocation}")
    private String apkFileLocation;

    public Page<Version> findVersionPage(String versionName, Integer pageNumber, Integer pageSize){
        Specification<Version> spec = new Specification<Version>() {
            @Override
            public Predicate toPredicate(Root<Version> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> list = new ArrayList<>();
                if(versionName != null && !versionName.equals("") && !versionName.equals(" ")){
                    list.add(cb.equal(root.get("versionName"), versionName));
                }
                //此时条件之间是没有任何关系的。
                Predicate[] arr = new Predicate[list.size()];
                return cb.and(list.toArray(arr));
            }
        };
        return this.versionDao.findAll(spec, PageRequest.of(pageNumber - 1, pageSize));
    }

    public List<String> findAllVersionName(){
        return this.versionDao.findAllVersionName();
    }

    public Version findVersionById(Integer id){
        return this.versionDao.findVersionById(id);
    }

    public Version findLastVersion(){
        return this.versionDao.findLastVersion();
    }

    @Transactional(readOnly = false)
    public void delete(List<Integer> idList){
        List<Version> versionList = this.versionDao.findAllById(idList);
        for(Version version : versionList){
            File file = new File(this.apkFileLocation + "/" + version.getFileName());
            if(file.exists()){
                file.delete();
            }
            this.versionDao.delete(version);
        }
    }

    @Transactional(readOnly = false)
    public void save(Version version){
        this.versionDao.save(version);
    }

}
