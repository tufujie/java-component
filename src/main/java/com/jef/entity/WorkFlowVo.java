package com.jef.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 * @date 2022/6/14
 */
public class WorkFlowVo implements Serializable {
    private static final long serialVersionUID = 6629174438488228878L;
    private String id;

    private String wfID;

    private String receiptId;

    private String rejectId;

    private String system;

    private String template;

    private String operate;

    private String title;

    private String tabloid;
    private String detailURL;

    private String notifyURL;

    private String openID;

    private String transferApproverOID;

    private String transferApproverName;

    private String approveType;

    private String networkID;

    private String bizzID;

    private String key;

    private Integer status;

    private String projectID;

    private String ecID;

    private String creator;

    private Date createTime;

    private String approver;

    private String billID;

    private Date updateTime;

    private String adaptor;

    private String formCodeId;

    private String formInstId;

    private String flowInstId;

    private String formDefId;

    private String group;

    private String order;

    private String flowInst;

    private String flowUrl;

    private Integer flowCount;

    private String projectName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWfID() {
        return wfID;
    }

    public void setWfID(String wfID) {
        this.wfID = wfID;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getRejectId() {
        return rejectId;
    }

    public void setRejectId(String rejectId) {
        this.rejectId = rejectId;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTabloid() {
        return tabloid;
    }

    public void setTabloid(String tabloid) {
        this.tabloid = tabloid;
    }

    public String getDetailURL() {
        return detailURL;
    }

    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getTransferApproverOID() {
        return transferApproverOID;
    }

    public void setTransferApproverOID(String transferApproverOID) {
        this.transferApproverOID = transferApproverOID;
    }

    public String getTransferApproverName() {
        return transferApproverName;
    }

    public void setTransferApproverName(String transferApproverName) {
        this.transferApproverName = transferApproverName;
    }

    public String getApproveType() {
        return approveType;
    }

    public void setApproveType(String approveType) {
        this.approveType = approveType;
    }

    public String getNetworkID() {
        return networkID;
    }

    public void setNetworkID(String networkID) {
        this.networkID = networkID;
    }

    public String getBizzID() {
        return bizzID;
    }

    public void setBizzID(String bizzID) {
        this.bizzID = bizzID;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProjectID() {
        return projectID;
    }

    public void setProjectID(String projectID) {
        this.projectID = projectID;
    }

    public String getEcID() {
        return ecID;
    }

    public void setEcID(String ecID) {
        this.ecID = ecID;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getApprover() {
        return approver;
    }

    public void setApprover(String approver) {
        this.approver = approver;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getAdaptor() {
        return adaptor;
    }

    public void setAdaptor(String adaptor) {
        this.adaptor = adaptor;
    }

    public String getFormCodeId() {
        return formCodeId;
    }

    public void setFormCodeId(String formCodeId) {
        this.formCodeId = formCodeId;
    }

    public String getFormInstId() {
        return formInstId;
    }

    public void setFormInstId(String formInstId) {
        this.formInstId = formInstId;
    }

    public String getFlowInstId() {
        return flowInstId;
    }

    public void setFlowInstId(String flowInstId) {
        this.flowInstId = flowInstId;
    }

    public String getFormDefId() {
        return formDefId;
    }

    public void setFormDefId(String formDefId) {
        this.formDefId = formDefId;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getFlowInst() {
        return flowInst;
    }

    public void setFlowInst(String flowInst) {
        this.flowInst = flowInst;
    }

    public String getFlowUrl() {
        return flowUrl;
    }

    public void setFlowUrl(String flowUrl) {
        this.flowUrl = flowUrl;
    }

    public Integer getFlowCount() {
        return flowCount;
    }

    public void setFlowCount(Integer flowCount) {
        this.flowCount = flowCount;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}