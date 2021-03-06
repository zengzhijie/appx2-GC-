package com.dreawer.goods.form;

import com.dreawer.goods.lang.GroupStatus;
import static com.dreawer.goods.constants.MessageConstants.*;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 更新分组状态表单
 */
public class UpdateGroupStatusForm {
    
	@NotEmpty(message=ENTRY_ERROR_EMPTY)
	private List<String> ids = null; // 分组ID列表
	
	@NotNull(message=ENTRY_ERROR_EMPTY)
	private GroupStatus status = null; // 分组状态
	
    // --------------------------------------------------------------------------------
    // getter 和 setter 方法
    // --------------------------------------------------------------------------------
	
	public List<String> getIds() {
		return ids;
	}

	public void setIds(List<String> ids) {
		this.ids = ids;
	}

	public GroupStatus getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = GroupStatus.get(status);
	}
	
}
