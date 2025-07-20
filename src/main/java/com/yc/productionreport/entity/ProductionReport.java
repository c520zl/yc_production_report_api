package com.yc.productionreport.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.util.Date;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 *  生产报表
 */
@TableName("production_report")
public class ProductionReport {
    /**
     * Primary key ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 报告日期
     */
    @NotNull(message = "报告日期不能为空")
    @TableField("report_date")
    private Date reportDate;

    /**
     * 班次（如：早班、中班、晚班）
     */
    @NotBlank(message = "班次不能为空")
    private String shift;

    /**
     * 机器标识符
     */
    @NotBlank(message = "机器ID不能为空")
    @TableField("machine_id")
    private String machineId;

    /**
     * 拣选员姓名
     */
    @NotBlank(message = "拣选员姓名不能为空")
    @TableField("picker_name")
    private String pickerName;

    /**
     * 机器操作员姓名
     */
    @NotBlank(message = "机器操作员不能为空")
    @TableField("machine_operator")
    private String machineOperator;

    /**
     * 机器速度（单位/分钟）
     */
    @Positive(message = "机器速度必须为正数")
    @TableField("machine_speed")
    private Integer machineSpeed;

    /**
     * 生产订单号
     */
    @NotBlank(message = "生产订单号不能为空")
    @TableField("production_order_number")
    private String productionOrderNumber;

    /**
     * 生产袋子类型
     */
    @NotBlank(message = "袋子类型不能为空")
    @TableField("bag_type")
    private String bagType;

    /**
     * 成品规格
     */
    @NotBlank(message = "成品规格不能为空")
    @TableField("finished_product_specification")
    private String finishedProductSpecification;

    /**
     * 来料长度
     */
    @DecimalMin(value = "0", message = "来料长度不能为负数")
    @TableField("incoming_material_length")
    private BigDecimal incomingMaterialLength;

    /**
     * 设置材料长度
     */
    @DecimalMin(value = "0", message = "设置材料长度不能为负数")
    @TableField("setup_material_length")
    private BigDecimal setupMaterialLength;

    /**
     * 结束材料长度
     */
    @DecimalMin(value = "0", message = "结束材料长度不能为负数")
    @TableField("end_material_length")
    private BigDecimal endMaterialLength;

    /**
     * 完成产品数量
     */
    @Min(value = 0, message = "完成数量不能为负数")
    @TableField("completed_quantity")
    private Integer completedQuantity;

    /**
     * 不良产品数量
     */
    @Min(value = 0, message = "不良数量不能为负数")
    @TableField("defect_count")
    private Integer defectCount;

    /**
     * 废料长度
     */
    @DecimalMin(value = "0", message = "废料长度不能为负数")
    @TableField("scrap_material_length")
    private BigDecimal scrapMaterialLength;

    /**
     * 剩余材料长度
     */
    @DecimalMin(value = "0", message = "剩余材料长度不能为负数")
    @TableField("remaining_material_length")
    private BigDecimal remainingMaterialLength;

    /**
     * 工作开始时间
     */
    @NotNull(message = "工作开始时间不能为空")
    @TableField("work_start_time")
    private Date workStartTime;

    /**
     * 工作结束时间
     */
    @NotNull(message = "工作结束时间不能为空")
    @TableField("work_end_time")
    private Date workEndTime;

    /**
     * 异常原因（如适用）
     */
    @Size(max = 500, message = "异常原因不能超过500个字符")
    @TableField("anomaly_reason")
    private String anomalyReason;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Date getReportDate() { return reportDate; }
    public void setReportDate(Date reportDate) { this.reportDate = reportDate; }

    public String getShift() { return shift; }
    public void setShift(String shift) { this.shift = shift; }

    public String getMachineId() { return machineId; }
    public void setMachineId(String machineId) { this.machineId = machineId; }

    public String getPickerName() { return pickerName; }
    public void setPickerName(String pickerName) { this.pickerName = pickerName; }

    public String getMachineOperator() { return machineOperator; }
    public void setMachineOperator(String machineOperator) { this.machineOperator = machineOperator; }

    public Integer getMachineSpeed() { return machineSpeed; }
    public void setMachineSpeed(Integer machineSpeed) { this.machineSpeed = machineSpeed; }

    public String getProductionOrderNumber() { return productionOrderNumber; }
    public void setProductionOrderNumber(String productionOrderNumber) { this.productionOrderNumber = productionOrderNumber; }

    public String getBagType() { return bagType; }
    public void setBagType(String bagType) { this.bagType = bagType; }

    public String getFinishedProductSpecification() { return finishedProductSpecification; }
    public void setFinishedProductSpecification(String finishedProductSpecification) { this.finishedProductSpecification = finishedProductSpecification; }

    public @DecimalMin(value = "0", message = "来料长度不能为负数") BigDecimal getIncomingMaterialLength() { return incomingMaterialLength; }
    public void setIncomingMaterialLength(BigDecimal incomingMaterialLength) { this.incomingMaterialLength = incomingMaterialLength; }

    public @DecimalMin(value = "0", message = "设置材料长度不能为负数") BigDecimal getSetupMaterialLength() { return setupMaterialLength; }
    public void setSetupMaterialLength(BigDecimal setupMaterialLength) { this.setupMaterialLength = setupMaterialLength; }

    public @DecimalMin(value = "0", message = "结束材料长度不能为负数") BigDecimal getEndMaterialLength() { return endMaterialLength; }
    public void setEndMaterialLength(BigDecimal endMaterialLength) { this.endMaterialLength = endMaterialLength; }

    public Integer getCompletedQuantity() { return completedQuantity; }
    public void setCompletedQuantity(Integer completedQuantity) { this.completedQuantity = completedQuantity; }

    public Integer getDefectCount() { return defectCount; }
    public void setDefectCount(Integer defectCount) { this.defectCount = defectCount; }

    public @DecimalMin(value = "0", message = "废料长度不能为负数") BigDecimal getScrapMaterialLength() { return scrapMaterialLength; }
    public void setScrapMaterialLength(BigDecimal scrapMaterialLength) { this.scrapMaterialLength = scrapMaterialLength; }

    public @DecimalMin(value = "0", message = "剩余材料长度不能为负数") BigDecimal getRemainingMaterialLength() { return remainingMaterialLength; }
    public void setRemainingMaterialLength(BigDecimal remainingMaterialLength) { this.remainingMaterialLength = remainingMaterialLength; }

    public Date getWorkStartTime() { return workStartTime; }
    public void setWorkStartTime(Date workStartTime) { this.workStartTime = workStartTime; }

    public Date getWorkEndTime() { return workEndTime; }
    public void setWorkEndTime(Date workEndTime) { this.workEndTime = workEndTime; }

    public String getAnomalyReason() { return anomalyReason; }
    public void setAnomalyReason(String anomalyReason) { this.anomalyReason = anomalyReason; }
}