package com.jef.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 返租合同
 * @author Jef
 * @date 2018/8/20
 */
public class RentBackContractVo implements Serializable {
    private static final long serialVersionUID = -4044561166841626874L;
    /**
     * Redis缓存key
     */
    public static final String OBJECT_KEY = "t_rt_rentbackcontract";
    private String id;
    /**
     * ecID
     */
    private String ecID;
    /**
     * 项目ID
     */
    private String projectID;
    /**
     * 合同编码
     */
    private String number;
    /**
     * 合同名称
     */
    private String name;
    /**
     * 合同类型，默认1
     */
    private Integer contractType;
    /**
     * 合同状态，参考了合同的状态
     */
    private Integer status;
    /**
     * 开始日期
     */
    private Date startDate;
    /**
     * 结束日期
     */
    private Date endDate;
    /**
     * 合同总金额
     */
    private BigDecimal totalAmount;
    /**
     * 总共建筑面积
     */
    private BigDecimal buildingArea;
    /**
     * 总共租赁面积
     */
    private BigDecimal rentArea;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 审核日期
     */
    private Date auditTime;
    /**
     * 审核人
     */
    private String auditorID;
    /**
     * 是否有原合同（0否（默认），1是）
     */
    private Integer main;
    /**
     * 变更原因
     */
    private String changeReason;
    /**
     * 原合同ID
     */
    private String mainContractID;
    /**
     * 签约日期
     */
    private Date signDate;
    /**
     * 退租日期
     */
    private Date finishDate;
    /**
     * 备注、说明
     */
    private String description;
    /**
     * 补充条款
     */
    private String additionRemark;
    /**
     * 甲方ID
     */
    private String partAID;
    /**
     * 乙方ID
     */
    private String partBID;
    /**
     * 招商顾问ID
     */
    private String consultantID;
    /**
     * 返还周期
     */
    private Integer backType;
    /**
     * 周期形式
     */
    private Integer circleType;
    /**
     * 自定义周期时间
     */
    private Integer circleDays;
    /**
     * 付款日期类别
     */
    private Integer rentType;
    /**
     * 付款日天数，为固定日，一次缴清2018-08-20 固定周期-年08-20 其它20
     */
    private String rentDays;
    /**
     * 首期形式
     */
    private Integer firstPayType;
    /**
     * 首期应收时间
     */
    private Date firstPayDate;
    /**
     * 首期应收开始时间
     */
    private Date firstStartDate;
    /**
     * 首期应收截止时间
     */
    private Date firstEndDate;
    /**
     * 文件url，多个文件ID以英文逗号分割
     */
    private String fileUrl;
    /**
     * 变更前的合同ID
     */
    private String changeID;
    /**
     * 开户行
     */
    private String bankName;
    /**
     * 开户行账号
     */
    private String bankAccount;
    /**
     * 是否设置了返租比例约定
     */
    private Integer backSetting = 0;

    // 拓展字段
    /**
     * 返租资源
     */
    private String productName;
    /**
     * 原合同编码
     */
    private String preNumber;
    /**
     * 原合同名称
     */
    private String preName;
    /**
     * 业主名称
     */
    private String partBName;
    /**
     * 甲方
     */
    private String partAName;
    /**
     * 招商顾问名称
     */
    private String consultantName;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getContractType() {
        return contractType;
    }

    public void setContractType(Integer contractType) {
        this.contractType = contractType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getBuildingArea() {
        return buildingArea;
    }

    public void setBuildingArea(BigDecimal buildingArea) {
        this.buildingArea = buildingArea;
    }

    public BigDecimal getRentArea() {
        return rentArea;
    }

    public void setRentArea(BigDecimal rentArea) {
        this.rentArea = rentArea;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditorID() {
        return auditorID;
    }

    public void setAuditorID(String auditorID) {
        this.auditorID = auditorID;
    }

    public Integer getMain() {
        return main;
    }

    public void setMain(Integer main) {
        this.main = main;
    }

    public String getChangeReason() {
        return changeReason;
    }

    public void setChangeReason(String changeReason) {
        this.changeReason = changeReason;
    }

    public String getMainContractID() {
        return mainContractID;
    }

    public void setMainContractID(String mainContractID) {
        this.mainContractID = mainContractID;
    }

    public Date getSignDate() {
        return signDate;
    }

    public void setSignDate(Date signDate) {
        this.signDate = signDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAdditionRemark() {
        return additionRemark;
    }

    public void setAdditionRemark(String additionRemark) {
        this.additionRemark = additionRemark;
    }

    public String getPartAID() {
        return partAID;
    }

    public void setPartAID(String partAID) {
        this.partAID = partAID;
    }

    public String getPartBID() {
        return partBID;
    }

    public void setPartBID(String partBID) {
        this.partBID = partBID;
    }

    public String getConsultantID() {
        return consultantID;
    }

    public void setConsultantID(String consultantID) {
        this.consultantID = consultantID;
    }

    public Integer getBackType() {
        return backType;
    }

    public void setBackType(Integer backType) {
        this.backType = backType;
    }

    public Integer getCircleType() {
        return circleType;
    }

    public void setCircleType(Integer circleType) {
        this.circleType = circleType;
    }

    public Integer getCircleDays() {
        return circleDays;
    }

    public void setCircleDays(Integer circleDays) {
        this.circleDays = circleDays;
    }

    public Integer getRentType() {
        return rentType;
    }

    public void setRentType(Integer rentType) {
        this.rentType = rentType;
    }

    public String getRentDays() {
        return rentDays;
    }

    public void setRentDays(String rentDays) {
        this.rentDays = rentDays;
    }

    public Integer getFirstPayType() {
        return firstPayType;
    }

    public void setFirstPayType(Integer firstPayType) {
        this.firstPayType = firstPayType;
    }

    public Date getFirstPayDate() {
        return firstPayDate;
    }

    public void setFirstPayDate(Date firstPayDate) {
        this.firstPayDate = firstPayDate;
    }

    public Date getFirstStartDate() {
        return firstStartDate;
    }

    public void setFirstStartDate(Date firstStartDate) {
        this.firstStartDate = firstStartDate;
    }

    public Date getFirstEndDate() {
        return firstEndDate;
    }

    public void setFirstEndDate(Date firstEndDate) {
        this.firstEndDate = firstEndDate;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getChangeID() {
        return changeID;
    }

    public void setChangeID(String changeID) {
        this.changeID = changeID;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public Integer getBackSetting() {
        return backSetting;
    }

    public void setBackSetting(Integer backSetting) {
        this.backSetting = backSetting;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPreNumber() {
        return preNumber;
    }

    public void setPreNumber(String preNumber) {
        this.preNumber = preNumber;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public String getPartBName() {
        return partBName;
    }

    public void setPartBName(String partBName) {
        this.partBName = partBName;
    }

    public String getPartAName() {
        return partAName;
    }

    public void setPartAName(String partAName) {
        this.partAName = partAName;
    }

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }
}
