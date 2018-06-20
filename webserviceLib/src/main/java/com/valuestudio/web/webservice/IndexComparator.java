package com.valuestudio.web.webservice;

import java.util.Comparator;

/**
 * 参数的序号排序
 * 
 * @description
 * @author zuolong
 */
public class IndexComparator implements Comparator<String> {

	public IndexComparator() {
	}

	/**
	 * compare 实现排序
	 * 
	 * @description
	 * @param indexStr1
	 * @param indexStr2
	 * @return
	 * @author zuolong
	 */
	@Override
	public int compare(String indexStr1, String indexStr2) {
		try {
			int index1 = Integer.parseInt(indexStr1);
			int index2 = Integer.parseInt(indexStr2);
			if (index1 < index2) {
				return -1;
			} else if (index1 > index2) {
				return 1;
			} else {
				return 0;
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return 0;
		}
	}

}
