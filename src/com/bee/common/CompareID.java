package com.bee.common;

import java.util.Comparator;

import com.bee.app.ui.CItem;

public class CompareID implements Comparator<CItem> {

	@Override
	public int compare(CItem lhs, CItem rhs) {
		// TODO Auto-generated method stub
		
		return lhs.GetID().compareToIgnoreCase(rhs.GetID());
		
	}

}
