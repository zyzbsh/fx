package fxtrader.com.app.entity;

import java.util.List;

public class MAChartVO {

	private float maxPrice;
	private float minPrice;
	private float avePrice;
	private List<String> ytitle;
	private List<String> xtitle;
	private List<Float> values;

	public float getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public float getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}

	public float getAvePrice() {
		return avePrice;
	}

	public void setAvePrice(float avePrice) {
		this.avePrice = avePrice;
	}

	public List<String> getYtitle() {
		return ytitle;
	}

	public void setYtitle(List<String> ytitle) {
		this.ytitle = ytitle;
	}

	public List<String> getXtitle() {
		return xtitle;
	}

	public void setXtitle(List<String> xtitle) {
		this.xtitle = xtitle;
	}

	public List<Float> getValues() {
		return values;
	}

	public void setValues(List<Float> values) {
		this.values = values;
	}

}
