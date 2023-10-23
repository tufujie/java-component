package com.jef.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author Administrator
 * @date 2022/6/14
 */
public class FileVo implements Serializable {

    private String id;
    private String number;
    private String description;
    private String url;
    private String type;
    private String billID;
    private String billType;
    private String creator;
    private String createTime;
    private String ecID;
    private String projectID;
    private String cfolderID;
    private String cfileID;
    private String cname;
    private String cext;
    private String cskey;
    private int imgWidth;
    private int imgHeight;
    private int imgMode;
    private int imgQuality;
    private String thumbUrl;
    private Integer seq;

    private int storeType;

    private String signUrl;


    private List<String> billIDList;

    // 过期时间
    private Date expireAt;
    // 批次标记
    private String batch;
    private String csize; //文件大小 单位byte
    private int isOld; //旧版本的单据的文件, 用于合同等修改/删除 原有上传的文件的过滤
    private String oldID; //旧文件id, 用于修改/删除 原有上传的文件的过滤
    private String number_prefix; // 附件编码前缀，用于维护附件时构造编码为 number_prefix.FCExt
    /**
     * 是否上传成功
     */
    private Boolean success;

    private String titele;
    /**
     * 用于偶尔临时存储文件字节数组
     * 请尽量避免使用文件字节数组，而应该使用流InputStream、OutputStream！
     */
    private byte[] bytes;

    private String errorMessage;
    /**
     * 附件是否允许被删除
     */
    private int filesCanBeDeleted;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getEcID() {
        return ecID;
    }

    public void setEcID(String ecID) {
        this.ecID = ecID;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getCfolderID() {
        return cfolderID;
    }

    public void setCfolderID(String cfolderID) {
        this.cfolderID = cfolderID;
    }

    public String getCfileID() {
        return cfileID;
    }

    public void setCfileID(String cfileID) {
        this.cfileID = cfileID;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getCext() {
        return cext;
    }

    public void setCext(String cext) {
        this.cext = cext;
    }

    public String getCskey() {
        return cskey;
    }

    public void setCskey(String cskey) {
        this.cskey = cskey;
    }

    public int getImgWidth() {
        return imgWidth;
    }

    public void setImgWidth(int imgWidth) {
        this.imgWidth = imgWidth;
    }

    public int getImgHeight() {
        return imgHeight;
    }

    public void setImgHeight(int imgHeight) {
        this.imgHeight = imgHeight;
    }

    public int getImgMode() {
        return imgMode;
    }

    public void setImgMode(int imgMode) {
        this.imgMode = imgMode;
    }

    public int getImgQuality() {
        return imgQuality;
    }

    public void setImgQuality(int imgQuality) {
        this.imgQuality = imgQuality;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }

    public int getStoreType() {
        return storeType;
    }

    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    public String getSignUrl() {
        return signUrl;
    }

    public void setSignUrl(String signUrl) {
        this.signUrl = signUrl;
    }

    public List<String> getBillIDList() {
        return billIDList;
    }

    public void setBillIDList(List<String> billIDList) {
        this.billIDList = billIDList;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }

    public String getBatch() {
        return batch;
    }

    public void setBatch(String batch) {
        this.batch = batch;
    }

    public String getCsize() {
        return csize;
    }

    public void setCsize(String csize) {
        this.csize = csize;
    }

    public int getIsOld() {
        return isOld;
    }

    public void setIsOld(int isOld) {
        this.isOld = isOld;
    }

    public String getOldID() {
        return oldID;
    }

    public void setOldID(String oldID) {
        this.oldID = oldID;
    }

    public String getNumber_prefix() {
        return number_prefix;
    }

    public void setNumber_prefix(String number_prefix) {
        this.number_prefix = number_prefix;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getTitele() {
        return titele;
    }

    public void setTitele(String titele) {
        this.titele = titele;
    }

    public byte[] getBytes() {
        return bytes;
    }

    public void setBytes(byte[] bytes) {
        this.bytes = bytes;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getFilesCanBeDeleted() {
        return filesCanBeDeleted;
    }

    public void setFilesCanBeDeleted(int filesCanBeDeleted) {
        this.filesCanBeDeleted = filesCanBeDeleted;
    }
}