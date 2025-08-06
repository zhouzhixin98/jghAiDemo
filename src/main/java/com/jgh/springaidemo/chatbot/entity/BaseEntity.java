package com.jgh.springaidemo.chatbot.entity;

import com.jgh.springaidemo.common.handler.UserIdAutoFillHandler;
import lombok.Data;
import org.dromara.autotable.annotation.AutoColumn;
import org.dromara.autotable.annotation.mysql.MysqlTypeConstant;
import org.dromara.mpe.autofill.annotation.InsertFillData;
import org.dromara.mpe.autofill.annotation.InsertFillTime;
import org.dromara.mpe.autofill.annotation.InsertUpdateFillData;
import org.dromara.mpe.autofill.annotation.InsertUpdateFillTime;


/**
 * 实体公共基类
 */
@Data
public class BaseEntity {

    @AutoColumn(comment = "排序字段",sort = -6)
    protected Integer sort;

    @AutoColumn(comment = "创建人id",sort = -5)
    @InsertFillData(UserIdAutoFillHandler.class)
    protected String createUser;

    @AutoColumn(comment = "创建时间",sort = -4)
    @InsertFillTime(format = "yyyy-MM-dd HH:mm:ss")
    protected String createTime;

    @AutoColumn(comment = "更新人id",sort = -3)
    @InsertUpdateFillData(UserIdAutoFillHandler.class)
    protected String updateUser;

    @AutoColumn(comment = "更新时间",sort = -2)
    @InsertUpdateFillTime(format = "yyyy-MM-dd HH:mm:ss")
    protected String updateTime;

    @AutoColumn(comment = "状态 1--正常 -1--删除", defaultValue = "1",sort = -1,type = MysqlTypeConstant.TINYINT)
    protected Integer status;

}
