package com.dreawer.goods.form;

import static com.dreawer.goods.constants.MessageConstants.*;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 购买信息列表表单
 */
public class PurchaseInfosForm {
    
	@NotEmpty(message=ENTRY_ERROR_EMPTY)
	private List<PurchaseInfoForm> purchaseInfos = null;

    // --------------------------------------------------------------------------------
    // getter 和 setter 方法
    // --------------------------------------------------------------------------------
	
	public List<PurchaseInfoForm> getPurchaseInfos() {
		return purchaseInfos;
	}

	public void setPurchaseInfos(List<PurchaseInfoForm> purchaseInfos) {
		this.purchaseInfos = purchaseInfos;
	}
	
}
