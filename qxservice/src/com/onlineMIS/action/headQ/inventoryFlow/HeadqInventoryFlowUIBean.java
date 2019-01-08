package com.onlineMIS.action.headQ.inventoryFlow;

import java.util.ArrayList;
import java.util.List;
import com.onlineMIS.ORM.entity.headQ.inventory.HeadqInvenTraceInfoVO;


public class HeadqInventoryFlowUIBean{
	private List<HeadqInvenTraceInfoVO> traceItems = new ArrayList<HeadqInvenTraceInfoVO>();
	private HeadqInvenTraceInfoVO traceFooter = new HeadqInvenTraceInfoVO();
	public List<HeadqInvenTraceInfoVO> getTraceItems() {
		return traceItems;
	}
	public void setTraceItems(List<HeadqInvenTraceInfoVO> traceItems) {
		this.traceItems = traceItems;
	}
	public HeadqInvenTraceInfoVO getTraceFooter() {
		return traceFooter;
	}
	public void setTraceFooter(HeadqInvenTraceInfoVO traceFooter) {
		this.traceFooter = traceFooter;
	}
	
}
